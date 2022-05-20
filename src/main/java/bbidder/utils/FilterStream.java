package bbidder.utils;

import java.util.function.Predicate;

public class FilterStream<E> implements MyStream<E> {
    final MyStream<E> stream;
    final Predicate<E> f;

    public FilterStream(MyStream<E> stream, Predicate<E> f) {
        super();
        this.stream = stream;
        this.f = f;
    }

    @Override
    public E next() {
        for(;;) {
            E o = stream.next();
            if (o == null) {
                return null;
            }
            if (f.test(o)) {
                return o;
            }
        }
    }

}
