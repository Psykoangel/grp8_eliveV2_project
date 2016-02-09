package fr.group8.elive.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.UiThread;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fr.group8.elive.models.Patient;
import fr.group8.elive.view.ItemAlergieMaladieAdapter;
import fr.group8.elive.view.ItemTraitementAdapter;


public class MedicalFileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static Context context;
    public static ImageView imageViewNFC;
    public static ImageView imageViewConnexion;
    private static DrawerLayout drawer;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageViewNFC = (ImageView) findViewById(R.id.imageviewnfc);
        imageViewConnexion = (ImageView) findViewById(R.id.imageviewinternet);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
        long lDelai = 4000;
        long lPeriode = 10000;
        Timer timer = new Timer();
        MyTimer mytimer = new MyTimer();
        timer.schedule(mytimer, lDelai, lPeriode);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MedicalFile Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://fr.group8.elive.app/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MedicalFile Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://fr.group8.elive.app/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
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
        private Patient patient;
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


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            patient =  new Patient(
                    new Patient.Information("MATYSIAK", "Hervé", "27 Porte de Buhl\n 68530 BUHL\n France", "a+", "MATYSIAK", "Papa", "MATYSIAK", "Maman"),
                    new ArrayList<Patient.Traitement>() {{
                        add(new Patient.Traitement("Advil"));
                        add(new Patient.Traitement("Doliprane 1000 mg"));
                        add(new Patient.Traitement("Effelralgan"));
                        add(new Patient.Traitement("Dafalgan"));
                        add(new Patient.Traitement("Levothyrox"));
                        add(new Patient.Traitement("Kardegic"));
                        add(new Patient.Traitement("Tahor"));
                        add(new Patient.Traitement("Atarax"));
                        add(new Patient.Traitement("Forlax"));
                        add(new Patient.Traitement("Daflon"));
                        add(new Patient.Traitement("Hexaquine"));
                        add(new Patient.Traitement("Spasfon"));
                    }},
                    new ArrayList<Patient.AlergieMaladie>() {{
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


            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                rootView = inflater.inflate(R.layout.item_information, container, false);
                TextView tvNom = (TextView) rootView.findViewById(R.id.nom);
                TextView tvPrenom = (TextView) rootView.findViewById(R.id.prenom);
                TextView tvAdresse = (TextView) rootView.findViewById(R.id.adresse);
                TextView tvGrouppeSanguin = (TextView) rootView.findViewById(R.id.groupe_sanguin);
                TextView tvMere = (TextView) rootView.findViewById(R.id.mere);
                TextView tvPere = (TextView) rootView.findViewById(R.id.pere);
                TextView tvNomLabel = (TextView) rootView.findViewById(R.id.nomlabel);
                TextView tvPrenomLabel = (TextView) rootView.findViewById(R.id.prenomlabel);
                TextView tvAdresseLabel = (TextView) rootView.findViewById(R.id.adresse);
                TextView tvGrouppeSanguinLabel = (TextView) rootView.findViewById(R.id.groupe_sanguinlabel);
                TextView tvMereLabel = (TextView) rootView.findViewById(R.id.merelabel);
                TextView tvPereLabel = (TextView) rootView.findViewById(R.id.perelabel);

                tvAdresseLabel.setText(getString(R.string.adress));
                tvGrouppeSanguinLabel.setText(getString(R.string.groupe_sanguin));
                tvNomLabel.setText(getString(R.string.nom));
                tvPrenomLabel.setText(getString(R.string.prenom));
                tvMereLabel.setText(getString(R.string.pere));
                tvPereLabel.setText(getString(R.string.mere));

                tvPere.setText( patient.getInformation().getsNomPerePatient() + " " + patient.getInformation().getsPrenomPerePatient());
                tvMere.setText(patient.getInformation().getsNomMerePatient() + " " + patient.getInformation().getsPrenomMerePatient());

                tvPrenom.setText(patient.getInformation().getsPrenomPatient());
                tvNom.setText(patient.getInformation().getsNomPatient());
                tvAdresse.setText(patient.getInformation().getsAdresse());
                tvGrouppeSanguin.setText(patient.getInformation().getsGrouppeSanguin());



            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                rootView = inflater.inflate(R.layout.frag_traitement, container, false);
                mListView = (ListView) rootView.findViewById(R.id.listViewTraitement);
                List<Patient.Traitement> traitements = patient.getListTraitement();
                ItemTraitementAdapter adapter = new ItemTraitementAdapter(getContext(), traitements);
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
                    return "Traitement";
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

                        imageViewNFC.setImageResource(R.drawable.nfcvert);
                    }


                    //code pour le connexion internet

                    //if ( ... ) {
                    //    imageViewConnexion.setImageResource(R.drawable.connexionrouge);
                    //}else{

                    //    imageViewConnexion.setImageResource(R.drawable.connexionvert);
                    //}

                }
            });

        }
    }
}
