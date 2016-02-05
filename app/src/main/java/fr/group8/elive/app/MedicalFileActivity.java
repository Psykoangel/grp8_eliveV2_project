package fr.group8.elive.app;

import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.group8.elive.models.Patient;
import fr.group8.elive.view.ItemAlergieMaladieAdapter;
import fr.group8.elive.view.ItemTraitementAdapter;


public class MedicalFileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_file);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);




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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        menu.add("NFC").setIcon(R.drawable.ic_menu_gallery);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.test) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
            Patient hmatysiak = new Patient(
                    new Patient.Information("MATYSIAK", "Hervé", "27 Porte de Buhl\n 68530 BUHL\n France", "a+", "MATYSIAK","Papa","MATYSIAK","Maman"),
                    new ArrayList<Patient.Traitement>(){{
                        add(new Patient.Traitement("Advil"));
                        add(new Patient.Traitement("Ricola"));
                    }},
                    new ArrayList<Patient.AlergieMaladie>(){{
                        add(new Patient.AlergieMaladie("Glucide pondere0", new Date()));
                        add(new Patient.AlergieMaladie("Ricola", new Date()));
                    }}
            );



            if(getArguments().getInt(ARG_SECTION_NUMBER)==1)
            {
                rootView = inflater.inflate(R.layout.item_information, container, false);
                TextView tvNom = (TextView) rootView.findViewById(R.id.nom);
                TextView tvPrenom = (TextView) rootView.findViewById(R.id.prenom);
                TextView tvAdresse = (TextView) rootView.findViewById(R.id.adresse);
                TextView tvGrouppeSanguin = (TextView) rootView.findViewById(R.id.groupe_sanguin);
                TextView tvMere = (TextView) rootView.findViewById(R.id.mere);
                TextView tvPere = (TextView) rootView.findViewById(R.id.pere);

                tvAdresse.setText(hmatysiak.getInformation().getsAdresse());
                tvGrouppeSanguin.setText(getString(R.string.groupe_sanguin)+" " +hmatysiak.getInformation().getsGrouppeSanguin());
                tvNom.setText(hmatysiak.getInformation().getsNomPatient());
                tvPrenom.setText(hmatysiak.getInformation().getsPrenomPatient());
                tvPere.setText(getString(R.string.pere)+" " + hmatysiak.getInformation().getsNomPerePatient()+" "+hmatysiak.getInformation().getsPrenomPerePatient());
                tvMere.setText(getString(R.string.mere) +" "+ hmatysiak.getInformation().getsNomMerePatient()+" "+hmatysiak.getInformation().getsPrenomMerePatient());
                //tvPrenom.setText(getString(R.string.prenom)+patient.getInformation().getsPrenomPatient());
                //tvNom.setText(getString(R.string.nom)+patient.getInformation().getsNomPatient());
                //tvAdresse.setText(getString(R.string.adress)+patient.getInformation().getsAdresse());
                //tvGrouppeSanguin.setText(getString(R.string.groupe_sanguin)+patient.getInformation().getsGrouppeSanguin());


            }else if(getArguments().getInt(ARG_SECTION_NUMBER)==2)
            {
                rootView = inflater.inflate(R.layout.frag_traitement, container, false);
                mListView = (ListView) rootView.findViewById(R.id.listViewTraitement);
                List<Patient.Traitement> traitements = hmatysiak.getListTraitement();
                ItemTraitementAdapter adapter = new ItemTraitementAdapter(getContext(),traitements);
                if (mListView != null || adapter != null){
                    mListView.setAdapter(adapter);
                }


            }else if(getArguments().getInt(ARG_SECTION_NUMBER)==3)
            {
                rootView = inflater.inflate(R.layout.frag_alergie_maladie, container, false);
                mListView = (ListView) rootView.findViewById(R.id.listViewAlergieMaladie);
                List<Patient.AlergieMaladie> alergieMaladies = hmatysiak.getListAlergieMaladie();
                ItemAlergieMaladieAdapter adapter = new ItemAlergieMaladieAdapter(getContext(),alergieMaladies);
                if (mListView != null || adapter != null){
                    mListView.setAdapter(adapter);
                }

            }else{
                rootView = inflater.inflate(R.layout.fragment_medical_file, container, false);

            }

            android.nfc.NfcAdapter mNfcAdapter = android.nfc.NfcAdapter.getDefaultAdapter(rootView.getContext());

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
}
