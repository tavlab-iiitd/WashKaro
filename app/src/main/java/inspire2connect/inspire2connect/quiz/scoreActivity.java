package inspire2connect.inspire2connect.quiz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.home.homeActivity;
import inspire2connect.inspire2connect.utils.BaseActivity;

public class scoreActivity extends BaseActivity {
    private TextView score;
    private Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_score);

        score = findViewById(R.id.sa_score);
        done = findViewById(R.id.sa_done);

        String score_str = getIntent().getStringExtra("SCORE");
        score.setText(score_str);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable ( Color.TRANSPARENT));


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(scoreActivity.this, homeActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }
}
