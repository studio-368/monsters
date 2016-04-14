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

package edu.bsu.storygame.html;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.json.NarrativeParser;
import playn.core.json.JsonParserException;
import playn.html.HtmlPlatform;
import tripleplay.platform.TPPlatform;

public class MonsterGameHtml implements EntryPoint {

    private HtmlPlatform plat;

    @Override
    public void onModuleLoad() {
        HtmlPlatform.Config platConf = new HtmlPlatform.Config();
        // use config to customize the HTML platform, if needed
        plat = new HtmlPlatform(platConf);
        plat.assets().setPathPrefix("monsters/");
        MonsterGame.Config gameConf = new MonsterGame.Config(plat);
        handleNarrativeOverride(gameConf);

        HtmlTpPlatform tpPlatform = new HtmlTpPlatform(plat) {
            {
                TPPlatform._instance = this;
            }
        };

        new MonsterGame(gameConf);
        plat.start();
    }

    private void handleNarrativeOverride(MonsterGame.Config gameConf) {
        if (Window.Location.getParameter("override") != null) {
            String json = popupNarrativeOverride();
            try {
                gameConf.narrativeOverride = new NarrativeParser(plat.json()).parse(json);
            } catch (JsonParserException parseException) {
                popupParseException();
            }
        }
    }

    private native String popupNarrativeOverride() /*-{
        return prompt("Paste your custom JSON narrative below.");
    }-*/;

    private native void popupParseException() /*-{
        alert('Failed to parse JSON narrative. Continuing with default narrative.\nReload page to try again.');
    }-*/;


}
