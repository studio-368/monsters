package edu.bsu.storygame.core.model;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public final class Skill implements Comparable<Skill> {

    public static Skill named(String name) {
        String normalized = toTitleCase(name);
        return new Skill(normalized);
    }

    private static String toTitleCase(String name) {
        return String.valueOf(Character.toUpperCase(name.charAt(0))) +
                name.substring(1).toLowerCase();
    }

    public final String name;

    private Skill(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("name", name).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof Skill) {
            Skill other = (Skill) obj;
            return this.name.equals(other.name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    // IntelliJ IDEA wants to annotate the method with @NotNull, but that's inferred--not inherited--and we
    // don't want to confuse the rest of the compiler settings.
    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(Skill other) {
        return this.name.compareTo(other.name);
    }
}
