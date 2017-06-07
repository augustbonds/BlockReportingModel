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
