package com.app.shakealertla.UserInterface.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.shakealertla.R;
import com.app.shakealertla.UserInterface.Activities.FindAshelterActivity;
import com.app.shakealertla.UserInterface.Activities.HomeActivity;
import com.app.shakealertla.UserInterface.Activities.WebViewActivity;

import java.util.ArrayList;

public class RecoveryFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recovery, container, false);
    }

    ListView listView;
    ImageView back;
    @Override
    public void initializeComponents(View rootView) {
        back = rootView.findViewById(R.id.back);
        ArrayList<Recovery> recoveryArrayList = new ArrayList<Recovery>();
        recoveryArrayList.add(new Recovery(R.mipmap.when_shaking_stops, getString(R.string.when_shaking_stops)));
        recoveryArrayList.add(new Recovery(R.mipmap.find_a_shelter, getString(R.string.find_a_shelter)));
        recoveryArrayList.add(new Recovery(R.mipmap.volunteer_to_help, getString(R.string.volunteer_to_help)));
        recoveryArrayList.add(new Recovery(R.mipmap.access_disaster_services, getString(R.string.get_help)));
        listView = rootView.findViewById(R.id.recoveryListView);
        RecoveryAdapter adapter = new RecoveryAdapter(getActivity(), R.layout.recovery_list_item,recoveryArrayList );
        listView.setAdapter(adapter);
    }

    @Override
    public void setupListeners(View rootView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent webViewIntent = new Intent(getActivity(), WebViewActivity.class);
                switch (position) {
                    case 0:
                        webViewIntent.putExtra("title",getString(R.string.when_shaking_stops));
                        webViewIntent.putExtra("file",getString(R.string.when_shaking_stops_file));
                        webViewIntent.putExtra("color",R.color.recoverycolorPrimary);
                        startActivity(webViewIntent);
                        break;
                    case 1:
                        Intent findShelterIntent = new Intent(getActivity(), FindAshelterActivity.class);
                        startActivity(findShelterIntent);
                        break;
                    case 2:
                        webViewIntent.putExtra("title",getString(R.string.volunteer_to_help));
                        webViewIntent.putExtra("file",getString(R.string.volunteer_to_help_file));
                        webViewIntent.putExtra("color",R.color.recoverycolorPrimary);
                        startActivity(webViewIntent);
                        break;
                    case 3:
                        webViewIntent.putExtra("title",getString(R.string.get_help));//Access Disaster Service
                        webViewIntent.putExtra("file",getString(R.string.access_disaster_services_file));
                        webViewIntent.putExtra("color",R.color.recoverycolorPrimary);
                        startActivity(webViewIntent);
                        break;
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).viewPager.setCurrentItem(0);
            }
        });
    }

    public class Recovery {
        public int image;
        public String text;

        public Recovery(int image, String text) {
            this.image = image;
            this.text = text;
        }
    }

    public class RecoveryAdapter extends ArrayAdapter<Recovery> {

        private ArrayList<Recovery> timelineArrayList;
        private Context context;
        private int mResource;


        public RecoveryAdapter(Context context, int mResource, ArrayList<Recovery> timelineArrayList) {
            super(context, mResource, timelineArrayList);
            this.timelineArrayList = timelineArrayList;
            this.mResource = mResource;
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

            Recovery recovery = timelineArrayList.get(position);
            ImageView imageView = result.findViewById(R.id.image);
            TextView tv_title = result.findViewById(R.id.text);
            tv_title.setText(recovery.text);
            imageView.setImageResource(recovery.image);

            return result;
        }

    }
}
