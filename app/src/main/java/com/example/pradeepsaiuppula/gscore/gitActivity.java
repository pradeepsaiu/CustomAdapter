package com.example.pradeepsaiuppula.gscore;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import java.util.ArrayList;
import java.util.List;

public class gitActivity extends AppCompatActivity {

    ArrayList<GitDetails> detailed_view = new ArrayList<GitDetails>();
    GitAdapter list_adapter;
    ListView display_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git);

        //Change this String to a class object


        list_adapter = new GitAdapter(this, detailed_view);
        display_list = (ListView) findViewById(R.id.main_list);



//
//        detailed_view.add(new GitDetails("User1",1,1000));
//        detailed_view.add(new GitDetails("User2",2,500));
//        detailed_view.add(new GitDetails("User3",3,400));
//        detailed_view.add(new GitDetails("User4",4,300));
//        detailed_view.add(new GitDetails("User5",5,200));
//        detailed_view.add(new GitDetails("User6",6,100));
//        detailed_view.add(new GitDetails("User7",7,50));
//        detailed_view.add(new GitDetails("User8",8,490));
//



        new UrlConnection().execute("https://api.github.com/users");

        display_list = (ListView) findViewById(R.id.main_list);

    }

    private class UrlConnection extends AsyncTask<String,String,String> {

        URL url;
        HttpURLConnection urlConnection;
        int code;
        String output;

        public String stringReader(InputStream is) throws IOException {

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

                for(int i=0; i<j.length();i++){
                    String s1 = j.getJSONObject(i).getString("login");
                    Log.d("error",s1+"");
                    detailed_view.add(new GitDetails(s1,1,1));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            display_list.setAdapter( list_adapter );
        }
    }

}
