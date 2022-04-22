package bbidder;

public final class GeneralityContext {
    public final Generality generality;

    public final SymbolTable suits;

    public GeneralityContext(Generality generality, SymbolTable suits) {
        super();
        this.generality = generality;
        this.suits = suits;
    }
}