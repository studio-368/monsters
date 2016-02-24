package edu.bsu.storygame.editor.model;

import java.util.ArrayList;
import java.util.List;

public class Story {
    public String text;
    public List<SkillTrigger> triggers;

    public Story(String text, List<SkillTrigger> triggers) {
        this.text = text;
        this.triggers = new ArrayList<>(triggers);
    }
}
