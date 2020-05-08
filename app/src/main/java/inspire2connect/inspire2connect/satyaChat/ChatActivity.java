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

    private Context context;
    private RequestQueue requestQueue;
    private String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.satya_chatbot);
        context = this;
        requestQueue = Volley.newRequestQueue(context);
        answer = "answer";

        sendRequest(requestQueue, getString(R.string.hello));

    }

    private void sendRequest(RequestQueue requestQueue, String text) {

        JsonObjectRequest ExampleRequest = new JsonObjectRequest(Request.Method.GET, createURL(context, text), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Logv("Chirag", response.getString(answer));
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(ExampleRequest);

    }

    private static String createURL(Context ctx, String text) {
        text = text.trim();
        return ctx.getString(R.string.satya_chatbot_url) + "?query=" + text + "&lang=" + getCurLang();
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
