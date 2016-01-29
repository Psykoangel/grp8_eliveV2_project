package fr.group8.elive.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import fr.group8.elive.app.R;

/**
 * Created by chriis on 20/01/2016.
 */
public class Adaptermaladie {
    ListView listView;
    public View v;
    public Context context;
    public String nameItem ="";
    private LinearLayout layoutOfDynamicContente;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.dmufragment, container, false);
        context = container.getContext();
        layoutOfDynamicContente = (LinearLayout) v;
        listView = (ListView) v.findViewById(R.id.listViewDmu);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nameItem = (String) parent.getItemAtPosition(position);
                CallFunc(nameItem, position);
            }
        });
        return v;
    }

    private void CallFunc(String nameItem, int position) {

        switch (nameItem) {
            case "Information personnel":
                break;
            case "Relation":
                break;
            case "Maladie":

                break;

        }
    }

    private void itemCategorie(){
        layoutOfDynamicContente.removeAllViewsInLayout();
    }
    private void itemRelationship(){
        layoutOfDynamicContente.removeAllViewsInLayout();
    }
    private void itemMaladie(){
        layoutOfDynamicContente.removeAllViewsInLayout();
    }
    private void itemInformationPersonnel(){
        layoutOfDynamicContente.removeAllViewsInLayout();
    }

}
