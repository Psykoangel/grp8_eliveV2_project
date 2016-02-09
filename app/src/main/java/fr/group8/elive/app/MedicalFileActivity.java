package fr.group8.elive.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.nfc.NfcAdapter;
import android.os.Build;
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

import java.io.InputStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fr.group8.elive.exceptions.NfcException;
import fr.group8.elive.models.Patient;
import fr.group8.elive.models.User;
import fr.group8.elive.utils.AlertHelper;
import fr.group8.elive.utils.JsonHelper;
import fr.group8.elive.utils.NfcWrapper;
import fr.group8.elive.utils.StorageManager;
import fr.group8.elive.utils.WebService;
import fr.group8.elive.view.ItemAlergieMaladieAdapter;

import ru.noties.storm.exc.StormException;

import fr.group8.elive.view.ItemRelationAdapter;



public class MedicalFileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Defines the single acces to the Storage Manager object
    // Handles the SQLite and private files management
    private StorageManager stManager;
    // Defines the Internet Access Connection Status as a boolean value
    private boolean isConnected;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static Context context;
    public static ImageView imageViewNFC;
    public static ImageView imageViewConnexion;
    private static DrawerLayout drawer;
    public static Patient patient;

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

        AlertHelper.showAlert(this, "DEBUG", "UserId : " + userId);

        if (userId != null && !userId.isEmpty())
        {
            Patient p = null;
            if (isConnected) {
                WebService ws = new WebService();
                InputStream is = ws.getUserInfos(userId);

                // ** TODO Run this on another Thread
                try {
                    stManager.storeJsonData(is);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // ** TODO !!!

                User u = JsonHelper.Instance().translateJsonToObject(User.class, is);
                p = new Patient(u);
            } else {
                p = searchLocalData(userId);
                if (p == null)
                    AlertHelper.showAlert(this, "ERROR", "No patient found !");
            }

            if (p != null) {
                // Display function to display Patient Info
                // TODO Here /!\
                // Display(p);
            } else {
                AlertHelper.showAlert(this, "ERROR", "No user found locally either remotely.");
            }
        }
    }

    private Patient searchLocalData(String userId) {

        try {
            User u = stManager.searchUser(userId);
            Patient p = new Patient(u);
            return p;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void checkInternetStatus() {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        patient =  new Patient(
                new Patient.Information(" ", " ", " ", " ", " "," "),
                new ArrayList<Patient.Relation>() {{
                    add(new Patient.Relation("Aucune donnée disponible..."," "," "));
                }},
                new ArrayList<Patient.AlergieMaladie>() {{
                    add(new Patient.AlergieMaladie("Aucune donnée disponible...", new Date()));
                }}
        );

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

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        context = this;

        Timer timer = new Timer();
        MyTimer mytimer = new MyTimer();
        timer.schedule(mytimer, 0, 1000 * 60 * 2);

        checkInternetStatus();

        try {
            NfcWrapper.Instance().setNfcAdapter(this);
            NfcWrapper.Instance().activateForegroundIntentCatching(this);
        } catch (NfcException e) {
            e.printStackTrace();
        }

        stManager = new StorageManager();
        stManager.loadContext(this);
        try {
            stManager.createDataBase(this);
        } catch (StormException e) {
            e.printStackTrace();
        }
        stManager.initiateDBManager(this, StorageManager.APP_DB_NAME);

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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
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
        private ListView mListView;
        public View rootView;

        public PlaceholderFragment() {
        }

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


        public void initPatient(Patient pPatient){
            patient = pPatient;
            newInstance(1);
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            Patient hmatysiak = new Patient(
                new Patient.Information("MATYSIAK", "Hervé", "27 Porte de Buhl\n 68530 BUHL\n France", "a+", "", ""),
                new ArrayList<Patient.Relation>(),
                new ArrayList<Patient.AlergieMaladie>(){{
                    add(new Patient.AlergieMaladie("Glucide pondere0", new Date()));
                    add(new Patient.AlergieMaladie("angine", new Date()));
                    add(new Patient.AlergieMaladie("Ricola", new Date()));
                    add(new Patient.AlergieMaladie("Brûlures", new Date()));
                    add(new Patient.AlergieMaladie("Cellulite", new Date()));
                    add(new Patient.AlergieMaladie("Aphtes", new Date()));
                    add(new Patient.AlergieMaladie("Anxiété", new Date()));
                    add(new Patient.AlergieMaladie("Conjonctivite", new Date()));
                    add(new Patient.AlergieMaladie("Chiasse ! MotherFucker :(", new Date()));
                }}
            );




            if(getArguments().getInt(ARG_SECTION_NUMBER)==1)
            {
                rootView = inflater.inflate(R.layout.item_information, container, false);
                TextView tvNom = (TextView) rootView.findViewById(R.id.nom);
                TextView tvPrenom = (TextView) rootView.findViewById(R.id.prenom);
                TextView tvAdresse = (TextView) rootView.findViewById(R.id.adresse);
                TextView tvGrouppeSanguin = (TextView) rootView.findViewById(R.id.groupe_sanguin);
                TextView tvTel = (TextView) rootView.findViewById(R.id.tel);
                TextView tvId = (TextView) rootView.findViewById(R.id.id);
                TextView tvNomLabel = (TextView) rootView.findViewById(R.id.nomlabel);
                TextView tvPrenomLabel = (TextView) rootView.findViewById(R.id.prenomlabel);
                TextView tvAdresseLabel = (TextView) rootView.findViewById(R.id.adresse);
                TextView tvGrouppeSanguinLabel = (TextView) rootView.findViewById(R.id.groupe_sanguinlabel);
                TextView tvIdLabel = (TextView) rootView.findViewById(R.id.idlabel);
                TextView tvTelLabel = (TextView) rootView.findViewById(R.id.tellabel);

                tvAdresseLabel.setText(getString(R.string.adress));
                tvGrouppeSanguinLabel.setText(getString(R.string.groupe_sanguin));
                tvNomLabel.setText(getString(R.string.nom));
                tvPrenomLabel.setText(getString(R.string.prenom));
                tvIdLabel.setText(getString(R.string.Tel));
                tvTelLabel.setText(getString(R.string.Id));

                tvId.setText( patient.getInformation().getsId());
                tvTel.setText(patient.getInformation().getsTel());

                tvPrenom.setText(patient.getInformation().getsPrenomPatient());
                tvNom.setText(patient.getInformation().getsNomPatient());
                tvAdresse.setText(patient.getInformation().getsAdresse());
                tvGrouppeSanguin.setText(patient.getInformation().getsGrouppeSanguin());



            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                rootView = inflater.inflate(R.layout.frag_relation, container, false);
                mListView = (ListView) rootView.findViewById(R.id.listViewRelation);
                List<Patient.Relation> relations = patient.getListRelation();
                ItemRelationAdapter adapter = new ItemRelationAdapter(getContext(), relations);

                if (mListView != null || adapter != null) {
                    mListView.setAdapter(adapter);
                }

            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                rootView = inflater.inflate(R.layout.frag_alergie_maladie, container, false);
                mListView = (ListView) rootView.findViewById(R.id.listViewAlergieMaladie);
                List<Patient.AlergieMaladie> alergieMaladies = patient.getListAlergieMaladie();
                ItemAlergieMaladieAdapter adapter = new ItemAlergieMaladieAdapter(getContext(), alergieMaladies);

                if (mListView != null || adapter != null) {
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

                    //coed pour le nfc
                    NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
                    if (!mNfcAdapter.isEnabled()) {
                        imageViewNFC.setImageResource(R.drawable.nfcrouge);
                    } else {

                        if (!mNfcAdapter.isEnabled())
                            imageViewNFC.setImageResource(R.drawable.nfcrouge);
                        else imageViewNFC.setImageResource(R.drawable.nfcvert);


                        //code pour le connexion internet
                        checkInternetStatus();

                        if (!isConnected)
                            imageViewConnexion.setImageResource(R.drawable.connexionrouge);
                        else imageViewConnexion.setImageResource(R.drawable.connexionvert);
                    }
                }
            });

        }
    }
}
