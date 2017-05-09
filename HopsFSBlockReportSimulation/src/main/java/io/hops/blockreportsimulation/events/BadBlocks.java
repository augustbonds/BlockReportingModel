package io.hops.blockreportsimulation.events;

import com.google.common.collect.ImmutableList;
import io.hops.blockreportsimulation.types.Replica;
import se.sics.kompics.KompicsEvent;

import java.util.List;

// I am following the naming in HopsFS, but actually this message
// reports bad replicas, not bad blocks.
// Can be sent from both client and source: can contain remote replicas.
//TODO: Currently unused
public class BadBlocks implements KompicsEvent {
    public final long source;
    public final ImmutableList<Replica> replicas;

    public BadBlocks(List<Replica> replicas, long source) {
        this.source = source;
        this.replicas = ImmutableList.copyOf(replicas);
    }
}
