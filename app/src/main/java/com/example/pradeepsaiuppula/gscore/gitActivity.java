package com.example.pradeepsaiuppula.gscore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class gitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git);

        //Change this String to a class object
        ArrayList<GitDetails> detailed_view = new ArrayList<GitDetails>();

        detailed_view.add(new GitDetails("User1",1,1000));
        detailed_view.add(new GitDetails("User2",2,500));
        detailed_view.add(new GitDetails("User3",3,400));
        detailed_view.add(new GitDetails("User4",4,300));
        detailed_view.add(new GitDetails("User5",5,200));
        detailed_view.add(new GitDetails("User6",6,100));
        detailed_view.add(new GitDetails("User7",7,50));
        detailed_view.add(new GitDetails("User8",8,490));


        GitAdapter list_adapter = new GitAdapter(this, detailed_view);

        ListView display_list = (ListView) findViewById(R.id.main_list);
        display_list.setAdapter( list_adapter );

        Log.d("",""+new UrlConnection().execute("https://api.github.com/users"));
    }
}
