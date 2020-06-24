package com.oakleafproject.takeoutmapk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String markerId = intent.getStringExtra("shop-index");
        try {

            ImageView openImage = findViewById(R.id.OpenImage);
            openImage.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

            ImageView addressImage = findViewById(R.id.AddressImage);
            addressImage.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            ImageView telImage = findViewById(R.id.TelImage);
            telImage.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            TApplication app = (TApplication)getApplication();
            JSONObject shop = app.shopList.get(markerId);
            ImageView shopImage = findViewById(R.id.ShopImage);
            Log.v("meg", markerId);
            Log.v("meg", app.shopList.toString());
            if (shop.has("写真URL")) {
                String imageUrl = shop.getString("写真URL");
                Log.v("output", imageUrl);
                if (imageUrl.length() > 0) {
                    new ImageTask(shopImage).execute(imageUrl);
                }else{
                    shopImage.setVisibility(View.GONE);
                }
            }


            TextView shopName = findViewById(R.id.ShopName);
            shopName.setText(shop.getString("店名"));

            TextView addressLabel = findViewById(R.id.AddressLabel);
            String address = shop.getString("所在地");
            if (shop.has("所在地(ビル名)")) {
                String buildingName = shop.getString("所在地(ビル名)");
                addressLabel.setText(address + " " + buildingName);
            }

            TextView openLabel = findViewById(R.id.OpenLabel);
            String openDay = shop.getString("実施曜日");
            openLabel.setText(openDay);

            TextView telLabel = findViewById(R.id.TelLabel);
            String tel = shop.getString("問い合わせ先 (TEL)");
            telLabel.setText(tel);

            ImageView snsImage = findViewById(R.id.SNSImage);
            if (shop.has("よく情報発信しているSNSのURL") && shop.getString("よく情報発信しているSNSのURL").length() > 0) {
                TextView snsLabel = findViewById(R.id.SNSLabel);
                String sns = shop.getString("よく情報発信しているSNSのURL");
                snsLabel.setText(sns);
                if (sns.indexOf("instagram.com") != -1){
                    snsImage.setImageResource(R.drawable.icon_instagram);
                }else if (sns.indexOf("facebook.com") != -1){
                    snsImage.setImageResource(R.drawable.icon_facebook);

                }else if (sns.indexOf("twitter.com") != -1){
                    snsImage.setImageResource(R.drawable.icon_twitter);
                }else{
                    snsImage.setImageResource(R.drawable.icon_homepage);
                }
                snsImage.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

            }else{
                LinearLayout layout = findViewById(R.id.SNSSection);
                layout.setVisibility(View.GONE);
            }

            ImageView deriveryImage = findViewById(R.id.DeriveryImage);
            deriveryImage.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            if (shop.has("デリバリーの有無")) {
                String derivery = shop.getString("デリバリーの有無");
                TextView deriveryLabel = findViewById(R.id.DeriveryLabel);
                deriveryLabel.setText(derivery);

            }

            if (shop.has("メニュー (説明文 or URL) や備考など自由記入")){
                TextView descLabel = findViewById(R.id.Description);
                String desc = shop.getString("メニュー (説明文 or URL) や備考など自由記入");
                descLabel.setText(desc);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
