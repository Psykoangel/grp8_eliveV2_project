package fr.group8.elive.utils;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import com.google.gson.reflect.TypeToken;
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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
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

    // Default search string to count in the file or for search functions
    public final String SEARCH_DELTA = "userUniqId";
    // Reference to a context activity
    private Context mContext;
    private DatabaseManager dbManager;
    private int fileNumberCount;
    private Class<?> [] dbTypes = {
            DataUser.class,
            BloodGroup.class,
            RelationType.class,
            CMAItem.class
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
            fileNumberCount = mContext.getExternalFilesDir(null).listFiles().length;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Method used to store input data to the index SQLite database
     * on the first part and the complete object in a common JSON file
     * in the application directory. Should be run on background.
     */
    public void storeJsonData(User user) throws Exception {

        // Search in indexDB if already checked once
        // and retrieve fileName if checked once.
        DataUser existingUser = select(user.getUserUniqId());

        if (existingUser != null) {
            try {
                UpdateProcess(user, existingUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        // if uniqId is not indexed
        try {
            CreationProcess(user.getUserUniqId(), user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CreationProcess(String uniqId, User user) throws Exception {
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Put default template to new file
        List<User> newList = new ArrayList<User>();
        writeJsonDataFile(
            JsonHelper.toJson(newList, newList.getClass()),
            file
        );

        try {
            createUserInJsonFile(file, user);
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

    private void createUserInJsonFile(File file, User user) {
        // Add the incoming json object to file

        try {
            List<User> usersList = new ArrayList<>();
            usersList = JsonHelper.translateJsonToObject(usersList.getClass(), getStringFromFile(file));

            if (usersList != null && usersList.size() > 0) {
                usersList.add(user);
            }

            if (usersList != null) {
                writeJsonDataFile(
                    JsonHelper.toJson(usersList, usersList.getClass()),
                    file);
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

    private void UpdateProcess(User user, DataUser existingUser) {
        // Retrieve private file
        String fileName = existingUser.getFileLocation();

        // Change the targeted Json Object
        changeExistingUserInJsonFile(
                fileName,
                user
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
            List<User> usersList = new ArrayList<>();
            String fileContent = getStringFromFile(file);
            Type listType = new TypeToken<ArrayList<User>>(){}.getType();
            usersList = JsonHelper.translateJsonToObject(listType, fileContent);

            if (usersList != null && usersList.size() > 0) {
                usersList = changeExistingUser(usersList, user);
            } else {
                usersList.add(user);
            }

            if (usersList != null && usersList.size() > 0) {
                writeJsonDataFile(
                        JsonHelper.toJson(usersList, usersList.getClass()),
                        file);
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

    private String searchInDataStream(InputStream is, String uniqId) {
        JsonReader reader = null;
        try {
            boolean found = false;
            User tmp = JsonHelper.translateJsonToObject(User.class, is);

            reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
            reader.beginObject();

            while (!found && reader.hasNext()) {

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
        File selectedFile = new File(mContext.getExternalFilesDir(null), fileName);

        //if (!selectedFile.exists()) throw new FileNotFoundException("File not found on internal storage");

        return selectedFile;
    }

    private void writeJsonDataFile(String fileContent, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
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
            reader.beginArray();

            while (reader.hasNext()) {

                reader.beginObject();

                while (reader.hasNext()) {
                    String name =  reader.nextName();
                    if (name.equals(SEARCH_DELTA)) {
                        count++;
                    } else
                        reader.skipValue();
                }

                reader.endObject();
            }

            reader.endArray();
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
            File newFile  = new File(mContext.getExternalFilesDir(null), fileName);
            newFile.createNewFile();
            return newFile;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User searchUser(String identifier) throws Exception {
        // Search in indexDB if already checked once
        // and retrieve fileName if checked once.
        DataUser existingUser = select(identifier);

        if (existingUser == null) return null;

        File file = new File(mContext.getExternalFilesDir(null), existingUser.getFileLocation());

        User u = searchSpecificUserInFile(file, existingUser);

        if (u == null)
            throw new Exception("User not found in the existing file " + file.getName());

        return u;
    }

    private User searchSpecificUserInFile(File file, DataUser user) {
        return JsonHelper.searchObjectInFile(User.class, file, user.getUniqId());
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
        if (getDbManager() != null
                && getDbManager().isOpen()) {
            getDbManager().close();
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
            getDbManager().open();
            initiateDbStaticData();
        } catch (SQLiteException e) {
            dbManager = null;
            isDbReady = false;
            e.printStackTrace();
        }
    }

    private void initiateDbStaticData() {
        List<BloodGroup> tmpBloodGroups = Storm.newSelect(getDbManager()).queryAll(BloodGroup.class);
        if (tmpBloodGroups == null || !(tmpBloodGroups.size() > 0)) {
            getDbManager().getDataBase().execSQL(
                mContext.getString(R.string.db_sqlinsert_bloodgroup)
            );
        }
        List<RelationType> tmpRelationTypes = Storm.newSelect(getDbManager()).queryAll(RelationType.class);
        if (tmpRelationTypes == null || !(tmpRelationTypes.size() > 0)) {
            getDbManager().getDataBase().execSQL(
                mContext.getString(R.string.db_sqlinsert_relationtype)
            );
        }

        if (!(getDbManager().count(CMAItem.class) > 0)) {
            InputStream stream = mContext.getResources().openRawResource(R.raw.db_sqlinsert_cma);
            String insertInto = "INSERT INTO `cma` (`cma_id`, `cma_code1`, `cma_code2`, `cma_level`, `cma_value`) VALUES ";
            if(stream != null)
            {
                BufferedReader br = null;

                String line;
                try {

                    br = new BufferedReader(new InputStreamReader(stream));
                    while ((line = br.readLine()) != null) {
                        getDbManager().getDataBase().execSQL(insertInto + line + ";");
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
            }
        }
    }

    public void insert(DataUser user) {
        if (isAppDbAvailable())
            throw new SQLiteException("DbManager has not been instanciated yet");
        try {
            Storm.newInsert(getDbManager()).insert(user);
        } catch (StormException e) {
            e.printStackTrace();
        }
    }

    private boolean isAppDbAvailable() {
        return !this.getDbManager().isOpen() || !isDbReady;
    }

    public void insert(String uniqId, String fileName) {
        this.insert(new DataUser(uniqId, fileName));
    }

    public DataUser select(String uniqId) {
        if (isAppDbAvailable())
            throw new SQLiteException("DbManager has not been instanciated yet");

        DataUser userData = Storm.newSelect(getDbManager()).query(DataUser.class, Selection.eq("uniqId", uniqId));

        if (userData != null) {
            return userData;
        } else {
            return null;
        }
    }

    public String selectBloodGroup(int id) {
        if (isAppDbAvailable())
            throw new SQLiteException("DbManager has not been instanciated yet");

        BloodGroup bloodGroup = Storm.newSelect(getDbManager()).query(BloodGroup.class, Selection.eq("bloodgroup_id", id));

        if (bloodGroup != null && bloodGroup.isValid()) {
            return bloodGroup.getName() + bloodGroup.getSign();
        } else {
            return null;
        }
    }

    public String selectCMA(int id) {
        if (isAppDbAvailable())
            throw new SQLiteException("DbManager has not been instanciated yet");

        CMAItem cma = Storm.newSelect(getDbManager()).query(CMAItem.class, Selection.eq("cma_id", id));

        if (cma != null && cma.isValid()) {
            return cma.getValue();
        } else {
            return null;
        }
    }

    public String selectRelationType(int id) {
        if (isAppDbAvailable())
            throw new SQLiteException("DbManager has not been instanciated yet");

        RelationType relationType = Storm.newSelect(getDbManager()).query(RelationType.class, Selection.eq("relationtype_id", id));

        if (relationType != null && relationType.isValid()) {
            return relationType.getName();
        } else {
            return null;
        }
    }

    public void update(DataUser user) {
        if (isAppDbAvailable())
            throw new SQLiteException("DbManager has not been instanciated yet");

        try {
            Storm.newUpdate(getDbManager()).update(user, Selection.eq("uniqId", user.getUniqId()));
        } catch (StormException e) {
            e.printStackTrace();
        }
    }

    public DatabaseManager getDbManager() {
        return dbManager;
    }
}
