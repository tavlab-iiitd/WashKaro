package inspire2connect.inspire2connect.satyaChat;

import android.content.Context;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;

public class ChatActivity extends BaseActivity {

    private static String baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.satya_chatbot);

        baseUrl = getString(R.string.satya_chatbot_url) + "?query=something&lang=en";

        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest ExampleRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Logv("Chirag", response.toString());
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        ExampleRequestQueue.add(ExampleRequest);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
