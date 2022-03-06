package bbidder;

public interface IBoundInference {
    public boolean matches(Hand hand);
    public boolean negatable();
    public IBoundInference negate();
}
