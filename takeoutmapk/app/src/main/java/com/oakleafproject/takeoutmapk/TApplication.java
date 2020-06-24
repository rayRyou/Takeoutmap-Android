package com.oakleafproject.takeoutmapk;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TApplication extends Application {
    public HashMap<String, JSONObject> shopList = new HashMap<>();
    public String jsonUrl = null;
    public long mustVersion = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("boot-setting").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Map<String, Object> data = document.getData();
                        String url = (String)data.get("json-url");
                        jsonUrl = url;
                        mustVersion = (long)data.get("android-version");
                    }
                }
            }
        });


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    public static double getVersionCode(Context context){
        PackageManager pm = context.getPackageManager();
        double versionCode = 0.0;
        try{
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return versionCode;
    }
}
