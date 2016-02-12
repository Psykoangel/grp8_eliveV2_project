package fr.group8.elive.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fr.group8.elive.app.R;
import fr.group8.elive.models.Patient;

/**
 * Created by chriis on 03/02/2016.
 */
public class ItemRelationAdapter extends ArrayAdapter<Patient.Relation> {

    public ItemRelationAdapter(Context context, List<Patient.Relation> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_relation,parent,false);
        }

        ItemViewHolder viewHolder = (ItemViewHolder) convertView.getTag();

        if(viewHolder == null){
            viewHolder = new ItemViewHolder();
            viewHolder.textNom = (TextView) convertView.findViewById(R.id.TextViewRelationNom);
            viewHolder.textPrenom = (TextView) convertView.findViewById(R.id.TextViewRelationPrenom);
            viewHolder.textType = (TextView) convertView.findViewById(R.id.TextViewRelationType);
            convertView.setTag(viewHolder);
        }

        Patient.Relation Relation = getItem(position);
        viewHolder.textNom.setText(Relation.getsNomRelation());
        viewHolder.textPrenom.setText(Relation.getsPrenomRelation());
        viewHolder.textType.setText(Relation.getsType());

        return convertView;
    }

    private class ItemViewHolder{
        public TextView textNom;
        public TextView textPrenom;
        public TextView textType;

    }
}
