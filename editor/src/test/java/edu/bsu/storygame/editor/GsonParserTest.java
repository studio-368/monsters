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
