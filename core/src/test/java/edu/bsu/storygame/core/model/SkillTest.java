package edu.bsu.storygame.core.model;

import com.google.common.testing.EqualsTester;
import org.junit.Test;

public class SkillTest {

    @Test
    public void testEquals() {
        new EqualsTester().addEqualityGroup(
                Skill.named("weapon use"),
                Skill.named("Weapon Use")
        ).addEqualityGroup(Skill.named("magic"),
                Skill.named("Magic"))
                .testEquals();
    }
}
