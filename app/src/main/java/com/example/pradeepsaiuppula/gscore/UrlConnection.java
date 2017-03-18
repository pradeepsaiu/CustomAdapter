package com.example.pradeepsaiuppula.gscore;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import static java.sql.Types.NULL;

/**
 * Created by pradeepsaiuppula on 3/17/17.
 */

public class UrlConnection extends AsyncTask<String,String,String> {

    URL url;
    HttpURLConnection urlConnection;
    int code;
    String output;

    public String stringReader(InputStream is) throws IOException{

        StringBuilder return_value= new StringBuilder();
        if(is!=null)
        {
            InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
            BufferedReader bf = new BufferedReader(isr);
            String line = bf.readLine();
            while(line!=null){
                 return_value.append(line);
                line = bf.readLine();
            }
        }
        return  return_value.toString();
    }

    @Override
    protected String doInBackground(String... params) {
        String jsonResponse = "";
        InputStream inputStream = null;
        try {
            url = new URL(params[0]);
            if (url == null) {
                return jsonResponse;
            }

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(1500);
            urlConnection.connect();
//            Log.e("urlConnection===>",urlConnection.getResponseCode()+"");
            code = urlConnection.getResponseCode();
            if (code == 200) {
                inputStream = urlConnection.getInputStream();
                output = jsonResponse = stringReader(inputStream);
                Log.d("==>",""+jsonResponse);


            } else {
//                throw (Exception e(""));
            }


        }
        catch (Exception e) {
            Log.e("Exception connection", "" + e.getMessage() + e.getLocalizedMessage() + e.toString());
        }
        finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return jsonResponse;
        }
    }

    @Override
    protected void onPostExecute(String s) {

        try {

            JSONArray j = new JSONArray(s);
            Log.d("length=",j.length() +"");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
