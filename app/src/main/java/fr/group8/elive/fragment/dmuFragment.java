package fr.group8.elive.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import fr.group8.elive.app.R;

/**
 * Created by chriis on 20/01/2016.
 */
public class dmuFragment extends Fragment{
    ListView listView;
    public View v;
    public Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.dmufragment, container, false);
        context = container.getContext();
        listView = (ListView) v.findViewById(R.id.listViewDmu);

        return v;
    }
}
