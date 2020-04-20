package inspire2connect.inspire2connect;


import android.app.AppComponentFactory;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;

public class QuestionsActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public static String myName;
    public static int[] ans;
    TextView tv;
    Button yes_button, no_button;
    RadioGroup radio_g;
    //  final TextView textView=(TextView)findViewById(R.id.pageNo);
    RadioButton rb1, rb2;
    ArrayList<String>items;
    String questions[];
    int flag = 0;
    QuestionsAdapter customAdapter;
    DatabaseReference ref;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }



    @Override
    public boolean onSupportNavigateUp() {
            finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        final TextView textView = findViewById(R.id.pageNo);
        ref = FirebaseDatabase.getInstance().getReference();

        questions = new String[]{
                getString(R.string.question1),
                getString(R.string.question2),
                getString(R.string.question3),
                getString(R.string.question4),
                getString(R.string.question5),
                getString(R.string.question6),
                getString(R.string.question7)
        };
        items = new ArrayList<String>();
        items.add("Hello! Please give correct answers!");
        items.add(questions[flag]);


        recyclerView = (RecyclerView)findViewById(R.id.msgs_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
         customAdapter = new QuestionsAdapter(QuestionsActivity.this, items);
        recyclerView.setAdapter(customAdapter);


        ans = new int[7];
        for (int i = 0; i < 7; i++)
            ans[i] = 0;


    Button back = (Button)findViewById(R.id.bot_back_button);
    back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });
    }

    public void yes_clicked(View view){
        recyclerView.smoothScrollToPosition(customAdapter.getItemCount());

        items.add("yes");
        customAdapter.notifyItemInserted(items.size() -1);
        ans[flag] =1;
        flag++;


        if(flag == 7){
            all_questions_asked();
        }
        else{
            items.add(questions[flag]);
            customAdapter.notifyItemInserted(items.size() -1);

        }

    }
    public void no_clicked(View view){
        recyclerView.smoothScrollToPosition(customAdapter.getItemCount());

        items.add("no");
        customAdapter.notifyItemInserted(items.size() -1);
        ans[flag] =2;
        flag++;

        if(flag == 7){
            all_questions_asked();
        }
        else{
            items.add(questions[flag]);
            customAdapter.notifyItemInserted(items.size() -1);

        }
    }

    public void  all_questions_asked(){
        Button b1 = (Button)findViewById(R.id.st_bot_yes);
        Button b2 = (Button)findViewById(R.id.st_bot_no);
        Button b3 = (Button)findViewById(R.id.bot_back_button);
        b1.setVisibility(View.GONE);
        b2.setVisibility(View.GONE);
        b3.setVisibility(View.VISIBLE);

        //calculating result
        String sb ="" ;
        boolean val = true;
        String to_push = "";
        for (int i = 0; i < ans.length; i++) {
            to_push += ans[i] + " ";
        }
        ref.child("user_symptom_data").push().setValue(to_push);

        boolean ARI = false;
        if (ans[2] == 2) {
            val = false;
        } else if (ans[4] == 2) {
            if (ans[3] == 2) {
                val = false;
            } else if (ans[3] == 1) {
                ARI = true;
            }
        } else if (ans[4] == 1)
            ARI = true;
        if (ARI) {
            if (ans[0] == 1) {
                val = true;
            } else if (ans[1] == 1) {
                val = true;
            } else if (ans[5] == 1 && ans[6] == 1) {
                val = false;
            } else if (ans[5] == 1 && ans[6] == 2) {
                val = true;
            }
        }

        if (val) {
            sb+=(getString(R.string.covid_is_present));
        } else {
            sb+=(getString(R.string.covid_is_not_present));
        }

        items.add(sb);
        customAdapter.notifyItemInserted(items.size() -1);
        ans=new int[7];
        //tv2.setText(QuestionsActivity.myName);

    }

}
