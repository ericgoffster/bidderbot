package bbidder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class TagSet {
    public static TagSet EMPTY = new TagSet(Set.of());
    private final Set<String> tagSet;

    private TagSet(Set<String> tagSet) {
        this.tagSet = tagSet;
    }

    public TagSet addTag(String name) {
        Set<String> newTags = new HashSet<>(tagSet);
        newTags.add(name);
        return new TagSet(newTags);
    }

    public TagSet and(TagSet rho) {
        Set<String> s = new HashSet<>(tagSet);
        s.retainAll(rho.tagSet);
        return new TagSet(s);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String str : tagSet) {
            sb.append(":\"" + str + "\"");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagSet);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TagSet other = (TagSet) obj;
        return Objects.equals(tagSet, other.tagSet);
    }

    public boolean isEmpty() {
        return tagSet.isEmpty();
    }

    public boolean contains(String tag) {
        return tagSet.contains(tag);
    }
}
