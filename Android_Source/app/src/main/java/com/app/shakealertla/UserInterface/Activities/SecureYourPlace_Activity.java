package com.app.shakealertla.UserInterface.Activities;

import android.content.Context;
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
import com.app.shakealertla.Services.SecureYourPlace_Service;
import com.app.shakealertla.Utils.ConfigConstants;
import com.app.shakealertla.Utils.SharedPreferenceManager;

import java.util.ArrayList;

public class SecureYourPlace_Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_your_place);
        setupComponents();
    }

    ImageView back;
    ListView listView;
    ArrayList<About> securePlacesList;

    @Override
    public void initializeComponents() {
        back = findViewById(R.id.back);
        securePlacesList = SecureYourPlace_Service.getSecurePlacesList();
        listView = findViewById(R.id.secureYourPlaceListView);
        AboutAdapter adapter = new AboutAdapter(SecureYourPlace_Activity.this, R.layout.secure_your_place_list_item, securePlacesList);
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
                About about = securePlacesList.get(position);
                about.completed = about.completed == 0 ? 1 : 0;
                SecureYourPlace_Service.updateSecurePlace(about);
//                AppUtils.Toast("Updated");
                initializeComponents();
            }
        });
    }

    public static class About {
        public int completed;
        public String text;
        public String text_es;
        public int ID;

        public About() {
        }

        public About(int completed, String text) {
            this.completed = completed;
            this.text = text;
        }

        public int getImage() {
            if (completed == 0)
                return R.mipmap.off;
            else
                return R.mipmap.check;
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
            if (SharedPreferenceManager.getLanguage().matches(ConfigConstants.LANGUAGE_ENGLISH)) {
                tv_title.setText(recovery.text);
                result.setContentDescription((recovery.completed == 0?"Unchecked ":"Checked ")+recovery.text);
            }else {
                tv_title.setText(recovery.text_es);
                result.setContentDescription((recovery.completed == 0?"Unchecked ":"Checked ")+recovery.text_es);
            }
            imageView.setImageResource(recovery.getImage());

            return result;
        }

    }
}
