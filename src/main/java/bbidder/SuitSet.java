package bbidder;

public interface SuitSet {
    public short evaluate(Players players);

    public SuitSet replaceVars(InferenceContext bc);
}