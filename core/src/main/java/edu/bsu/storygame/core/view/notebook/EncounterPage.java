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

import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.assets.Typeface;
import edu.bsu.storygame.core.model.Encounter;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Player;
import edu.bsu.storygame.core.util.IconScaler;
import edu.bsu.storygame.core.view.GameStyle;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.util.Colors;

import static com.google.common.base.Preconditions.checkNotNull;

public class EncounterPage extends PageLayer {


    public EncounterPage(final Interface iface, final GameContext context, final Player player) {
        super(iface, context, player);
    }

    @Override
    protected Root createRoot() {
        root = iface.createRoot(AxisLayout.vertical(), GameStyle.newSheet(context.game), EncounterPage.this)
                .addStyles(Style.BACKGROUND.is(Background.solid(Colors.GREEN)))
                .setSize(400, 300);
        root.add(new Label("I encountered a ").addStyles(
                Style.FONT.is(Typeface.HANDWRITING.in(context.game).atSize(0.045f))));
        Encounter encounter = checkNotNull(context.encounter.get(), "Encounter must be non-null to create this root");
        root.add(new EncounterImage(encounter));
        root.add(new Label(encounter.name).addStyles(
                Style.FONT.is(Typeface.HANDWRITING.in(context.game).atSize(0.045f))));
        return root;
    }

    private class EncounterImage extends Label {
        private IconScaler scaler;
        final float IMAGE_SIZE = 0.8f;

        private EncounterImage(Encounter encounter) {
            this.scaler = new IconScaler(context.game);
            ImageCache.Key imageKey;
            try {
                imageKey = ImageCache.Key.valueOf(encounter.imageKey.toUpperCase());
            } catch (IllegalArgumentException e) {
                imageKey = ImageCache.Key.MISSING_IMAGE;
            }
            final float desiredWidth = IMAGE_SIZE * root.size().width();
            Icon scaledIcon = scaler.scale(imageKey, desiredWidth);
            icon.update(scaledIcon);
        }
    }

}
