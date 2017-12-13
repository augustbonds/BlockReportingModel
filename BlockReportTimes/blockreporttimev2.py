#!/usr/bin/env python3
import math
import matplotlib.pyplot as plt
import sys
import numpy as np

'''
    Assuming the blocks are spread evenly across the cluster, and the block modifications too
'''
def changesPerDatanodePerSecond(operationsPerSecond, writePercentage, replicationLevel, numDatanodes):
    return math.ceil(operationsPerSecond*writePercentage/100.0*replicationLevel/numDatanodes)


RTT=0.002
RTT_DB=0.001
numDatanodes=10000
numBlocksPerDn=1000*1000
bitsPerReportedBlock=30*8
networkSpeed=10*1000*1000*1000.0
errorsBetweenReports=0
numBuckets=1000
correctionsPerSecond=1/0.004*10 #4ms per correction, 10 parallel threads
databaseReadBlocksPerSecond=200*1000

debugging = True
def debug(message):
    if debugging==True:
        print(message)

def br(n,cps, prevResults):
    debug("Start: br({})".format(n))
    if n in prevResults:
        return prevResults[n]
    if n == 1:
        time = RTT+numBlocksPerDn*bitsPerReportedBlock/networkSpeed
        debug("br({}): time={}".format(n,time))
        return time
    
    time = RTT
    debug("br({}): RTT={}".format(n,RTT))
    incorrectBuckets=min(max(errorsBetweenReports, br(n-1,cps,prevResults)*cps),numBuckets)
    debug("br({}): {}/{} buckets correct".format(n,numBuckets-incorrectBuckets,numBuckets))
    time = time + RTT_DB + incorrectBuckets/numBuckets * (numBlocksPerDn/databaseReadBlocksPerSecond)
    debug("br({}): Time plus database reads ={}".format(n,time))
    time = time + errorsBetweenReports/correctionsPerSecond
    debug("br({}): Time plus errorCorrection ={}".format(n,time))
    prevResults[n] = time
    return time

def brExperiment(multiples, numOps, numDatanodes):
    cps=changesPerDatanodePerSecond(numOps, 2.7, 3, numDatanodes)
    return [(multiple, br(500,multiple*cps, {})) for multiple in multiples]
    
#results = brExperiment(np.arange(.5,10,0.5), 1000*1000, 10000)
results = brExperiment([1], 1000*1000, 500)

for (x,y) in results:
    print("({},{})".format(x,y), end='')
