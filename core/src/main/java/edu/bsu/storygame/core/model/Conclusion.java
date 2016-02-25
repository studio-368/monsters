package edu.bsu.storygame.core.model;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public final class Conclusion {

    public static class Builder {
        private String text;
        private int points = 0;
        private String skill = null;

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder points(int points) {
            this.points = points;
            return this;
        }

        public Builder skill(String skill) {
            this.skill = skill;
            return this;
        }

        public Conclusion build() {
            return new Conclusion(this);
        }
    }

    public final String text;
    public final int points;
    public final String skill;

    private Conclusion(Builder importer) {
        this.text = importer.text;
        this.points = importer.points;
        this.skill = importer.skill;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("text", text)
                .add("points", points)
                .add("skill", skill)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof Conclusion) {
            Conclusion other = (Conclusion) obj;
            return Objects.equals(this.text, other.text)
                    && Objects.equals(this.points, other.points)
                    && Objects.equals(this.skill, other.skill);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, points, skill);
    }
}
