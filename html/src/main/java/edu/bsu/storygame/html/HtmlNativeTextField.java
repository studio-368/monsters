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

package edu.bsu.storygame.html;

import com.google.common.collect.Maps;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import edu.bsu.storygame.core.assets.FontConstants;
import playn.html.HtmlPlatform;
import pythagoras.f.IRectangle;
import tripleplay.platform.NativeTextField;
import tripleplay.ui.Field;

import java.util.Map;

import static com.google.common.base.Preconditions.*;

public class HtmlNativeTextField implements NativeTextField {

    private final static Map<Integer, HtmlNativeTextField> map = Maps.newHashMap();
    private static final float FONT_SIZE_PERCENT_OF_FIELD_HEIGHT = 0.75f;
    private static final int MAX_NAME_LENGTH = 8;

    private static int nextId = 0;
    private final Field.Native field;
    private final HtmlPlatform plat;
    private final Element element;

    static {
        exportStaticMethod();
    }

    public static native void exportStaticMethod() /*-{
      $wnd.onTextChange = $entry(@edu.bsu.storygame.html.HtmlNativeTextField::onInput(I));
    }-*/;

    public HtmlNativeTextField(Field.Native field, HtmlPlatform plat) {
        final int id = nextId++;
        this.field = checkNotNull(field);
        this.plat = checkNotNull(plat);

        this.element = DOM.createElement("input");
        this.element.setAttribute("type", "text");
        this.element.setAttribute("oninput", "onTextChange(" + id + ")");
        this.element.setAttribute("maxlength", String.valueOf(MAX_NAME_LENGTH));
        this.element.getStyle().setPosition(Style.Position.ABSOLUTE);
        this.element.getStyle().setProperty("font-family", FontConstants.HANDWRITING_NAME);
        this.element.getStyle().setFontSize(field.field().size().height() * FONT_SIZE_PERCENT_OF_FIELD_HEIGHT, Style.Unit.PX);
        this.element.setAttribute("id", "" + id);

        map.put(id, this);
    }

    @Override
    public void setEnabled(boolean enabled) {
        element.setPropertyBoolean("disabled", !enabled);
    }

    @Override
    public void focus() {
        element.focus();
    }

    // This method is called by native Javascript, so the Java compiler doesn't recognize it as used.
    @SuppressWarnings("unused")
    public static void onInput(int id) {
        HtmlNativeTextField target = map.get(id);
        String text = target.element.getPropertyString("value");
        target.field.field().text.update(text);
    }

    @Override
    public boolean insert(String text) {
        int selectionStart = Integer.parseInt(element.getAttribute("selectionStart"));
        int selectionEnd = Integer.parseInt(element.getAttribute("selectionEnd"));
        String originalText = element.getAttribute("value");
        element.setAttribute("value",
                originalText.substring(0, selectionStart)
                        + text
                        + originalText.substring(selectionEnd));
        return true;
    }

    @Override
    public void setBounds(IRectangle bounds) {
        Style style = element.getStyle();
        style.setLeft(bounds.x(), Style.Unit.PX);
        style.setTop(bounds.y(), Style.Unit.PX);
        style.setWidth(bounds.width(), Style.Unit.PX);
        style.setHeight(bounds.height(), Style.Unit.PX);
    }

    @Override
    public void add() {
        if (!element.hasParentElement()) {
            DOM.getElementById("playn-root").insertFirst(element);
            element.focus();
        }
    }

    @Override
    public void remove() {
        element.getParentElement().removeChild(element);
        HtmlNativeTextField removedElement = map.remove(Integer.parseInt(element.getAttribute("id")));
        if (removedElement == null) {
            plat.log().warn("Attempt to remove element not in map: " + element);
        }

    }
}
