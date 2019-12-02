package com.app.shakealertla.UserInterface.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.error.VolleyError;
import com.app.shakealertla.Adapters.HomeTabstPagerAdapter;
import com.app.shakealertla.HttpUtils.RestAPI;
import com.app.shakealertla.Listener.ServiceListener;
import com.app.shakealertla.Models.Shelters;
import com.app.shakealertla.R;
import com.app.shakealertla.Services.ShelterService;
import com.app.shakealertla.UserInterface.Fragments.FindAshelter_ListFragment;
import com.app.shakealertla.UserInterface.Fragments.FindAshelter_MapFragment;
import com.app.shakealertla.Utils.AppLog;
import com.app.shakealertla.Utils.AppUtils;
import com.app.shakealertla.Utils.GsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FindAshelterActivity extends BaseActivity {

    private TabLayout tabLayout;
    public Shelters sheltersArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ashelter);
        final ProgressDialog dialog = new ProgressDialog(FindAshelterActivity.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        ShelterService.getSheltersList(FindAshelterActivity.this, new ServiceListener<Shelters, VolleyError>() {
            @Override
            public void success(Shelters shelters) {
                sheltersArray = shelters;
                setupComponents();
                dialog.dismiss();
            }

            @Override
            public void error(VolleyError error) {
                dialog.dismiss();
//                AppUtils.Toast(error.getMessage());
            }
        });
    }

    public ViewPager viewPager;
    public FindAshelter_MapFragment findAshelter_mapFragment;
    ImageView back;

    @Override
    public void initializeComponents() {
        back = findViewById(R.id.back);
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new FindAshelter_ListFragment());
        findAshelter_mapFragment = new FindAshelter_MapFragment();
        fragments.add(findAshelter_mapFragment);
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(new HomeTabstPagerAdapter(getSupportFragmentManager(), FindAshelterActivity.this, fragments));

        // TODO: TABLAYOUT LINK https://github.com/codepath/android_guides/wiki/Google-Play-Style-Tabs-using-TabLayout#add-icons-to-tablayout
        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        String[] stringArray = getResources().getStringArray(R.array.shelter_tabs);
        for (int i = 0; i < stringArray.length; i++) {
            String string = stringArray[i];
            tabLayout.getTabAt(i).setContentDescription(string);
        }
    }

    @Override
    public void setupListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // configure icons
        final int[] imageResId = {
                R.mipmap.un_select_list,
                R.mipmap.un_select_map};

        final int[] selectedResId = {
                R.mipmap.list,
                R.mipmap.map};

        for (int i = 0; i < imageResId.length; i++) {
            tabLayout.getTabAt(i).setIcon(imageResId[i]);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                for (int i = 0; i < imageResId.length; i++) {
                    tabLayout.getTabAt(i).setIcon(imageResId[i]);
                }
                tab.setIcon(selectedResId[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
