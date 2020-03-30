package inspire2connect.inspire2connect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class male_female_2 extends AppCompatActivity {
    private WebView feedbackformlink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_male_female_2);
        feedbackformlink = (WebView) findViewById(R.id.web_view);
        WebSettings webSettings = feedbackformlink.getSettings();

        feedbackformlink.setInitialScale(200);
        feedbackformlink.getSettings().setSupportZoom(true);
        feedbackformlink.getSettings().setLoadWithOverviewMode(true);
        feedbackformlink.getSettings().setBuiltInZoomControls(true);

        webSettings.setJavaScriptEnabled(true);
        feedbackformlink.loadUrl("https://forms.gle/qfFkSneeTgT4UKgy9");
        feedbackformlink.setWebViewClient(new WebViewClient());
    }
}
