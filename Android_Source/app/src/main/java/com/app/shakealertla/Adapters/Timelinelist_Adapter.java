package com.app.shakealertla.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.shakealertla.R;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by AST on 10/25/2017.
 */

public class Timelinelist_Adapter extends ArrayAdapter<Object>{

    private ArrayList<Object> timelineArrayList;
    private ArrayList<Object> timelineArrayListBackup = new ArrayList<>();
    private Context context;
    private int mResource;


    public Timelinelist_Adapter(Context context, int mResource, ArrayList<Object> timelineArrayList) {
        super(context,mResource,timelineArrayList);
        this.timelineArrayList = timelineArrayList;
        this.mResource = mResource;
        this.timelineArrayListBackup.addAll(timelineArrayList);
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result = convertView;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(mResource, parent, false);
            result = convertView;
        }

        Object timeline = timelineArrayList.get(position);

        TextView tv_title = result.findViewById(R.id.ticker);
        TextView tv_desc = result.findViewById(R.id.toolbar);


        tv_title.setText(timeline.toString());
        tv_desc.setText(timeline.toString());


        return result;
    }

//    @Override
//    public void add(@Nullable Timeline timeline) {
//        super.add(timeline);
//        timelineArrayListBackup.add(timeline);
//    }
//
//    @Override
//    public void addAll(Timeline... items) {
//        super.addAll(items);
//        timelineArrayListBackup.addAll(Arrays.asList(items));
//    }
//
//    public void filter(String charText) {
//        charText = charText.toLowerCase();
//        timelineArrayList.clear();
//        if (charText.length() == 0) {
//            timelineArrayList.addAll(timelineArrayListBackup);
//        } else {
//            for (Timeline timeline : timelineArrayListBackup) {
//                if (timeline.title.toLowerCase().contains(charText)) {
//                    timelineArrayList.add(timeline);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }
}
