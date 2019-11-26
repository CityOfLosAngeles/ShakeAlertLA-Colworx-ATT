package com.app.shakealertla.UserInterface.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.shakealertla.R;
import com.app.shakealertla.ShakeAlertLA;
import com.app.shakealertla.UserInterface.Activities.AboutSLAActivity;
import com.app.shakealertla.UserInterface.Activities.HomeActivity;
import com.app.shakealertla.UserInterface.Activities.PlanActivity;
import com.app.shakealertla.UserInterface.Activities.RecentEarthquakesActivity;
import com.app.shakealertla.UserInterface.Activities.WebViewActivity;

import java.util.Locale;

public class HomeFragment extends BaseFragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    RelativeLayout prepare, understanding, seeing, recovering;
    TextView aboutSLA,terms;
    @Override
    public void initializeComponents(View rootView) {
        prepare = rootView.findViewById(R.id.prepare);prepare.setOnClickListener(this);
        understanding = rootView.findViewById(R.id.understanding);understanding.setOnClickListener(this);
        seeing = rootView.findViewById(R.id.seeing);seeing.setOnClickListener(this);
        recovering = rootView.findViewById(R.id.recovering);recovering.setOnClickListener(this);
        aboutSLA = rootView.findViewById(R.id.aboutSLA);aboutSLA.setOnClickListener(this);
        terms = rootView.findViewById(R.id.terms);terms.setOnClickListener(this);

    }

    @Override
    public void setupListeners(View rootView) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prepare:{
                Intent planActivityIntent = new Intent(getActivity(), PlanActivity.class);
                startActivity(planActivityIntent);
                break;
            }
            case R.id.understanding:{
//                Intent webViewIntent = new Intent(getActivity(), WebViewActivity.class);
//                webViewIntent.putExtra("title",getString(R.string.how_early_warning_work));
//                webViewIntent.putExtra("file",getString(R.string.how_does_early_warning_work));
//                startActivity(webViewIntent);
                ((HomeActivity)getActivity()).viewPager.setCurrentItem(1);
                break;
            }
            case R.id.seeing:{
                Intent recentEarthquakesIntent = new Intent(getActivity(), RecentEarthquakesActivity.class);
                startActivity(recentEarthquakesIntent);
                break;
            }
            case R.id.recovering:{
                ((HomeActivity)getActivity()).viewPager.setCurrentItem(2);
                break;
            }
            case R.id.aboutSLA:{
                Intent aboutSLAIntent = new Intent(getActivity(), AboutSLAActivity.class);
                startActivity(aboutSLAIntent);
                break;
            }
            case R.id.terms:{
                Intent webViewIntent = new Intent(getActivity(), WebViewActivity.class);
                webViewIntent.putExtra("title",getString(R.string.terms_conditions));
                webViewIntent.putExtra("file",getString(R.string.terms_of_use_file));
                startActivity(webViewIntent);
                break;
            }
        }
    }
}
