package inspire2connect.inspire2connect;


import androidx.appcompat.app.AppCompatActivity;

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

public class QuestionsActivity extends BaseActivity {
    TextView tv;
    Button submitbutton, prevbutton;
    RadioGroup radio_g;
//  final TextView textView=(TextView)findViewById(R.id.pageNo);
    int curr_lang = 2;//1 for eng , 2 for Hindi
    RadioButton rb1,rb2,rb3,rb4;
    String questions[];
    String questionsEnglish[] = {
            "Have you traveled or lived a country/Indian state reporting community transmission in the last 14 days?",
            "Have you been in close contact with a probable or confirmed COVID-19 patient within the past 14 days?",
            "Do you have fever?",
            "Do you have cough?",
            "Do you have shortness in breath?",
            "Were your symptoms severe enough to require hospital admission?",
            "Did the doctor provide an alternative explanation for the symptoms?"
    };
    String questionsHindi[] = {
            "क्या आपने पिछले 14 दिनों में COVID 19 की रिपोर्टिंग करने वाले किसी देश / भारतीय राज्य की यात्रा की है?",
            "क्या आप पिछले 14 दिनों के भीतर एक संभावित या पुष्टि किए गए COVID-19 रोगी के निकट संपर्क में हैं?",
            "क्या आपको बुखार है?",
            "क्या आपको खांसी है?",
            "क्या आपको सांस में तकलीफ है?",
            "क्या आप अस्पताल गए हैं?",
            "क्या डॉक्टर ने लक्षणों के लिए एक वैकल्पिक स्पष्टीकरण प्रदान किया है?"
    };
    int flag=0;
    public static String myName;
    public static int ans[];
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        final TextView textView=(TextView)findViewById(R.id.pageNo);

        if(flag==0){
//            Intent in = new Intent(getApplicationContext(),symptom_activity.class);
//
//            startActivity(in);
            finish();

        }else{
            flag--;
            textView.setText(Integer.toString(flag+1)+"/7");
            tv.setText(questions[flag]);
            if(ans[flag] ==1){
                rb1.performClick();
            }else{
                rb2.performClick();
            }
        }
        //r
    }
    @Override
    public boolean onSupportNavigateUp() {
        final TextView textView=(TextView)findViewById(R.id.pageNo);

        if(flag==0){
//            Intent in = new Intent(getApplicationContext(),symptom_activity.class);
//            startActivity(in);
            finish();


        }else{
            flag--;
            textView.setText(Integer.toString(flag+1)+"/7");
            tv.setText(questions[flag]);
            if(ans[flag] ==1){
                rb1.performClick();
            }else{
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
        rb1=(RadioButton)findViewById(R.id.yesButton);
        rb2=(RadioButton)findViewById(R.id.noButton);
        final TextView textView=(TextView)findViewById(R.id.pageNo);
        Intent prev_intent=getIntent();
        if(prev_intent.getStringExtra("Language").equalsIgnoreCase("hindi"))
            curr_lang=2;
        else
            curr_lang=1;        //String name= intent.getStringExtra("myname");
        textView.setText(Integer.toString(flag+1)+"/7");
        ans = new int[7];
        submitbutton=(Button)findViewById(R.id.nextQues);

        for(int i=0; i<7; i++)
            ans[i] = 0;
        if(curr_lang==2){
            questions = questionsHindi;
            rb1.setText("हाँ");
            rb2.setText("नहीं");
            submitbutton.setText("आगे");
        }else{
            questions = questionsEnglish;
            submitbutton.setText("Next");
        }
        tv=(TextView) findViewById(R.id.question);

        radio_g=(RadioGroup)findViewById(R.id.answers);

        tv.setText(questions[flag]);
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radio_g.getCheckedRadioButtonId()==-1)
                {
                    if(curr_lang==1)
                        Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "कृपया एक विकल्प चुनें", Toast.LENGTH_SHORT).show();

                    return;
                }
                RadioButton uans = (RadioButton) findViewById(radio_g.getCheckedRadioButtonId());
                String ansText = uans.getText().toString();
                if(radio_g.getCheckedRadioButtonId()==R.id.yesButton)
                {
                    ans[flag] = 1;
                    Log.d("Testing","Yes selected");
                }
                else
                {
                    ans[flag] = 2;
                    Log.d("Testing","No selected");
                }
//                if(ansText.equals("Yes"))
//                {
//                    ans[flag] = 1;
//                }
//                else if(ansText.equals("No")){
//                    ans[flag] = 2;
//                }

                flag++;
                if(flag<questions.length)
                {
                    textView.setText(Integer.toString(flag+1)+"/7");
                    tv.setText(questions[flag]);
                }
                else
                {
                    Intent in = new Intent(getApplicationContext(),ResultActivity.class);
                    if(curr_lang==1)
                        in.putExtra("Language","english");
                    else
                        in.putExtra("Language","hindi");
                    startActivity(in);
                }
                radio_g.clearCheck();
            }
        });
    }
}
