package edu.bsu.storygame.core.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class Encounter {

    private static final int APPROXIMATE_MAX_CAPACITY = 4;

    public static Builder with(String name) {
        return new Builder(name);
    }

    public static final class Builder {
        private String name;
        private String imageKey;
        private List<Reaction> reactions = Lists.newArrayListWithCapacity(APPROXIMATE_MAX_CAPACITY);

        private Builder(String name) {
            this.name = checkNotNull(name);
        }

        public Builder image(String path) {
            this.imageKey = checkNotNull(path);
            return this;
        }

        public Builder reaction(Reaction reaction) {
            checkNotNull(reaction);
            this.reactions.add(reaction);
            return this;
        }

        public Encounter build() {
            return new Encounter(this);
        }
    }

    public final String name;
    public final String imageKey;
    public final ImmutableList<Reaction> reactions;

    private Encounter(Builder builder) {
        this.name = builder.name;
        this.imageKey = checkNotNull(builder.imageKey);
        this.reactions = ImmutableList.copyOf(builder.reactions);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("imageKey", imageKey)
                .add("reactions", reactions)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, imageKey, reactions);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof Encounter) {
            Encounter other = (Encounter) obj;
            return Objects.equals(this.name, other.name)
                    && Objects.equals(this.imageKey, other.imageKey)
                    && Objects.equals(this.reactions, other.reactions);
        } else {
            return false;
        }
    }
}
