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

import java.util.List;

public class Region {
    public String region;
    public List<Encounter> encounters;

    public Region(String region, List<Encounter> encounters) {
        this.region = region;
        this.encounters = encounters;
    }

    @Override
    public String toString() {
        return region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Region region1 = (Region) o;

        if (region != null ? !region.equals(region1.region) : region1.region != null) return false;
        return encounters != null ? encounters.equals(region1.encounters) : region1.encounters == null;

    }

    @Override
    public int hashCode() {
        int result = region != null ? region.hashCode() : 0;
        result = 31 * result + (encounters != null ? encounters.hashCode() : 0);
        return result;
    }
}
