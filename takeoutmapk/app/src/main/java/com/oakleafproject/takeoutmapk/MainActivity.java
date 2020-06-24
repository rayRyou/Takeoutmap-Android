package com.oakleafproject.takeoutmapk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static com.oakleafproject.takeoutmapk.TApplication.getVersionCode;

public class MainActivity extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9350514743264939/4033312535");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        boolean success = new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                showVersionAlert();
            }
        }, 500);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int adTime = sharedPreferences.getInt("UPDATE_TIME", 8);


        if (mInterstitialAd.isLoaded()){
            if (adTime <= 0){
                mInterstitialAd.show();
                adTime = 8;
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }else{
                adTime -= 1;
            }
        }else{
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
        editor.putInt("UPDATE_TIME", adTime);
        editor.commit();


    }

    public void showVersionAlert(){
        double version = getVersionCode(getApplicationContext());
        TApplication app = (TApplication)getApplication();
        Log.v("ALert", "version:"+ String.valueOf(version));
        Log.v("ALert", "version:"+ String.valueOf(app.mustVersion));


        if (version < app.mustVersion){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.version_alert));
            builder.setMessage(getString(R.string.version_message));
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
