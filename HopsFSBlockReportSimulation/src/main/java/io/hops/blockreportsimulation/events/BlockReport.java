package io.hops.blockreportsimulation.events;

import com.google.common.collect.ImmutableList;
import com.google.common.hash.HashCode;
import io.hops.blockreportsimulation.types.Replica;
import se.sics.kompics.KompicsEvent;

import java.util.ArrayList;
import java.util.List;

public class BlockReport implements KompicsEvent {
    public final long source;
    public final int numReplicas;
    public final int numBuckets;
    public final ImmutableList<ImmutableList<Replica>> buckets;
    public final ImmutableList<HashCode> hashes;

    public BlockReport(long source, List<List<Replica>> buckets, List<HashCode> hashes){
        this.source = source;
        int numReplicas = 0;
        for (List<Replica> bucket : buckets){
            numReplicas += bucket.size();
        }
        this.numReplicas = numReplicas;
        this.numBuckets = buckets.size();

        List<ImmutableList<Replica>> immutableBuckets = new ArrayList<>();
        for (List<Replica> bucket : buckets) {
            immutableBuckets.add(ImmutableList.copyOf(bucket));
        }
        this.buckets = ImmutableList.copyOf(immutableBuckets);

        this.hashes = ImmutableList.copyOf(hashes);

    }
}
