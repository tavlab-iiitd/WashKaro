package inspire2connect.inspire2connect;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;

public class QuestionsActivity extends BaseActivity {
    public static String myName;
    public static int[] ans;
    TextView tv;
    Button submitbutton, prevbutton;
    RadioGroup radio_g;
    //  final TextView textView=(TextView)findViewById(R.id.pageNo);
    int curr_lang = 2;//1 for eng , 2 for Hindi
    RadioButton rb1, rb2, rb3, rb4;
    String[] questions;
    String[] questionsEnglish = {
            "Have you traveled or lived a country/Indian state reporting community transmission in the last 14 days?",
            "Have you been in close contact with a probable or confirmed COVID-19 patient within the past 14 days?",
            "Do you have fever?",
            "Do you have cough?",
            "Do you have shortness in breath?",
            "Were your symptoms severe enough to require hospital admission?",
            "Did the doctor provide an alternative explanation for the symptoms?"
    };
    String[] questionsHindi = {
            "क्या आपने पिछले 14 दिनों में COVID 19 की रिपोर्टिंग करने वाले किसी देश / भारतीय राज्य की यात्रा की है?",
            "क्या आप पिछले 14 दिनों के भीतर एक संभावित या पुष्टि किए गए COVID-19 रोगी के निकट संपर्क में हैं?",
            "क्या आपको बुखार है?",
            "क्या आपको खांसी है?",
            "क्या आपको सांस में तकलीफ है?",
            "क्या आप अस्पताल गए हैं?",
            "क्या डॉक्टर ने लक्षणों के लिए एक वैकल्पिक स्पष्टीकरण प्रदान किया है?"
    };
    int flag = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public void onBackPressed() {
        final TextView textView = findViewById(R.id.pageNo);

        if (flag == 0) {
//            Intent in = new Intent(getApplicationContext(),symptom_activity.class);
//
//            startActivity(in);
            finish();

        } else {
            flag--;
            textView.setText((flag + 1) + "/7");
            tv.setText(questions[flag]);
            if (ans[flag] == 1) {
                rb1.performClick();
            } else {
                rb2.performClick();
            }
        }
        //r
    }

    @Override
    public boolean onSupportNavigateUp() {
        final TextView textView = findViewById(R.id.pageNo);

        if (flag == 0) {
//            Intent in = new Intent(getApplicationContext(),symptom_activity.class);
//            startActivity(in);
            finish();


        } else {
            flag--;
            textView.setText((flag + 1) + "/7");
            tv.setText(questions[flag]);
            if (ans[flag] == 1) {
                rb1.performClick();
            } else {
                rb2.performClick();
            }
        }
        //return super.onSupportNavigateUp();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rb1 = findViewById(R.id.yesButton);
        rb2 = findViewById(R.id.noButton);
        final TextView textView = findViewById(R.id.pageNo);

        switch (getCurLang()) {
            case englishCode:
                curr_lang = 1;
                break;
            case hindiCode:
                curr_lang = 2;
                break;
        }

        textView.setText((flag + 1) + "/7");
        ans = new int[7];
        submitbutton = findViewById(R.id.nextQues);

        for (int i = 0; i < 7; i++)
            ans[i] = 0;
        if (curr_lang == 2) {
            questions = questionsHindi;
        } else {
            questions = questionsEnglish;
        }
        tv = findViewById(R.id.question);

        radio_g = findViewById(R.id.answers);

        tv.setText(questions[flag]);
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio_g.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), getString(R.string.please_select_one_choice), Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton uans = findViewById(radio_g.getCheckedRadioButtonId());
                String ansText = uans.getText().toString();
                if (radio_g.getCheckedRadioButtonId() == R.id.yesButton) {
                    ans[flag] = 1;
                    Logd("Testing", "Yes selected");
                } else {
                    ans[flag] = 2;
                    Logd("Testing", "No selected");
                }
//                if(ansText.equals("Yes"))
//                {
//                    ans[flag] = 1;
//                }
//                else if(ansText.equals("No")){
//                    ans[flag] = 2;
//                }

                flag++;
                if (flag < questions.length) {
                    textView.setText((flag + 1) + "/7");
                    tv.setText(questions[flag]);
                } else {
                    Intent in = new Intent(getApplicationContext(), ResultActivity.class);
                    startActivity(in);
                }
                radio_g.clearCheck();
            }
        });
    }
}
