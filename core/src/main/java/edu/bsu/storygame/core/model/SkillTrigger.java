package edu.bsu.storygame.core.model;

import static com.google.common.base.Preconditions.checkNotNull;

public class SkillTrigger {
    public final Skill skill;
    public final String story;
    public SkillTrigger(Skill skill, String story) {
        this.skill = checkNotNull(skill);
        this.story = checkNotNull(story);
    }
}
