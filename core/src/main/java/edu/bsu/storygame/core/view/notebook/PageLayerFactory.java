/*
 * Copyright 2016 Traveler's Notebook: Monster Tales project authors
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

package edu.bsu.storygame.core.view.notebook;

import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Player;
import pythagoras.f.Dimension;
import pythagoras.f.IDimension;
import tripleplay.ui.Interface;

import static com.google.common.base.Preconditions.checkNotNull;

public class PageLayerFactory {
    private final Interface iface;
    private final GameContext context;
    private final Player player;
    private final IDimension closedSize;

    public PageLayerFactory(Interface iface, GameContext context, Player player, IDimension closedSize) {
        this.iface = checkNotNull(iface);
        this.context = checkNotNull(context);
        this.player = checkNotNull(player);
        this.closedSize = new Dimension(closedSize);
    }

    public CoverPage createCover() {
        return new CoverPage(iface, context, player);
    }

    public EncounterPage createEncounterPage() {
        return new EncounterPage(iface, context, player);
    }

    public ReactionPage createReactionPage() {
        return new ReactionPage(iface, context, player);
    }

    public PageLayer[] createAll() {
        return new PageLayer[]{
                createCover(),
                createEncounterPage(),
                createReactionPage()
        };
    }
}
