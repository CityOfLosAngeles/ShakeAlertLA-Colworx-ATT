package com.app.shakealertla.UserInterface.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.shakealertla.Models.Plan;
import com.app.shakealertla.R;
import com.app.shakealertla.Services.KitService;
import com.app.shakealertla.Utils.AppUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class KitDetailsActivity extends BaseActivity {

    private ArrayList<Plan> kit;
    private String Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kit_details);
        setupComponents();
    }

    ImageView back;
    ListView listView;
    TextView title;
    AboutAdapter adapter;

    @Override
    public void initializeComponents() {
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        if (getIntent().hasExtra("kit")) {
            kit = (ArrayList<Plan>) getIntent().getSerializableExtra("kit");
            Title = getIntent().getStringExtra("title");
        }
        title.setText(Title);
        listView = findViewById(R.id.kitDetailsListView);

        adapter = new AboutAdapter(KitDetailsActivity.this, R.layout.makeaplan_list_item, kit)/*{
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                convertView = super.getView(position, convertView, parent);
                TextView tv = convertView.findViewById(R.id.text);
                tv.setSelected(true);
                return convertView;
            }
        };*/;
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
                //If kit has details
//                Intent planDetailsIntent = new Intent(KitDetailsActivity.this, PlanDetails_Activity.class);
//                planDetailsIntent.putExtra("plan", kit.get(position));
//                planDetailsIntent.putExtra("position",position);
//                startActivityForResult(planDetailsIntent, 10);
                Plan plan = kit.get(position);
                if (plan.Completed == 0)
                    plan.Completed = 1;
                else
                    plan.Completed = 0;
                KitService.updateKitPlan(plan);
                adapter.notifyDataSetChanged();
//                AppUtils.Toast("Updated");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            int position = data.getIntExtra("position", -1);
            Plan plan = (Plan) data.getSerializableExtra("plan");
            if (position != -1) {
                kit.remove(position);
                kit.add(position, plan);
            }
            adapter.notifyDataSetChanged();
        }
    }

    public class AboutAdapter extends ArrayAdapter<Plan> {

        private ArrayList<Plan> timelineArrayList;
        private Context context;
        private int mResource;


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

            Plan recovery = timelineArrayList.get(position);
            ImageView imageView = result.findViewById(R.id.image);
            TextView tv_title = result.findViewById(R.id.text);
            ImageView arrow = result.findViewById(R.id.arrow);
            arrow.setVisibility(View.GONE);
//            if (recovery.Plan.contains("Sturdy boots"))
//                recovery.Plan = "<marque>"+recovery.Plan+"</marque>";
            tv_title.setText(recovery.Plan);
            imageView.setImageResource(recovery.getImage());

            result.setContentDescription((recovery.Completed == 0?"Unchecked ":"Checked ")+recovery.Plan);
            return result;
        }

    }
}
