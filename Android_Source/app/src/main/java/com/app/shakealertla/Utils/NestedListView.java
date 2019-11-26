package com.app.shakealertla.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import static com.google.android.gms.maps.CameraUpdateFactory.scrollBy;

/**
 * Created by AST on 5/9/2018.
 */

public class NestedListView extends ListView {

    boolean expanded = false;

    public NestedListView(Context context)
    {
        super(context);
    }

    public NestedListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public NestedListView(Context context, AttributeSet attrs,int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isExpanded()
    {
        return expanded;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // HACK! TAKE THAT ANDROID!
        if (isExpanded()) {
            // Calculate entire height by providing a very large height hint.
            // But do not use the highest 2 bits of this integer; those are
            // reserved for the MeasureSpec mode.
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}