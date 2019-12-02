package com.app.shakealertla.UserInterface.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.shakealertla.Models.Kits;
import com.app.shakealertla.Models.Plan;
import com.app.shakealertla.R;
import com.app.shakealertla.Services.KitService;
import com.app.shakealertla.Utils.NestedListView;

import java.util.ArrayList;

public class BuiltAkit_Activity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_built_a_kit);
        setupComponents();
    }

    ImageView back;
    NestedListView listView;
    ArrayList<Kits> kits;
    TextView buildAkitInfo;
    KitsAdapter adapter;
    ScrollView scrollView;
    @Override
    public void initializeComponents() {
        back = findViewById(R.id.back);
        scrollView = findViewById(R.id.scrollView);
        buildAkitInfo = findViewById(R.id.buildAkitInfo);
        buildAkitInfo.setMovementMethod(LinkMovementMethod.getInstance());
        kits = KitService.getKits();
        listView = findViewById(R.id.builtAkitListView);
        adapter = new KitsAdapter(BuiltAkit_Activity.this, R.layout.kit_item, kits);
        listView.setAdapter(adapter);
        listView.setExpanded(true);
        //Moving to Top of screen
        scrollView.smoothScrollTo(0,0);
    }

    @Override
    public void setupListeners() {
//        buildAkitInfo.setText(Html.fromHtml(getString(R.string.built_a_kit_info)));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent kitDetailsIntent = new Intent(BuiltAkit_Activity.this, KitDetailsActivity.class);
                kitDetailsIntent.putExtra("kit", kits.get(position).items);
                kitDetailsIntent.putExtra("title",kits.get(position).kitName);
                startActivityForResult(kitDetailsIntent,10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        kits = KitService.getKits();
        adapter = new KitsAdapter(BuiltAkit_Activity.this, R.layout.kit_item, kits);
        listView.setAdapter(adapter);
    }

    public class KitsAdapter extends ArrayAdapter<Kits> {

        private ArrayList<Kits> kits;
        private Context context;
        private int mResource;


        public KitsAdapter(Context context, int mResource, ArrayList<Kits> kits) {
            super(context, mResource, kits);
            this.kits = kits;
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

            Kits kit = kits.get(position);
            ArrayList<Plan> plans = kit.items;
            ProgressBar progressBar = result.findViewById(R.id.progressBar);
            TextView tv_title = result.findViewById(R.id.text);
            TextView desc = result.findViewById(R.id.desc);
            tv_title.setText(kit.kitName);
            progressBar.setMax(plans.size());
            int completed = 0;
            for (Plan plan : plans) {
                if (plan.Completed == 1) completed += 1;
            }
            progressBar.setProgress(completed);
            desc.setText(completed + "/" + plans.size());
            return result;
        }

    }
}
