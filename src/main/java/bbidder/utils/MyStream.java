package bbidder.utils;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface MyStream<E> {
    public E next();
    default Optional<E> findFirst() {
        return Optional.ofNullable(next());
    }
    default MyStream<E> filter(Predicate<E> p) {
        return new FilterStream<E>(this, p);
    }
    default <F> MyStream<F> map(Function<E, F> f) {
        return new MappedStream<F, E>(this, f);
    }
    default <F> MyStream<F> flatMap(Function<E, MyStream<F>> f) {
        return new FlatMappedStream<F, E>(this, f);
    }
    default MyStream<E> concat(MyStream<E> s1) {
        return new ConcatStream<E>(this, s1);
    }
    public static <E> MyStream<E> ofOptional(Optional<E> s1) {
        return s1.isEmpty() ? empty() : of(s1.get());
    }
    public static <E> MyStream<E> of(E o) {
        return new SingletonStream<E>(o);
    }
    public static <E> MyStream<E> empty() {
        return new MyStream<E>() {

            @Override
            public E next() {
                return null;
            }
            
        };
    }
    default void forEach(Consumer<E> cons) {
        for(;;) {
            E o = next();
            if (o == null) {
                break;
            }
            cons.accept(o);
        }
    }
    
    default Iterator<E> iterator() {
        return new Iterator<E>() {
            E o = MyStream.this.next();

            @Override
            public boolean hasNext() {
                return o != null;
            }

            @Override
            public E next() {
                E res = o;
                o = MyStream.this.next();
                return res;
            }
            
        };
    }
    default <U> U reduce(U identity,
            BiFunction<U, ? super E, U> accumulator,
            BinaryOperator<U> combiner) {
        U res = identity;
        for(;;) {
            E o = next();
            if (o == null) {
                break;
            }
            res = accumulator.apply(res, o);
        }
        return res;
    }
}
