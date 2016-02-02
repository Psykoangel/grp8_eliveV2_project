package fr.group8.elive.utils;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.util.JsonReader;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import fr.group8.elive.models.DataUser;
import ru.noties.storm.DatabaseManager;
import ru.noties.storm.Storm;
import ru.noties.storm.exc.StormException;
import ru.noties.storm.query.Selection;

/**
 * Created by psyko on 18/01/16.
 */
public class StorageManager {
    private static StorageManager ourInstance = new StorageManager();
    public static StorageManager Instance() {
        return ourInstance;
    }

    public static String appFileName = "elive_app_data.json";

    public final Boolean IS_DEBUG = true;
    public final int DB_VERSION = 1;
    public final int FILE_MAX_USER_NUMBER = 500;
    public Context mContext;
    public Boolean isContextloaded;

    private DatabaseManager dbManager;
    private int fileNumberCount;

    private boolean isDbReady;


    private StorageManager() {
        dbManager = null;
        isDbReady = false;

        mContext = null;
        isContextloaded = false;

        fileNumberCount = 0;
    }

    public void loadContext(Context context) {
        mContext = context;
        isContextloaded = true;

        contextDependantAssignations();
    }

    private void contextDependantAssignations() {

        try {
            fileNumberCount = mContext.getFilesDir().listFiles().length;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
      Method used to store input data to the index SQLite database
      on the first part and the complete object in a common JSON file
      in the application directory. Should be run on background.
     */
    public void storeJsonData(InputStream is) {

        // Search for the uniqId in the Stream
        String uniqId = searchInDataStream(is, "uniqId");

        // Search in indexDB if already checked once
        // and retrive fileName if checked once.

            // Retrieve private file

            // load private file as Json Object

            // Change the targeted Json Object

        // if uniqId is not indexed
        // Create index Entry

        // Retrieve last created file
        // "elivedata.{fileNumberCount}.json"

        // load last file in memory as Json Object

        // if last file content equals FILE_MAX_USER_NUMBER
        // free memory, increment fileNumberCount and create a new file
        // Put default template to new file

        // Load file as Json Object

        // Add the incoming json object to file

        // if no exception, commit index Entry

    }

    private String searchInDataStream(InputStream is, String uniqId) {
        JsonReader reader = null;
        Boolean found = false;
        try {
            reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
            reader.beginObject();

            while (reader.hasNext() && !found) {
                
                String token = reader.nextName();
                if (token.contentEquals(uniqId)) {
                    found = true;
                    reader.close();
                    return reader.nextString();
                } else reader.skipValue();

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    private void loadJsonDataFile(String fileName) throws FileNotFoundException {
        File selectedFile = mContext.getFileStreamPath(fileName);

        if (!selectedFile.exists()) throw new FileNotFoundException("File not found on internal storage");

    }

    private void addDataToJsonFile(File fileJson) {

        String strFileJson = null;
        JSONObject jsonObj = null;

        try {
            strFileJson = getStringFromFile(fileJson.toString());
            jsonObj = new JSONObject(strFileJson);

        } catch (JSONException e) {
            if (isContextloaded) AlertHelper.showAlert(mContext, "Error", "JsonObject Parsing Error");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();

        // writeJsonFile(fileJson, jsonObj.toString());

    }

    private String getStringFromFile (String filePath) throws Exception {

        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String res = convertStreamToString(fin);
        fin.close();

        return res;
    }

    private String convertStreamToString(InputStream is) throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }

        return sb.toString();
    }

    private void writeJsonFile(File file, String json)
    {
        BufferedWriter bufferedWriter = null;
        try {

            if(!file.exists()){
                FileOutputStream fos = mContext.openFileOutput(file.getName(), Context.MODE_PRIVATE);
                fos.close();
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null){
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String searchDataFromJsonFile() {

        try {
            String user = readJsonStream(new FileInputStream(""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "User";
    }

    private String readJsonStream(InputStream is) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));

        try {

            return "User";
        } finally {
            reader.close();
        }
    }

    public void createDataBase(Context context) {
        if (!isDbReady)
        {
            Storm.getInstance().init(context, IS_DEBUG);
            isDbReady = true;
        }
    }


    public void initiateDBManager(Context context, String dbName) {
        this.closeDBManager();

        dbManager = new DatabaseManager(context, dbName, DB_VERSION, null);

        try {
            dbManager.open();
        } catch (SQLiteException e) {
            dbManager = null;
            e.printStackTrace();
        }
    }

    public void closeDBManager() {
        if (dbManager != null
                && dbManager.isOpen()) {
            dbManager.close();
            dbManager = null;
        }
    }



    public void insert(DataUser user) {
        if (!this.dbManager.isOpen())
            throw new SQLiteException("DbManager has not been instanciated yet");
        try {
            Storm.newInsert(dbManager).insert(user);
        } catch (StormException e) {
            e.printStackTrace();
        }
    }
    
    public void insert(String uniqId, String fileName) {
        this.insert(new DataUser(uniqId, fileName));
    }

    public DataUser select(String uniqId) {
        if (!this.dbManager.isOpen())
            throw new SQLiteException("DbManager has not been instanciated yet");

        return Storm.newSelect(dbManager).query(DataUser.class, Selection.eq("uniqId", uniqId));
    }
}
