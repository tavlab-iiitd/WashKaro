package inspire2connect.inspire2connect.about;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;


@SuppressWarnings("SpellCheckingInspection")
public class aboutActivity extends BaseActivity implements View.OnClickListener {

    aboutElem[] elems;
    GridView aboutGrid;

    ImageView tavlab;
    ImageView precog;
    ImageView iiitd;
//    TextView chirag;
//    TextView chiragTag;
//    TextView priyanka;
//    TextView priyankaTag;
//    TextView pk;
//    TextView pkTag;
//    TextView tav;
//    TextView tavTag;
//    TextView kanav;
//    TextView kanavTag;
//    TextView rohan;
//    TextView rohanTag;
//    TextView himanshu;
//    TextView himanshuTag;
//    TextView vaibhav;
//    TextView vaibhavTag;
//    TextView bhavika;
//    TextView bhavikaTag;
//    TextView tanuj;
//    TextView tanujTag;
//    TextView vrinda;
//    TextView vrindaTag;
//    TextView harsh;
//    TextView harshTag;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        elems = new aboutElem[]{
            new aboutElem(this, R.string.bhavika, R.string.bhavika_tag, R.string.bhavikaURL),
                new aboutElem(this, R.string.chirag, R.string.chirag_tag, R.string.chiragURL),
                new aboutElem(this, R.string.harsh, R.string.harsh_tag, R.string.harshURL),
                new aboutElem(this, R.string.himanshu, R.string.himanshu_tag, R.string.himanshuURL),
                new aboutElem(this, R.string.kanav, R.string.kanav_tag, R.string.kanavURL),
                new aboutElem(this, R.string.priyanka, R.string.priyanka_tag, R.string.priyankaURL),
                new aboutElem(this, R.string.tanuj, R.string.tanuj_tag, R.string.tanujURL),
                new aboutElem(this, R.string.vaibhav, R.string.vaibhav_tag, R.string.vaibhavURL),
                new aboutElem(this, R.string.vrinda, R.string.vrinda_tag, R.string.vrindaURL),
                new aboutElem(this, R.string.tav, R.string.tav_description, R.string.tavURL),
                new aboutElem(this, R.string.pk, R.string.pk_description, R.string.pkURL)
        };

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ca_activity_about);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        aboutGrid = findViewById(R.id.namesGrid);

        tavlab = findViewById(R.id.tavlabLogo);
        precog = findViewById(R.id.iiitdLogo);
        iiitd = findViewById(R.id.precogLogo);

        tavlab.setOnClickListener(this);
        precog.setOnClickListener(this);
        iiitd.setOnClickListener(this);

        aboutAdapter adapter = new aboutAdapter(getApplicationContext(), elems);
        aboutGrid.setAdapter(adapter);
        aboutGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(elems[position].url));
                startActivity(i);
            }
        });

//        chirag = findViewById(R.id.chirag);
//        chiragTag = findViewById(R.id.chiragTag);
//        priyanka = findViewById(R.id.priyanka);
//        priyankaTag = findViewById(R.id.priyankaTag);
//        pk = findViewById(R.id.pk);
//        pkTag = findViewById(R.id.pkTag);
//        tav = findViewById(R.id.tav);
//        tavTag = findViewById(R.id.tavTag);
//        rohan = findViewById(R.id.rohan);
//        rohanTag = findViewById(R.id.rohanTag);
//        himanshu = findViewById(R.id.himanshu);
//        himanshuTag = findViewById(R.id.himanshuTag);
//        kanav = findViewById(R.id.kanav);
//        kanavTag = findViewById(R.id.kanavTag);
//        vaibhav = findViewById(R.id.vaibhav);
//        vaibhavTag = findViewById(R.id.vaibhavTag);
//        bhavika = findViewById(R.id.bhavika);
//        bhavikaTag = findViewById(R.id.bhavikaTag);
//        vrinda = findViewById(R.id.vrinda);
//        vrindaTag = findViewById(R.id.vrindaTag);
//        tanuj = findViewById(R.id.tanuj);
//        tanujTag = findViewById(R.id.tanujTag);
//        harsh = findViewById(R.id.harsh);
//        harshTag = findViewById(R.id.harshTag);

//        chiragTag.setOnClickListener(this);
//        chirag.setOnClickListener(this);
//        priyankaTag.setOnClickListener(this);
//        priyanka.setOnClickListener(this);
//        pk.setOnClickListener(this);
//        pkTag.setOnClickListener(this);
//        bhavikaTag.setOnClickListener(this);
//        bhavika.setOnClickListener(this);
//        vaibhavTag.setOnClickListener(this);
//        vaibhav.setOnClickListener(this);
//        tav.setOnClickListener(this);
//        tavTag.setOnClickListener(this);
//        rohanTag.setOnClickListener(this);
//        rohan.setOnClickListener(this);
//        himanshuTag.setOnClickListener(this);
//        himanshu.setOnClickListener(this);
//        kanav.setOnClickListener(this);
//        kanavTag.setOnClickListener(this);
//        tanuj.setOnClickListener(this);
//        tanujTag.setOnClickListener(this);
//        vrindaTag.setOnClickListener(this);
//        vrinda.setOnClickListener(this);
//        harsh.setOnClickListener(this);
//        harshTag.setOnClickListener(this);
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
        String url = getString(R.string.iiitdURL);
        switch (view.getId()) {
            case R.id.iiitdLogo:
                url = getString(R.string.iiitdURL);
                break;
            case R.id.precogLogo:
                url = getString(R.string.precogURL);
                break;
            case R.id.tavlabLogo:
                url = getString(R.string.tavlabURL);
                break;
//            case R.id.chirag:
//            case R.id.chiragTag:
//                url = "https://www.linkedin.com/in/jnchirag/";
//                break;
//            case R.id.priyanka:
//            case R.id.priyankaTag:
//                url = "https://www.linkedin.com/in/priyanka-syal-671b9495/";
//                break;
//            case R.id.pk:
//            case R.id.pkTag:
//                url = "https://www.linkedin.com/in/ponguru/";
//                break;
//            case R.id.tav:
//            case R.id.tavTag:
//                url = "https://www.linkedin.com/in/tavpritesh/";
//                break;
//            case R.id.rohan:
//            case R.id.rohanTag:
//                url = "https://www.linkedin.com/in/rohan-pandey-145170175/";
//                break;
//            case R.id.himanshu:
//            case R.id.himanshuTag:
//                url = "https://www.linkedin.com/in/himanshu-sharma2950/";
//                break;
//            case R.id.kanav:
//            case R.id.kanavTag:
//                url = "https://www.linkedin.com/in/kanav-bhagat-133229130/";
//                break;
//            case R.id.bhavika:
//            case R.id.bhavikaTag:
//                url = "https://www.linkedin.com/in/bhavikarana/";
//                break;
//            case R.id.vaibhav:
//            case R.id.vaibhavTag:
//                url = "https://www.linkedin.com/in/vaibhav-gautam-171775187/";
//                break;
//            case R.id.harsh:
//            case R.id.harshTag:
//                url = "https://www.linkedin.com/in/harshbandhey/";
//                break;
//            case R.id.vrinda:
//            case R.id.vrindaTag:
//                url = "https://www.linkedin.com/in/vrinda-narayan-6a42a71a3/";
//                break;
//            case R.id.tanuj:
//            case R.id.tanujTag:
//                url = "https://www.linkedin.com/in/tanuj-dabas-7b28a9151/";
//                break;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}