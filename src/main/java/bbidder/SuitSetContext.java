package bbidder;

/**
 * Represents a suitsets in the context of a symbol table.
 * 
 * @author goffster
 *
 */
public final class SuitSetContext {
    public final SuitSet suitSet;
    public final SuitTable suitTable;

    public SuitSetContext(SuitSet ss, SuitTable suitTable) {
        super();
        this.suitSet = ss;
        this.suitTable = suitTable;
    }
}