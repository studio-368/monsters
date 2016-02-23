package edu.bsu.storygame.editor.json;

import edu.bsu.storygame.core.model.Encounter;
import edu.bsu.storygame.core.model.Reaction;
import edu.bsu.storygame.core.model.SkillTrigger;
import edu.bsu.storygame.core.model.Story;
import org.json.JSONArray;
import org.json.JSONObject;

public class EncounterParser {

    public Encounter parse(JSONObject object) {
        final String name = object.getString("name");
        final String imageKey = object.getString("image");

        Encounter.Builder encounterBuilder = Encounter.with(name).image(imageKey);
        JSONArray jsonReactions = object.getJSONArray("reactions");
        for (int i = 0, limit = jsonReactions.length(); i < limit; i++) {
            JSONObject jsonReaction = jsonReactions.getJSONObject(i);
            String reactionName = jsonReaction.getString("name");
            Reaction reaction = Reaction.create(reactionName).story(parseStory(jsonReaction.getJSONObject("story")));
            encounterBuilder.reaction(reaction);
        }
        return encounterBuilder.build();
    }

    public Encounter parse(String string) {
        JSONObject object = new JSONObject(string);
        return parse(object);
    }

    private Story parseStory(JSONObject jsonStory) {
        final String text = jsonStory.getString("text");
        Story.Builder storyBuilder = Story.withText(text);
        JSONArray jsonTriggers = jsonStory.getJSONArray("triggers");
        for (int i = 0, limit = jsonTriggers.length(); i < limit; i++) {
            JSONObject jsonTrigger = jsonTriggers.getJSONObject(i);
            String skill = jsonTrigger.getString("skill");
            String conclusion = jsonTrigger.getString("conclusion");
            storyBuilder.trigger(SkillTrigger.skill(skill).conclusion(conclusion));
        }
        return storyBuilder.build();
    }
}
