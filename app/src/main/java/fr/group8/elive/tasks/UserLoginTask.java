package fr.group8.elive.tasks;

/**
 * Created by psyko on 18/01/16.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import fr.group8.elive.app.LoginActivity;
import fr.group8.elive.app.R;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String mEmail;
    private final String mPassword;
    private final LoginActivity mActivity;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "elive:elive", "herve:herve", "michael:michael", "chris:chris"
    };

    public UserLoginTask(LoginActivity activity, String email, String password) {
        mActivity = activity;
        mEmail = email;
        mPassword = password;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            // Simulate network access.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return false;
        }

        for (String credential : DUMMY_CREDENTIALS) {
            String[] pieces = credential.split(":");
            if (pieces[0].equals(mEmail)) {
                // Account exists, return true if the password matches.
                return pieces[1].equals(mPassword);
            }
        }

        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        mActivity.setmAuthTask(null);
        mActivity.showProgress(false);

        if (success) {
            mActivity.finish();
        } else {
            mActivity.getmPasswordView().setError(mActivity.getString(R.string.error_incorrect_password));
            mActivity.getmPasswordView().requestFocus();
        }
    }

    @Override
    protected void onCancelled() {
        mActivity.setmAuthTask(null);
        mActivity.showProgress(false);
    }
}
