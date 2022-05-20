package bbidder.utils;

import java.util.function.Function;

public class MappedStream<E, F> implements MyStream<E> {
    final MyStream<F> stream;
    final Function<F, E> f;

    public MappedStream(MyStream<F> stream, Function<F, E> f) {
        super();
        this.stream = stream;
        this.f = f;
    }

    @Override
    public E next() {
        F o = stream.next();
        if (o == null) {
            return null;
        }
        return f.apply(o);
    }

}
