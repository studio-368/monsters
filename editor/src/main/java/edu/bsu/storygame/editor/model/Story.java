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

package edu.bsu.storygame.editor.model;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class Story {
    public String text;
    public List<SkillTrigger> triggers;

    public static Story emptyStory() {
        return new Story("", Lists.newArrayList(
                new SkillTrigger("No skill", Conclusion.emptyConclusion())));
    }

    public Story(String text, List<SkillTrigger> triggers) {
        this.text = text;
        this.triggers = new ArrayList<>(triggers);
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Story story = (Story) o;

        if (text != null ? !text.equals(story.text) : story.text != null) return false;
        return triggers != null ? triggers.equals(story.triggers) : story.triggers == null;

    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (triggers != null ? triggers.hashCode() : 0);
        return result;
    }
}
