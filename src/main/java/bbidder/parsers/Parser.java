package bbidder.parsers;

import java.io.IOException;

public interface Parser<T> {
    public T parse(Input inp) throws IOException;
}
