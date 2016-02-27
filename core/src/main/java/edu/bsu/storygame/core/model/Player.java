package edu.bsu.storygame.core.model;

import com.google.common.collect.Lists;
import react.RList;
import react.Value;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

public class Player {

    public final String name;
    public final int color;
    public RList<Skill> skills;
    public Value<Integer> storyPoints = Value.create(0);
    public final Value<Region> location = Value.create(Region.AFRICA);

    public static class Builder {

        private String name;
        private int color;
        private List<Skill> skills;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder skills(List<Skill> skills) {
            checkState(this.skills == null, "Skills already specified");
            this.skills = Lists.newArrayList(skills);
            Collections.sort(this.skills);
            return this;
        }

        public Player build() {
            return new Player(this);
        }
    }

    private Player(Builder builder) {
        this.name = builder.name;
        this.color = builder.color;
        this.skills = RList.create(builder.skills);
    }

}

