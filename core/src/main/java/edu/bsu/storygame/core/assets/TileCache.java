package edu.bsu.storygame.core.assets;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import playn.core.Assets;
import playn.core.Image;
import playn.core.Tile;
import react.*;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class TileCache {

    public enum Key {
        BACKGROUND("worldMap.jpg");
        private final String path;

        Key(String relativePath) {
            this.path = "images/" + relativePath;
        }
    }

    private final EnumMap<Key, Tile> map = Maps.newEnumMap(Key.class);

    public TileCache(Assets assets) {
        final List<RFuture<Tile>> futures = Lists.newArrayListWithCapacity(Key.values().length);
        for (final Key key : Key.values()) {
            final Image image = assets.getImage(key.path);
            final RPromise<Tile> promise = RPromise.create();
            image.state.onSuccess(new Slot<Image>() {
                @Override
                public void onEmit(Image image) {
                    RFuture<Tile> future = image.tileAsync();
                    future.onSuccess(new Slot<Tile>() {
                        @Override
                        public void onEmit(Tile tile) {
                            map.put(key, tile);
                            promise.succeed(tile);
                        }
                    });
                    future.onFailure(new Slot<Throwable>() {
                        @Override
                        public void onEmit(Throwable throwable) {
                            promise.fail(throwable);
                        }
                    });
                }
            });
            image.state.onFailure(new Slot<Throwable>() {
                @Override
                public void onEmit(Throwable throwable) {
                    promise.fail(throwable);
                }
            });
            futures.add(promise);
        }
        RFuture.collect(futures).onComplete(new Slot<Try<Collection<Tile>>>() {
            @Override
            public void onEmit(Try<Collection<Tile>> collectionTry) {
                if (collectionTry.isSuccess()) {
                    ((RPromise<TileCache>)state).succeed(TileCache.this);
                } else {
                    ((RPromise<TileCache>)state).fail(collectionTry.getFailure());
                }
            }
        });
    }

    public final RFuture<TileCache> state = RPromise.create();

    public Tile tile(Key key) {
        checkNotNull(key);
        Tile tile = map.get(key);
        checkNotNull(tile, "Tile not cached " + key.name());
        return tile;
    }
}
