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

package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import playn.scene.GroupLayer;
import tripleplay.game.ScreenStack;

public abstract class BoundedUIScreen extends ScreenStack.UIScreen {

    protected final GroupLayer content;

    protected BoundedUIScreen(MonsterGame game) {
        super(game);
        this.content = new GroupLayer(game.bounds.width(), game.bounds.height());
        this.layer.addCenterAt(content, game.plat.graphics().viewSize.width() / 2, game.plat.graphics().viewSize.height() / 2);
    }
}
