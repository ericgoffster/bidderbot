package bbidder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import bbidder.parsers.SymbolParser;
import bbidder.symbols.ConstSymbol;
import bbidder.symbols.DownSymbol;
import bbidder.symbols.MajorSymbol;
import bbidder.symbols.MinorSymbol;
import bbidder.symbols.OtherMajorSymbol;
import bbidder.symbols.OtherMinorSymbol;
import bbidder.symbols.SteppedSymbol;
import bbidder.symbols.VarSymbol;

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
    }
}
