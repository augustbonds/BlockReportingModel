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


debugging = False
def debug(message):
    if debugging==True:
        print(message)

def info(message):
    print(message)

def br(n,cps,numBlocksPerDn,numBuckets, conf, prevResults):
    debug("Start: br({})".format(n))
    RTT = conf["RTT"]
    RTT_DB = conf["RTT_DB"]
    errorsBetweenReports = conf["errorsBetweenReports"]
    bitsPerReportedBlock = conf["bitsPerReportedBlock"]
    networkSpeed = conf["networkSpeed"]
    databaseReadBlocksPerSecond = conf["databaseReadBlocksPerSecond"]
    correctionsPerSecond = conf["correctionsPerSecond"]
    if n in prevResults:
        return prevResults[n]
    if n == 1:
        time = RTT+numBlocksPerDn*bitsPerReportedBlock/networkSpeed
        debug("br({}): time={}".format(n,time))
        return time
    
    time = RTT
    debug("br({}): RTT={}".format(n,RTT))
    incorrectBuckets=min(max(errorsBetweenReports, br(n-1,cps,numBlocksPerDn,numBuckets,conf,prevResults)*cps),numBuckets)
    debug("br({}): {}/{} buckets correct".format(n,numBuckets-incorrectBuckets,numBuckets))
    time = time + RTT_DB + incorrectBuckets/numBuckets * (numBlocksPerDn/databaseReadBlocksPerSecond)
    debug("br({}): Time plus database reads ={}".format(n,time))
    time = time + errorsBetweenReports/correctionsPerSecond
    debug("br({}): Time plus errorCorrection ={}".format(n,time))
    prevResults[n] = time
    return time

def brExperiment(multiples, numOps, numDatanodes, numBlocksPerDn, numBuckets, conf):
    cps=changesPerDatanodePerSecond(numOps, conf["writePercentage"], conf["replicationLevel"], numDatanodes)
    return [(multiple, br(500,multiple*cps, numBlocksPerDn,numBuckets, conf, {})) for multiple in multiples]
    

conf = {
    "RTT": 0.005,
    "RTT_DB":0.001,
    "bitsPerReportedBlock":30*8,
    "networkSpeed":0.5*1000*1000*1000.0,
    "errorsBetweenReports":50,
    "correctionsPerSecond":1/0.010*10, #10ms per correction, 10 parallel threads
    "databaseReadBlocksPerSecond":100*1000,
    "replicationLevel":3,
    "writePercentage":2.7
}

results = brExperiment(np.arange(.1,10,0.1), 1000*1000, 1000,1000*1000,2000, conf)
#results = brExperiment([1], 0, 40)

for (x,y) in results:
    print("({},{})".format(x,y), end='')
print()

def showGraph(results):
    xs = []
    ys = []

    for (x,y) in results:
        xs.append(x)
        ys.append(y)
    plt.plot(xs,ys)
    plt.show()

showGraph(results)