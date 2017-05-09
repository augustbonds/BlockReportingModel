package io.hops.blockreportsimulation.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.hash.HashCode;
import io.hops.blockreportsimulation.Configuration;
import io.hops.blockreportsimulation.HashUtil;
import io.hops.blockreportsimulation.events.BadBlocks;
import io.hops.blockreportsimulation.events.BlockReport;
import io.hops.blockreportsimulation.events.BlocksReceivedAndDeleted;
import io.hops.blockreportsimulation.ports.NameNodeDataNodePort;
import io.hops.blockreportsimulation.types.Replica;
import org.apache.log4j.Logger;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Start;

import java.util.*;

public class NameNodeComponent extends ComponentDefinition {


    private static final Logger LOG = Logger.getLogger(NameNodeComponent.class);

    //Ports
    private Negative<NameNodeDataNodePort> dnp = provides(NameNodeDataNodePort.class);

    //State
    //DataNode -> Buckets
    private Map<Long, List<ArrayList<Replica>>> replicasMap = new HashMap<>();
    //DataNode -> Hashes
    private Map<Long, ArrayList<HashCode>> replicaHashesMap = new HashMap<>();


    /* Order of operations for processing a single reported replica
     * The process schedules future work for the block manager to do.
     *
     * The actual updates happen in:
     *  - BlockManager.addStoredBlockUnderConstruction
     *  - BlockManager.addStoredBlock
     *  - BlockManager.addToInvalidates
     *  - BlockManager.markBlockAsCorrupt
     *
     * reportDiff():
     * input: reported replica, replicaAlreadyExists:boolean
     *
     * 1. getBlockInfo for associated block.
     * 2. if blockInfo == null: TO_INVALIDATE(block); return null;
     * 3. get Under Construction state for associated block
     * 4. checkReplicaCorrupt(storedBlockInfo,storedUcState,reportedBlock,reportedReplicaState,dataNode) (returns BlockToMarkCorrupt or null)
     * 5. if toMarkCorrupt != null: TO_CORRUPT(toMarkCorrupt); return blockInfo;
     * 6. isBlockUnderConstruction(storedBlockInfo, storedUCState, reportedReplicaState) (returns boolean)
     * 7. if isUnderUC: TO_UC(StatefulBlockInfo(storedBlockInfo, reportedReplicaState); return storedBlockInfo;
     * 8. "add replica if appropriate" //TODO expand point 8
     *
     * checkReplicaCorrupt():
     * switch (reportedState):
     *  case FINALIZED:
     *      switch(ucState):
     *          case COMPLETE:
     *          case COMMITTED:
     *              if (gs AND numBytes match)
     *                  //not corrupt
     *                  return null;
     *              else
     *                  return new BlockToMarkCorrupt
     *          default:
     *              //not corrupt
     *              return null
     *  case RBW:
     *  case RWR:
     *      if(!storedBlockInfo.isComplete())
     *          //not corrupt
     *          return null;
     *      else if (gs don't match)
     *          //should be complete but gs don't match
     *          return new BlockToMarkCorrupt
     *      else
     *          //complete block, same genstamp
     *          if (reportedReplicaState == RBW):
     *              // if RWB report for COMPLETE block, might be delayed
     *              // block report after pipeline closed. Ignore this
     *              // assuming there will be FINALIZED replica later.
     *              //not corrupt
     *              return null;
     *          else
     *              return new BlockToMarkCorrupt
     *  case RUR: // Should not be reported
     *  case TEMPORARY: // Should not be reported
     *  default:
     *      //broken invariant
     *      derp();
     *
     *
     * isBlockUnderConstruction():
     * switch(reportedReplicaState):
     *  case FINALIZED:
     *      switch (storedUCState):
     *          case UNDER_CONSTRUCTION:
     *          case UNDER_RECOVERY:
     *              //is under construction
     *              return true;
     *          default:
     *              return false;
     *  case RBW:
     *  case RWR:
     *      return (!storedBlockInfo.isComplete())
     *  case RUR: //should not be reported
     *  case TEMPORARY: // should not be reported
     *  default:
     *      return false;
     */



