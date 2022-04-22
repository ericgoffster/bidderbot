package bbidder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SymbolParserTest {
    @Test
    public void test() {
        assertEquals(new ConstSymbol(3), SymbolParser.parseSymbol("S"));
        assertEquals(new ConstSymbol(3), SymbolParser.parseSymbol("s"));
        assertEquals(new ConstSymbol(1), SymbolParser.parseSymbol("D"));
        assertEquals(new VarSymbol("x"), SymbolParser.parseSymbol("x"));
        assertEquals(new OtherMinorSymbol(), SymbolParser.parseSymbol("om"));
        assertEquals(new OtherMajorSymbol(), SymbolParser.parseSymbol("OM"));
        assertEquals(new MinorSymbol(), SymbolParser.parseSymbol("m"));
        assertEquals(new MajorSymbol(), SymbolParser.parseSymbol("M"));
        assertEquals(new SteppedSymbol(new VarSymbol("x"), 1), SymbolParser.parseSymbol("x-1"));
        assertEquals(new DownSymbol(new VarSymbol("x")), SymbolParser.parseSymbol("x:down"));
        assertEquals(new DownSymbol(new VarSymbol("x")), SymbolParser.parseSymbol("x:Down"));
        assertEquals(new NotSymbol(new VarSymbol("x")), SymbolParser.parseSymbol("~x"));
    }
}
