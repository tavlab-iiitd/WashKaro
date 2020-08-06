package inspire2connect.inspire2connect.survey;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.about.aboutActivity;
import inspire2connect.inspire2connect.home.homeActivity;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;

public class maleFemaleActivity extends BaseActivity {
    final int requestcode = 123;
    final private int PERMISSION_ACCESS_FINE_LOCATION = 1;
    public String gender, lat, lon, locatio;
    public String disease_info;
    Button submit;
    boolean can_Access;
    int curr_lang = 2; //1 for eng , 2 for hindi
    DatabaseReference ref;
    String location_provider = LocationManager.NETWORK_PROVIDER;
    LocationManager mLocationManager;
    LocationListener mLocationListener;
    Button skip;
    private DatabaseReference mDatabaseReference;
    private WebView webView;
    private ProgressDialog progDailog;

    private void send_data() {
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("user_data").push().setValue("Location:" + locatio);
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        //return super.onSupportNavigateUp();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_male__female);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        if (!prefManager.isFirstTimeLaunch())
//        {
//
//            prefManager.setFirstTimeLaunch(false);
//            Intent i=new Intent(Male_Female.this,Home_Activity.class);
//            startActivity(i);
//            finish();
//        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
        } else
            workwithlocation();

//        Spinner gender_spinner=(Spinner)(findViewById(R.id.gender_spinner));
//        Spinner disease_spinner=(Spinner)(findViewById(R.id.disease_spinner));
//        gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//            {
//                //Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+"is selected",Toast.LENGTH_SHORT).show();
//                gender=parent.getItemAtPosition(position).toString();
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent)
//            {
//
//            }
//        });
//        disease_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//            {
//                //Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+"is selected",Toast.LENGTH_SHORT).show();
//                disease_info=parent.getItemAtPosition(position).toString();
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent)
//            {
//
//            }
//        });
//        submit=(Button)(findViewById(R.id.submit));
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                send_data();
//                Intent i=new Intent(Male_Female.this,CardViewActivity.class);
//                prefManager.setFirstTimeLaunch(false);
//                startActivity(i);
//            }
//        });

        //progDailog = ProgressDialog.show(this, "Loading","Please wait...", true);
        //progDailog.setCancelable(false);

        switch (getCurLang()) {
            case englishCode:
                switch_to_eng();
                break;
            case hindiCode:
                switch_to_hindi();
                break;
        }

        //Firebase Analytics
        Bundle bundle = new Bundle();
        bundle.putString("UID", firebaseUser.getUid());
        bundle.putString("Screen", "Survey Screen");
        firebaseAnalytics.logEvent("CurrentScreen", bundle);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    workwithlocation();
                    Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    public void workwithlocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("xyz1234", "on location changed");
                //Log.d("xyz1234",location.toString());
                lat = Double.toString(location.getLatitude());
                lon = Double.toString(location.getLongitude());
                Double lat1 = location.getLatitude();
                Double lon1 = location.getLongitude();
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(maleFemaleActivity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(lat1, lon1, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    Log.d("City", city);
                    Log.d("City", address);
                    Log.d("City", state);
                    Log.d("City", country);
                    Log.d("City", postalCode);
                    Log.d("City", knownName);
                    locatio = addresses.get(0).getAddressLine(0);
                    //TextView location_set=(TextView)findViewById(R.id.location);
                    //location_set.setText(city);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestcode);
            return;
        }
        mLocationManager.requestLocationUpdates(location_provider, 5000, 1000, mLocationListener);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.lang_togg_butt) {
            // Firebase Analytics
            Bundle bundle = new Bundle();
            bundle.putString("UID", firebaseUser.getUid());
            if(Locale.getDefault().getLanguage().equals("en"))
                bundle.putString("Current_Language", "Hindi");
            else if(Locale.getDefault().getLanguage().equals("hi"))
                bundle.putString("Current_Language", "English");

            bundle.putString("Language_Change_Activity", "Survey Activity");
            firebaseAnalytics.logEvent("Language_Toggle", bundle);

            toggleLang(this);
            switch_language();


        } else if (id == R.id.Survey) {
            Intent i = new Intent(maleFemaleActivity.this, maleFemaleActivity.class);
            startActivity(i);
        } else if (id == R.id.developers) {
            Intent i = new Intent(maleFemaleActivity.this, aboutActivity.class);
            startActivity(i);
        } else if (id == R.id.privacy_policy) {
            openPrivacyPolicy(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public void switch_language() {
        if (curr_lang == 1) {
            curr_lang = 2;
            switch_to_hindi();
        } else {
            curr_lang = 1;
            switch_to_eng();
        }
    }

    public void switch_to_hindi() {
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

                if (url.equalsIgnoreCase("https://docs.google.com/forms/d/e/1FAIpQLSd6z9IzgbDvkG08rSjlq2pvTo3ChdHrSAr2u6iRqnl-FX1oFw/formResponse")) {
                    send_data();
                    Intent i = new Intent(maleFemaleActivity.this, homeActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        webView.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSd6z9IzgbDvkG08rSjlq2pvTo3ChdHrSAr2u6iRqnl-FX1oFw/viewform?usp=sf_link");
    }

    public void switch_to_eng() {
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

                if (url.equalsIgnoreCase("https://docs.google.com/forms/u/0/d/e/1FAIpQLSf-hLHpQPd7sLkoXqNfGf-RT390Q4cSP7JBmrrSXLZmqEYEYw/formResponse")) {
                    send_data();
                    Intent i = new Intent(maleFemaleActivity.this, homeActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        webView.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSf-hLHpQPd7sLkoXqNfGf-RT390Q4cSP7JBmrrSXLZmqEYEYw/viewform?usp=sf_link");

    }


}
