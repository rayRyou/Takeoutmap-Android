package com.oakleafproject.takeoutmapk.ui.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.oakleafproject.takeoutmapk.DetailActivity;
import com.oakleafproject.takeoutmapk.R;
import com.oakleafproject.takeoutmapk.Shop;
import com.oakleafproject.takeoutmapk.TApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardFragment extends Fragment implements AdapterView.OnItemClickListener{

    private DashboardViewModel dashboardViewModel;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final ListView listView = root.findViewById(R.id.ShopList);

        TApplication app = (TApplication)getActivity().getApplication();
        ShopListAdapter adapter = new ShopListAdapter(this.getContext());
        long objId = 0;
        ArrayList<Shop> shopList = new ArrayList<>();
        for (String key : app.shopList.keySet()) {
            try {
                JSONObject shopData = app.shopList.get(key);
                Log.d("dev", shopData.toString());

                Shop shop = new Shop(getContext(), shopData, objId, key);
                shopList.add(shop);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            objId++;
        }
        adapter.shopList = shopList;
        Log.v("",""+listView.getCount());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return root;
    }

//    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TApplication app = (TApplication)getActivity().getApplication();
        int objId = 0;
        String objKey = null;
        for (String key : app.shopList.keySet()) {
            if (id == objId){
                objKey = key;
            }
            objId++;
        }

        if (objKey != null) {
            Intent detail = new Intent(getActivity().getApplication(), DetailActivity.class);
            detail.putExtra("shop-index", objKey);
            startActivity(detail);
        }

    }
}
