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
        setContentView(R.layout.activity_nfc);

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

        Integer userId = 0;

        try {
            userId = Integer.parseInt(NfcWrapper.Instance().handleTagIntent(intent));
        } catch (NfcException e) {
            e.printStackTrace();
        }

        AlertHelper.showAlert(this, "DEBUG", "UserId : " + userId.toString());

        // Intent nfcInfo = new Intent(this, FolderActivity.class);
        // nfcInfo.putExtra("user", "data");

        // this.startActivity(nfcInfo);
    }
}
