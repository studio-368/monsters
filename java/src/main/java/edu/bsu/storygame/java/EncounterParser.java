package edu.bsu.storygame.java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import edu.bsu.storygame.core.model.Encounter;

import java.io.InputStream;
import java.io.InputStreamReader;

import static com.google.common.base.Preconditions.checkNotNull;

public class EncounterParser {
    public Encounter parse(InputStream in) {
        checkNotNull(in);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Encounter.class, new EncounterAdapter())
                .create();
        JsonReader reader = new JsonReader(new InputStreamReader(in));
        return gson.fromJson(reader, Encounter.class);
    }
}
