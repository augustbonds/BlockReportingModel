# How do different file system operations and rpc calls affect the replica state on the namenode?

## ClientProtocol.addBlock
_addBlock_ is used to request a new block for writing.
It will try to commit or complete the last block of the file, then create a 
new block under construction that it hands back to the client.

*Effects*
* committing the last block of the file (update block state
 to COMMITTED, update numBytes, gs)
* completing the last block of the file (removing replicas under construction)
* creating a new block for writing (create new UC block, create expected 
replicas)

## ClientProtocol.complete
The client is closing it's write stream and the file (and therefore last 
block) is requested to be finalized. This behaves differently depending on if
 the file is small(stored on the database) or normal (stored on the datanode).

*Effects*
* commit the last block of the file (update block state to COMMITTED, update 
numBytes, gs)
* complete the last block of the file (remove replicas under construction)

## ClientProtocol.create
A client wants to create a new file (or overwrite an exisiting file). The 
namenode allocates nodes for a new block or returns locations for an existing
 block for the client to write to.

*Effects*
* lease recovery if other write in progress already (see _Lease Recovery_)
* if overwrite, delete previous file
* if append, create expected replicas and change block state to UC
* if not append, create new block (UC) and create expected replicas

## ClientProtocol.updatePipeline
A client is setting up a pipeline for append or recovery, and is notifying 
the namenode of which the new block gs and which datanodes will be written to.

*Effects*


## InterDatanodeProtocol.updateReplicaUnderRecovery
On the datanode, this operation will turn a RUR replica into a FINALIZED 
replica with new gs (recovery id) and length. See _Recover block_ for more 
info.

*Effects*
* When the block has been updated on the datanode it notifies the namenode by 
_blockReceivedAndDeleted_ (RECEIVED) with the new GS and new length.



## Block received
When the datanode has completely received a block, it will notify the namenode.

*Effects*
* Notify Namenode by _blockReceivedAndDeleted_ (RECEIVED) with gs, length and 
ID of the finalized block.

## Lease Recovery

## Recover block (NN requests to primary DN)
