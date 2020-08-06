package inspire2connect.inspire2connect.satyaChat;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;

public class ChatActivity extends BaseActivity {

    private Context context;
    private RequestQueue requestQueue;
    private static final String answer = "answer";

    public ListView chatListView;
    ArrayList<ChatElem> items;

    private ChatAdapter chatAdapter;

    private EditText sendText;
    private ImageButton sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.satya_chatbot);
        context = this;
        requestQueue = Volley.newRequestQueue(context);

        items = new ArrayList<>();

        sendText = findViewById(R.id.chatInpField);
        sendButton = findViewById(R.id.chatSendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Firebase Analytics
                Bundle bundle = new Bundle();
                bundle.putString("UID", firebaseUser.getUid());
                bundle.putString("ChatBot_Text", "Sent");
                firebaseAnalytics.logEvent("ChatBot_Activity", bundle);

                String txtToSend = sendText.getText().toString().trim();
                items.add(new ChatElem(txtToSend, true));
                sendRequest(requestQueue, txtToSend);
                updateListView();
                sendText.setText("");
            }
        });

        chatListView = findViewById(R.id.chat_list);
        chatAdapter = new ChatAdapter(this, items);
        chatListView.setAdapter(chatAdapter);

        String hello = getString(R.string.hello);
//        items.add(new ChatElem(hello, true));
//        updateListView();
        sendRequest(requestQueue, hello);

        //Firebase Analytics
        Bundle bundle = new Bundle();
        bundle.putString("UID", firebaseUser.getUid());
        bundle.putString("Screen", "Satya Chatbot Screen");
        firebaseAnalytics.logEvent("CurrentScreen", bundle);

    }

    private void updateListView() {
        chatAdapter.notifyDataSetChanged();
        final View v1 = chatListView.getChildAt(chatListView.getLastVisiblePosition() - chatListView.getFirstVisiblePosition());
        final Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.chat_item_add_anim);
        if (v1 != null) {
            v1.startAnimation(animation1);
        }
    }

    private void sendRequest(RequestQueue requestQueue, String text) {

        JsonObjectRequest ExampleRequest = new JsonObjectRequest(Request.Method.GET, createURL(context, text), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String responseMsg = response.getString(answer).trim();
                    items.add(new ChatElem(responseMsg, false));
                    updateListView();
                } catch (Exception e) {

                }
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
        String uid = firebaseUser.getUid().toString();
        return ctx.getString(R.string.satya_chatbot_url) + "?query=" + text + "&lang=" + getCurLang() + "&uid=" + uid;
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
