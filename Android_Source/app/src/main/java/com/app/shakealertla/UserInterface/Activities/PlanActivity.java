package com.app.shakealertla.UserInterface.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.shakealertla.R;

import java.util.ArrayList;

public class PlanActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        setupComponents();
    }

    ImageView back;
    ListView listView;
    RelativeLayout findMore;
    TextView getEmergency;
    @Override
    public void initializeComponents() {
        back = findViewById(R.id.back);
        getEmergency = findViewById(R.id.getEmergency);
        getEmergency.setMovementMethod(LinkMovementMethod.getInstance());
        findMore = findViewById(R.id.findMore);
        ArrayList<Plan> plansArrayList = new ArrayList<Plan>();
        plansArrayList.add(new Plan(R.mipmap.make_a_plan, getString(R.string.make_a_plan)));
        plansArrayList.add(new Plan(R.mipmap.build_a_kit, getString(R.string.built_a_kit)));
        plansArrayList.add(new Plan(R.mipmap.secure_your_place, getString(R.string.secure_your_place)));
        plansArrayList.add(new Plan(R.mipmap.protective_action, getString(R.string.proactive)));
        plansArrayList.add(new Plan(R.mipmap.find_out, getString(R.string.find_out_more_on_how_to_prepare)));
        listView = findViewById(R.id.planListView);
        PlanAdapter adapter = new PlanAdapter(PlanActivity.this, R.layout.recovery_list_item,plansArrayList );
        listView.setAdapter(adapter);
    }

    @Override
    public void setupListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        findMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.earthquakecountry.org"));
//                startActivity(browserIntent);
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent makePlanIntent = new Intent(PlanActivity.this,MakeAplanActivity.class);
                        startActivity(makePlanIntent);
                        break;
                    case 1:
                        Intent securePlaceIntent = new Intent(PlanActivity.this,BuiltAkit_Activity.class);
                        startActivity(securePlaceIntent);
                        break;
                    case 2:
                        Intent builtKitIntent = new Intent(PlanActivity.this,SecureYourPlace_Activity.class);
                        startActivity(builtKitIntent);
                        break;
                    case 3:
                        Intent webViewIntent = new Intent(PlanActivity.this, WebViewActivity.class);
                        webViewIntent.putExtra("title",getString(R.string.proactive));
                        webViewIntent.putExtra("file",getString(R.string.protective_action));
                        webViewIntent.putExtra("color",R.color.plancolorPrimary);
                        startActivity(webViewIntent);
                        break;
                    case 4:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.earthquakecountry.org"));
                        startActivity(browserIntent);
                        break;
                }
            }
        });
    }
    public class Plan {
        public int image;
        public String text;

        public Plan(int image, String text) {
            this.image = image;
            this.text = text;
        }
    }
    public class PlanAdapter extends ArrayAdapter<Plan> {

        private ArrayList<Plan> timelineArrayList;
        private Context context;
        private int mResource;


        public PlanAdapter(Context context, int mResource, ArrayList<Plan> timelineArrayList) {
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
            tv_title.setText(plan.text);
            imageView.setImageResource(plan.image);

            return result;
        }

    }
}
