# How do different file system operations and rpc calls affect the replica state on the namenode?

## ClientProtocol.addBlock [done]
_addBlock_ is used to request a new block for writing.
It will try to commit or complete the last block of the file, then create a 
new block under construction that it hands back to the client.

*Effects*
* committing the last block of the file (update block state
 to COMMITTED, update numBytes, gs)
* completing the last block of the file (removing replicas under construction)
* creating a new block for writing (create new UC block, create expected 
replicas)

## ClientProtocol.complete [done]
The client is closing it's write stream and the file (and therefore last 
block) is requested to be finalized. This behaves differently depending on if
 the file is small(stored on the database) or normal (stored on the datanode).

*Effects*
* commit the last block of the file (update block state to COMMITTED, update 
numBytes, gs)
* complete the last block of the file (remove replicas under construction)

## ClientProtocol.create [done]
A client wants to create a new file (or overwrite an exisiting file). The 
namenode allocates nodes for a new block or returns locations for an existing
 block for the client to write to.

*Effects*
* lease recovery if other write in progress already (see _Lease Recovery_)
* if overwrite, delete previous file (See _Delete File_)
* if append, create expected replicas and change block state to UC
* if not append, create new block (UC) and create expected replicas

## ClientProtocol.updatePipeline [done]
A client is setting up a pipeline for append or recovery, and is notifying 
the namenode of which the new block gs, numBytes and which datanodes will
 be written to.

*Effects*
* Update the num bytes and gs of the block
* "set" current expected replicas (current implementation only adds/updates, 
not removes)

## ClientProtocol.append [done]
A client wants to append to an existing file.

*Effects*
* lease recovery if other write in progress (see _Lease Recovery)
* if overwrite, delete previous file (see _Delete File_)
* If file exists convert block to UC (and create expected replicas)

## InterDatanodeProtocol.updateReplicaUnderRecovery [done]
On the datanode, this operation will turn a RUR replica into a FINALIZED 
replica with new gs (recovery id) and length. See _Recover block_ for more 
info.

*Effects*
* When the block has been updated on the datanode it notifies the namenode by 
_blockReceivedAndDeleted_ (RECEIVED) with the new GS and new length.

## DatanodeProtocol.blockReport
A block report contains a list of all blocks on a datanode. Possible 
reported replica states are: FINALIZED, RBW, RWR.




## DatanodeProtocol.blockReceivedAndDeleted [done]
When a block is modified or deleted on the datanode, it sends a notification 
to the Namenode. The notification comes in three different types: RECEIVING, 
RECEIVED, DELETED. RECEIVING corresponds to a replica being written (RBW), 
RECEIVED corresponds to a replica finalized/recovered (FINALIZED) and DELETED
 simply means the replica has been removed.

Effects (RECEIVING):
* If there is no corresponding block, queue for deletion (invalidate)
* If the generation stamp does not match the block, or the block is complete 
with a different GS mark as corrupt.
* Change the block to UC and add expected replicas (doesn't really happen 
since the expected replica is there already).

Effects (RECEIVED):
* If there is no corresponding block, queue for deletion (invalidate)
* If there is a gs or length mismatch, mark as corrupt
* Store the replica in the database. If min replication reached, complete the 
block and remove all expected replicas.

Effects (DELETED):
* Remove corresponding replica

## DatanodeProtocol.commitBlockSynchronization

## Block received
When the datanode has completely received a block, it will notify the namenode.

*Effects*
* Notify Namenode by _blockReceivedAndDeleted_ (RECEIVED) with gs, length and 
ID of the finalized block.

## Lease Recovery

## Recover block (NN requests to primary DN)

## Delete file