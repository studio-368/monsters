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
import playn.html.HtmlPlatform;
import pythagoras.f.IRectangle;
import tripleplay.platform.NativeTextField;
import tripleplay.ui.Field;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class HtmlNativeTextField implements NativeTextField {

    private final static Map<Integer, HtmlNativeTextField> map = Maps.newHashMap();

    private static int nextId = 0;
    private final int id = nextId++;
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
        this.field = checkNotNull(field);
        this.plat = checkNotNull(plat);

        this.element = DOM.createElement("input");
        this.element.setAttribute("type", "text");
        this.element.setAttribute("oninput", "onTextChange(" + id + ")");
        this.element.getStyle().setPosition(Style.Position.ABSOLUTE);
        this.element.setAttribute("id", "" + id);

        map.put(id, this);
    }

    @Override
    public void setEnabled(boolean enabled) {
        plat.log().debug("setEnabled: " + enabled);
        element.setPropertyBoolean("disabled", !enabled);
    }

    @Override
    public void focus() {
        plat.log().debug("focus--- cannot currently handle this?");
    }

    public static void onInput(int id) {
        log("on input: " + id);
        HtmlNativeTextField target = map.get(id);
        String text = target.element.getPropertyString("value");
        target.field.field().text.update(text);
        log("text is " + text);
    }

    public static native void log(String message) /*-{
      console.log(message);
    }-*/;

    @Override
    public boolean insert(String text) {
        plat.log().debug("insert: " + text);
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
        plat.log().debug("setBounds " + bounds);
        Style style = element.getStyle();
        style.setLeft(bounds.x(), Style.Unit.PX);
        style.setTop(bounds.y(), Style.Unit.PX);
        style.setWidth(bounds.width(), Style.Unit.PX);
        style.setHeight(bounds.height(), Style.Unit.PX);
    }

    @Override
    public void add() {
        plat.log().debug("add");
        DOM.getElementById("playn-root").insertFirst(element);
    }

    @Override
    public void remove() {
        plat.log().debug("remove");
        DOM.getElementById("playn-root").removeChild(element);
    }
}
