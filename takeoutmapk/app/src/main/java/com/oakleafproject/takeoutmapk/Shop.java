package com.oakleafproject.takeoutmapk;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Shop extends Activity {
    enum Day {
        Monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday,
        Saturday,
        Sunday,
        Other
    }
    public long objId;
    public String markerId;
    public String shopName;
    public String address;
    public String addressBuilding;
    public String tel;
    public String contact;
    public String description;
    public  String canDerivery;
    public  String deriveryUrl;
    public  String snsUrl;
    public String imageUrl;
    public String mailAddress;
    public String category;
    public ArrayList<Day> openDays;
    public LatLng location;
    public boolean isOpenData;
    public boolean openStatus = true;
    JSONObject sourceData;

    public JSONObject getSourceData() {
        return sourceData;
    }

    public Shop(Context context, JSONObject shopData, long objId, String markerId) throws JSONException {
        this.sourceData = shopData;
        this.objId = objId;
        this.markerId = markerId;
        this.shopName = shopData.getString(context.getString(R.string.shop_name));
        this.address = shopData.getString(context.getString(R.string.shop_address));
        String daysStr = shopData.getString(context.getString(R.string.shop_open_day));

        if (shopData.has(context.getString(R.string.shop_building))) {
            this.addressBuilding = shopData.getString(context.getString(R.string.shop_building));
        }
        this.openDays = new ArrayList<>();
        String[] days = daysStr.split(", ");
        for (String day: days) {
            if (day.equals(context.getString(R.string.week_mon))){
                this.openDays.add(Day.Monday);
            }else if (day.equals(context.getString(R.string.week_tue))){
                this.openDays.add(Day.Tuesday);
            }else if (day.equals(context.getString(R.string.week_wed))){
                this.openDays.add(Day.Wednesday);
            }else if (day.equals(context.getString(R.string.week_thu))){
                this.openDays.add(Day.Thursday);
            }else if (day.equals(context.getString(R.string.week_fri))){
                this.openDays.add(Day.Friday);
            }else if (day.equals(context.getString(R.string.week_sat))){
                this.openDays.add(Day.Saturday);
            }else if (day.equals(context.getString(R.string.week_sun))){
                this.openDays.add(Day.Sunday);
            }else{
                this.openDays.add(Day.Other);
            }
        }

        if (shopData.has(context.getString(R.string.shop_tel))) {
            this.tel = shopData.getString(context.getString(R.string.shop_tel));
        }

        if (shopData.has(context.getString(R.string.shop_contact))) {
            this.contact = shopData.getString(context.getString(R.string.shop_contact));
        }

        if (shopData.has(context.getString(R.string.shop_description))) {
            this.description = shopData.getString(context.getString(R.string.shop_description));
        }

        if (shopData.has(context.getString(R.string.shop_derivery))) {
            this.canDerivery = shopData.getString(context.getString(R.string.shop_derivery));
        }

        if (shopData.has(context.getString(R.string.shop_sns))) {
            this.snsUrl = shopData.getString(context.getString(R.string.shop_sns));
        }

        if (shopData.has(context.getString(R.string.shop_image_url))) {
            this.imageUrl = shopData.getString(context.getString(R.string.shop_image_url));
        }

        if (shopData.has(context.getString(R.string.shop_mailaddress))) {
            this.mailAddress = shopData.getString(context.getString(R.string.shop_mailaddress));
        }

        if (shopData.has(context.getString(R.string.shop_open_data))) {
            String res = shopData.getString(context.getString(R.string.shop_open_data));
            if (res.equals("はい")){
                this.isOpenData = false;
            }else{
                this.isOpenData = true;
            }

        }

        if (shopData.has(context.getString(R.string.shop_category))) {
            this.category = shopData.getString(context.getString(R.string.shop_category));
        }

        if (shopData.has(context.getString(R.string.shop_is_open))) {
            String openSt = shopData.getString(context.getString(R.string.shop_is_open));
            if (openSt.equals("営業中")) {
                this.openStatus = true;
            }else if (openSt.equals("休止")){
                this.openStatus = false;
            }
        }

        if (shopData.has(context.getString(R.string.shop_location))) {
            String locStr = shopData.getString(context.getString(R.string.shop_location));
            String[] locs = locStr.split(", ");
            Double lat = Double.parseDouble(locs[0]);
            Double lng = Double.parseDouble(locs[1]);
            LatLng location = new LatLng(lat, lng);
            this.location = location;
        }

    }
}
