package edu.bsu.storygame.editor;

import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import edu.bsu.storygame.core.json.NarrativeParser;
import edu.bsu.storygame.core.util.EncounterMatchingTestJson;
import edu.bsu.storygame.editor.model.*;
import org.junit.Test;
import playn.core.json.JsonParserException;
import playn.java.JavaPlatform;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class GsonParserTest {

    private static final GsonParser parser = new GsonParser();
    public static final String JSON_TEST = loadStringFromFile("assets/json/test-json.json");
    public static final Narrative NARRATIVE_TEST = new Narrative(Lists.newArrayList(
            new Region("Africa", Lists.newArrayList(
                    new Encounter("Cockatrice", "pic", Lists.newArrayList(
                            new Reaction("Fight", new Story(
                                    "Story 1",
                                    Lists.newArrayList(
                                            new SkillTrigger(
                                                    "Logic",
                                                    "Conclusion 1"
                                            ),
                                            new SkillTrigger(
                                                    "Magic",
                                                    "Conclusion 2"
                                            )
                                    )
                            )),
                            new Reaction("Hide", new Story(
                                    "Story 2",
                                    Lists.newArrayList(
                                            new SkillTrigger(
                                                    "Logic",
                                                    "Conclusion 1-A"
                                            ),
                                            new SkillTrigger(
                                                    "Magic",
                                                    "Conclusion 2-B"
                                            )
                                    )
                            ))
                    ))
            ))
    ));

    private static String loadStringFromFile(String filePath) {
        URL url = Resources.getResource(filePath);
        try {
            return Resources.toString(url, Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testJsonToNarrative() {
        Narrative narrative = parser.parse(JSON_TEST);
        assertEquals(NARRATIVE_TEST, narrative);
    }

    @Test
    public void testParserGeneratesAcceptableJson() {
        JavaPlatform.Headless plat = new JavaPlatform.Headless(new JavaPlatform.Config());
        NarrativeParser coreParser = new NarrativeParser(plat.json());
        String generatedJson = parser.convertToJson(NARRATIVE_TEST);
        edu.bsu.storygame.core.model.Narrative narrativeFromParser;
        try {
            narrativeFromParser = coreParser.parse(generatedJson);
        } catch (JsonParserException e) {
            fail();
            return;
        }
        edu.bsu.storygame.core.model.Encounter generatedEncounter =
                narrativeFromParser.forRegion(edu.bsu.storygame.core.model.Region.AFRICA).chooseOne();
        edu.bsu.storygame.core.model.Encounter testEncounter =
                EncounterMatchingTestJson.create();
        assertEquals(generatedEncounter, testEncounter);
    }
}
