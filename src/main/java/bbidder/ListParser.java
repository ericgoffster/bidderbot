package bbidder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ListParser<T> implements Parser<List<T>> {

    public final Parser<T> elementParser;
    public final String delimiter;

    public ListParser(Parser<T> elementParser, String delimiter) {
        super();
        this.elementParser = elementParser;
        this.delimiter = delimiter;
    }

    @Override
    public List<T> parse(Input inp) throws IOException {
        List<T> l = new ArrayList<>();
        for (;;) {
            T elem = elementParser.parse(inp);
            if (elem == null) {
                break;
            }
            l.add(elem);
            if (delimiter.length() > 0) {
                while (Character.isWhitespace(inp.ch)) {
                    inp.advance();
                }
                if (!inp.readKeyword(delimiter)) {
                    return l;
                }
            }
        }
        return l;
    }
}