    //Handlers
    private Handler<BlockReport> blockReportHandler = new Handler<BlockReport>() {
        @Override
        public void handle(BlockReport blockReport) {

            long source = blockReport.source;
            LOG.info("got block report from" + source + " of " + blockReport.numReplicas + " blocks");

            if (replicasMap.get(source) != null) {
                List<HashCode> oldHashes = replicaHashesMap.get(source);
                ImmutableList<HashCode> hashes = blockReport.hashes;
                assert oldHashes.size() == hashes.size();

                //Find differing ranges
                LinkedList<Integer> bucketsToUpdate = new LinkedList<>();
                for (int i = 0 ; i < blockReport.numBuckets ; i++){
                    if (!oldHashes.get(i).equals(hashes.get(i))){
                        bucketsToUpdate.add(i);
                    }
                }

                if (!bucketsToUpdate.isEmpty()){
                    //update conflicting buckets and hashes namenode state
                    for(int differingBucketIndex : bucketsToUpdate){
                        replicasMap.get(source).set(differingBucketIndex, Lists.newArrayList(blockReport.buckets.get(differingBucketIndex)));
                        replicaHashesMap.get(source).set(differingBucketIndex, blockReport.hashes.get(differingBucketIndex));
                    }

                    LOG.info(String.format("report diffed in %d/%d buckets", bucketsToUpdate.size(), blockReport.numBuckets));

                } else {
                    LOG.info(String.format("report diffed in 0/%d buckets", blockReport.numBuckets));
                }

            } else {
                // first report, just put it all in, deep copy to make mutable
                LOG.debug("Got first report from " + source);
                List<ArrayList<Replica>> buckets = new ArrayList<>();
                for(ImmutableList<Replica> bucket : blockReport.buckets){
                    buckets.add(Lists.newArrayList(bucket));
                }
                replicasMap.put(source, buckets);
                replicaHashesMap.put(source, Lists.newArrayList(blockReport.hashes));
            }

        }
    };

    private Handler<BlocksReceivedAndDeleted> receivedAndDeletedHandler = new Handler<BlocksReceivedAndDeleted>() {
        @Override
        public void handle(BlocksReceivedAndDeleted blocksReceivedAndDeleted) {
            long source = blocksReceivedAndDeleted.source;
            LOG.info("Got received/deleted report from: " + source);
            List<Replica> received = blocksReceivedAndDeleted.received;
            List<Replica> deleted = blocksReceivedAndDeleted.deleted;

            if (!received.isEmpty()) {
                // Process "received" replicas if any
                List<List<Replica>> buckets = HashUtil.bucketize(received, Configuration.NUM_BUCKETS);

                for(int i = 0; i < buckets.size(); i++){
                    if (!buckets.get(i).isEmpty()){
                        //add all replicas
                        replicasMap.get(source).get(i).addAll(buckets.get(i));
                        //Update the corresponding hash
                        HashCode oldHash = replicaHashesMap.get(source).get(i);
                        HashCode newHash = HashUtil.incrementalHash(oldHash, HashUtil.hash(buckets.get(i), HashUtil.Action.ADD_BLOCK));


                        replicaHashesMap.get(source).set(i, newHash);
                    }
                }
            }

            if (!deleted.isEmpty()){
                // Process "deleted" replicas if any
                List<List<Replica>> buckets = HashUtil.bucketize(deleted, Configuration.NUM_BUCKETS);
                for(int i = 0; i < buckets.size() ; i++){
                    if (!buckets.get(i).isEmpty()){
                        //remove all replicas
                        replicasMap.get(source).get(i).removeAll(buckets.get(i));

                        //Update the corresponding hash
                        HashCode oldHash = replicaHashesMap.get(source).get(i);
                        HashCode newHash = HashUtil.incrementalHash(oldHash, HashUtil.hash(buckets.get(i), HashUtil.Action.REMOVE_BLOCK));
                        replicaHashesMap.get(source).set(i, newHash);
                    }
                }
            }
        }
    };

    private Handler<BadBlocks> badBlocksHandler = new Handler<BadBlocks>() {
        @Override
        public void handle(BadBlocks badBlocks) {

        }
    };

    private Handler<Start> startHandler = new Handler<Start>() {
        @Override
        public void handle(Start start) {
            LOG.info("Hello, World!");
        }
    };

    {
        subscribe(startHandler, control);
        subscribe(blockReportHandler, dnp);
        subscribe(receivedAndDeletedHandler, dnp);
        subscribe(badBlocksHandler, dnp);
    }
}
