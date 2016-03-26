package edu.bsu.storygame.core.util;

import com.google.common.collect.Maps;
import pythagoras.f.IRectangle;
import pythagoras.f.Rectangle;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class ScalablePicker<T extends Enum> {

    private final Map<IRectangle, T> map = Maps.newHashMap();
    private float scale = 1.0f;

    public T pick(float x, float y) {
        // It is faster to scale the point once by the inverse scale than
        // to scale each rectangle
        final float scaledX = x / scale;
        final float scaledY = y / scale;
        for (IRectangle rect : map.keySet()) {
            if (rect.contains(scaledX, scaledY)) {
                return map.get(rect);
            }
        }
        return null;
    }

    public void register(IRectangle rectangle, T value) {
        checkNotNull(value);
        checkNotNull(rectangle);
        checkArgument(rectangle.width() > 0 && rectangle.height() > 0);
        map.put(new Rectangle(rectangle), value);
    }

    public void scale(float scale) {
        this.scale = scale;
    }
}
