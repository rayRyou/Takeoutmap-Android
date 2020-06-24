package com.oakleafproject.takeoutmapk;

import android.app.Application;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.oakleafproject.takeoutmapk.ui.home.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.Logger;


public class HttpTask extends AsyncTask<String, Void, String> {
    private Fragment mFragment;

    public HttpTask(Fragment activity) {
        mFragment = activity;
    }
    StringBuilder result = new StringBuilder();

    @Override
    protected String doInBackground(String... strings) {
        final int CONNECTION_TIMEOUT = 30 * 1000;
        final int READ_TIMEOUT = 30 * 1000;
        URL url = null;
        try {
            url = new URL(strings[0]);
            if (url == null) {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn == null){
                Log.d("dev", "conn == null");
            }else {
                Log.d("dev", conn.toString());
            }

            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setRequestMethod("GET");
            conn.connect();
            int statusCode = conn.getResponseCode();
            if(statusCode == HttpURLConnection.HTTP_OK){
                //responseの読み込み
                final InputStream in = conn.getInputStream();
                String encoding = conn.getContentEncoding();

                if (encoding == null) {
                    Log.d("dev", "encoding == null");
                    encoding = "UTF-8";
                }

                final InputStreamReader inReader = new InputStreamReader(in, encoding);
                final BufferedReader bufferedReader = new BufferedReader(inReader);
                String line = null;
                while((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                bufferedReader.close();
                inReader.close();
                in.close();
                HomeFragment homeFragment = (HomeFragment)mFragment;
                homeFragment.parseJson(result.toString());
                return result.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }


}
