package com.app.shakealertla.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SquareButton extends RelativeLayout {
    public SquareButton(Context context) {
        super(context);
    }

    public SquareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SquareButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        ((Activity) getContext()).getWindowManager()
//                .getDefaultDisplay()
//                .getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels;
//        int width = View.MeasureSpec.getSize(widthMeasureSpec);
//        int height = View.MeasureSpec.getSize(heightMeasureSpec);
//        int size = width > height ? height : width;
        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
//        setMeasuredDimension(((LinearLayout)getParent()).getWidth() / 2, ((LinearLayout)getParent()).getWidth() / 2); // make it square

    }
}