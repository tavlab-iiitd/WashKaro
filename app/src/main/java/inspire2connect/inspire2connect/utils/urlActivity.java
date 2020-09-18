package inspire2connect.inspire2connect.utils;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseUser;

import inspire2connect.inspire2connect.R;

public class urlActivity extends BaseActivity {
    WebView webView;

    String currentUserID;

    public String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_url);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        String title = intent.getStringExtra("name");

        //Firebase Analytics
        if(firebaseUser != null) {
            currentUserID = firebaseUser.getUid();
        }
        Bundle bundle = new Bundle();
        bundle.putString("UID", currentUserID);
        bundle.putString("WebPage_Title", title);
        bundle.putString("WebPage_URL", url);
        FirebaseAnalytics.getInstance ( this ).logEvent("Webpages", bundle);

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //progDailog.show();
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {

            }
        });

        webView.loadUrl(url);

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }
}

