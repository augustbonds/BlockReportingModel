package io.hops.blockreportsimulation.events;

import com.google.common.collect.ImmutableList;
import io.hops.blockreportsimulation.types.Replica;
import se.sics.kompics.KompicsEvent;

import java.util.List;

public class BlocksReceivedAndDeleted implements KompicsEvent {
    public final long source;
    public final ImmutableList<Replica> received;
    public final ImmutableList<Replica> deleted;

    public BlocksReceivedAndDeleted(long source, List<Replica> received, List<Replica> deleted){
        this.source = source;
        this.received = ImmutableList.copyOf(received);
        this.deleted = ImmutableList.copyOf(deleted);
    }

}
