package bbidder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A map of strings to suits.
 * @author goffster
 *
 */
public final class SuitTable {
    public static SuitTable EMPTY = new SuitTable(Map.of());
    private final Map<String, Integer> suitMap;

    private SuitTable(Map<String, Integer> suitMap) {
        this.suitMap = suitMap;
    }

    /**
     * @param name
     *            The name to add
     * @param suit
     *            The value to add
     * @return A new Symbol table with the given symbol added.
     */
    public SuitTable withSuitAdded(String name, int suit) {
        Map<String, Integer> newSuits = new HashMap<>(suitMap);
        newSuits.put(name, suit);
        return new SuitTable(newSuits);
    }
    
    @Override
    public String toString() {
        return "{" + String.join(",",
                suitMap.entrySet().stream().map(e -> e.getKey() + "=" + Strain.getName(e.getValue())).collect(Collectors.toList()))
                + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(suitMap);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SuitTable other = (SuitTable) obj;
        return Objects.equals(suitMap, other.suitMap);
    }

    /**
     * @param name The name to lookup.
     * @return The suit associated with the name.  Null if not found.
     */
    public Integer getSuit(String name) {
        return suitMap.get(name);
    }

    /**
     * @return A bitmap field contains all suits in the suit table.
     */
    public int getSuits() {
        int patt = 0;
        for (int v : suitMap.values()) {
            patt |= (1 << v);
        }
        return patt;
    }
}
