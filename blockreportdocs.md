# Block Reporting in HopsFS
_Block Report_ is a mechanism in HDFS that acts as a fail-safe, guarding your
 data, making sure that any corruption or loss of copies of data are discovered
  and appropriate measures to re-replicate the data are taken. 
  
The mechanism itself works by each storage node sending a complete report of 
its stored data to the namenode every hour. Sending and processing this 
report is very heavy, as a datanode can host millions of blocks, and is 
therefore a bottleneck for scaling HDFS to clusters bigger than 4000
 nodes (citation needed).
 
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
    
We wanted to 

