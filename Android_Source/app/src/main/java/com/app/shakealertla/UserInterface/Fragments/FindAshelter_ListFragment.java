package com.app.shakealertla.UserInterface.Fragments;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.error.VolleyError;
import com.app.shakealertla.Listener.ServiceListener;
import com.app.shakealertla.Models.Shelters;
import com.app.shakealertla.R;
import com.app.shakealertla.Services.ShelterService;
import com.app.shakealertla.UserInterface.Activities.FindAshelterActivity;
import com.app.shakealertla.Utils.AppUtils;
import com.app.shakealertla.Utils.NestedListView;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FindAshelter_ListFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_findashelter_list, container, false);
    }

    NestedListView listView;
    ArrayList<Shelters.Shelter> shelters;
    RecoveryAdapter adapter;
    SwipeRefreshLayout swipeContainer;
    TextView lastRefresh;

    @Override
    public void initializeComponents(View rootView) {
        shelters = ((FindAshelterActivity) getActivity()).sheltersArray.features;
        lastRefresh = rootView.findViewById(R.id.lastRefresh);
        listView = rootView.findViewById(R.id.shelterListView);
        adapter = new RecoveryAdapter(getActivity(), R.layout.shelter_list_item, shelters);
        listView.setAdapter(adapter);
        listView.setExpanded(true);
        updateLastRefresh();

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                refresh();
            }
        });

    }

    @Override
    public void setupListeners(View rootView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewPager viewPager = ((FindAshelterActivity)getActivity()).viewPager;
                viewPager.setCurrentItem(1);
                ((FindAshelterActivity)getActivity()).findAshelter_mapFragment.selectMarker(shelters.get(position).attributes.FacilityName);
            }
        });
    }

    private void refresh() {
//        final ProgressDialog dialog = new ProgressDialog(getActivity());
//        dialog.setMessage("Loading...");
//        dialog.show();
        ShelterService.getSheltersList(getActivity(), new ServiceListener<Shelters, VolleyError>() {
            @Override
            public void success(Shelters sheltersArray) {
//                dialog.dismiss();
                shelters = sheltersArray.features;
                adapter.notifyDataSetChanged();
                updateLastRefresh();
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void error(VolleyError error) {
//                dialog.dismiss();
                AppUtils.Toast(error.getMessage());
            }
        });
    }

    private void updateLastRefresh() {
        SimpleDateFormat sf = new SimpleDateFormat("MMM dd',' hh:mm a", Locale.US);
        lastRefresh.setText(getString(R.string.last_refreshed) +" " +sf.format(new Date()));
    }

    public class RecoveryAdapter extends ArrayAdapter<Shelters.Shelter> {

        private ArrayList<Shelters.Shelter> timelineArrayList;
        private Context context;
        private int mResource;


        public RecoveryAdapter(Context context, int mResource, ArrayList<Shelters.Shelter> timelineArrayList) {
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

            Shelters.Shelter shelter = timelineArrayList.get(position);
//            ImageView imageView = result.findViewById(R.id.image);
            TextView tv_title = result.findViewById(R.id.text);
            TextView tv_desc = result.findViewById(R.id.desc);
            tv_title.setText(shelter.attributes.FacilityName);
            tv_desc.setText(shelter.address!=null?shelter.address:"");
//            imageView.setImageResource(recovery.image);

            return result;
        }


    }
}
