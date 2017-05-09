package io.hops.blockreportsimulation;

import com.google.common.hash.*;
import io.hops.blockreportsimulation.types.Replica;

import java.util.*;

public class HashUtil {

    public enum Action {
        ADD_BLOCK,
        REMOVE_BLOCK,
        MARK_BLOCK_CORRUPT
    }

    public static List<List<Replica>> bucketize(Collection<Replica> blocks, int numBuckets){
        List<List<Replica>> buckets = new ArrayList<>();
        for (int i = 0; i < numBuckets ; i++){
            buckets.add(new ArrayList<Replica>());
        }

        for (Replica next : blocks) {
            int bucket = bucketIndex(next, numBuckets);
            buckets.get(bucket).add(next);

        }
        return buckets;
    }

    public static int bucketIndex(Replica replica, int numBuckets){
        return (int) (replica.id % numBuckets);
    }

    public static HashCode hash(List<Replica> blocks){
        List<HashCode> hashes = new LinkedList<>();
        for (Replica replica : blocks){
            hashes.add(hash(replica));
        }

        if (!hashes.isEmpty()){
            return Hashing.combineUnordered(hashes);
        } else {
            return HashCode.fromInt(0);
        }
    }

    public static List<HashCode> hashBuckets(List<List<Replica>> buckets){
        //Calculate hashes
        List<HashCode> hashes = new LinkedList<>();

        for (List<Replica> bucket : buckets){
            hashes.add(hash(bucket));
        }

        return hashes;
    }


    public static HashCode incrementalHash(HashCode original, List<HashCode> updates){
        List<HashCode>  toCombine = new ArrayList<>();
        toCombine.add(original);
        toCombine.addAll(updates);
        return Hashing.combineUnordered(toCombine);
    }

    public static HashCode incrementalHash(HashCode original, Replica replica, Action action){
        HashCode update = hash(replica, action);
        return Hashing.combineUnordered(Arrays.asList(original, update));
    }

    public static List<HashCode> hash(List<Replica> replicas, Action action){
        ArrayList<HashCode> result = new ArrayList<>();
        for (Replica replica: replicas){
            result.add(hash(replica, action));
        }
        return result;
    }

    private static HashCode hash(Replica replica){
        long toHash = 0;
        toHash += replica.id;
        toHash += replica.gs*100;
        switch(replica.state) {
            case BEING_WRITTEN:
                toHash += 1000;
                break;
            case FINALIZED:
                toHash += 2000;
                break;
            case TEMPORARY:
                toHash += 3000;
                break;
            case UNDER_RECOVERY:
                toHash += 4000;
                break;
            case WAITING_TO_BE_RECOVERED:
                toHash += 5000;
                break;
        }
        toHash += replica.dataNode;

        return HashCode.fromLong(toHash);
    }

    public static HashCode hash(Replica replica, Action action){
        //TODO: Increase collision resistance
        HashCode rHash = hash(replica);

        long aHash = 0;
        switch (action){
            case ADD_BLOCK:
                aHash += 10000;
                break;
            case MARK_BLOCK_CORRUPT:
                aHash += 20000;
                break;
            case REMOVE_BLOCK:
                aHash += 30000;
                break;
        }

        return Hashing.combineUnordered(Arrays.asList(rHash, HashCode.fromLong(aHash)));
    }

}
