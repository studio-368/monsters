/*
 * Copyright 2016 Paul Gestwicki
 *
 * This file is part of Traveler's Notebook: Monster Tales
 *
 * Traveler's Notebook: Monster Tales is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Traveler's Notebook: Monster Tales is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Traveler's Notebook: Monster Tales.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.bsu.storygame.editor.model;

public class SkillTrigger {
    public String skill;
    public Conclusion conclusion;

    public static final String[] availableSkills = {
            null,
            "Athleticism",
            "Logic",
            "Magic",
            "Persuasion",
            "Stealth",
            "Weapon Use"
    };

    public SkillTrigger(String skill, Conclusion conclusion) {
        this.skill = skill;
        this.conclusion = conclusion;
    }

    @Override
    public String toString() {
        if (skill == null) {
            return "No skill";
        }
        return skill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SkillTrigger that = (SkillTrigger) o;

        if (skill != null ? !skill.equals(that.skill) : that.skill != null) return false;
        return conclusion != null ? conclusion.equals(that.conclusion) : that.conclusion == null;

    }

    @Override
    public int hashCode() {
        int result = skill != null ? skill.hashCode() : 0;
        result = 31 * result + (conclusion != null ? conclusion.hashCode() : 0);
        return result;
    }
}
