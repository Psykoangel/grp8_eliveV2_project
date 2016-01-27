package fr.group8.elive.utils;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

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

    private final Boolean IS_DEBUG = true;
    private final int DB_VERSION = 1;
    private FileOutputStream fileOutputStream;
    private DatabaseManager dbManager;
    private boolean dbReady;


    private StorageManager() {
        fileOutputStream = null;
        dbManager = null;
        dbReady = false;
    }

    public boolean openFileStorage(Context context, String fileName) {

        if (fileOutputStream == null) return false;

        String innerFileName = appFileName;

        if (fileName == null || fileName.isEmpty())
        {
            innerFileName = fileName;
        }

        try {
            fileOutputStream = context.openFileOutput(innerFileName, Context.MODE_APPEND);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void appendFileStorage(String text) {
        if (text == null
                || text.isEmpty()
                || fileOutputStream == null) return;

        try {
            fileOutputStream.write(text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean closeFileStorage() {
        if (fileOutputStream == null) return true;

        try {
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createDataBase(Context context) {
        if (!dbReady)
        {
            Storm.getInstance().init(context, IS_DEBUG);
            dbReady = true;
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
