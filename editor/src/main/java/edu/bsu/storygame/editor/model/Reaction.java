package edu.bsu.storygame.editor.model;

public class Reaction {

    public String name;
    public Story story;

    public static Reaction emptyReaction() {
        return new Reaction("", Story.emptyStory());
    }

    public Reaction(String name, Story story) {
        this.name = name;
        this.story = story;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reaction reaction = (Reaction) o;

        if (name != null ? !name.equals(reaction.name) : reaction.name != null) return false;
        return story != null ? story.equals(reaction.story) : reaction.story == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (story != null ? story.hashCode() : 0);
        return result;
    }
}
