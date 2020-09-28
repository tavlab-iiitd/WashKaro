package inspire2connect.inspire2connect.quiz;

import androidx.appcompat.app.AppCompatActivity;
import inspire2connect.inspire2connect.R;

import inspire2connect.inspire2connect.tweets.tweetActivity;
import inspire2connect.inspire2connect.tweets.tweetObject;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.urlActivity;

import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class quizSolutionsActivity extends BaseActivity {

    ListView listView;
    ArrayList<questionObject> questions;
    questionAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_solutions);
        setStatusBarGradiant(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ArrayList<questionObject> questions = quizActivity.selected_questions;



                listView = findViewById(R.id.list);
        adapter = new questionAdapter(this, questions);
        listView.setAdapter(adapter);

    }

}