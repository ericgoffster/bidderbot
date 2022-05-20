package bbidder;

public enum Position {
    ME("i"),
    RHO("rho"),
    PARTNER("partner"),
    LHO("lho");
    
    final String name;

    private Position(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    public Position getOpposite() {
        switch(this) {
        case ME: return PARTNER;
        case PARTNER: return ME;
        case LHO: return RHO;
        case RHO: return LHO;
        default:
            throw new IllegalStateException();
        }
    }
    
    public static Position getPosition(String name) {
        switch(name.toUpperCase()) {
        case "I":
        case "ME":
            return ME;
        case "PARTNER":
            return PARTNER;
        case "LHO":
            return LHO;
        case "RHO":
            return RHO;
        default:
            throw new IllegalArgumentException();
        }
    }
}
