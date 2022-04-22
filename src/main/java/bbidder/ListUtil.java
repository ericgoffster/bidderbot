package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class ListUtil {
    public static <T,U> List<T> map(Iterable<U> list, Function<U, T> f) {
        List<T> l = new ArrayList<>();
        for(U u: list) {
            l.add(f.apply(u));
        }
        return l;
    }
    public static <T,U> List<T> flatMap(Iterable<U> list, Function<U, List<T>> f) {
        List<T> l = new ArrayList<>();
        for(U u: list) {
            l.addAll(f.apply(u));
        }
        return l;
    }
}
