package inspire2connect.inspire2connect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import inspire2connect.inspire2connect.contactTracer.base.BaseActivity;


@SuppressWarnings("SpellCheckingInspection")
public class about extends BaseActivity implements View.OnClickListener {

    private ImageView tavlab;
    private ImageView precog;
    private ImageView iiitd;
    private TextView chirag;
    private TextView chiragTag;
    private TextView priyanka;
    private TextView priyankaTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ca_activity_about);

        if(getActionBar()!=null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tavlab = findViewById(R.id.tavlabLogo);
        precog = findViewById(R.id.precogLogo);
        iiitd = findViewById(R.id.iiitdLogo);
        chirag = findViewById(R.id.chirag);
        chiragTag = findViewById(R.id.chiragTag);
        priyanka = findViewById(R.id.priyanka);
        priyankaTag = findViewById(R.id.priyankaTag);

        tavlab.setOnClickListener(this);
        precog.setOnClickListener(this);
        iiitd.setOnClickListener(this);
        chiragTag.setOnClickListener(this);
        chirag.setOnClickListener(this);
        priyankaTag.setOnClickListener(this);
        priyanka.setOnClickListener(this);

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
        switch (view.getId()){
            case R.id.precogLogo:
                url = "http://precog.iiitd.edu.in/";
                break;
            case R.id.iiitdLogo:
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
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
