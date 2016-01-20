package fr.group8.elive.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by chriis on 20/01/2016.
 */
public class nfcFragment extends Fragment{

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nfcfragment, container, false);

        Button button = (Button) v.findViewById(R.id.btnNFC);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                android.nfc.NfcAdapter mNfcAdapter = android.nfc.NfcAdapter.getDefaultAdapter(v.getContext());

                if (mNfcAdapter == null) {
                    showAlert(v.getContext(), "Info", getString(R.string.msg_nonfc));
                    return;
                }

                if (!mNfcAdapter.isEnabled()) {

                    AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getContext());

                    alertbox.setTitle("Info");
                    alertbox.setMessage(getString(R.string.msg_nfcoff));
                    alertbox.setPositiveButton("Paramètres NFC", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                startActivity(intent);
                            }
                        }
                    });
                    alertbox.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertbox.show();

                }else{
                    showAlert(v.getContext(), "Info", getString(R.string.msg_nfcon));
                }
            }
        });

        return v;
    }

    public void showAlert(Context context, String title, String msg) {

        AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
        alertbox.setTitle(title);
        alertbox.setMessage(msg);
        alertbox.show();

    }
}
