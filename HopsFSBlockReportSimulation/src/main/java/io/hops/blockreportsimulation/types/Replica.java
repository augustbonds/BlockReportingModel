package io.hops.blockreportsimulation.types;

public class Replica {
    public final long id;
    public final long gs;
    public final State state;
    public final long dataNode;

    public Replica(long id, long gs, State state, long dataNode){
        this.id = id;
        this.gs = gs;
        this.state = state;
        this.dataNode = dataNode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Replica){
            Replica other = (Replica) obj;
            return other.id == id &&
                    other.gs == gs &&
                    other.state == state &&
                    other.dataNode == dataNode;
        } else {
            return false;
        }
    }

    public enum State{
        FINALIZED,
        BEING_WRITTEN,
        WAITING_TO_BE_RECOVERED,
        UNDER_RECOVERY,
        TEMPORARY
    }
}
