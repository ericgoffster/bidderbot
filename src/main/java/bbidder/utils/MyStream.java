package bbidder.utils;

import java.util.function.Function;

public interface MyStream<E> {
    public E next();
    default <F> MyStream<F> map(Function<E, F> f) {
        return new MappedStream<F, E>(this, f);
    }
    default <F> MyStream<F> flatMap(Function<E, MyStream<F>> f) {
        return new FlatMappedStream<F, E>(this, f);
    }
    default MyStream<E> concat(MyStream<E> s1) {
        return new ConcatStream<E>(this, s1);
    }
}
