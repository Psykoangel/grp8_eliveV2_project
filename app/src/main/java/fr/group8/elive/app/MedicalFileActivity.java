package fr.group8.elive.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fr.group8.elive.exceptions.NfcException;
import fr.group8.elive.models.CMAItem;
import fr.group8.elive.models.Patient;
import fr.group8.elive.models.Relationship;
import fr.group8.elive.models.User;
import fr.group8.elive.tasks.AutoLocalStorageTask;
import fr.group8.elive.utils.AlertHelper;
import fr.group8.elive.utils.JsonHelper;
import fr.group8.elive.utils.NfcWrapper;
import fr.group8.elive.utils.StorageManager;
import fr.group8.elive.utils.WebService;
import fr.group8.elive.view.ItemAlergieMaladieAdapter;
import fr.group8.elive.view.ItemRelationAdapter;
import ru.noties.storm.exc.StormException;

import static fr.group8.elive.utils.NfcExceptionType.NFC_FUNCTION_NOT_ENABLE;

public class MedicalFileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Defines the single acces to the Storage Manager object
    // Handles the SQLite and private files management
    // StorageManager follows the Singleton pattern,
    // it does not need to be instancied in a member variable of this activity.

    // Defines the Internet Access Connection Status as a boolean value
    private boolean isConnected;

    // Defines the NFC Connection Status as a boolean value
    private boolean isNfcConnected;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private PlaceholderFragment placeholderFragment;
    private ViewPager mViewPager;
    // public static Context context;
    private ImageView imageViewNFC;
    private ImageView imageViewConnexion;
    private DrawerLayout drawer;

    public static Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        patient = null;

        setContentView(R.layout.activity_medical_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageViewNFC = (ImageView) findViewById(R.id.imageviewnfc);
        imageViewConnexion = (ImageView) findViewById(R.id.imageviewinternet);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        placeholderFragment = new PlaceholderFragment();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Timer timer = new Timer();
        MyTimer mytimer = new MyTimer();
        timer.schedule(mytimer, 0, 1000 * 60);// 1000 = 1sec; *60 = 1min.

        checkMedias();

        try {
            NfcWrapper.Instance().setNfcAdapter(this);
        } catch (NfcException e) {
            if (e.getExceptionType() == NFC_FUNCTION_NOT_ENABLE.toString()) {
                isNfcConnected = false;
            }
        }

        StorageManager.Instance().loadContext(this);
        try {
            StorageManager.Instance().createDataBase(this);
        } catch (StormException e) {
            e.printStackTrace();
        }
        StorageManager.Instance().initiateDBManager(this, StorageManager.APP_DB_NAME);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Toast.makeText(this, getString(R.string.nfc_tag_detected), Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "NB : " + StorageManager.Instance().getDbManager().count(CMAItem.class), Toast.LENGTH_LONG).show();

        String userId = null;

        try {
            userId = NfcWrapper.Instance().handleTagIntent(intent);
        } catch (NfcException e) {
            e.printStackTrace();
        }

        // AlertHelper.showAlert(this, "DEBUG", "UserId : " + userId);

        if (userId != null && !userId.isEmpty())
        {
            Patient p = null;
            if (isConnected) {
                WebService ws = new WebService();
                InputStream is = ws.getUserInfos(userId);

                User user = JsonHelper.translateJsonToObject(User.class, is);

                AutoLocalStorageTask task = new AutoLocalStorageTask();
                task.execute(user);

                List<User> relations = null;
                if (!user.getRelationships().isEmpty()) {
                    relations = new ArrayList<>();
                    for (Relationship r : user.getRelationships()) {
                        relations.add(JsonHelper.translateJsonToObject(User.class, ws.getUserInfos(r.getUserUniqTargetId())));
                    }
                }

                if (relations != null) {
                    p = new Patient(user, relations);
                } else {
                    p = new Patient(user);
                }
            } else {
                p = searchLocalData(userId);
                if (p == null)
                    AlertHelper.showAlert(this, "ERROR", "No patient found !");
            }

            if (p != null) {
                // Display function to display Patient Info
                displayPatient(p);
            } else {
                AlertHelper.showAlert(this, "ERROR", "No user found locally either remotely.");
            }
        }

    }

    private Patient searchLocalData(String userId) {

        try {
            User user = searchLocalUser(userId);
            List<User> relations = null;
            if (!user.getRelationships().isEmpty()) {
                relations = new ArrayList<>();
                for (Relationship r : user.getRelationships()) {
                    relations.add(searchLocalUser(r.getUserUniqTargetId()));
                }
            }

            if (relations != null) {
                return new Patient(user, relations);
            } else {
                return new Patient(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private User searchLocalUser(String userTargetUniqId) throws Exception {

        User u = StorageManager.Instance().searchUser(userTargetUniqId);

        return u;
    }

    private void checkInternetStatus() {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void checkNfcStatus() {
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        isNfcConnected = mNfcAdapter.isEnabled();
    }

    private void checkMedias() {
        checkNfcStatus();
        checkInternetStatus();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyMediaStatusUI();
            }
        });
    }

    private void notifyMediaStatusUI() {
        if (!isNfcConnected)
            imageViewNFC.setImageResource(R.drawable.nfcrouge);
        else imageViewNFC.setImageResource(R.drawable.nfcvert);

        if (!isConnected)
            imageViewConnexion.setImageResource(R.drawable.connexionrouge);
        else imageViewConnexion.setImageResource(R.drawable.connexionvert);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (NfcWrapper.Instance().IsContextReady())
            NfcWrapper.Instance().desactivateForegroundIntentCatching(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkMedias();
        if (NfcWrapper.Instance().IsContextReady()) {
            NfcWrapper.Instance().activateForegroundIntentCatching(this);
        } else {
            try {
                NfcWrapper.Instance().setNfcAdapter(this);
                NfcWrapper.Instance().activateForegroundIntentCatching(this);
            } catch (NfcException e) {
                e.printStackTrace();
            }
        }

    }

    public void displayPatient(Patient pPatient){
        patient = pPatient;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_manage) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {}

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {

            PlaceholderFragment fragment = new PlaceholderFragment();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            ListView mListView;
            View rootView;

            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1)
            {
                rootView = inflater.inflate(R.layout.item_information, container, false);

                TextView tvNomLabel = (TextView) rootView.findViewById(R.id.nomlabel);
                TextView tvPrenomLabel = (TextView) rootView.findViewById(R.id.prenomlabel);
                TextView tvAdresseLabel = (TextView) rootView.findViewById(R.id.adresse);
                TextView tvGrouppeSanguinLabel = (TextView) rootView.findViewById(R.id.groupe_sanguinlabel);
                TextView tvIdLabel = (TextView) rootView.findViewById(R.id.idlabel);
                TextView tvTelLabel = (TextView) rootView.findViewById(R.id.tellabel);

                TextView tvNom = (TextView) rootView.findViewById(R.id.nom);
                TextView tvPrenom = (TextView) rootView.findViewById(R.id.prenom);
                TextView tvAdresse = (TextView) rootView.findViewById(R.id.adresse);
                TextView tvTel = (TextView) rootView.findViewById(R.id.tel);
                TextView tvId = (TextView) rootView.findViewById(R.id.id);
                TextView tvGrouppeSanguin = (TextView) rootView.findViewById(R.id.groupe_sanguin);

                tvNomLabel.setText(getString(R.string.nom));
                tvPrenomLabel.setText(getString(R.string.prenom));
                tvAdresseLabel.setText(getString(R.string.adress));
                tvTelLabel.setText(getString(R.string.tel));
                tvIdLabel.setText(getString(R.string.id));
                tvGrouppeSanguinLabel.setText(getString(R.string.groupe_sanguin));

                if (patient != null) {

                    tvNom.setText(patient.getInformation().getsNomPatient());
                    tvPrenom.setText(patient.getInformation().getsPrenomPatient());
                    tvAdresse.setText(patient.getInformation().getsAdresse());
                    tvTel.setText(patient.getInformation().getsTel());
                    tvId.setText(patient.getInformation().getsId());
                    tvGrouppeSanguin.setText(patient.getInformation().getsGroupeSanguin());
                }

            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                rootView = inflater.inflate(R.layout.frag_relation, container, false);

                mListView = (ListView) rootView.findViewById(R.id.listViewRelation);
                TextView emptyText = (TextView) rootView.findViewById(R.id.empty_relations);

                mListView.setEmptyView(emptyText);

                if (patient != null && !patient.getListRelation().isEmpty()) {
                    ItemRelationAdapter adapter = new ItemRelationAdapter(getContext(), patient.getListRelation());

                    mListView.setAdapter(adapter);
                }


            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                rootView = inflater.inflate(R.layout.frag_alergie_maladie, container, false);

                mListView = (ListView) rootView.findViewById(R.id.listViewAlergieMaladie);
                TextView emptyText = (TextView) rootView.findViewById(R.id.empty_alergies);

                mListView.setEmptyView(emptyText);

                if (patient != null && !patient.getListAlergieMaladie().isEmpty()) {
                    ItemAlergieMaladieAdapter adapter = new ItemAlergieMaladieAdapter(getContext(), patient.getListAlergieMaladie());

                    mListView.setAdapter(adapter);
                }

            } else {
                rootView = inflater.inflate(R.layout.fragment_medical_file, container, false);

            }

            NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(rootView.getContext());

            if (mNfcAdapter == null) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage(getString(R.string.msg_nonfc));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Information";
                case 1:
                    return "Relation";
                case 2:
                    return "Alergie et maladie";
            }
            return null;
        }

    }

    class MyTimer extends TimerTask {

        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //code exécuté par l'UI thread

                    checkNfcStatus();

                    checkInternetStatus();

                    notifyMediaStatusUI();
                }
            });

        }
    }
}
