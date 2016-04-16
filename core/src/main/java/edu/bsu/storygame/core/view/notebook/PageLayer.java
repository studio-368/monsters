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
import playn.scene.GroupLayer;
import playn.scene.Layer;
import react.Slot;
import tripleplay.ui.Interface;
import tripleplay.ui.Root;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class PageLayer extends GroupLayer {

    protected final Interface iface;
    protected final GameContext context;
    protected final Player player;
    protected Root root;

    public PageLayer(final Interface iface, final GameContext context, final Player player) {
        this.iface = checkNotNull(iface);
        this.context = checkNotNull(context);
        this.player = checkNotNull(player);
        onAdded(new Slot<Layer>() {
            @Override
            public void onEmit(Layer layer) {
                root = createRoot();
            }
        });
        onRemoved(new Slot<Layer>() {
            @Override
            public void onEmit(Layer layer) {
                iface.removeRoot(root);
            }
        });
    }

    protected abstract Root createRoot();
}
