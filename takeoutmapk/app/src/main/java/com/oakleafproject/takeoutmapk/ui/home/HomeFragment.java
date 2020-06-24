package com.oakleafproject.takeoutmapk.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.oakleafproject.takeoutmapk.DetailActivity;
import com.oakleafproject.takeoutmapk.HttpTask;
import com.oakleafproject.takeoutmapk.R;
import com.oakleafproject.takeoutmapk.TApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private HomeViewModel homeViewModel;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Handler mainThreadHandler;
    JSONArray shops;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);                // ここでNullPointerExceptionが発生します

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        LatLng location = new LatLng(35.862088, 139.971479);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean success = new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                doGetJson();
            }
        }, 500);

    }
    public void doGetJson(){
        HttpTask task = new HttpTask(this);
        TApplication app = (TApplication)getActivity().getApplication();
        String urlStr = app.jsonUrl;
        if (urlStr != null){
            task.execute(urlStr, "");
        }else{
            boolean success = new Handler().postDelayed(new Runnable(){
                @Override
                public void run(){
                    doGetJson();
                }
            }, 500);
        }

    }

    public void parseJson(String dataStr) {
        try {
            shops = new JSONArray(dataStr);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addAllMapMarker(shops);
                }
            });
        }catch (JSONException ex) {
            Log.d("dev", ex.toString());
        }
        return;
    }
    public void addAllMapMarker(JSONArray shopArray) {
        try {
            for (int cnt = 0; cnt < shopArray.length(); cnt++) {
                JSONObject shop = shopArray.getJSONObject(cnt);

                addMapMarker(shop, cnt);

            }

        }catch (JSONException ex){

        }
    }

    public void addMapMarker(JSONObject shop, int index) {
        try {
            String loc = shop.getString("Glide Location");
            String[] locData = loc.split(", ");
            String shopName = shop.getString("店名");
            String description = shop.getString("メニュー (説明文 or URL) や備考など自由記入");
            if (locData.length == 2) {
                Double lat = Double.parseDouble(locData[0]);
                Double lng = Double.parseDouble(locData[1]);
                LatLng location = new LatLng(lat.doubleValue() , lng.doubleValue());
                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.icon_takeout);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 120, 120, false);

                MarkerOptions options = new MarkerOptions()
                        .position(location).title(shopName).snippet(description);
                options.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                Marker marker = mMap.addMarker(options);
                String markerId = marker.getId();

                TApplication app = (TApplication)getActivity().getApplication();
                app.shopList.put(markerId, shop);
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker marker) {

                        Intent detail = new Intent(getActivity().getApplication(), DetailActivity.class);
                        detail.putExtra("shop-index", marker.getId());
                        startActivity(detail);
                    }
                });

            }
        }catch (JSONException ex){

        }



    }

}
