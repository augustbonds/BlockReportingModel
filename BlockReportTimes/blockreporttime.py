#!/usr/bin/env python3
import math
import matplotlib.pyplot as plt
import sys

'''
    Assuming the blocks are spread evenly across the cluster, and the block modifications too
'''
def changesPerDatanodePerSecond(operationsPerSecond, writePercentage, replicationLevel, numDatanodes):
    return math.ceil(operationsPerSecond*writePercentage/100.0*replicationLevel/numDatanodes)

print ("Spotify workload")
operationsPerSecond = 1250000
writePercentage = 2.7
replicationFactor = 3
print("{} ops/s, {}% writes, replication factor {}".format(operationsPerSecond, writePercentage, replicationFactor))
for numDns in [1000,2000,5000,10000,20000,50000]:
    cds = changesPerDatanodePerSecond(operationsPerSecond, writePercentage, replicationFactor, numDns)
    print ("for {} datanodes, {} changes per dn per second".format(numDns, cds))


def minBrTime(numBuckets, numBlocks):
    #bandwidth = 1000 * 1000 * 1000
    #return 0.150 + numBuckets * 8 / float(bandwidth)
    bandwidth = 10 * 1000 * 1000 * 1000
    return 0.15 + numBlocks * 30 * 8 / float(bandwidth) #ignore buckets since they hardly matter

def expectedNumInconsistentBuckets(numChanges, numBuckets):
    #as per https://math.stackexchange.com/a/868454
    #expected number of empty cells = n(1-1/n)^m , n = num buckets, m = num balls
    return numBuckets - numBuckets*pow((1.0-1.0/numBuckets),numChanges)

def brTime(minBrTime, prevBrTime, prevChangesPerSecond, numBuckets, blocksPerDatanode, reportedBlocksPerSecondPerNamenode ):
    numUncaughtChanges = prevBrTime * prevChangesPerSecond
    inconsistentBuckets = expectedNumInconsistentBuckets(numUncaughtChanges, numBuckets)
    numBlocksReported = inconsistentBuckets/numBuckets*blocksPerDatanode
    return minBrTime + numBlocksReported/reportedBlocksPerSecondPerNamenode

#from HopsFS paper
# 100k blocks/s/nn block reporting performance
print ("\nCalculating report time progression from startup")


numDns = 1000
blocksPerDn = 1000 * 1000
rbsn = 100000
numBuckets = 2000

if (len(sys.argv) == 5):
    numDns = sys.argv[1]
    blocksPerDn = sys.argv[2]
    rbsn = sys.argv[3]
    numBuckets = sys.argv[4]

operationsPerSecond = 500000
writePercentage = 2.7
replicationFactor = 3
minTime = minBrTime(numBuckets, blocksPerDn)

title = '{}k dns, {}k blocks/dn,\n {}k buckets, {}k block/s processing, {}% writes'.format(numDns/1000, blocksPerDn/1000, numBuckets/1000, rbsn/1000, writePercentage)

cds = changesPerDatanodePerSecond(operationsPerSecond, writePercentage, replicationFactor, numDns)

print("{} reported blocks per second per namenode \n{}s to report matching buckets (minimum report time)".format(rbsn, minBrTime))            
print("{} ops/s\n{}%writes\n{} replication factor\n{} data nodes\n{} buckets\n".format(operationsPerSecond, writePercentage, replicationFactor, numDns, numBuckets))


time = 0 # expect 0 inconsistencies the first report
for i in range(20):
    #print("br#{}:\tt = {:3.4f}".format(i,time))
    time = brTime(minTime, time,cds, numBuckets, blocksPerDn, rbsn)

xs = []
ys = []

for x in range(10):
    opss = operationsPerSecond * x
    time = minBrTime(numBuckets, blocksPerDn) # irrespective of the starting time, the report time will stabilize
    for i in range(1000):
        cps = changesPerDatanodePerSecond(opss, writePercentage, replicationFactor, numDns)
        time = brTime(minTime, time, cps, numBuckets, blocksPerDn, rbsn)

    y = float(time)
    print('{}M ops/s, {} min br time, actual time {}'.format(x, minTime, y))
    xs.append(int(x))
    ys.append(y)

fig = plt.figure()

ax = fig.add_subplot(111)
ax.plot(xs,ys)
ax.set_title(title)
ax.set_xlabel('M ops/s')
ax.set_ylabel('block report time (s) ')
ax.set_ylim([0,12])
plt.savefig('{}kdns{}kblocks{}kbuckets{}kblockspersec{}percentwrites.png'.format(numDns/1000, blocksPerDn/1000, numBuckets/1000, rbsn/1000, writePercentage))
