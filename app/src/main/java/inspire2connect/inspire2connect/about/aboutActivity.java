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

    private ImageView tavlab;
    private ImageView precog;
    private ImageView iiitd;
    public String names[];
    public String sal[],urls[];
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

        names = new String[]{getString(R.string.tav),getString(R.string.pk),
                getString(R.string.rohan),getString(R.string.vaibhav),getString(R.string.priyanka),
                getString(R.string.himanshu),getString(R.string.chirag),getString(R.string.bhavika),getString(R.string.kanav),
                getString(R.string.harsh),getString(R.string.vrinda)
        };
        sal = new String[]{getString(R.string.tav_description),getString(R.string.pk_description),
                getString(R.string.rohan_tag),getString(R.string.vaibhav_tag),getString(R.string.priyanka_tag),
                getString(R.string.himanshu_tag),getString(R.string.chirag_tag),getString(R.string.bhavika_tag),getString(R.string.kanav_tag),
                getString(R.string.harsh_tag),getString(R.string.vrinda_tag)
        };

        urls=new String[]{"https://www.linkedin.com/in/tavpritesh/","https://www.linkedin.com/in/ponguru/", "https://www.linkedin.com/in/rohan-pandey-145170175/","https://www.linkedin.com/in/vaibhav-gautam-171775187/",
                "https://www.linkedin.com/in/priyanka-syal-671b9495/","https://www.linkedin.com/in/himanshu-sharma2950/",
                "https://www.linkedin.com/in/jnchirag/","https://www.linkedin.com/in/bhavikarana/","https://www.linkedin.com/in/kanav-bhagat-133229130/",
                "https://www.linkedin.com/in/harshbandhey/","https://www.linkedin.com/in/vrinda-narayan-6a42a71a3/"
        };
        GridView gridView = (GridView)findViewById(R.id.gridview);
        about_adapter aboutAdapter = new about_adapter(this, names,sal);
        gridView.setAdapter(aboutAdapter);


        tavlab = findViewById(R.id.tavlabLogo);
        precog = findViewById(R.id.iiitdLogo);
        iiitd = findViewById(R.id.precogLogo);

        tavlab.setOnClickListener(this);
        precog.setOnClickListener(this);
        iiitd.setOnClickListener(this);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                String url = urls[position];
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                 startActivity(i);
            }
        });


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
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
