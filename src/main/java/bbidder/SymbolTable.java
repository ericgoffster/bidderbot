package bbidder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SymbolTable {
    public static SymbolTable EMPTY = new SymbolTable(Map.of());
    private final Map<String, Integer> suits;
    private SymbolTable(Map<String, Integer> suits) {
        this.suits = suits;
    }
    public SymbolTable add(String symbol, int value) {
        Map<String, Integer> newSuits = new HashMap<>(suits);
        newSuits.put(symbol, value);
        return new SymbolTable(newSuits);
    }
    
    public boolean containsValue(int v) {
        return suits.containsValue(v);
    }
    
    public SymbolTable add(SymbolTable other) {
        Map<String, Integer> newSuits = new HashMap<>(suits);
        newSuits.putAll(other.suits);
        return new SymbolTable(newSuits);
    }
    
    @Override
    public String toString() {
        return suits.toString();
    }
    @Override
    public int hashCode() {
        return Objects.hash(suits);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SymbolTable other = (SymbolTable) obj;
        return Objects.equals(suits, other.suits);
    }
    public boolean containsKey(String string) {
        return suits.containsKey(string);
    }
    public Integer get(String string) {
        return suits.get(string);
    }
}
