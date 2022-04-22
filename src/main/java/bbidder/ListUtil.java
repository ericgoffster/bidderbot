package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class ListUtil {
    public static <T,U> List<T> map(List<U> list, Function<U, T> f) {
        List<T> l = new ArrayList<>(list.size());
        for(U u: list) {
            l.add(f.apply(u));
        }
        return l;
    }
    public static <T,U> List<T> flatMap(List<U> list, Function<U, List<T>> f) {
        List<T> l = new ArrayList<>(list.size());
        for(U u: list) {
            l.addAll(f.apply(u));
        }
        return l;
    }
}
