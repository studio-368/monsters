package edu.bsu.storygame.core.model;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class Reaction {
    public final String name;
    public final Story story;


    public Reaction(String name, Story story) {
        this.name = checkNotNull(name);
        this.story = checkNotNull(story);
    }
}
