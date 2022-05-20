package bbidder;

import java.util.Objects;
import java.util.Set;

public final class TaggedBoundInference {
    public final IBoundInference inf;
    public final Set<String> tags;
    public TaggedBoundInference(IBoundInference inf, Set<String> tags) {
        super();
        this.inf = inf;
        this.tags = tags;
    }
    @Override
    public int hashCode() {
        return Objects.hash(inf, tags);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TaggedBoundInference other = (TaggedBoundInference) obj;
        return Objects.equals(inf, other.inf) && Objects.equals(tags, other.tags);
    }
    @Override
    public String toString() {
        return inf + ":" + tags;
    }
}
