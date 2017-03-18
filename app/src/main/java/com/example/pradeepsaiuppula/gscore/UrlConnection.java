package com.example.pradeepsaiuppula.gscore;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pradeepsaiuppula on 3/17/17.
 */

public class UrlConnection extends AsyncTask<String,Void,String> {

    URL url;
    HttpURLConnection urlConnection;
    int code;

    @Override
    protected String doInBackground(String... params) {
        try{
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(1500);
            urlConnection.connect();
            Log.e("urlConnection===>",urlConnection.getResponseCode()+"");
            code = urlConnection.getResponseCode();
        }
        catch (Exception e){
            Log.e("urlConnection<---",""+e.getMessage()+e.getLocalizedMessage()+e.toString());
        }
        return "hello";
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("UrlConnecion",""+code);
    }
}
