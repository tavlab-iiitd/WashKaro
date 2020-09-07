package inspire2connect.inspire2connect.aqi_cough;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import inspire2connect.inspire2connect.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class GeoTagPage extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    private static final int REQUEST_CODE = 101;

//    public String finalAQI;
//    public LatLng currLoc;
    public String myLocURL;
    public double currLat, currLong;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.geotagpage);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fetchLastLocation();

        Button coughButton = (Button) findViewById(R.id.coughButton);

        coughButton.setOnClickListener(new android.view.View.OnClickListener(){

            @Override
            public void onClick(android.view.View view) {
                Intent coughPage = new Intent(GeoTagPage.this, cough_recorder.class);
                startActivity(coughPage);
            }

        });

//        finalAQI = "";
    }

    private void fetchAQI(final double lat, final double lon, final String markerLabel, final int colorVal ) {
        myLocURL = "http://api.waqi.info/feed/geo:" + lat + ";" + lon + "/?token=262ba73b900a0ff15d3ac7bbbee593ddbb543aa9";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(myLocURL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    try {
                        JSONObject aqiJSON = new JSONObject(myResponse);
                        JSONObject aqiData = (JSONObject) aqiJSON.get("data");
                        final String AQIVal = aqiData.get("aqi").toString();

                        GeoTagPage.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LatLng locPtr = new LatLng (lat,lon);
                                map.addMarker(new MarkerOptions ().position(locPtr).title(markerLabel).snippet("AQI = " + AQIVal + " , Coordinates (" + lat + "," + lon + ")").icon( BitmapDescriptorFactory.defaultMarker(colorVal)));
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        com.google.android.gms.tasks.Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location> () {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    currentLocation = location;
                    currLat = currentLocation.getLatitude();
                    currLong = currentLocation.getLongitude();
                    fetchAQI(currLat, currLong, "Current Location", 270);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        fetchAQI(28.5456, 77.2732, "IIIT Delhi", 30);
        fetchAQI(28.5355, 77.3910, "Noida", 30);
        fetchAQI(28.4595, 77.0266, "Gurgaon", 30);
        fetchAQI(19.0760, 72.8777, "Mumbai", 30);
        fetchAQI(12.9716 , 77.5946, "Bangalore",30);
        fetchAQI(22.5726 , 88.3639, "Kolkata", 30);
        LatLng IIITD = new LatLng (28.5456, 77.2732);
        map.moveCamera( CameraUpdateFactory.newLatLng(IIITD));
        map.animateCamera( CameraUpdateFactory.newLatLngZoom(IIITD, 10));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
