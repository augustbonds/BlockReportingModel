# How do different file system operations and rpc calls affect the replica state on the namenode?

## ClientProtocol.addBlock
_addBlock_ is used to request a new block for writing. It will try to commit or complete the last block of the file, then create a new block under construction that it hands back to the client.

## ClientPRotocol. 
