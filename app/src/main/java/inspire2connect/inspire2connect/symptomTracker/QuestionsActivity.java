package inspire2connect.inspire2connect.symptomTracker;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;

public class QuestionsActivity extends BaseActivity implements View.OnClickListener {
    public static int[] ans;
    public ListView questionsListView;
    ArrayList<String> items;
    String[] questions;
    int flag = 0;
    QuestionsAdapter questionsAdapter;
    DatabaseReference ref;
    Button yesBT;
    Button noBT;
    Button actBT;
    private Context context;
    private boolean isAllQuestionsAsked;

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
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.symptom_tracker);
        }

        context = this;
        isAllQuestionsAsked = false;

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

        yesBT = findViewById(R.id.st_bot_yes);
        noBT = findViewById(R.id.st_bot_no);
        actBT = findViewById(R.id.bot_back_button);

        actBT.setOnClickListener(this);
        yesBT.setOnClickListener(this);
        noBT.setOnClickListener(this);

        items = new ArrayList<>();
        questionsListView = findViewById(R.id.msgs_recycler);
        questionsAdapter = new QuestionsAdapter(QuestionsActivity.this, items);
        questionsListView.setAdapter(questionsAdapter);

        items.add("Hello! Please give correct answers!");
        items.add(questions[flag]);

        updateListView();

        ans = new int[7];
        for (int i = 0; i < 7; i++)
            ans[i] = 0;
    }

    private void updateListView() {
        questionsAdapter.notifyDataSetChanged();
        final View v1 = questionsListView.getChildAt(questionsListView.getLastVisiblePosition() - questionsListView.getFirstVisiblePosition() - 1);
        final View v2 = questionsListView.getChildAt(questionsListView.getLastVisiblePosition() - questionsListView.getFirstVisiblePosition());
        final Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.chat_item_add_anim);
        final Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.chat_item_add_anim);
        if (v1 != null) {
            v1.startAnimation(animation1);
        }
        if (v2 != null) {
            v2.setVisibility(View.GONE);
        }

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (v2 != null) {
                    v2.setVisibility(View.VISIBLE);
                    v2.startAnimation(animation2);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (isAllQuestionsAsked) {
                    yesBT.setVisibility(View.GONE);
                    noBT.setVisibility(View.GONE);
                    actBT.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    public void yes_clicked() {

        items.add(getString(R.string.yes));
        ans[flag] = 1;
        flag++;
        updateListView();

        if (flag == 7) {
            all_questions_asked();
        } else {
            items.add(questions[flag]);
            updateListView();
        }

    }

    public void no_clicked() {

        items.add(getString(R.string.no));
        ans[flag] = 2;
        flag++;
        updateListView();

        if (flag == 7) {
            all_questions_asked();
        } else {
            items.add(questions[flag]);
            updateListView();
        }
    }

    public void all_questions_asked() {

        isAllQuestionsAsked = true;

        String sb = "";
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
            sb += (getString(R.string.covid_is_present));
        } else {
            sb += (getString(R.string.covid_is_not_present));
        }

        items.add(sb);
        updateListView();
        ans = new int[7];
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.st_bot_yes:
                yes_clicked();
                break;
            case R.id.st_bot_no:
                no_clicked();
                break;
            case R.id.bot_back_button:
                finish();
                break;
            default:
                break;
        }
    }
}
