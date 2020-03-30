package inspire2connect.inspire2connect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class symptom_activity extends AppCompatActivity {
    int curr_lang = 2;//1 for eng , 2 for Hindi
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_activity);
        Intent i=getIntent();
        if(i.getStringExtra("Language").equalsIgnoreCase("hindi"))
            curr_lang=2;
        else
            curr_lang=1;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title2 = "<b> 2-Minute Self Assessment </b> <br> Based on WHO Guidelines for Suspect Case Identification";
        String button_text="BEGIN";
        Button b = findViewById(R.id.startButton);
        if(curr_lang==2)
        {
            button_text="शुरू करो";
            title2="<b> 2-मिनट स्व-मूल्यांकन </ b> <br> WHO दिशानिर्देशों के आधार पर संदिग्ध मामले की पहचान के लिए";
        }
        TextView t = findViewById(R.id.title2Text);
        t.setText(Html.fromHtml(title2));
        b.setText(button_text);
    }
    @Override
    public boolean onSupportNavigateUp() {

        finish();
        //return super.onSupportNavigateUp();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.lang_togg_butt) {
            Toast.makeText(symptom_activity.this, "Language Changed", Toast.LENGTH_SHORT).show();
            if (curr_lang == 2) {
                curr_lang = 1;

            } else {
                curr_lang = 2;

            }
            switch_language();

        } else if (id == R.id.Survey) {
            Intent i = new Intent(symptom_activity.this, Male_Female.class);
            if (curr_lang == 2)
                i.putExtra("Language", "hindi");
            else
                i.putExtra("Language", "english");
            startActivity(i);
        } else if (id == R.id.developers) {
            Intent i = new Intent(symptom_activity.this, about.class);
            startActivity(i);
        } else if (id == R.id.privacy_policy) {
            Intent i = new Intent(symptom_activity.this, privacy_policy.class);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }
    public void switch_language()
    {
        String title2 = "<b> 2-Minute Self Assessment </b> <br> Based on WHO Guidelines for Suspect Case Identification";
        String button_text="BEGIN";
        if(curr_lang==2)
        {
            title2 = "<b> 2-मिनट स्व-मूल्यांकन </ b> <br> WHO दिशानिर्देशों के आधार पर संदिग्ध मामले की पहचान के लिए";
            button_text="शुरू करो";
        }
        Button b = findViewById(R.id.startButton);
        b.setText(button_text);
        TextView t = findViewById(R.id.title2Text);
        t.setText(Html.fromHtml(title2));
    }
    public void onClickStart(View view) {
        Intent intent=new Intent(getApplicationContext(),QuestionsActivity.class);
//        RadioGroup  radio_g=(RadioGroup)findViewById(R.id.languageGroup);
//        RadioButton rb1=(RadioButton)findViewById(R.id.hindiButton);
//        RadioButton rb2=(RadioButton)findViewById(R.id.englishButton);
//        if(radio_g.getCheckedRadioButtonId()==-1)
//        {
//            Toast.makeText(getApplicationContext(), "Please select a language.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        RadioButton uans = (RadioButton) findViewById(radio_g.getCheckedRadioButtonId());
//        String ansText = uans.getText().toString();
//        if(ansText.equals("Hindi")) {
//            intent.putExtra("language", "hindi");
//        }else {
//            intent.putExtra("language", "english");
//        }
        if(curr_lang==1)
            intent.putExtra("Language", "english");
        else
            intent.putExtra("Language", "hindi");

        startActivity(intent);
    }
}

