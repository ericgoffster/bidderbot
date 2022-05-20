package bbidder.parsers;

import java.io.IOException;

import bbidder.RangeOf;

public class RangeParser implements Parser<RangeOf> {
    @Override
    public RangeOf parse(Input inp) throws IOException {
        if (inp.readKeyword("MAX")) {
            return new RangeOf(null, null, true);
        }
        inp.advanceWhite();
        if (!Character.isDigit(inp.ch)) {
            return null;
        }
        String n1 = inp.readToken(ch -> Character.isDigit(ch));
        if (inp.readKeyword("-")) {
            inp.advanceWhite();
            if (Character.isDigit(inp.ch)) {
                String n2 = inp.readToken(ch -> Character.isDigit(ch));
                return new RangeOf(Integer.parseInt(n1), Integer.parseInt(n2), false);
            }
            return new RangeOf(null, Integer.parseInt(n1), false);
        }
        if (inp.readKeyword("+")) {
            return new RangeOf(Integer.parseInt(n1), null, false);
        }
        try {
            return new RangeOf(Integer.parseInt(n1), Integer.parseInt(n1), false);
        } catch (NumberFormatException e) {
            throw e;
        }
    }
}
