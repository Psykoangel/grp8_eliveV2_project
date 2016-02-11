package fr.group8.elive.tasks;

import android.os.AsyncTask;

import java.io.InputStream;

import fr.group8.elive.utils.StorageManager;

/**
 * Created by psyko on 10/02/16.
 */
public class AutoLocalStorageTask extends AsyncTask<InputStream, Void, Void> {
    @Override
    protected Void doInBackground(InputStream... params) {

        if (!(params.length > 0)) return null;

        try {
            StorageManager.Instance().storeJsonData(params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
