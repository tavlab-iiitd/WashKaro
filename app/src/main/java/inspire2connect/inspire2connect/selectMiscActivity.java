package inspire2connect.inspire2connect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;

public class selectMiscActivity extends BaseActivity implements View.OnClickListener {

    LinearLayout[] misc_buttons = new LinearLayout[10];

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_misc_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.misc_tile);
        misc_buttons[0] = findViewById(R.id.misc_but1_layout);
        misc_buttons[1] = findViewById(R.id.misc_but2_layout);
        misc_buttons[2] = findViewById(R.id.misc_but3_layout);

        for (int i = 0; i < 3; i++) {
            misc_buttons[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == misc_buttons[0]) {
            Intent i = new Intent(selectMiscActivity.this, mapActivity.class);
            startActivity(i);

        }
        if (view == misc_buttons[1]) {
            Intent i = new Intent(selectMiscActivity.this, dailyGuidelinesActivity.class);
            startActivity(i);
        }
        if (view == misc_buttons[2]) {
            Intent i = new Intent(selectMiscActivity.this, mythsActivity.class);
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
            toggleLang(this);
        } else if (id == R.id.Survey) {
            Intent i = new Intent(selectMiscActivity.this, maleFemaleActivity.class);
            startActivity(i);
        } else if (id == R.id.developers) {
            Intent i = new Intent(selectMiscActivity.this, aboutActivity.class);
            startActivity(i);
        } else if (id == R.id.privacy_policy) {
            Intent i = new Intent(selectMiscActivity.this, privacyPolicyActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}
