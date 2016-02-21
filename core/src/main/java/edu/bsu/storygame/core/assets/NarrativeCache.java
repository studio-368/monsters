package edu.bsu.storygame.core.assets;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.json.NarrativeParser;
import edu.bsu.storygame.core.model.Narrative;
import react.RFuture;
import react.RPromise;
import react.Slot;

public final class NarrativeCache {
    private static final String NARRATIVE = "encounters/narrative.json";
    public final RFuture<Narrative> state = RPromise.create();

    public NarrativeCache(final MonsterGame game) {
        game.plat.assets().getText(NARRATIVE).onSuccess(new Slot<String>() {
            @Override
            public void onEmit(String jsonString) {
                final NarrativeParser narrativeParser = new NarrativeParser(game.plat.json());
                Narrative narrative = narrativeParser.parse(jsonString);
                ((RPromise<Narrative>) state).succeed(narrative);
            }
        }).onFailure(new Slot<Throwable>() {
            @Override
            public void onEmit(Throwable throwable) {
                game.plat.log().warn("Cannot load narrative from " + NARRATIVE);
                throw new IllegalStateException(throwable);
            }
        });
    }
}
