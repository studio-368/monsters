package edu.bsu.storygame.core;

import edu.bsu.storygame.core.model.Encounter;
import edu.bsu.storygame.core.model.Region;

import java.util.List;

public interface EncounterConfiguration {
    List<Encounter> encountersFor(Region region);
}
