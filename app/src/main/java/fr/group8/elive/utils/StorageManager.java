package fr.group8.elive.utils;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.group8.elive.app.R;
import fr.group8.elive.models.BloodGroup;
import fr.group8.elive.models.CMAItem;
import fr.group8.elive.models.DataUser;
import fr.group8.elive.models.RelationType;
import fr.group8.elive.models.User;
import ru.noties.storm.DatabaseManager;
import ru.noties.storm.Storm;
import ru.noties.storm.exc.StormException;
import ru.noties.storm.query.Selection;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by psyko on 18/01/16.
 */
public class StorageManager {
    private static StorageManager ourInstance = new StorageManager();
    // Single Instance of StorageManager
    public static StorageManager Instance() {
        return ourInstance;
    }

    // Name of the private files of the application
    public final static String APP_FILE_NAME = "elivedata";
    // Separator of the file name's information
    public final static String APP_FILE_SEPARATOR = ".";
    // Final extension of each private files of the application
    public final static String APP_FILE_EXTENSION = "json";
    // Name of the SQLite Database for the application
    public final static String APP_DB_NAME = "elivedb";

    // Specify the Debug State of the StorageManager
    public final Boolean IS_DEBUG = true;
    // Specify the version of the SQLite Database Version
    public final int DB_VERSION = 1;
    // Default Maximal user object per private file of the app
    public final int FILE_MAX_USER_NUMBER = 500;
    // Boolean that defines if a context has been registered in the mContext variable
    public Boolean isContextLoaded;
    // Boolean that defines if the Gson parser has been correctly initialised
    public Boolean isJsonParserReady;

    // Default search string to count in the file or for search functions
    private final String SEARCH_DELTA = "userUniqId";
    // Reference to a context activity
    private Context mContext;
    private DatabaseManager dbManager;
    private int fileNumberCount;
    private Class<?> [] dbTypes = {
            DataUser.class,
            BloodGroup.class,
            RelationType.class
    };

    private boolean isDbReady;


    public StorageManager() {
        dbManager = null;
        isDbReady = false;

        mContext = null;
        isContextLoaded = false;

        fileNumberCount = 0;
    }

