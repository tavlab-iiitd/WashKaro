package inspire2connect.inspire2connect.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.satyaChat.ChatActivity;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;

public class selectChatBotActivity extends BaseActivity implements View.OnClickListener {
    Button cbot0, cbot1, cbot2, cbot3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.chatbot_tile);
        }
        setContentView(R.layout.activity_select_chatbot_activity);
        cbot0 = findViewById(R.id.cbot0);
        cbot1 = findViewById(R.id.cbot1);
        cbot2 = findViewById(R.id.cbot2);
        cbot3 = findViewById(R.id.cbot3);
        cbot0.setOnClickListener(this);
        cbot1.setOnClickListener(this);
        cbot2.setOnClickListener(this);
        cbot3.setOnClickListener(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cbot0:
                Intent intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
                break;
            case R.id.cbot1:
                String nmbr = "+41798931892";
                openWhatsapp(nmbr);
                break;
            case R.id.cbot2:
                String nmbr1 = "+919013151515";
                openWhatsapp(nmbr1);
                break;
            case R.id.cbot3:
                String nmbr2 = "+918800007722";
                openWhatsapp(nmbr2);
                break;
            default:
                startActivity(new Intent(this, null));
                break;
        }

//        if(view == cbot0) {
//            Intent intent = new Intent(this, ChatActivity.class);
//            startActivity(intent);
//        }
//        if (view == cbot1) {
//            String nmbr = "+41798931892";
//            openWhatsapp(nmbr);
//        }
//        if (view == cbot2) {
//            String nmbr = "+919013151515";
//            openWhatsapp(nmbr);
//        }
//        if (view == cbot3) {
//            String nmbr = "+918800007722";
//            openWhatsapp(nmbr);
//        }

    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }

    private void openWhatsapp(String nmbr) {
        String url = "https://api.whatsapp.com/send?phone=" + nmbr;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

}
