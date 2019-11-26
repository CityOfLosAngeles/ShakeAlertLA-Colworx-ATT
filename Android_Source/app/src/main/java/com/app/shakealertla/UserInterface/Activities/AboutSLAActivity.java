package com.app.shakealertla.UserInterface.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.shakealertla.R;

import java.util.ArrayList;

public class AboutSLAActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_sla);
        setupComponents();
    }

    ImageView back;
    ListView listView;
    @Override
    public void initializeComponents() {
        back = findViewById(R.id.back);
        ArrayList<About> recoveryArrayList = new ArrayList<About>();
        recoveryArrayList.add(new About(R.mipmap.partners, getString(R.string.partners)));
        recoveryArrayList.add(new About(R.mipmap.disclaimer, getString(R.string.desclaimer)));
//        recoveryArrayList.add(new About(R.mipmap.using_shake, getString(R.string.using_shakealertla)));
        recoveryArrayList.add(new About(R.mipmap.info, getString(R.string.how_early_warning_work)));
        recoveryArrayList.add(new About(R.mipmap.help, getString(R.string.what_i_get_from_shakealertla)));
        recoveryArrayList.add(new About(R.mipmap.disclaimer_white, getString(R.string.quick_demo_functionalities)));
        listView = findViewById(R.id.aboutListView);
        AboutAdapter adapter = new AboutAdapter(AboutSLAActivity.this, R.layout.recovery_list_item,recoveryArrayList );
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent webViewIntent = new Intent(AboutSLAActivity.this, WebViewActivity.class);
                switch (position) {
                    case 0:
                        webViewIntent.putExtra("title",getString(R.string.partners));
                        webViewIntent.putExtra("file",getString(R.string.partner_file));
                        startActivity(webViewIntent);
                        break;
                    case 1:
                        webViewIntent.putExtra("title",getString(R.string.desclaimer));
                        webViewIntent.putExtra("file",getString(R.string.disclamer_file));
                        webViewIntent.putExtra("isDisclaimer",true);
                        startActivity(webViewIntent);
                        break;
//                    case 2:
//                        webViewIntent.putExtra("title",getString(R.string.using_shakealertla));
//                        webViewIntent.putExtra("file",getString(R.string.using_shake_alert_la_file));
//                        startActivity(webViewIntent);
//                        break;
                    case 2:
                        webViewIntent.putExtra("title",getString(R.string.how_this_works));
                        webViewIntent.putExtra("file",getString(R.string.how_early_warning_work_file));
                        startActivity(webViewIntent);
                        break;
                    case 3:
                        webViewIntent.putExtra("title",getString(R.string.shakealert_info));
                        webViewIntent.putExtra("file",getString(R.string.what_i_get_from_shakealertla_file));
                        startActivity(webViewIntent);
                        break;
                    case 4:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=0zs-88RG9xU"));
                        startActivity(browserIntent);
                        break;
                }
            }
        });
    }

    public class About {
        public int image;
        public String text;

        public About(int image, String text) {
            this.image = image;
            this.text = text;
        }
    }

    public class AboutAdapter extends ArrayAdapter<About> {

        private ArrayList<About> timelineArrayList;
        private Context context;
        private int mResource;


        public AboutAdapter(Context context, int mResource, ArrayList<About> timelineArrayList) {
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

            About recovery = timelineArrayList.get(position);
            ImageView imageView = result.findViewById(R.id.image);
            TextView tv_title = result.findViewById(R.id.text);
            tv_title.setText(recovery.text);
            imageView.setImageResource(recovery.image);

            return result;
        }

    }
}
