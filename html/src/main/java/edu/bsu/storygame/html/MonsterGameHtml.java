package edu.bsu.storygame.html;

import com.google.gwt.core.client.EntryPoint;
import playn.html.HtmlPlatform;
import edu.bsu.storygame.core.MonsterGame;

public class MonsterGameHtml implements EntryPoint {

  @Override public void onModuleLoad () {
    HtmlPlatform.Config config = new HtmlPlatform.Config();
    // use config to customize the HTML platform, if needed
    HtmlPlatform plat = new HtmlPlatform(config);
    plat.assets().setPathPrefix("monsters/");
    new MonsterGame(plat);
    plat.start();
  }
}
