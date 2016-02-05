package fr.group8.elive.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.group8.elive.app.R;
import fr.group8.elive.models.ItemAlergieMaladie;

/**
 * Created by chriis on 03/02/2016.
 */
public class ItemAlergieMaladieAdapter extends ArrayAdapter<ItemAlergieMaladie> {
    public ItemAlergieMaladieAdapter(Context context, List<ItemAlergieMaladie> items){super(context,0,items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView ==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_alergie_maladie,parent,false);
        }
        ItemViewHolder viewHolder = (ItemViewHolder) convertView.getTag();
        if(viewHolder ==null){
            viewHolder = new ItemViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.TextViewAlergieMaladie);
            viewHolder.date = (TextView) convertView.findViewById(R.id.TextViewDateAlergieMaladie);
            convertView.setTag(viewHolder);
        }

        ItemAlergieMaladie itemAlergieMaladie = getItem(position);
        viewHolder.text.setText(itemAlergieMaladie.getsNomMaladie());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date dDate = itemAlergieMaladie.getdDateMaladie();
        String reportDate = df.format(dDate);
        viewHolder.date.setText(reportDate);

        return convertView;


    }

    private class ItemViewHolder{
        public TextView text;
        public TextView date;
    }
}
