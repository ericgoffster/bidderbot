package bbidder.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import bbidder.utils.IteratorStream;
import bbidder.utils.MyIntStream;
import bbidder.utils.MyStream;

public class MyStreamTest {
    @Test
    public void test1() {
        MyStream<Integer> i = new IteratorStream<Integer>(List.of(4, 5).iterator());
        assertEquals(4, i.next().intValue());
        assertEquals(5, i.next().intValue());
        assertNull(i.next());
    }

    @Test
    public void test2() {
        MyStream<Integer> i = new IteratorStream<Integer>(List.of(4, 5).iterator()).map(f -> f + 1);
        assertEquals(5, i.next().intValue());
        assertEquals(6, i.next().intValue());
        assertNull(i.next());
    }

    @Test
    public void test3() {
        MyStream<Integer> i = new IteratorStream<Integer>(List.of(4, 5).iterator())
                .flatMap(f -> new IteratorStream<Integer>(List.of(f + 1, f + 2).iterator()));
        assertEquals(5, i.next().intValue());
        assertEquals(6, i.next().intValue());
        assertEquals(6, i.next().intValue());
        assertEquals(7, i.next().intValue());
        assertNull(i.next());
    }

    @Test
    public void test4() {
        MyStream<Integer> i = new IteratorStream<Integer>(List.of(4, 5).iterator()).concat(new IteratorStream<Integer>(List.of(6, 7).iterator()));
        assertEquals(4, i.next().intValue());
        assertEquals(5, i.next().intValue());
        assertEquals(6, i.next().intValue());
        assertEquals(7, i.next().intValue());
        assertNull(i.next());
    }

    @Test
    public void test5() {
        assertNull(MyStream.empty().next());
        MyStream<Integer> i = MyStream.of(5);
        assertEquals(5, i.next().intValue());
        assertNull(i.next());
    }

    @Test
    public void test6() {
        MyStream<Integer> i = MyStream.ofOptional(Optional.of(5));
        assertEquals(5, i.next().intValue());
        assertNull(i.next());
    }

    @Test
    public void test7() {
        MyStream<Integer> i = MyStream.ofOptional(Optional.empty());
        assertNull(i.next());
    }

    @Test
    public void test8() {
        MyStream<Integer> i = new MyIntStream(new int[] { 4, 5, 6 });
        assertEquals(4, i.next().intValue());
        assertEquals(5, i.next().intValue());
        assertEquals(6, i.next().intValue());
        assertNull(i.next());
    }

    @Test
    public void test9() {
        MyStream<Integer> i = new MyIntStream(new int[] { 4, 5, 6 });
        assertEquals(15, i.reduce(0, (a, b) -> a + b, (a, b) -> a + b).intValue());
    }
}
