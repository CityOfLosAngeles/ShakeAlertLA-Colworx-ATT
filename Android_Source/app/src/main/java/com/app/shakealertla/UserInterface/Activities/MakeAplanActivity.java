package com.app.shakealertla.UserInterface.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.shakealertla.DatabaseHelper.DatabaseAccess;
import com.app.shakealertla.Models.Plan;
import com.app.shakealertla.R;
import com.app.shakealertla.Services.PlanService;
import com.app.shakealertla.Utils.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MakeAplanActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_a_plan);
        setupComponents();
    }

    ImageView back;
    ListView listView;
    AboutAdapter adapter;
    private ArrayList<Plan> plans;
    @Override
    public void initializeComponents() {
        back = findViewById(R.id.back);
        plans = PlanService.getPlans();
        listView = findViewById(R.id.makeaplanListView);
        adapter = new AboutAdapter(MakeAplanActivity.this, R.layout.makeaplan_list_item, plans);
        listView.setAdapter(adapter);
    }

    @Override
    public void setupListeners() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView tv = view.findViewById(R.id.text);
                tv.setSelected(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv.setSelected(false);
                    }
                },5000);
                return true;
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent planDetailsIntent = new Intent(MakeAplanActivity.this, PlanDetails_Activity.class);
                planDetailsIntent.putExtra("plan",plans.get(position));
                startActivityForResult(planDetailsIntent,10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            adapter.clear();
            adapter.addAll(PlanService.getPlans());
    }

    public class AboutAdapter extends ArrayAdapter<Plan> {

        private ArrayList<Plan> timelineArrayList;
        private Context context;
        private int mResource;
        private boolean isEnglish = true;

        public AboutAdapter(Context context, int mResource, ArrayList<Plan> timelineArrayList) {
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

            Plan plan = timelineArrayList.get(position);
            ImageView imageView = result.findViewById(R.id.image);
            TextView tv_title = result.findViewById(R.id.text);
            tv_title.setText(plan.Plan);
            imageView.setImageResource(plan.getImage());

            result.setContentDescription((plan.Completed == 0?"Unchecked ":"Checked ")+plan.Plan);
            return result;
        }

    }
}
