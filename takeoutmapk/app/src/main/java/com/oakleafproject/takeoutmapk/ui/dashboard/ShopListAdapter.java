package com.oakleafproject.takeoutmapk.ui.dashboard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oakleafproject.takeoutmapk.R;
import com.oakleafproject.takeoutmapk.Shop;

import java.util.ArrayList;

public class ShopListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Shop> shopList;

    public ShopListAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void setFoodList(ArrayList<Shop> foodList) {
        this.shopList = foodList;
    }

    @Override
    public int getCount() {
        return shopList.size();
    }

    @Override
    public Object getItem(int position) {
        return shopList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return shopList.get(position).objId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.shoplist_cell,parent,false);
        Shop shop = shopList.get(position);
        ((TextView)convertView.findViewById(R.id.cellTitle)).setText(shop.shopName);
        ((TextView)convertView.findViewById(R.id.cellDescription)).setText(shopList.get(position).description);

        return convertView;
    }
}
