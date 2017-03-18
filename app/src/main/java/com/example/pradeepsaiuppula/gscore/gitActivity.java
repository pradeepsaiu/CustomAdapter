package com.example.pradeepsaiuppula.gscore;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
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
    String git_head="https://api.github.com/users";
    int user_base = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git);

        //Change this String to a class object


        list_adapter = new GitAdapter(this, detailed_view);
        display_list = (ListView) findViewById(R.id.main_list);


        new UrlConnection().execute(git_head,"?since="+user_base);

        display_list = (ListView) findViewById(R.id.main_list);
        display_list.setAdapter( list_adapter );
        display_list.setOnScrollListener(new EndlessScrollListener());

    }

    public class EndlessScrollListener implements AbsListView.OnScrollListener {

        private int visibleThreshold = 5;
        private int currentPage = 0;
        private int previousTotal = 0;
        private boolean loading = true;

        public EndlessScrollListener() {
        }
        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    currentPage++;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // I load the next page of gigs using a background task,
                // but you can call any function here.
                new UrlConnection().execute(git_head,"?since="+user_base);
                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
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
                url = new URL(params[0]+params[1]);
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
                    String username = j.getJSONObject(i).getString("login");
                    int id = j.getJSONObject(i).getInt("id");
                    detailed_view.add(new GitDetails(username,id,1,""));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            user_base+=30;
            list_adapter.notifyDataSetChanged();
        }
    }

}
