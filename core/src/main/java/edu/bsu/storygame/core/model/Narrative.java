package edu.bsu.storygame.core.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

public final class Narrative {

    public static final class Builder {
        private final Map<Region, EncounterDeck> map = Maps.newEnumMap(Region.class);

        public Builder() {
        }

        public Builder put(Region region, EncounterDeck deck) {
            this.map.put(region, deck);
            return this;
        }

        public Narrative build() {
            return new Narrative(this);
        }
    }

    private final ImmutableMap<Region, EncounterDeck> map;

    private Narrative(Builder builder) {
        this.map = ImmutableMap.copyOf(builder.map);
    }

    public EncounterDeck forRegion(Region region) {
        if (!map.containsKey(region)) {
            throw new IllegalArgumentException("No deck for region " + region);
        }
        return map.get(region);
    }
}
