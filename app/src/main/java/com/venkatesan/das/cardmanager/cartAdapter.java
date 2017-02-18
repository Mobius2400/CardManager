package com.venkatesan.das.cardmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.venkatesan.das.cardmanager.R;
import com.venkatesan.das.cardmanager.YugiohCard;

import java.util.ArrayList;

/**
 * Created by Das on 2/16/2017.
 */

public class cartAdapter extends BaseAdapter {
    private static ArrayList<YugiohCard> searchArrayList;

    private LayoutInflater mInflater;

    public cartAdapter(Context context, ArrayList<YugiohCard> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.cart_list, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.cardName);
            holder.txtPrintTag = (TextView) convertView.findViewById(R.id.printTag);
            holder.txtRarity = (TextView) convertView.findViewById(R.id.rarity);
            holder.txtQuantity = (TextView) convertView.findViewById(R.id.numQuantity);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(searchArrayList.get(position).getName());
        holder.txtPrintTag.setText(searchArrayList.get(position).getPrint_tag());
        holder.txtRarity.setText(searchArrayList.get(position).getRarity());
        holder.txtQuantity.setText(Integer.toString(searchArrayList.get(position).getNumInventory()));

        return convertView;
    }

    static class ViewHolder {
        TextView txtName;
        TextView txtPrintTag;
        TextView txtRarity;
        TextView txtQuantity;
    }
}
