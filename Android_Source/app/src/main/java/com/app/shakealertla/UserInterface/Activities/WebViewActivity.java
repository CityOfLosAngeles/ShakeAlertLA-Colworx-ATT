package com.app.shakealertla.UserInterface.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.shakealertla.R;

public class WebViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        }
        setupComponents();
    }

    TextView titleTextView;
    ImageView back, feedback;
    String file;
    String title;
    LinearLayout toolbar;
    @Override
    public void initializeComponents() {
        toolbar = findViewById(R.id.toolbar);
        titleTextView = findViewById(R.id.titleTextView);
        feedback = findViewById(R.id.feedback);
        back = findViewById(R.id.back);
        if (getIntent().hasExtra("color")) {
            int color = getIntent().getIntExtra("color",0);
            if (color!=0)
            toolbar.setBackgroundColor(getResources().getColor(color));
        }
        if (getIntent().hasExtra("title") && getIntent().hasExtra("file")) {
            title = getIntent().getStringExtra("title");
            titleTextView.setText(title);
            titleTextView.setContentDescription(title);
            file = getIntent().getStringExtra("file");
            WebView webView = findViewById(R.id.webView);
            webView.loadUrl("file:///android_asset/" + file + ".html");
        }
    }

    @Override
    public void setupListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getIntent().hasExtra("isDisclaimer")){
            feedback.setVisibility(View.VISIBLE);
        }

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendemail("early-earthquake-warning@lacity.org",null,null);
            }
        });
    }

    private void sendemail(String To, String subject, String message) {
        String[] reciever = new String[]{To};
        Intent mailIntent = new Intent(Intent.ACTION_SENDTO);//for only email apps
        mailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
        mailIntent.putExtra(Intent.EXTRA_EMAIL, reciever);
//        mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
//        mailIntent.putExtra(Intent.EXTRA_TEXT, message);
//        mailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(mailIntent, "Choose an application to send your mail with"));
    }
}
