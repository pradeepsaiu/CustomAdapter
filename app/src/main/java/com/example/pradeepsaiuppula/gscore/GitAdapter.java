package com.example.pradeepsaiuppula.gscore;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pradeepsaiuppula on 3/13/17.
 */

public class GitAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<GitDetails> gd;

    public GitAdapter(Activity context, ArrayList<GitDetails> androidFlavors) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, androidFlavors);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {



        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.eachlist, parent, false);
        }


        GitDetails obj = (GitDetails) getItem(position);

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.name);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        nameTextView.setText(obj.getUsername());

        TextView rankTextView = (TextView) listItemView.findViewById(R.id.stars);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        rankTextView.setText(obj.getTotal_stars()+"");

        TextView starsTextView = (TextView) listItemView.findViewById(R.id.score);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        starsTextView.setText(obj.getRanking()+"");

        return listItemView;
    }
}
