package edu.bsu.storygame.editor.model;

import java.util.ArrayList;
import java.util.List;

public class Story {
    public String text;
    public List<SkillTrigger> triggers;

    public static Story emptyStory() {
        return new Story("", new ArrayList<>());
    }

    public Story(String text, List<SkillTrigger> triggers) {
        this.text = text;
        this.triggers = new ArrayList<>(triggers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Story story = (Story) o;

        if (text != null ? !text.equals(story.text) : story.text != null) return false;
        return triggers != null ? triggers.equals(story.triggers) : story.triggers == null;

    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (triggers != null ? triggers.hashCode() : 0);
        return result;
    }
}
