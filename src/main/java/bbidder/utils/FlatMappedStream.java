package bbidder.utils;

import java.util.function.Function;

public class FlatMappedStream<E, F> implements MyStream<E> {
    final MyStream<F> stream;
    final Function<F, MyStream<E>> f;
    MyStream<E> curr;

    public FlatMappedStream(MyStream<F> stream, Function<F, MyStream<E>> f) {
        super();
        this.stream = stream;
        this.f = f;
        this.curr = null;
    }

    @Override
    public E next() {
        if (curr == null) {
            if (!advance()) {
                return null;
            }
        }
        for(;;) {
            E o = curr.next();
            if (o != null) {
                return o;
            }
            if (!advance()) {
                return null;
            }
        }
    }

    private boolean advance() {
        F o = stream.next();
        if (o == null) {
            return false;
        }
        curr = f.apply(o);
        return true;
    }

}
