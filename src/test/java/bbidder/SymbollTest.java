package bbidder;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import bbidder.symbols.ConstSymbol;
import bbidder.symbols.MajorSymbol;
import bbidder.symbols.MinorSymbol;
import bbidder.symbols.OtherMajorSymbol;
import bbidder.symbols.OtherMinorSymbol;
import bbidder.symbols.SteppedSymbol;
import bbidder.symbols.VarSymbol;

public class SymbollTest {
    @Test
    public void testConst() {
        Symbol sym = new ConstSymbol(3);
        assertEquals(3, sym.getResolvedStrain());
        assertEquals("S", sym.toString());
    }

    @Test
    public void testOtherMinor() {
        Symbol sym = new OtherMinorSymbol();
        assertEquals("om", sym.toString());
    }

    @Test
    public void testMinor() {
        Symbol sym = new MinorSymbol();
        assertEquals("m", sym.toString());
    }

    @Test
    public void testOtherMajor() {
        Symbol sym = new OtherMajorSymbol();
        assertEquals("OM", sym.toString());
    }

    @Test
    public void testMajor() {
        Symbol sym = new MajorSymbol();
        assertEquals("M", sym.toString());
    }

    @Test
    public void testVar() {
        Symbol sym = new VarSymbol("x");
        assertEquals("x", sym.toString());
    }

    @Test
    public void testStepped() {
        Symbol sym = new SteppedSymbol(new VarSymbol("x"), 2);
        assertEquals("x-2", sym.toString());
    }
}
