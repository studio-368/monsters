package edu.bsu.storygame.core.intro;

import edu.bsu.storygame.core.assets.ImageCache;

import static com.google.common.base.Preconditions.checkNotNull;

public final class SlideData {

    public static SlideData text(String text) {
        return new SlideData(text);
    }

    public final String text;
    public ImageCache.Key imageKey;
    public String popupText;
    public String nextButtonText;

    private SlideData(String text) {
        this.text = checkNotNull(text);
    }

    public SlideData imageKey(ImageCache.Key key) {
        this.imageKey = checkNotNull(key);
        return this;
    }

    public SlideData popupText(String text) {
        this.popupText = checkNotNull(text);
        return this;
    }

    public SlideData nextButtonText(String text) {
        this.nextButtonText = checkNotNull(text);
        return this;
    }

}
