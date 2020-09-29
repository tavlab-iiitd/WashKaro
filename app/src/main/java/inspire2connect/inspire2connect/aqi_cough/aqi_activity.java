package inspire2connect.inspire2connect.aqi_cough;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class aqi_activity extends BaseActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.aqi_activity);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final TextView avgAQI = findViewById(R.id.avgAQI);

        Button resetButton = findViewById(R.id.resetButton);
        Button geotagButton = findViewById(R.id.geotagButton);

        final EditText dataCollectedAt = findViewById(R.id.dataCollectedAt);
        final EditText aqiPredictText = findViewById(R.id.aqiPredictText);
        final EditText bmiText = findViewById(R.id.bmiText);
        final EditText ageText = findViewById(R.id.ageText);

        final CheckBox bronchitisCheck = findViewById(R.id.bronchitisCheck);
        final CheckBox asthmaCheck = findViewById(R.id.asthmaCheck);
        final CheckBox pneumoniaCheck = findViewById(R.id.pneumoniaCheck);
        final CheckBox lungCancerCheck = findViewById(R.id.lungCancerCheck);
        final CheckBox tbCheck = findViewById(R.id.tbCheck);
        final CheckBox otherRespCheck = findViewById(R.id.otherRespCheck);

        final CheckBox femaleCheck = findViewById(R.id.femaleCheck);
        final CheckBox maleCheck = findViewById(R.id.maleCheck);
        final CheckBox otherGenderCheck = findViewById(R.id.otherGenderCheck);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataCollectedAt.setText("", TextView.BufferType.EDITABLE);
                aqiPredictText.setText("", TextView.BufferType.EDITABLE);
                bmiText.setText("", TextView.BufferType.EDITABLE);
                ageText.setText("", TextView.BufferType.EDITABLE);
                bronchitisCheck.setChecked(false);
                asthmaCheck.setChecked(false);
                pneumoniaCheck.setChecked(false);
                lungCancerCheck.setChecked(false);
                tbCheck.setChecked(false);
                otherRespCheck.setChecked(false);
                femaleCheck.setChecked(false);
                maleCheck.setChecked(false);
                otherGenderCheck.setChecked(false);

            }
        });

        geotagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String aqiPredictString = aqiPredictText.getText().toString();
                String dataLocationString = dataCollectedAt.getText().toString();
                String bmiString = bmiText.getText().toString();
                String ageString = ageText.getText().toString();
                boolean bronchitisVal = bronchitisCheck.isChecked();    //To Convert to String : Boolean.toString(bronchitisVal)
                boolean asthmaVal = asthmaCheck.isChecked();
                boolean pneumoniaVal = pneumoniaCheck.isChecked();
                boolean lungCancerVal = lungCancerCheck.isChecked();
                boolean tbVal = tbCheck.isChecked();
                boolean otherRespVal = otherRespCheck.isChecked();
                boolean femaleVal = femaleCheck.isChecked();
                boolean maleVal = maleCheck.isChecked();
                boolean otherGenderVal = otherGenderCheck.isChecked();
                Date currentTime = Calendar.getInstance().getTime();

                UserHelperClass helperClass = new UserHelperClass(aqiPredictString, dataLocationString, bmiString, ageString, bronchitisVal, asthmaVal, pneumoniaVal, lungCancerVal, tbVal, otherRespVal, femaleVal, maleVal, otherGenderVal, currentTime);

                aqiReference.push().setValue(helperClass);

                Intent geotagIntent = new Intent(aqi_activity.this, GeoTagPage.class);
                startActivity(geotagIntent);
            }
        });

        maleCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (maleCheck.isChecked()) {
                    femaleCheck.setChecked(false);
                    otherGenderCheck.setChecked(false);
                } else if (femaleCheck.isChecked()) {
                    otherGenderCheck.setChecked(false);
                    maleCheck.setChecked(false);
                } else if (otherGenderCheck.isChecked()) {
                    maleCheck.setChecked(false);
                    femaleCheck.setChecked(false);
                }
            }
        });

        femaleCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (maleCheck.isChecked()) {
                    femaleCheck.setChecked(false);
                    otherGenderCheck.setChecked(false);
                } else if (femaleCheck.isChecked()) {
                    otherGenderCheck.setChecked(false);
                    maleCheck.setChecked(false);
                } else if (otherGenderCheck.isChecked()) {
                    maleCheck.setChecked(false);
                    femaleCheck.setChecked(false);
                }
            }
        });

        otherGenderCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (maleCheck.isChecked()) {
                    femaleCheck.setChecked(false);
                    otherGenderCheck.setChecked(false);
                } else if (femaleCheck.isChecked()) {
                    otherGenderCheck.setChecked(false);
                    maleCheck.setChecked(false);
                } else if (otherGenderCheck.isChecked()) {
                    maleCheck.setChecked(false);
                    femaleCheck.setChecked(false);
                }
            }
        });

        OkHttpClient client = new OkHttpClient();

        String url = "https://api.waqi.info/feed/delhi/?token=262ba73b900a0ff15d3ac7bbbee593ddbb543aa9";

        Request request = new Request.Builder()
                .url(url)
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
                    String finalAQI = "Unavailable";
                    try {
                        JSONObject aqiJSON = new JSONObject(myResponse);
                        JSONObject aqiData = (JSONObject) aqiJSON.get("data");
                        finalAQI = "" + aqiData.get("aqi");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final String finalAQI1 = finalAQI;
                    aqi_activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            avgAQI.setText(finalAQI1);
                        }
                    });
                }

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
