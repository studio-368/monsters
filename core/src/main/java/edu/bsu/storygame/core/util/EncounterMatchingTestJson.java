/*
 * Copyright 2016 Traveler's Notebook: Monster Tales project authors
 *
 * This file is part of monsters
 *
 * monsters is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * monsters is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with monsters.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.bsu.storygame.core.util;

import edu.bsu.storygame.core.model.*;

public class EncounterMatchingTestJson {

    private static final Skill LOGIC = Skill.named("Logic");
    private static final Skill MAGIC = Skill.named("Magic");

    public static Encounter create() {
        return Encounter.with("Cockatrice")
                .image("pic")
                .reaction(Reaction.create("Fight")
                        .story(Story.withText("Story 1")
                                .trigger(SkillTrigger.skill(LOGIC)
                                        .conclusion(new Conclusion.Builder().text("Conclusion 1").points(1).build()))
                                .trigger(new NoSkillTrigger(new Conclusion.Builder().text("Conclusion 2").skill(Skill.named("RewardSkill")).build()))
                                .build()).build())
                .reaction(Reaction.create("Hide")
                        .story(Story.withText("Story 2")
                                .trigger(new NoSkillTrigger(new Conclusion.Builder().text("Conclusion 1-A").points(1).skill(Skill.named("RewardSkill-2")).build()))
                                .trigger(SkillTrigger.skill(MAGIC)
                                        .conclusion(new Conclusion.Builder().text("Conclusion 2-B").build()))
                                .build()).build())
                .build();
    }

}
