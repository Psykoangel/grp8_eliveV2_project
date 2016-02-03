package fr.group8.elive.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fr.group8.elive.app.R;
import fr.group8.elive.models.ItemTraitement;

/**
 * Created by chriis on 03/02/2016.
 */
public class ItemTraitementAdapter extends ArrayAdapter<ItemTraitement> {
    public ItemTraitementAdapter(Context context, List<ItemTraitement> items){super(context,0,items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView ==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_traitement,parent,false);
        }
        ItemViewHolder viewHolder = (ItemViewHolder) convertView.getTag();
        if(viewHolder ==null){
            viewHolder = new ItemViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.TextViewTraitement);
            convertView.setTag(viewHolder);
        }

        ItemTraitement itemTraitement = getItem(position);
        viewHolder.text.setText(itemTraitement.getsNomTraitement());

        return convertView;


    }

    private class ItemViewHolder{
        public TextView text;
    }
}
