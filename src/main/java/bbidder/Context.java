package bbidder;

public interface Context {
    int resolvePoints(String s);

    int lookupSuit(String s);

    short lookupSuitSet(String s);

    int resolveLength(String s);
}
