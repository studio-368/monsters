package edu.bsu.storygame.core.json;

import com.google.common.collect.Maps;
import edu.bsu.storygame.core.model.Skill;

import java.util.Map;

public class SkillParser {

    private final Map<String, Skill> cachedSkills = Maps.newHashMap();

    public Skill parse(String name) {
        String lowercase = name.toLowerCase();
        if (cachedSkills.containsKey(lowercase)) {
            return cachedSkills.get(lowercase);
        } else {
            Skill skill = Skill.named(lowercase);
            cachedSkills.put(lowercase, skill);
            return skill;
        }
    }
}
