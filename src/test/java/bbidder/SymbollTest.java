package bbidder;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

import bbidder.symbols.ConstSymbol;
import bbidder.symbols.DownSymbol;
import bbidder.symbols.MajorSymbol;
import bbidder.symbols.MinorSymbol;
import bbidder.symbols.NotSymbol;
import bbidder.symbols.OtherMajorSymbol;
import bbidder.symbols.OtherMinorSymbol;
import bbidder.symbols.SteppedSymbol;
import bbidder.symbols.VarSymbol;

public class SymbollTest {
    @Test
    public void testConst() {
        Symbol sym = new ConstSymbol(3);
        assertEquals(3, sym.evaluate(Map.of()).intValue());
        assertEquals(3, sym.getResolved());
        assertEquals("S", sym.toString());
    }

    @Test
    public void testOtherMinor() {
        Symbol sym = new OtherMinorSymbol();
        assertEquals(1, sym.evaluate(Map.of("m", 0)).intValue());
        assertEquals(0, sym.evaluate(Map.of("om", 0)).intValue());
        assertEquals(Map.of("om", 0), sym.unevaluate(0));
        assertEquals("om", sym.toString());
    }

    @Test
    public void testMinor() {
        Symbol sym = new MinorSymbol();
        assertEquals(0, sym.evaluate(Map.of("m", 0)).intValue());
        assertEquals(1, sym.evaluate(Map.of("om", 0)).intValue());
        assertEquals(Map.of("m", 0), sym.unevaluate(0));
        assertEquals("m", sym.toString());
    }

    @Test
    public void testOtherMajor() {
        Symbol sym = new OtherMajorSymbol();
        assertEquals(3, sym.evaluate(Map.of("M", 2)).intValue());
        assertEquals(2, sym.evaluate(Map.of("OM", 2)).intValue());
        assertEquals(Map.of("OM", 2), sym.unevaluate(2));
        assertEquals("OM", sym.toString());
    }

    @Test
    public void testMajor() {
        Symbol sym = new MajorSymbol();
        assertEquals(2, sym.evaluate(Map.of("M", 2)).intValue());
        assertEquals(3, sym.evaluate(Map.of("OM", 2)).intValue());
        assertEquals(Map.of("M", 2), sym.unevaluate(2));
        assertEquals("M", sym.toString());
    }

    @Test
    public void testVar() {
        Symbol sym = new VarSymbol("x");
        assertEquals(2, sym.evaluate(Map.of("x", 2)).intValue());
        assertEquals(Map.of("x", 2), sym.unevaluate(2));
        assertEquals("x", sym.toString());
    }

    @Test
    public void testDown() {
        Symbol sym = new DownSymbol(new VarSymbol("x"));
        assertEquals(2, sym.evaluate(Map.of("x", 2)).intValue());
        assertEquals(Map.of("x", 2), sym.unevaluate(2));
        assertEquals("x:down", sym.toString());
    }

    @Test
    public void testStepped() {
        Symbol sym = new SteppedSymbol(new VarSymbol("x"), 2);
        assertEquals(0, sym.evaluate(Map.of("x", 2)).intValue());
        assertEquals(4, sym.evaluate(Map.of("x", 1)).intValue());
        assertEquals(3, sym.evaluate(Map.of("x", 0)).intValue());
        assertEquals(Map.of("x", 2), sym.unevaluate(0));
        assertEquals(Map.of("x", 1), sym.unevaluate(4));
        assertEquals("x-2", sym.toString());
    }

    @Test
    public void testNot() {
        Symbol sym = new NotSymbol(new VarSymbol("x"));
        assertEquals(2, sym.evaluate(Map.of("x", 1, "~x", 2)).intValue());
        assertEquals(Map.of("~x", 2), sym.unevaluate(2));
        assertEquals("~x", sym.toString());
    }
}
