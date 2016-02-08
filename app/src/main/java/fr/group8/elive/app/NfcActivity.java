package fr.group8.elive.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import fr.group8.elive.exceptions.NfcException;
import fr.group8.elive.utils.AlertHelper;
import fr.group8.elive.utils.NfcWrapper;

public class NfcActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            NfcWrapper.Instance().setNfcAdapter(this);
        } catch (NfcException e) {
            AlertHelper.showAlert(this, e.getExceptionType(), e.getMessage());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        NfcWrapper.Instance().desactivateForegroundIntentCatching(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        NfcWrapper.Instance().activateForegroundIntentCatching(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        AlertHelper.showAlert(this, getString(R.string.general_title_info), getString(R.string.nfc_tag_detected));

        String userId = null;

        try {
            userId = NfcWrapper.Instance().handleTagIntent(intent);
        } catch (NfcException e) {
            e.printStackTrace();
        }

        AlertHelper.showAlert(this, "DEBUG", "UserId : " + userId.toString());


    }
}
