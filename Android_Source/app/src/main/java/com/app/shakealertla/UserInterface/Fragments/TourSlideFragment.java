package com.app.shakealertla.UserInterface.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.shakealertla.R;
import com.app.shakealertla.ShakeAlertLA;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class TourSlideFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private int drawable;
    private int indicatorDrawable;
    private int text, header;

    public static TourSlideFragment newInstance(int param1, int text, int header, int param2) {
        TourSlideFragment fragment = new TourSlideFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, text);
        args.putInt(ARG_PARAM4, header);
        args.putInt(ARG_PARAM3, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            drawable = getArguments().getInt(ARG_PARAM1);
            text = getArguments().getInt(ARG_PARAM2);
            indicatorDrawable = getArguments().getInt(ARG_PARAM3);
            header = getArguments().getInt(ARG_PARAM4);
        }
    }

    ImageView slide;
    TextView title, desc;
    ImageView carosal;
    @Override
    public void initializeComponents(View rootView) {
        slide = rootView.findViewById(R.id.slide);
        title = rootView.findViewById(R.id.header);
        desc = rootView.findViewById(R.id.desc);
        carosal = rootView.findViewById(R.id.carosal);

        Glide.with(this).load(drawable).into(slide);
        Glide.with(this).load(indicatorDrawable).into(carosal);
        title.setText(getString(header));
        desc.setText(getString(text));
//        Glide.with(ShakeAlertLA.getContext()).load(drawable).into(slide);
//        slide.setImageResource(drawable);
    }

    @Override
    public void setupListeners(View rootView) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tour_slide, container, false);
    }

}
