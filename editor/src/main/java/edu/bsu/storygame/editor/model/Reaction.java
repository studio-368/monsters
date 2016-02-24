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
}
