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

package edu.bsu.storygame.core.assets;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import playn.core.Assets;
import playn.core.Sound;
import react.RFuture;
import react.RPromise;
import react.Slot;
import react.Try;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

import static com.google.common.base.Preconditions.*;

public class AudioCache {

    public enum AudioKey {

        PAGE_FLIP("test"),
        CLICK("test");

        private final String relativePath;

        AudioKey(String relativePath) {
            this.relativePath = "sounds/" + relativePath;
        }
    }

    private final EnumMap<AudioKey, Sound> map = Maps.newEnumMap(AudioKey.class);

    public AudioCache(Assets assets) {
        List<RFuture<Sound>> futures = Lists.newArrayListWithCapacity(AudioKey.values().length);
        for (final AudioKey key : AudioKey.values()) {
            final Sound sound = assets.getSound(key.relativePath);
            map.put(key, sound);
            futures.add(sound.state);
        }
        RFuture.collect(futures).onComplete(new Slot<Try<Collection<Sound>>>() {
            @Override
            public void onEmit(Try<Collection<Sound>> collectionTry) {
                if (collectionTry.isSuccess()) {
                    ((RPromise<AudioCache>) state).succeed(AudioCache.this);
                } else {
                    ((RPromise<AudioCache>) state).fail(collectionTry.getFailure());
                }
            }
        });
    }

    public final RFuture<AudioCache> state = RPromise.create();

    public Sound sound(AudioKey audioKey) {
        checkNotNull(audioKey);
        Sound sound = map.get(audioKey);
        checkState(sound.isLoaded(), "Sound not cached: " + audioKey.name());
        return sound;
    }

    public RFuture<Sound> stateOf(AudioKey key) {
        return map.get(key).state;
    }

}
