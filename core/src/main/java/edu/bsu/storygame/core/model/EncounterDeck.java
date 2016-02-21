package edu.bsu.storygame.core.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class EncounterDeck {


    public static final class Builder {
        private List<Encounter> encounters = Lists.newArrayList();

        public Builder add(Encounter... encounters) {
            this.encounters.addAll(Arrays.asList(encounters));
            return this;
        }

        public EncounterDeck build() {
            return new EncounterDeck(encounters);
        }
    }

    public static EncounterDeck fromArray(Encounter[] encounters) {
        return new EncounterDeck(Arrays.asList(encounters));
    }

    private final ImmutableList<Encounter> encounters;

    private EncounterDeck(Collection<Encounter> encounters) {
        this.encounters = ImmutableList.copyOf(encounters);
    }

    public int size() {
        return encounters.size();
    }

    public Encounter chooseOne() {
        return encounters.get((int) (encounters.size() * Math.random()));
    }
}
