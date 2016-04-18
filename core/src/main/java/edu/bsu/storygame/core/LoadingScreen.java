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

package edu.bsu.storygame.core;

import com.google.common.collect.Lists;
import edu.bsu.storygame.core.assets.AudioCache;
import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.model.Narrative;
import edu.bsu.storygame.core.view.GameStyle;
import edu.bsu.storygame.core.view.ProgressBar;
import edu.bsu.storygame.core.view.StartScreen;
import playn.core.Game;
import react.Function;
import react.RFuture;
import react.Slot;
import react.Try;
import tripleplay.game.ScreenStack;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.util.Colors;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoadingScreen extends ScreenStack.UIScreen {

    private final MonsterGame game;
    private Root root;
    private ProgressBar progressBar;

    public LoadingScreen(final MonsterGame game, final ScreenStack screenStack) {
        super(game.plat);
        this.game = checkNotNull(game);
        configureProgressBar();

        List<RFuture<Boolean>> futures = Lists.newArrayListWithCapacity(3);
        futures.add(game.imageCache.state.map(new Function<ImageCache, Boolean>() {
            @Override
            public Boolean apply(ImageCache imageCache) {
                progressBar.increment(1);
                return true;
            }
        }));
        futures.add(game.audioCache.state.map(new Function<AudioCache, Boolean>() {
            @Override
            public Boolean apply(AudioCache audioCache) {
                progressBar.increment(1);
                return true;
            }
        }));
        futures.add(game.narrativeCache.state.map(new Function<Narrative, Boolean>() {
            @Override
            public Boolean apply(Narrative narrative) {
                progressBar.increment(1);
                return true;
            }
        }));

        RFuture.collect(futures)
                .onComplete(new Slot<Try<Collection<Boolean>>>() {
                    @Override
                    public void onEmit(Try<Collection<Boolean>> collectionTry) {
                        if (collectionTry.isSuccess()) {
                            screenStack.push(new StartScreen(game), screenStack.slide().left());
                        } else {
                            root.add(new Label("Failure caching resources; see log for details.")
                                    .setStyles(Style.COLOR.is(Colors.WHITE)));
                            // IDEA is confused about the fact that we are actually handling this Throwable. Suppress it.
                            //noinspection ThrowableResultOfMethodCallIgnored
                            game.plat.log().error(collectionTry.getFailure().getMessage());
                        }
                    }
                });

        root = iface.createRoot(AxisLayout.vertical(), GameStyle.newSheet(game), layer)
                .setSize(size())
                .add(new Label("Loading...")
                        .addStyles(Style.COLOR.is(Colors.WHITE)));
    }

    private void configureProgressBar() {
        final int numberOfCaches = 3;
        final float width = this.size().width();
        final float height = this.size().height();
        progressBar = new ProgressBar(numberOfCaches, width * 0.55f, height * 0.02f, game, ProgressBar.FillType.HORIZONTAL);
        layer.addCenterAt(progressBar, width / 2, height * 3 / 5);
    }



    @Override
    public Game game() {
        return game;
    }
}
