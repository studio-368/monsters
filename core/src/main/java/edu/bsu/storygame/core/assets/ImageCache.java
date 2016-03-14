package edu.bsu.storygame.core.assets;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import playn.core.Assets;
import playn.core.Image;
import react.RFuture;
import react.RPromise;
import react.Slot;
import react.Try;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ImageCache {

    public enum Key {
        COCKATRICE("cockatrice.png"),
        LOGO("logo.png"),
        MISSING_IMAGE("missing_image.png"),
        BUNYIP("bunyip.png"),
        KAPPA("kappa.png"),
        KRAKEN("kraken.png"),
        NESSY("nessy.png"),
        WRAITH("wraith.png"),
        STAR("star.png"),
        CHUPACABRA("chupacabra.png"),
        CHINESE_DRAGON("chinese_dragon.png"),
        DEER_WOMEN("deer_woman.png"),
        IMPUNDULU("impundulu.png"),
        NINKI_NAKA("ninki_naka.png");

        private final String path;

        Key(String relativePath) {
            this.path = "images/" + relativePath;
        }
    }

    private final EnumMap<Key, Image> map = Maps.newEnumMap(Key.class);

    public ImageCache(Assets assets) {
        List<RFuture<Image>> futures = Lists.newArrayListWithCapacity(Key.values().length);
        for (final Key key : Key.values()) {
            final Image image = assets.getImage(key.path);
            image.state.onSuccess(new Slot<Image>() {
                @Override
                public void onEmit(Image image) {
                    map.put(key,image);
                }
            });
            futures.add(image.state);
        }
        RFuture.collect(futures).onComplete(new Slot<Try<Collection<Image>>>() {
            @Override
            public void onEmit(Try<Collection<Image>> collectionTry) {
                if (collectionTry.isSuccess()) {
                    ((RPromise<ImageCache>)state).succeed(ImageCache.this);
                } else {
                    ((RPromise<ImageCache>)state).fail(collectionTry.getFailure());
                }
            }
        });

    }

    public final RFuture<ImageCache> state = RPromise.create();

    public Image image(Key key) {
        checkNotNull(key);
        Image image = map.get(key);
        checkNotNull(image, "Image not cached: " + key.name());
        return image;
    }
}
