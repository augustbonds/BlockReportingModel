# Block Reporting in HopsFS
_Block Report_ is a mechanism in HDFS that acts as a fail-safe, guarding your
 data, making sure that any corruption or loss of copies of data are discovered
  and appropriate measures to re-replicate the data are taken. 
  
The mechanism itself works by each storage node sending a complete report of 
its stored data to the namenode every hour. Sending and processing this 
report is very heavy, as a datanode can host millions of blocks, and is 
therefore a bottleneck for scaling HDFS to clusters bigger than 4000
 nodes (CITATION NEEDED).
 
Interestingly enough, every block modification a datanode performs is 
reported as it completes (we call this _Incremental Block Report_) so in case of
 no byzantine 
failures (due to 
programmer error or manual deletion of data from a datanode) the full block 
report is redundant.
 
HopsFS stores all it's metadata in a distributed in-memory database called 
MySQL Cluster or NDB. Hops also, like HADOOP, deploys multiple NameNodes. 
However in Hops, because of storing the metadata in a distributed database, 
NameNodes can do more extensive sharing of the workload. Storing the 
metadata in the database does come with some limitations though, namely 
bandwidth and access time, compared to the in-JVM solution of HADOOP. So 
despite sharing the load of receiving block reports between different 
NameNodes, the old block report is too slow.

## A New Way of Reporting Blocks
In our redesign of the Block Report we had certain requirements:
* Safety - Eventually all missing or corrupt blocks should be 
reported.
* Timeliness (partial synchrony?) - Under normal circumstances the state 
should be up to date at least once every hour.
* Speed - The new method should allow for scaling Hops clusters several times
 their previous size.
* Size - The method should use limited bandwidth so as to not crowd the 
network and not be as taxing on the database.

The old reporting mechanism submitted a complete list of blocks:

    ReplicaState := FINALIZED
                | REPLICA_BEING_WRITTEN
                | REPLICA_UNDER_RECOVERY
                | REPLICA_WAITING_TO_BE_RECOVERED
                | TEMPORARY
    ReportedReplica := <Id, GenerationStamp, Size, ReplicaState> 
    BlockReport(dataNode) := Array[ReportedReplica] (for replica in datanode)
    
Our new solution divides the blocks of the report into **U** buckets. The 
blocks are distributed among the buckets by modding the block id, and for 
each bucket a hash is computed. The hash is computed by means of adding the 
hashes of every block together, with state, like so:

    hash(ReportedReplica) := hash(<Id, GenerationStamp, Size, ReplicaState>)
    Bucket := Array[ReportedReplica]
    hash(Bucket) := sum([ hash(ReportedReplica) for ReportedReplica in Bucket ]) mod Hash.MAX_VALUE
    ReportedBucket := <Bucket, hash(bucket)>
    
    NewBlockReport(dataNode) := Array[ReportedBucket]

Our goal is to avoid processing parts of the report based on what we have 
previously seen reported incrementally, while making sure that we compare a 
representation of the complete state of the DataNode, rather than just a 
sequence of updates (in case a modification has not been successfully 
reported).

The key is using the sum of the hashes of the blocks to represent the bucket.
 When an incremental report arrives we can undo the hash of the preceding 
 block state and then apply the new hash, thereby enabling us to maintain a 
 hash on the NameNode side that should correspond to the actual state of the 
 DataNode, as long as all incremental updates arrive exactly once.

    // On the namenode side
    IncrementalReport = 
    
# Trade-offs between old and new reporting
| | Old Reporting | New Reporting |
| --- :|: --- :|: --- :|
| DN - NN bandwidth | ~30 bytes * #blocks | ~30 bytes * #blocks |
| NN - DB bandwidth (worst case)|~30 bytes * #blocks |~30 bytes * #blocks|
| NN - DB bandwidth (best case)| ~30 bytes * #blocks | ~8 bytes * #hashes |
| NN - DB bandwidth (average case)| ~30 bytes * #blocks | depends, see table 2|

# Failure scenarios and how they are handled

## Mass disk failure

## Temporary NameNode slow-down and blocking of reports

## Cluster Start-Up and exiting safe mode

## Massive block movement (e.g. server migration)
# A Brief History of HDFS Append
Files in HDFS are stored as a collection of fixed size blocks that are replicated on the datanodes of the cluster.
Writing to a file in HDFS is highly durable and fast.
A client that wishes to create or append a file requests block locations (datanodes that store or should store the block to be written) from the namenode, and the proceeds with setting up a write pipeline flowing from the client through each location.

In early versions of HDFS a block would either exist or not exist, with no in-progress state in between. 
During writing, a half written block would be invisible, and if writing the block failed its contents would be lost [History of HDFS append][http://blog.cloudera.com/blog/2009/07/file-appends-in-hdfs/],[Revisit Append Umbrella JIRA][https://issues.apache.org/jira/browse/HDFS-265].
HBase required stricter guarantees on synching and writing, so a synced append called Hflush[Hflush/append design document][https://issues.apache.org/jira/secure/attachment/12445209/appendDesign3.pdf] was introduced.

The the initial implementation a replica would be in one of two states: finalized, or temporary, where only finalized replicas would be visible to the file system and the user. Already back then, block report was an essential mechanism for durability of the data, and it consited of simply sending a list of finalized blocks to the namenode once an hour.

Append/hflush created a need for a more complex state machine.
Intermediary states had to be represented to properly account for streaming failures, pipeline failures, close failures and their handling. 

# The Replica State Machine (or "How HDFS can provide durable sync of distributed writing to replicas")

HDFS recovers from several types of failures, generally categorized as lease failures, data corruption, pipeline failures, close failures and streaming failures.

* A lease failure occurs when a client that has held one or more leases to files, but has stopped responding/writing
* Data corruption happens when physical disks error out
* Pipeline failures happen when a node in the pipeline stops 

#References
[1] History of HDFS append

[2]"Revisit Append" umbrella JIRA
