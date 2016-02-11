package edu.bsu.storygame.core.model;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Story {
    public final String text;
    public final ImmutableList<SkillTrigger> triggers;

    public Story(String text, List<SkillTrigger> triggers) {
        this.text = checkNotNull(text);
        checkArgument(!text.trim().isEmpty(), "Text may not be empty");
        this.triggers = ImmutableList.copyOf(triggers);
        checkArgument(!triggers.isEmpty(), "Must have at least one trigger");
    }
}
