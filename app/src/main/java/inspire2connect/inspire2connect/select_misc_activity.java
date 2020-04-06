package inspire2connect.inspire2connect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class select_misc_activity extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout misc_buttons[] = new ConstraintLayout[10];
    int curr_lang =2;
    String Lang_extra = "hindi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_misc_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        misc_buttons[0] = findViewById(R.id.misc_but1_layout);
        misc_buttons[1] = findViewById(R.id.misc_but2_layout);
        misc_buttons[2] = findViewById(R.id.misc_but3_layout);

        for(int i =0;i<3;i++){
            misc_buttons[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if(view == misc_buttons[0]){
            Intent i = new Intent(select_misc_activity.this,map_activity.class);
            i.putExtra("Language",Lang_extra);
            startActivity(i);

        }
        if(view == misc_buttons[1]){
            Intent i = new Intent(select_misc_activity.this,daily_guidelines.class);
            i.putExtra("Language",Lang_extra);
            startActivity(i);
        }
        if(view == misc_buttons[2]){
            Intent i = new Intent(select_misc_activity.this,Myths.class);
            i.putExtra("Language",Lang_extra);
            startActivity(i);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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
            //switch_language();
            if (curr_lang == 2) {
                curr_lang = 1;
                change_to_english();
            } else {
                curr_lang = 2;
                change_to_hindi();
            }
        } else if (id == R.id.Survey) {
            Intent i = new Intent(select_misc_activity.this, Male_Female.class);
            if (curr_lang == 2)
                i.putExtra("Language", "hindi");
            else
                i.putExtra("Language", "english");
            startActivity(i);
        } else if (id == R.id.developers) {
            Intent i = new Intent(select_misc_activity.this, about.class);
            startActivity(i);
        } else if (id == R.id.privacy_policy) {
            Intent i = new Intent(select_misc_activity.this, privacy_policy.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public void change_to_english(){

        TextView t1 = (TextView)findViewById(R.id.misc_but1_text);
        TextView t2 = (TextView)findViewById(R.id.misc_but2_text);
        TextView t3 = (TextView)findViewById(R.id.misc_but3_text);

        t1.setText("India COVID Map");
        t2.setText("WHO Guidelines");
        t3.setText("Myth Busters");
    }
    public void change_to_hindi(){

        TextView t1 = (TextView)findViewById(R.id.misc_but1_text);
        TextView t2 = (TextView)findViewById(R.id.misc_but2_text);
        TextView t3 = (TextView)findViewById(R.id.misc_but3_text);

        t1.setText("India COVID Map");
        t2.setText("WHO Guidelines");
        t3.setText("Myth Busters");
    }

}
