package edu.bsu.storygame.editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.bsu.storygame.editor.model.Narrative;
import edu.bsu.storygame.editor.model.Region;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonParser {

    private final Gson gson;

    public GsonParser() {
        this(new Gson());
    }

    public GsonParser(Gson gson) {
        this.gson = gson;
    }

    public Narrative parse(String json) {
        Type listType = new TypeToken<ArrayList<Region>>() {
        }.getType();
        List<Region> regions = gson.fromJson(json, listType);
        return new Narrative(regions);
    }

    public String convertToJson(Narrative narrative) {
        return gson.toJson(narrative.regions);
    }
}
