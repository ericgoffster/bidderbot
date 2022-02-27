package bbidder;

public interface Context {
    int resolvePoints(String s);

    int lookupSuit(String s);

    int lookupSuitSet(String s);

    int resolveLength(String s);
}
