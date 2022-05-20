package bbidder.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import bbidder.utils.IteratorStream;
import bbidder.utils.MyStream;

public class MyStreamTest {
    @Test
    public void test1() {
        MyStream<Integer> i = new IteratorStream<Integer>(List.of(4,5).iterator());
        assertEquals(4, i.next().intValue());
        assertEquals(5, i.next().intValue());
        assertNull(i.next());
    }
    @Test
    public void test2() {
        MyStream<Integer> i = new IteratorStream<Integer>(List.of(4,5).iterator()).map(f -> f + 1);
        assertEquals(5, i.next().intValue());
        assertEquals(6, i.next().intValue());
        assertNull(i.next());
    }
    @Test
    public void test3() {
        MyStream<Integer> i = new IteratorStream<Integer>(List.of(4,5).iterator()).flatMap(f -> new IteratorStream<Integer>(List.of(f + 1, f + 2).iterator()));
        assertEquals(5, i.next().intValue());
        assertEquals(6, i.next().intValue());
        assertEquals(6, i.next().intValue());
        assertEquals(7, i.next().intValue());
        assertNull(i.next());
    }
    @Test
    public void test4() {
        MyStream<Integer> i = new IteratorStream<Integer>(List.of(4,5).iterator()).concat(new IteratorStream<Integer>(List.of(6, 7).iterator()));
        assertEquals(4, i.next().intValue());
        assertEquals(5, i.next().intValue());
        assertEquals(6, i.next().intValue());
        assertEquals(7, i.next().intValue());
        assertNull(i.next());
    }
}
