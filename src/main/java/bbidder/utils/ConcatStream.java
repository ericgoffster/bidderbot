package bbidder.utils;

public class ConcatStream<E> implements MyStream<E> {
    final MyStream<E> stream1;
    final MyStream<E> stream2;
    MyStream<E> which = null;

    public ConcatStream(MyStream<E> stream1, MyStream<E> stream2) {
        super();
        this.stream1 = stream1;
        this.stream2 = stream2;
        which = stream1;
    }

    @Override
    public E next() {
        E o = which.next();
        if (o == null) {
            if (which == stream2) {
                return null;
            }
            which = stream2;
            o = which.next();
        }
        return o;
    }

}