    public void loadContext(Context context) {
        mContext = context;
        isContextLoaded = true;

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
     * Method used to store input data to the index SQLite database
     * on the first part and the complete object in a common JSON file
     * in the application directory. Should be run on background.
     */
    public void storeJsonData(InputStream is) throws Exception {

        // Search for the uniqId in the Stream
        String uniqId = searchInDataStream(is, SEARCH_DELTA);

        // Search in indexDB if already checked once
        // and retrieve fileName if checked once.
        DataUser existingUser = select(uniqId);

        if (existingUser != null) {
            try {
                UpdateProcess(is, existingUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        // if uniqId is not indexed
        try {
            CreationProcess(uniqId, is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CreationProcess(String uniqId, InputStream is) throws Exception {
        // Create index Entry
        DataUser newEntry = new DataUser(uniqId);
        Boolean validateEntry = true;

        // Retrieve last created file
        // "elivedata.{fileNumberCount}.json"
        if (fileNumberCount == 0) fileNumberCount++;

        String fileName = APP_FILE_NAME
                + APP_FILE_SEPARATOR
                + fileNumberCount
                + APP_FILE_SEPARATOR
                + APP_FILE_EXTENSION;

        // load last found file
        File file = null;
        try {
            file = loadJsonDataFile(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            validateEntry = false;
        }

        if (file == null) return;

        // if last file content equals FILE_MAX_USER_NUMBER
        int count = 0;
        try {
            count = countJsonElementInStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            validateEntry = false;
        }
        // free memory as much as possible,
        // increment fileNumberCount and create a new file
        if (count > FILE_MAX_USER_NUMBER)
            throw new Exception("File should not count more than " + FILE_MAX_USER_NUMBER + " Objects.");
        else if (count == FILE_MAX_USER_NUMBER) {
            file = null;
            fileName = null;

            fileName = APP_FILE_NAME
                    + APP_FILE_SEPARATOR
                    + ++fileNumberCount
                    + APP_FILE_SEPARATOR
                    + APP_FILE_EXTENSION;
            file = createNewJsonFile(fileName);
        }

        // Put default template to new file
        List<User> newList = new ArrayList<User>();
        writeJsonDataFile(
                translateObjectToJson(newList, newList.getClass()),
                fileName
        );

        try {
            createUserInJsonFile(fileName, JsonHelper.Instance().translateJsonToObject(User.class, is));
        } catch (Exception e) {
            e.printStackTrace();
            validateEntry = false;
        }

        // if no exception, commit index Entry
        if (validateEntry) {
            newEntry.setFileLocation(fileName);
            newEntry.setLastUpdate(new Date());
            insert(newEntry);
        }
    }

    private void createUserInJsonFile(String fileName, User user) {
        // Add the incoming json object to file
        File file = null;
        try {
            file = loadJsonDataFile(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            List<User> usersList = new ArrayList<User>();
            usersList = JsonHelper.Instance().translateJsonToObject(usersList.getClass(), getStringFromFile(file));

            if (usersList != null && usersList.size() > 0) {
                usersList.add(user);
            }

            if (usersList != null) {
                writeJsonDataFile(
                        translateObjectToJson(usersList, usersList.getClass()),
                        fileName);
            }

        } catch (Exception e) {
            //TODO Remove dummy statement !
            if (e.getClass() == FileNotFoundException.class) {
                e.printStackTrace();
            } else {
                e.printStackTrace();
            }
        }
    }

    private void UpdateProcess(InputStream is, DataUser existingUser) {
        // Retrieve private file
        String fileName = existingUser.getFileLocation();

        // Change the targeted Json Object
        changeExistingUserInJsonFile(
                fileName,
                JsonHelper.Instance().translateJsonToObject(User.class, is)
        );

        // Update index entry
        existingUser.setLastUpdate(new Date());
        update(existingUser);
    }

    private void changeExistingUserInJsonFile(String fileName, User user) {
        // load private file as Json Object
        File file = null;
        try {
            file = loadJsonDataFile(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            List<User> usersList = new ArrayList<User>();
            usersList = JsonHelper.Instance().translateJsonToObject(usersList.getClass(), getStringFromFile(file));

            if (usersList != null && usersList.size() > 0) {
                usersList = changeExistingUser(usersList, user);
            }

            if (usersList != null) {
                writeJsonDataFile(
                        translateObjectToJson(usersList, usersList.getClass()),
                        fileName);
            }

        } catch (Exception e) {
            //TODO Remove dummy statement !
            if (e.getClass() == FileNotFoundException.class) {
                e.printStackTrace();
            } else {
                e.printStackTrace();
            }
        }
    }

    private List<User> changeExistingUser(List<User> usersList, User user) {
        try {
            int i;
            for (i = 0; i < usersList.size(); i++) {
                User u = usersList.get(i);
                if (u.getUserUniqId().equals(user.getUserUniqId()))
                    break;
            }

            if (i >= 0 && i < usersList.size())
                usersList.remove(i);

            usersList.add(user);

            return usersList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String translateObjectToJson(Object obj, Class classType) {
        return JsonHelper.Instance().toJson(obj, classType);
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

    private File loadJsonDataFile(String fileName) throws FileNotFoundException {
        File selectedFile = mContext.getFileStreamPath(fileName);

        if (!selectedFile.exists()) throw new FileNotFoundException("File not found on internal storage");

        return selectedFile;
    }

    private void writeJsonDataFile(String fileContent, String fileName) {
        try {
            FileOutputStream fos = mContext.openFileOutput(fileName, MODE_PRIVATE);
            fos.write(fileContent.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getStringFromFile (String filePath) throws Exception {

        File fl = new File(filePath);

        return getStringFromFile(fl);
    }

    private String getStringFromFile(File file) throws Exception {

        if (!file.exists()) throw new FileNotFoundException("File " + file.toString() + " not found");

        FileInputStream fin = new FileInputStream(file);
        String res = convertStreamToString(fin);
        fin.close();

        return res;
    }

    private int countJsonElementInStream(FileInputStream fileInputStream) {

        JsonReader reader = new JsonReader(new InputStreamReader(fileInputStream));
        int count = 0;

        try {
            while (reader.hasNext()) {
                if (reader.nextString().contentEquals(SEARCH_DELTA))
                    count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
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

    private File createNewJsonFile(String fileName) {
        try {
            FileOutputStream fos = mContext.openFileOutput(fileName, MODE_PRIVATE);
            fos.close();
            return mContext.getFileStreamPath(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User searchUser(String identifier) {
        // Search in indexDB if already checked once
        // and retrieve fileName if checked once.
        DataUser existingUser = select(identifier);

        if (existingUser == null) return null;

        File file = mContext.getFileStreamPath(existingUser.getFileLocation());

        return searchSpecificUserInFile(file, existingUser);
    }

    private User searchSpecificUserInFile(File file, DataUser user) {
        try {
            JsonReader reader = new JsonReader(new FileReader(file));
            return JsonHelper.Instance().translateStreamToSearchedObject(User.class, reader, user.getUniqId());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createDataBase(Context context) throws StormException {
        if (!isDbReady)
        {
            try {
                Storm.getInstance().init(context, IS_DEBUG);
                isDbReady = true;
            } catch (Exception e) {
                throw new StormException("Storm already initiated.");
            }
        }
    }

    public void closeDBManager() {
        if (dbManager != null
                && dbManager.isOpen()) {
            dbManager.close();
            dbManager = null;
        }
    }

    public void initiateDBManager(Context context, String dbName) {
        this.closeDBManager();

        dbManager = new DatabaseManager(
                context,
                dbName,
                DB_VERSION,
                dbTypes
        );

        try {
            dbManager.open();
            initiateDbStaticData();
        } catch (SQLiteException e) {
            dbManager = null;
            e.printStackTrace();
        }
    }

    private void initiateDbStaticData() {
        if (!(dbManager.count(BloodGroup.class) > 0)) {
            dbManager.getDataBase().execSQL(
                mContext.getString(R.string.db_sqlinsert_bloodgroup)
            );
        }
        if (!(dbManager.count(RelationType.class) > 0)) {
            dbManager.getDataBase().execSQL(
                mContext.getString(R.string.db_sqlinsert_relationtype)
            );
        }
        if (!(dbManager.count(CMAItem.class) > 0)) {
            InputStream stream = mContext.getResources().openRawResource(R.raw.db_sqlinsert_cma);
            dbManager.getDataBase().execSQL(
                inputStreamToString(stream)
            );
        }
    }

    private String inputStreamToString(InputStream stream) {

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
            return null;
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

    public String selectBloodGroup(int id) {
        if (!this.dbManager.isOpen())
            throw new SQLiteException("DbManager has not been instanciated yet");

        BloodGroup bloodGroup = Storm.newSelect(dbManager).query(BloodGroup.class, Selection.eq("bloodgroup_id", id));

        if (bloodGroup.isValide()) {
            return bloodGroup.getName() + bloodGroup.getSign();
        } else {
            return null;
        }
    }

    public void update(DataUser user) {
        if (!this.dbManager.isOpen())
            throw new SQLiteException("DbManager has not been instanciated yet");

        try {
            Storm.newUpdate(dbManager).update(user, Selection.eq("uniqId", user.getUniqId()));
        } catch (StormException e) {
            e.printStackTrace();
        }
    }
}
