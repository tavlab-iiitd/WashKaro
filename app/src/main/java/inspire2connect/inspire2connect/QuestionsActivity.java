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
    RadioButton rb1, rb2;
    String[] questions;
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

        questions = new String[]{
                getString(R.string.question1),
                getString(R.string.question2),
                getString(R.string.question3),
                getString(R.string.question4),
                getString(R.string.question5),
                getString(R.string.question6),
                getString(R.string.question7)
        };

        textView.setText((flag + 1) + "/7");
        ans = new int[7];
        submitbutton = findViewById(R.id.nextQues);

        for (int i = 0; i < 7; i++)
            ans[i] = 0;

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
