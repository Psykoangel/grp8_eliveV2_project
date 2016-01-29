package fr.group8.elive.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import fr.group8.elive.app.R;

/**
 * Created by psyko on 28/01/16.
 */
public class AlertHelper {
    public static void showAlert(Context context, String title, String msg) {
        AlertDialog.Builder alertBox = new AlertDialog.Builder(context, R.style.AppTheme_Dark);
        alertBox.setTitle(title);
        alertBox.setMessage(msg);
        alertBox.show();
    }
}
