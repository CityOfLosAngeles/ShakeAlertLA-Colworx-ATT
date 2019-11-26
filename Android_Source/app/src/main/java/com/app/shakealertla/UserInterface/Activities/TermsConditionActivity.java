package com.app.shakealertla.UserInterface.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.shakealertla.R;
import com.app.shakealertla.Utils.SharedPreferenceManager;

public class TermsConditionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        }
        setupComponents();
    }

    TextView titleTextView;
    ImageView back;
    String file;
    String title;
    LinearLayout toolbar;
    Button agree;

    @Override
    public void initializeComponents() {
        toolbar = findViewById(R.id.toolbar);
        agree = findViewById(R.id.agree);
        titleTextView = findViewById(R.id.titleTextView);
        back = findViewById(R.id.back);
        if (getIntent().hasExtra("color")) {
            int color = getIntent().getIntExtra("color", 0);
            if (color != 0)
                toolbar.setBackgroundColor(getResources().getColor(color));
        }
        if (getIntent().hasExtra("title") && getIntent().hasExtra("file")) {
            title = getIntent().getStringExtra("title");
            titleTextView.setText(title);
            titleTextView.setContentDescription(title);
            file = getIntent().getStringExtra("file");
            WebView webView = findViewById(R.id.webView);
            webView.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    agree.setVisibility(View.GONE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    agree.setVisibility(View.VISIBLE);
                }
            });
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
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tourIntent = new Intent(TermsConditionActivity.this, HomeActivity.class);
                tourIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(tourIntent);
                SharedPreferenceManager.getInstance().showTour(false);
            }
        });
    }
}
