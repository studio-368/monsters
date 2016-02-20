package edu.bsu.storygame.html;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.EntryPoint;
import edu.bsu.storygame.core.EncounterConfiguration;
import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.PlaceholderEncounterFactory;
import edu.bsu.storygame.core.model.Encounter;
import edu.bsu.storygame.core.model.Region;
import playn.html.HtmlPlatform;

import java.util.List;

public class MonsterGameHtml implements EntryPoint {

  @Override public void onModuleLoad () {
    HtmlPlatform.Config config = new HtmlPlatform.Config();
    // use config to customize the HTML platform, if needed
    HtmlPlatform plat = new HtmlPlatform(config);
    plat.assets().setPathPrefix("monsters/");
    new MonsterGame(plat, new EncounterConfiguration() {
      @Override
      public List<Encounter> encountersFor(Region region) {
        return ImmutableList.of(PlaceholderEncounterFactory.createEncounter());
      }
    });
    plat.start();
  }
}
