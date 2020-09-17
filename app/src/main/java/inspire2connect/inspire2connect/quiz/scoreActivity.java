package inspire2connect.inspire2connect.quiz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.home.homeActivity;
import inspire2connect.inspire2connect.utils.BaseActivity;

public class scoreActivity extends BaseActivity {
    private TextView score;
    private int[] user_selections;
    private Button done, solutions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_score);


        score = findViewById(R.id.sa_score);
        done = findViewById(R.id.sa_done);
        solutions = findViewById(R.id.button_view_solutions);

        String score_str = getIntent().getStringExtra("SCORE");
        user_selections = getIntent ().getIntArrayExtra ( "SELECTED_OPTIONS" );
        ArrayList<questionObject> questions = (ArrayList<questionObject>) getIntent().getSerializableExtra("QUESTIONS");
//        ArrayList<questionObject> questions = quizActivity.selected_questions
        score.setText(score_str);

//      Firebase Analytics
        Bundle bundle1 = new Bundle();
        bundle1.putString("UID", firebaseUser.getUid());
        bundle1.putString("QuizStatus", "Quiz Completed");
        bundle1.putString("QuizScore", score_str);
        FirebaseAnalytics.getInstance(this).logEvent("QuizStatus", bundle1);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable ( Color.TRANSPARENT));


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(scoreActivity.this, homeActivity.class);
                startActivity(intent);
                finish();

            }
        });

        solutions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(scoreActivity.this, quizSolutionsActivity.class);
//                intent.putExtra("QUESTIONS", questions);
                startActivity(intent);
            }
        });

    }
}
