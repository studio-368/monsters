package edu.bsu.storygame.core.json;

import edu.bsu.storygame.core.model.*;
import playn.core.Json;

import static com.google.common.base.Preconditions.*;

public final class EncounterParser {

    private final Json json;

    public EncounterParser(Json json) {
        this.json = checkNotNull(json);
    }

    public Encounter parse(Json.Object object) {
        final String name = object.getString("name");
        final String imageKey = object.getString("image");

        Encounter.Builder encounterBuilder = Encounter.with(name).image(imageKey);
        Json.Array jsonReactions = object.getArray("reactions");
        for (int i = 0, limit = jsonReactions.length(); i < limit; i++) {
            Json.Object jsonReaction = jsonReactions.getObject(i);
            String reactionName = jsonReaction.getString("name");
            try {
                Reaction reaction = Reaction.create(reactionName).story(parseStory(jsonReaction.getObject("story")));
                encounterBuilder.reaction(reaction);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to parse encounter " + name, e);
            }
        }
        return encounterBuilder.build();
    }

    public Encounter parse(String string) {
        Json.Object object = json.parse(string);
        return parse(object);
    }

    private Story parseStory(Json.Object jsonStory) {
        final String text = jsonStory.getString("text");
        Story.Builder storyBuilder = Story.withText(text);
        Json.Array jsonTriggers = jsonStory.getArray("triggers");
        for (int i = 0, limit = jsonTriggers.length(); i < limit; i++) {
            Json.Object jsonTrigger = jsonTriggers.getObject(i);
            parseTrigger(jsonTrigger, storyBuilder);
        }
        return storyBuilder.build();
    }

    private void parseTrigger(Json.Object jsonTrigger, Story.Builder storyBuilder) {
        String skillString = jsonTrigger.getString("skill");
        Json.Object conclusionObject = jsonTrigger.getObject("conclusion");
        Conclusion conclusion = parseConclusion(conclusionObject);
        if (skillString != null) {
            Skill skill = Skill.named(skillString);
            storyBuilder.trigger(SkillTrigger.skill(skill).conclusion(conclusion));
        } else {
            NoSkillTrigger noSkill = new NoSkillTrigger(conclusion);
            storyBuilder.trigger(noSkill);
        }
    }

    private Conclusion parseConclusion(Json.Object conclusionObject) {
        checkNotNull(conclusionObject, "Trigger must have a conclusion");
        Conclusion.Builder builder = new Conclusion.Builder();
        String text = conclusionObject.getString("text");
        checkNotNull(text, "Conclusion must have text");
        builder.text(text);
        builder.points(conclusionObject.getInt("points"));
        String skillReward = conclusionObject.getString("skill");
        if (skillReward != null) {
            builder.skill(Skill.named(skillReward));
        }
        return builder.build();
    }
}
