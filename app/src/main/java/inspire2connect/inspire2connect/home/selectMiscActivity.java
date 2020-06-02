package inspire2connect.inspire2connect.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.about.aboutActivity;
import inspire2connect.inspire2connect.survey.maleFemaleActivity;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;
import inspire2connect.inspire2connect.utils.urlActivity;

public class selectMiscActivity extends BaseActivity implements View.OnClickListener {

    ConstraintLayout[] misc_buttons = new ConstraintLayout[4];

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
//        misc_buttons[0] = findViewById(R.id.misc_but1_layout);
        misc_buttons[0] = findViewById(R.id.misc_but2_layout);
        misc_buttons[1] = findViewById(R.id.misc_but3_layout);
        misc_buttons[2] = findViewById(R.id.hunger_relief_layout);
        misc_buttons[3] = findViewById(R.id.shelter_relief_layout);

        for (int i = 0; i < misc_buttons.length; i++) {
            if(misc_buttons[i]!=null) {
                misc_buttons[i].setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {
//        if (view == misc_buttons[0]) {
//            Intent i = new Intent(selectMiscActivity.this, urlActivity.class);
//            i.putExtra("url", getString(R.string.covid_map));
//            i.putExtra("name", getString(R.string.india_covid_map_tile));
//            startActivity(i);
//        }
        if (view == misc_buttons[0]) {
            Intent i = getGuidelinesIntent(this);
            startActivity(i);
        }
        if (view == misc_buttons[1]) {
            Intent i = getMythIntent(this);
            startActivity(i);
        }
        if (view == misc_buttons[2]) {
            Intent i = new Intent(selectMiscActivity.this, urlActivity.class);
            i.putExtra("url", getString(R.string.hunger_relief_url));
            i.putExtra("name", getString(R.string.hunger_relief));
            startActivity(i);
        }
        if (view == misc_buttons[3]) {
            Intent i = new Intent(selectMiscActivity.this, urlActivity.class);
            i.putExtra("url", getString(R.string.shelter_relief_url));
            i.putExtra("name", getString(R.string.shelter_relief));
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
            openPrivacyPolicy(this);
        }

        return super.onOptionsItemSelected(item);
    }

}
