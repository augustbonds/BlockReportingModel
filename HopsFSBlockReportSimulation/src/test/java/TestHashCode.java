
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import junit.framework.TestCase;

import java.util.Arrays;

public class TestHashCode extends TestCase {

    public void testUnorderedCombination1000Hashes() {
        final int numHashes = 1000;
        HashCode[] hashCodes = new HashCode[numHashes];
        HashCode[] reversedList = new HashCode[numHashes];

        //Populate hash lists
        HashFunction hf = Hashing.murmur3_32();
        for (int i = 0 ; i < numHashes; i++) {
            hashCodes[i] = hf.newHasher().putInt(i).hash();
            reversedList[numHashes-i-1] = hf.newHasher().putInt(i).hash();
        }

        //Calculate combined hash iterating forward
        String forward = Hashing.combineUnordered(Arrays.asList(hashCodes)).toString();

        String backward = Hashing.combineUnordered(Arrays.asList(reversedList)).toString();

        assertNotSame("Reverse failed. First elements are the same", hashCodes[0], reversedList[0]);
        assertEquals("Different hash order did not generate same hash when combined", forward, backward);

    }

    public void testHashCodeInitialization(){
        assert 1000 == HashCode.fromInt(1000).asInt();
    }

    public void testUnorderedCombinationWithZero(){
        HashCode some = HashCode.fromInt(1337);
        HashCode zero = HashCode.fromInt(0);

        HashCode result = Hashing.combineUnordered(Arrays.asList(some, zero));

        assert result.asInt() == some.asInt();


    }

    public void testUnorderedCombination(){
        HashCode one = HashCode.fromInt(1);
        HashCode two = HashCode.fromInt(2);
        HashCode three = HashCode.fromInt(3);

        HashCode onePlusTwo = Hashing.combineUnordered(Arrays.asList(one,two));
        HashCode twoPlusThree = Hashing.combineUnordered(Arrays.asList(two,three));

        assertEquals(Hashing.combineUnordered(Arrays.asList(onePlusTwo,three)), Hashing.combineUnordered(Arrays.asList(one,twoPlusThree)) );

    }
}
