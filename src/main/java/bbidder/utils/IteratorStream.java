package bbidder.utils;

import java.util.Iterator;

public class IteratorStream<E> implements MyStream<E> {
    final Iterator<E> stream;

    public IteratorStream(Iterator<E> stream) {
        super();
        this.stream = stream;
    }

    @Override
    public E next() {
        if (!stream.hasNext()) {
           return null; 
        }
        return stream.next();
    }

}
