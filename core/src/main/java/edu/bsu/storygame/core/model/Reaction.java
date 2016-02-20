package edu.bsu.storygame.core.model;

import com.google.common.base.MoreObjects;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public final class Reaction {

    public static Builder create(String name) {
        return new Builder(name);
    }

    public static final class Builder {
        private final String name;
        private Story story;

        private Builder(String name) {
            this.name = checkNotNull(name);
        }

        public Reaction story(Story story) {
            this.story = checkNotNull(story);
            return new Reaction(this);
        }
    }

    public final String name;
    public final Story story;

    private Reaction(Builder builder) {
        this.name = builder.name;
        this.story = builder.story;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("story", story)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof Reaction) {
            Reaction other = (Reaction) obj;
            return Objects.equals(this.name, other.name)
                    && Objects.equals(this.story, other.story);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, story);
    }
}
