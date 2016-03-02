package edu.bsu.storygame.editor;

import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import edu.bsu.storygame.core.json.NarrativeParser;
import edu.bsu.storygame.editor.model.*;
import org.junit.Test;
import playn.core.json.JsonParserException;
import playn.java.JavaPlatform;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

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
                                                    new Conclusion("Conclusion 1", 1, null)
                                            ),
                                            new SkillTrigger(
                                                    null,
                                                    new Conclusion("Conclusion 2")
                                            )
                                    )
                            )),
                            new Reaction("Hide", new Story(
                                    "Story 2",
                                    Lists.newArrayList(
                                            new SkillTrigger(
                                                    "Logic",
                                                    new Conclusion("Conclusion 1-A", 1, null)
                                            ),
                                            new SkillTrigger(
                                                    null,
                                                    new Conclusion("Conclusion 2-B")
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
        try {
            coreParser.parse(generatedJson);
        } catch (JsonParserException e) {
            fail();
        }
    }

    @Test(expected = JsonParserException.class)
    public void testCoreParserThrowsAnExceptionWhenGivenAnInvalidNarrative() {
        final String validJsonButInvalidNarrative = "{\"studio\":368}";
        JavaPlatform.Headless plat = new JavaPlatform.Headless(new JavaPlatform.Config());
        NarrativeParser coreParser = new NarrativeParser(plat.json());
        coreParser.parse(validJsonButInvalidNarrative);
    }
}
