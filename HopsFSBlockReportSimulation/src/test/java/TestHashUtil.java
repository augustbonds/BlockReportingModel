import com.google.common.hash.HashCode;
import io.hops.blockreportsimulation.HashUtil;
import io.hops.blockreportsimulation.types.Replica;
import junit.framework.TestCase;

import java.util.LinkedList;

public class TestHashUtil extends TestCase {

  public void testHashReplicaAction(){
    Replica r1 = new Replica(1,1, Replica.State.BEING_WRITTEN, 1);
    Replica r2 = new Replica(1,1, Replica.State.FINALIZED, 2);

    HashUtil.Action a1 = HashUtil.Action.REMOVE_BLOCK;
    HashUtil.Action a2 = HashUtil.Action.ADD_BLOCK;

    assertNotSame("Hashes colliding on same action different replica", HashUtil.hash(r1, a1), HashUtil.hash(r2,a1));
    assertNotSame("Hashes colliding on same replica different action", HashUtil.hash(r1, a1), HashUtil.hash(r1,a2));
  }

  public void testHashMultipleIncremental(){

    int nextId = 0;
    LinkedList<Replica> startingStore = new LinkedList<>();
    LinkedList<Replica> toAdd = new LinkedList<>();
    LinkedList<Replica> toRemove = new LinkedList<>();

    while (nextId < 1000) {
      startingStore.add(new Replica(nextId++, 1, Replica.State.BEING_WRITTEN, 1));
    }

    while (nextId < 1500) {
      toAdd.add(new Replica(nextId++, 1, Replica.State.FINALIZED, 1));
    }

    nextId = 0;
    while (nextId < 1500) {
      if (nextId % 2 != 0){
        toRemove.add(new Replica(nextId, 2, Replica.State.TEMPORARY, 1));
      }
      nextId++;
    }


    HashCode original = HashUtil.hash(startingStore);

    HashCode incremental1 = HashCode.fromLong(original.asLong());
    for (Replica replica : toAdd){
      incremental1 = HashUtil.incrementalHash(incremental1, replica, HashUtil.Action.ADD_BLOCK);
    }
    for (Replica replica : toRemove) {
      incremental1 = HashUtil.incrementalHash(incremental1, replica, HashUtil.Action.REMOVE_BLOCK);
    }
    //Opposite order
    HashCode incremental2 = HashCode.fromLong(original.asLong());
    for (Replica replica : toRemove) {
      incremental2 = HashUtil.incrementalHash(incremental2, replica, HashUtil.Action.REMOVE_BLOCK);
    }
    for (Replica replica : toAdd){
      incremental2 = HashUtil.incrementalHash(incremental2, replica, HashUtil.Action.ADD_BLOCK);
    }

    assertEquals("incremental hashing gave different results when opposite order", incremental1,incremental2);

  }
}
