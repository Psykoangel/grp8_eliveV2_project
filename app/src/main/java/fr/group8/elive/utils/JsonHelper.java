package fr.group8.elive.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import fr.group8.elive.models.CMA;
import fr.group8.elive.models.CMAEntry;
import fr.group8.elive.models.PersonalData;
import fr.group8.elive.models.Relationship;
import fr.group8.elive.models.User;

/**
 * Created by psyko on 08/02/16.
 */
public class JsonHelper {
    private static final String SEARCH_DELTA = StorageManager.Instance().SEARCH_DELTA;

    private static Gson gson;
    private static boolean isInitialised = false;

    public JsonHelper() {
        initialise();
    }

    private static void initialise() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(User.class, new UserInstanceCreator());
        builder.registerTypeAdapter(Relationship.class, new RelationshipInstanceCreator());
        builder.registerTypeAdapter(PersonalData.class, new PersonalDataInstanceCreator());
        builder.registerTypeAdapter(CMA.class, new CmaInstanceCreator());
        builder.registerTypeAdapter(CMAEntry.class, new CmaEntryInstanceCreator());
        builder.serializeNulls()
                .setDateFormat(DateFormat.LONG);
        gson = builder.create();
        isInitialised = true;
    }

    public static  <T> T translateJsonToObject(Class<T> classType, String input) {
        if (!isInitialised)
            initialise();
        return gson.fromJson(new StringReader(input), classType);
    }

    public static  <T> T translateJsonToObject(Class<T> classType, InputStream is) {
        if (!isInitialised)
            initialise();

        return gson.fromJson(new InputStreamReader(is), classType);
    }

    public static  <T> T translateStreamToSearchedObject(Class<T> classType, JsonReader reader, String identifier) {
        if (!isInitialised)
            initialise();

        try {
            reader.beginArray();
            while (reader.hasNext()) {
                User t = searchUserInStream(reader, User.class, identifier);
                if (t != null)
                    return (T) t;
            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }/* finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        return null;
    }

    private static <T> T searchUserInStream(JsonReader reader, Class<T> classType, String identifier) throws IOException {
        T t = null;
        reader.beginObject();
        while (reader.hasNext()) {
/*
            String name =  reader.nextName();
            if (name.equals(SEARCH_DELTA)) {
                if (reader.peek() != JsonToken.NULL) {
                    if (reader.nextString().contentEquals(identifier)) {

                        break;
                    }
                }
            } else reader.skipValue();
*/
            t = gson.fromJson(reader, classType);
            if (((User)t).getUserUniqId().contentEquals(identifier)) {
                break;
            }
        }
        reader.endObject();

        return t;
    }

    public static  <T> T translateJsonToObject(Class<T> classType, InputStreamReader reader) {
        if (!isInitialised)
            initialise();
        return gson.fromJson(reader, classType);
    }

    public static String toJson(Object obj, Class classType) {
        if (!isInitialised)
            initialise();
        return gson.toJson(obj, classType);
    }

    public static List<User> translateJsonToObject(Type listType, String content) {
        if (!isInitialised)
            initialise();
        return gson.fromJson(content, listType);
    }

    public static String InputStreamToString(InputStream stream) {

        if(stream != null)
        {
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {

                br = new BufferedReader(new InputStreamReader(stream));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return sb.toString();
        }
        else
        {
            return "";
        }
    }

    public static User searchObjectInFile(Class<User> userClass, File file, String uniqId) {
        if (!isInitialised)
            initialise();
        try {
            String json = InputStreamToString(new FileInputStream(file));
            final Type maClasseType = new TypeToken<ArrayList<User>>() {}.getType();
            ArrayList<User> list = (ArrayList<User>) translateJsonToObject(maClasseType, json);

            for (int i = 0; i < list.size(); i++) {
                User u = list.get(i);
                if (u.getUserUniqId().contentEquals(uniqId))
                    return u;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class UserInstanceCreator implements InstanceCreator<User> {

        @Override
        public User createInstance(Type type) {
            return new User();
        }
    }

    private static class RelationshipInstanceCreator implements InstanceCreator<Relationship> {
        @Override
        public Relationship createInstance(Type type) {
            return new Relationship();
        }
    }

    private static class PersonalDataInstanceCreator implements InstanceCreator<PersonalData> {
        @Override
        public PersonalData createInstance(Type type) {
            return new PersonalData();
        }
    }

    private static class CmaInstanceCreator implements InstanceCreator<CMA> {
        @Override
        public CMA createInstance(Type type) {
            return new CMA();
        }
    }

    private static class CmaEntryInstanceCreator implements InstanceCreator<CMAEntry> {
        @Override
        public CMAEntry createInstance(Type type) {
            return new CMAEntry();
        }
    }

}
