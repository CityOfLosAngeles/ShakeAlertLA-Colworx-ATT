package com.app.shakealertla.UserInterface.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.app.shakealertla.Adapters.ScreenSlidePagerAdapter;
import com.app.shakealertla.R;
import com.app.shakealertla.UserInterface.Fragments.TourSlideFragment;
import com.app.shakealertla.Utils.SharedPreferenceManager;

import java.util.ArrayList;

public class TourActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        }
        setupComponents();
    }

    ViewPager viewPager;
    @Override
    public void initializeComponents() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(TourSlideFragment.newInstance(R.mipmap.b_onboarding,R.string.tour_a,R.string.header_a,R.mipmap.carousal_b));
        fragments.add(TourSlideFragment.newInstance(R.mipmap.c_onboarding,R.string.tour_b,R.string.header_b,R.mipmap.carousal_c));
        fragments.add(TourSlideFragment.newInstance(R.mipmap.d_onboarding,R.string.tour_c,R.string.header_c,R.mipmap.carousal_d));
        fragments.add(TourSlideFragment.newInstance(R.mipmap.e_onboarding,R.string.tour_d,R.string.header_d,R.mipmap.carousal_e));

        viewPager = (ViewPager) findViewById(R.id.tourViewPager);
        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void setupListeners() {
        ImageView next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem()==3){
                    Intent webViewIntent = new Intent(TourActivity.this, TermsConditionActivity.class);
                    webViewIntent.putExtra("title",getString(R.string.terms_conditions));
                    webViewIntent.putExtra("file",getString(R.string.terms_of_use_file));
                    startActivity(webViewIntent);
                }else
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        });
    }
}
