package bbidder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StopperTest {
    @Test
    public void testBalanced() {
        assertFalse(new Stoppers((byte) 0).stopperIn(0));
        assertTrue(new Stoppers((byte) 0).withStopperIn(0).stopperIn(0));
        assertFalse(new Stoppers((byte) 0).withStopperIn(0).stopperIn(1));
        assertTrue(new Stoppers((byte) 0).withStopperIn(0).withStopperIn(1).stopperIn(0));
        assertTrue(new Stoppers((byte) 0).withStopperIn(0).withStopperIn(1).stopperIn(1));
        assertFalse(new Stoppers((byte) 0).withStopperIn(0).withStopperIn(1).stopperIn(2));
    }
}
