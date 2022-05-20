package bbidder.utils;

public class SingletonStream<E> implements MyStream<E> {
    final E o;
    boolean consumed = false;

    public SingletonStream(E o) {
        super();
        this.o = o;
    }

    @Override
    public E next() {
        if (consumed) {
           return null; 
        }
        consumed = true;
        return o;
    }

}
