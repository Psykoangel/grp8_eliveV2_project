package fr.group8.elive.utils;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by psyko on 18/01/16.
 */
public class StorageManager {
    private static StorageManager ourInstance = new StorageManager();

    public static StorageManager Instance() {
        return ourInstance;
    }

    public static String appFileName = "elive_app_data";

    private FileOutputStream fileOutputStream;

    private StorageManager() {
        fileOutputStream = null;

    }

    public boolean openFileStorage(Context context, String fileName) {
        if (fileName == null || fileName.isEmpty() || fileOutputStream == null) return false;

        String innerFileName;
        if (fileName.isEmpty())
            innerFileName = appFileName;
        else
            innerFileName = fileName;

        try {
            fileOutputStream = context.openFileOutput(innerFileName, Context.MODE_APPEND);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void appendFileStorage(String text) {
        if (text == null || text.isEmpty() || fileOutputStream == null) return;

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
}
