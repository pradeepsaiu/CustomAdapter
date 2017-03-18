package com.pup.pradeepsaiuppula.gscore;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pup.pradeepsaiuppula.gscore.R;

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

public class gitActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<GitDetails>> {

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


//        new UrlConnection().execute(git_head,"?since="+user_base);

        //1 represents the unique key here
        getSupportLoaderManager().initLoader(1,null,this);

        display_list = (ListView) findViewById(R.id.main_list);
        display_list.setAdapter( list_adapter );
        display_list.setOnScrollListener(new EndlessScrollListener());

    }

    @Override
    public Loader<List<GitDetails>> onCreateLoader(int id, Bundle args) {
        //returns an instance of git loader object
        return new GitLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<GitDetails>> loader, List<GitDetails> data) {
        // loader contains the list returned from do in background
        list_adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<GitDetails>> loader) {

    }

    public class EndlessScrollListener implements AbsListView.OnScrollListener {

        private int visibleThreshold = 5;
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
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
//                new UrlConnection().execute(git_head,"?since="+user_base);
                getSupportLoaderManager().initLoader(1,null, gitActivity.this);
                loading = true;

            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }


    public class GitLoader extends AsyncTaskLoader<List<GitDetails>>{

        HttpURLConnection urlConnection;
        URL url;
        int code;//saves response code

        public GitLoader(Context context) {
            super(context);
        }

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
        public List<GitDetails> loadInBackground() {
            String jsonResponse = "";
            InputStream inputStream = null;
            try {
                url = new URL(git_head+"?since="+user_base);
                if (url == null) {
                    return null;
                }

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(1500);
                urlConnection.connect();
//            Log.e("urlConnection===>",urlConnection.getResponseCode()+"");
                code = urlConnection.getResponseCode();
                if (code == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = stringReader(inputStream);
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



                try {
                    JSONArray j = new JSONArray(jsonResponse);

                    for(int i=0; i<j.length();i++){
                        String username = j.getJSONObject(i).getString("login");
                        int id = j.getJSONObject(i).getInt("id");
                        detailed_view.add(new GitDetails(username,id,1,""));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                user_base+=30;
                return  detailed_view;
//                list_adapter.notifyDataSetChanged();
                }
            }
        }
    }
