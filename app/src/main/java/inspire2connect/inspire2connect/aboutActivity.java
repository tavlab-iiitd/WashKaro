package inspire2connect.inspire2connect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;


@SuppressWarnings("SpellCheckingInspection")
public class aboutActivity extends BaseActivity implements View.OnClickListener {

    private ImageView tavlab;
    private ImageView precog;
    private ImageView iiitd;
    private TextView chirag;
    private TextView chiragTag;
    private TextView priyanka;
    private TextView priyankaTag;
    private TextView pk;
    private TextView pkTag;
    private TextView tav;
    private TextView tavTag;
    private TextView kanav;
    private TextView kanavTag;
    private TextView rohan;
    private TextView rohanTag;
    private TextView himanshu;
    private TextView himanshuTag;
    private TextView vaibhav;
    private TextView vaibhavTag;
    private TextView bhavika;
    private TextView bhavikaTag;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ca_activity_about);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tavlab = findViewById(R.id.tavlabLogo);
        precog = findViewById(R.id.iiitdLogo);
        iiitd = findViewById(R.id.precogLogo);
        chirag = findViewById(R.id.chirag);
        chiragTag = findViewById(R.id.chiragTag);
        priyanka = findViewById(R.id.priyanka);
        priyankaTag = findViewById(R.id.priyankaTag);
        pk = findViewById(R.id.pk);
        pkTag = findViewById(R.id.pkTag);
        tav = findViewById(R.id.tav);
        tavTag = findViewById(R.id.tavTag);
        rohan = findViewById(R.id.rohan);
        rohanTag = findViewById(R.id.rohanTag);
        himanshu = findViewById(R.id.himanshu);
        himanshuTag = findViewById(R.id.himanshuTag);
        kanav = findViewById(R.id.kanav);
        kanavTag = findViewById(R.id.kanavTag);
        vaibhav = findViewById(R.id.vaibhav);
        vaibhavTag = findViewById(R.id.vaibhavTag);
        bhavika = findViewById(R.id.bhavika);
        bhavikaTag = findViewById(R.id.bhavikaTag);

        tavlab.setOnClickListener(this);
        precog.setOnClickListener(this);
        iiitd.setOnClickListener(this);
        chiragTag.setOnClickListener(this);
        chirag.setOnClickListener(this);
        priyankaTag.setOnClickListener(this);
        priyanka.setOnClickListener(this);
        pk.setOnClickListener(this);
        pkTag.setOnClickListener(this);
        bhavikaTag.setOnClickListener(this);
        bhavika.setOnClickListener(this);
        vaibhavTag.setOnClickListener(this);
        vaibhav.setOnClickListener(this);
        tav.setOnClickListener(this);
        tavTag.setOnClickListener(this);
        rohanTag.setOnClickListener(this);
        rohan.setOnClickListener(this);
        himanshuTag.setOnClickListener(this);
        himanshu.setOnClickListener(this);
        kanav.setOnClickListener(this);
        kanavTag.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onClick(View view) {
        String url = "https://www.google.com/";
        switch (view.getId()) {
            case R.id.iiitdLogo:
                url = "http://precog.iiitd.edu.in/";
                break;
            case R.id.precogLogo:
                url = "https://www.iiitd.ac.in/";
                break;
            case R.id.tavlabLogo:
                url = "https://tavlab.iiitd.edu.in/";
                break;
            case R.id.chirag:
            case R.id.chiragTag:
                url = "https://www.linkedin.com/in/jnchirag/";
                break;
            case R.id.priyanka:
            case R.id.priyankaTag:
                url = "https://www.linkedin.com/in/priyanka-syal-671b9495/";
                break;
            case R.id.pk:
            case R.id.pkTag:
                url = "https://www.linkedin.com/in/ponguru/";
                break;
            case R.id.tav:
            case R.id.tavTag:
                url = "https://www.linkedin.com/in/tavpritesh/";
                break;
            case R.id.rohan:
            case R.id.rohanTag:
                url = "https://www.linkedin.com/in/rohan-pandey-145170175/";
                break;
            case R.id.himanshu:
            case R.id.himanshuTag:
                url = "https://www.linkedin.com/in/himanshu-sharma2950/";
                break;
            case R.id.kanav:
            case R.id.kanavTag:
                url = "https://www.linkedin.com/in/kanav-bhagat-133229130/";
                break;
            case R.id.bhavika:
            case R.id.bhavikaTag:
                url = "https://www.linkedin.com/in/bhavikarana/";
                break;
            case R.id.vaibhav:
            case R.id.vaibhavTag:
                url = "https://www.linkedin.com/in/vaibhav-gautam-171775187/";
                break;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
