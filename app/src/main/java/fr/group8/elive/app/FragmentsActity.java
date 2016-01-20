package fr.group8.elive.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Pair;

import java.util.List;
import java.util.Vector;


/**
 * Created by chriis on 20/01/2016.
 */
public class FragmentsActity extends FragmentActivity {
    private dmuFragment dmuFrag;
    private nfcFragment nfcFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.viewpager);

        // Création de la liste de FragmentsActivity que fera défiler le PagerAdapter
        List<Fragment> fragments = new Vector<Fragment>();

        dmuFrag = new dmuFragment();
        nfcFrag = new nfcFragment();

        // Ajout des FragmentsActivity dans la liste
        fragments.add(nfcFrag);
        fragments.add(dmuFrag);

        // Création de l'adapter qui s'occupera de l'affichage de la liste de FragmentsActivity
        PagerAdapter mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);

        ViewPager pager = (ViewPager) super.findViewById(R.id.viewpager);
        // Affectation de l'adapter au ViewPager
        pager.setAdapter(mPagerAdapter);

        Pair<Boolean, String> result = nfcWrapper.Instance().setNfcAdapter(this);

        if (result != null && !result.first) nfcFrag.showAlert(this, "Error", result.second);
    }
}
