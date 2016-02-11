package edu.bsu.storygame.core.model;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class Reaction {
    public final String name;
    public final String story;
    public final ImmutableList<SkillTrigger> triggers;

    public Reaction(String name, String story, List<SkillTrigger> triggers) {
        this.name = checkNotNull(name);
        this.story = checkNotNull(story);
        this.triggers = ImmutableList.copyOf(triggers);
    }
}
