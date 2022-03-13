package bbidder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class StopperSetTest {
    @Test
    public void testValueOf() {
        StopperSet ss = new StopperSet(List.of(Stoppers.EMPTY.withStopperIn(0).withStopperIn(1), Stoppers.EMPTY.withStopperIn(2).withStopperIn(1)));
        assertFalse(ss.stopperIn(0));
        assertTrue(ss.stopperIn(1));
        assertFalse(ss.stopperIn(2));
    }

    @Test
    public void testOr() {
        StopperSet ss1 = new StopperSet(List.of(Stoppers.EMPTY.withStopperIn(0).withStopperIn(1)));
        StopperSet ss2 = new StopperSet(List.of(Stoppers.EMPTY.withStopperIn(2).withStopperIn(1)));
        assertEquals(ss1.or(ss2),
                new StopperSet(List.of(Stoppers.EMPTY.withStopperIn(0).withStopperIn(1), Stoppers.EMPTY.withStopperIn(2).withStopperIn(1))));
    }

    @Test
    public void testAnd() {
        StopperSet ss1 = new StopperSet(List.of(Stoppers.EMPTY.withStopperIn(0).withStopperIn(1), Stoppers.EMPTY.withStopperIn(2)));
        StopperSet ss2 = new StopperSet(List.of(Stoppers.EMPTY.withStopperIn(2).withStopperIn(1), Stoppers.EMPTY.withStopperIn(2)));
        assertEquals(ss1.and(ss2), new StopperSet(List.of(Stoppers.EMPTY.withStopperIn(2))));
    }
}
