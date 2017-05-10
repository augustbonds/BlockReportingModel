package io.hops.blockreportsimulation.components;

import com.google.common.hash.HashCode;
import io.hops.blockreportsimulation.Configuration;
import io.hops.blockreportsimulation.HashUtil;
import io.hops.blockreportsimulation.events.BlockReport;
import io.hops.blockreportsimulation.events.BlocksReceivedAndDeleted;
import io.hops.blockreportsimulation.ports.NameNodeDataNodePort;
import io.hops.blockreportsimulation.types.Replica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.timer.CancelPeriodicTimeout;
import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.kompics.timer.Timeout;
import se.sics.kompics.timer.Timer;

import java.util.*;

public class DataNodeComponent extends ComponentDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(DataNodeComponent
        .class);

    //Ports
    private Positive<NameNodeDataNodePort> nnp = requires(NameNodeDataNodePort.class);
    private Positive<Timer> timer = requires(Timer.class);

    private UUID timerId;

    //State
    private final long ID = 1; //TODO initialize source with id

    private Set<Replica> store = new TreeSet<>(new Comparator<Replica>() {
        @Override
        public int compare(Replica o1, Replica o2) {
            return new Long(o1.id).compareTo(o2.id);
        }
    });
    private List<HashCode> bucketHashes = new ArrayList<>();

    private int nextBlockId;

    // Handlers
    private Handler<Start> startHandler = new Handler<Start>() {
        @Override
        public void handle(Start start) {
            //Intially populate block store
            for (int i = 0; i < 10000 ; i++){
                store.add(new Replica(nextBlockId++, 1, Replica.State.FINALIZED, ID));
            }
            bucketHashes = HashUtil.hashBuckets(HashUtil.bucketize(store, Configuration.NUM_BUCKETS));

            //Schedule block reports
            SchedulePeriodicTimeout spt = new SchedulePeriodicTimeout(0, Configuration.BLOCK_REPORT_TIMEOUT);
            BlockReportTimeout timeout = new BlockReportTimeout(spt);
            spt.setTimeoutEvent(timeout);
            trigger(spt,timer);
            timerId = timeout.getTimeoutId();

            //Schedule random replica actions
            SchedulePeriodicTimeout spt2 = new SchedulePeriodicTimeout(100, Configuration.RND_ACTION_TIMEOUT);
            RandomActionTimeout raTimeout = new RandomActionTimeout(spt2);
            spt2.setTimeoutEvent(raTimeout);
            trigger(spt2, timer);
        }
    };

    private Handler<BlockReportTimeout> blockReportTimeoutHandler = new Handler<BlockReportTimeout>() {
        @Override
        public void handle(BlockReportTimeout blockReportTimeout) {
            blockReport();
        }
    };

    private Handler<RandomActionTimeout> randomActionTimeoutHandler = new Handler<RandomActionTimeout>() {
        @Override
        public void handle(RandomActionTimeout randomActionTimeout) {
            randomAction();
        }
    };

    {
        subscribe(startHandler, control);
        subscribe(blockReportTimeoutHandler, timer);
        subscribe(randomActionTimeoutHandler, timer);
    }

    @Override
    public void tearDown() {
        trigger(new CancelPeriodicTimeout(timerId), timer);
        super.tearDown();
    }

    private void blockReport(){
        BlockReport report = new BlockReport(ID, HashUtil.bucketize(store, Configuration.NUM_BUCKETS), bucketHashes);
        LOG.info("sending block report");
        trigger(report,nnp);
    }

    private void randomAction(){
        int choice = new Random(System.currentTimeMillis()).nextInt(3);
        switch(choice){
            case 0:
                randomBadBlock();
                break;
            case 1:
                randomAddBlock();
                break;
            case 2:
                randomDeleteBlock();
                break;
            default:
                //noop
        }
    }

    private void randomBadBlock(){
        //TODO: corruption not considered
    }

    private void randomAddBlock(){
        Replica newReplica = new Replica(nextBlockId++, 1, Replica.State.FINALIZED, ID);
        store.add(newReplica);

        //Update hash
        int bucket = HashUtil.bucketIndex(newReplica, Configuration.NUM_BUCKETS);
        HashCode oldHash = bucketHashes.get(bucket);
        bucketHashes.set(bucket, HashUtil.incrementalHash(oldHash, newReplica, HashUtil.Action.ADD_BLOCK));

        //Send incremental report
        List<Replica> received = new ArrayList<>();
        List<Replica> deleted = new ArrayList<>();
        received.add(newReplica);
        BlocksReceivedAndDeleted message = new BlocksReceivedAndDeleted(ID, received, deleted);
        LOG.info("sending blocks received and deleted message");
        trigger(message, nnp);
    }

    private void randomDeleteBlock(){
        Replica toRemove = store.iterator().next();
        store.remove(toRemove);

        //Update hash
        int bucket = HashUtil.bucketIndex(toRemove, Configuration.NUM_BUCKETS);
        HashCode oldHash = bucketHashes.get(bucket);
        bucketHashes.set(bucket, HashUtil.incrementalHash(oldHash, toRemove, HashUtil.Action.REMOVE_BLOCK));

        //Send incremental report
        List<Replica> received = new ArrayList<>();
        List<Replica> deleted = new ArrayList<>();
        deleted.add(toRemove);
        BlocksReceivedAndDeleted message = new BlocksReceivedAndDeleted(ID, received, deleted);
        LOG.info("sending blocks received and deleted message");
        trigger(message, nnp);
    }

    public static class RandomActionTimeout extends Timeout {
        RandomActionTimeout(SchedulePeriodicTimeout spt){
            super(spt);
        }
    }
    public static class BlockReportTimeout extends Timeout {
        BlockReportTimeout(SchedulePeriodicTimeout spt){
            super(spt);
        }
    }
}
