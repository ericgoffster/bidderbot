package bbidder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StopperTest {
    @Test
    public void testBalanced() {
        Stoppers nostoppers = Stoppers.EMPTY;
        assertFalse(nostoppers.stopperIn(0));
        assertTrue(nostoppers.withStopperIn(0).stopperIn(0));
        assertFalse(nostoppers.withStopperIn(0).stopperIn(1));
        assertTrue(nostoppers.withStopperIn(0).withStopperIn(1).stopperIn(0));
        assertTrue(nostoppers.withStopperIn(0).withStopperIn(1).stopperIn(1));
        assertFalse(nostoppers.withStopperIn(0).withStopperIn(1).stopperIn(2));
    }
}
