package bbidder;

import java.util.stream.Stream;

public interface SuitSet {
    public short evaluate(Players players);

    public Stream<SuitSetContext> resolveSymbols(SuitTable suitTable);
}