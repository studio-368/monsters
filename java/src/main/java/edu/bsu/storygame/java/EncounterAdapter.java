package edu.bsu.storygame.java;

import com.google.gson.*;
import edu.bsu.storygame.core.model.*;

import java.lang.reflect.Type;

public class EncounterAdapter implements JsonDeserializer<Encounter> {
    @Override
    public Encounter deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final String name = ((JsonObject) jsonElement).get("name").getAsString();
        final String imageKey = ((JsonObject) jsonElement).get("image").getAsString();
        Encounter.Builder builder = Encounter.with(name)
                .image(imageKey);

        JsonArray reactions = ((JsonObject) jsonElement).getAsJsonArray("reactions");
        for (JsonElement reactionElement : reactions) {
            Reaction reaction = parseReaction(reactionElement);
            builder.reaction(reaction);
        }
        return builder.build();
    }

    private Reaction parseReaction(JsonElement reactionElement) {
        final JsonObject reaction = (JsonObject) reactionElement;
        final String name = reaction.get("name").getAsString();
        final JsonObject story = reaction.getAsJsonObject("story");
        return Reaction.create(name)
                .story(parseStory(story));
    }

    private Story parseStory(JsonObject story) {
        final String text = story.get("text").getAsString();
        final Story.Builder storyBuilder = Story.withText(text);
        final JsonArray triggers = story.getAsJsonArray("triggers");
        for (JsonElement element : triggers) {
            JsonObject trigger = (JsonObject) element;
            final String skillString = trigger.get("skill").getAsString();
            final Skill skill = Skill.parse(skillString);
            final String conclusion = trigger.get("conclusion").getAsString();
            storyBuilder.trigger(SkillTrigger.skill(skill).conclusion(conclusion));
        }
        return storyBuilder.build();
    }
}
