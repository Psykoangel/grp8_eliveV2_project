package fr.group8.elive.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import fr.group8.elive.models.User;

/**
 * Created by psyko on 08/02/16.
 */
public class JsonHelper {
    private static final String SEARCH_DELTA = "userUniqId";
    private static JsonHelper ourInstance = new JsonHelper();

    public static JsonHelper Instance() {
        return ourInstance;
    }

    private Gson gson;

    private JsonHelper() {
        GsonBuilder builder = new GsonBuilder();
        // builder.registerTypeAdapter(RelationShip.class, new RelationShipInstanceCreator());
        // builder.registerTypeAdapter(CmaObject.class, new CmaObjectInstanceCreator());
        gson = builder.create();
    }

    public  <T> T translateJsonToObject(Class<T> classType, String input) {
        return gson.fromJson(new StringReader(input), classType);
    }

    public  <T> T translateJsonToObject(Class<T> classType, InputStream is) {

        return gson.fromJson(new InputStreamReader(is), classType);
    }

    public  <T> T translateStreamToSearchedObject(Class<T> classType, JsonReader reader, String identifier) {

        try {
            while (reader.hasNext()) {
                if (reader.nextString().equals(SEARCH_DELTA)) {
                    if (reader.nextString().contentEquals(identifier))
                        return gson.fromJson(reader, classType);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public <T> T translateJsonToObject(Class<T> classType, InputStreamReader reader) {
        return gson.fromJson(reader, classType);
    }

    public String toJson(Object obj, Class classType) {
        return gson.toJson(obj, classType);
    }
}
