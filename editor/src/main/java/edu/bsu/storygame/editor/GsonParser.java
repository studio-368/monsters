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

package edu.bsu.storygame.editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.bsu.storygame.editor.model.Narrative;
import edu.bsu.storygame.editor.model.Region;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonParser {

    private final Gson gson;

    public GsonParser() {
        this(new GsonBuilder().setPrettyPrinting().create());
    }

    public GsonParser(Gson gson) {
        this.gson = gson;
    }

    public Narrative parse(String json) {
        Type listType = new TypeToken<ArrayList<Region>>() {
        }.getType();
        List<Region> regions = gson.fromJson(json, listType);
        return new Narrative(regions);
    }

    public String convertToJson(Narrative narrative) {
        return gson.toJson(narrative.regions);
    }
}
