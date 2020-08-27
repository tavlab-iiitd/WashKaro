package inspire2connect.inspire2connect.quiz;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;

public class quizActivity extends BaseActivity implements View.OnClickListener {
    private static final int MY_REQUEST_CODE = 2399;
    TextView question, option1_text, option2_text, option3_text, option4_text, qCount, timer;
    ConstraintLayout[] options = new ConstraintLayout[4];
    public static final String TAG = "QuizActivity";
    public static final String seen_questions_tag = "QuestionsShown";
    public ArrayList<questionObject> result;
    public ArrayList<questionObject> selected_questions;
    public ArrayList<questionObject> seen_questions;
    DatabaseReference databaseReference;
    private Dialog loadingDialog;
    private CountDownTimer countDown;
    private int quesNum;
    private int score;
    private boolean setDate;
    private SharedPreferences mPrefs;

    private static SharedPreferences.Editor editor;


    public void update_handle() {
        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, AppUpdateType.IMMEDIATE, quizActivity.this, MY_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }

                } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, AppUpdateType.FLEXIBLE, quizActivity.this, MY_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Loge("UPDATE_STATUS", "Update flow failed! Result code: " + resultCode);
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }


    private void setUpdates() {
        quizReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                result = new ArrayList<>();

                if(dataSnapshot.exists()) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        questionObject obj;
                        obj = new questionObject(Objects.requireNonNull(snapshot.child("question_" + getCurLang()).getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("option1_" + getCurLang()).getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("option2_" + getCurLang()).getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("option3_" + getCurLang()).getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("option4_" + getCurLang()).getValue()).toString(),
                                Integer.parseInt(Objects.requireNonNull(snapshot.child("answer").getValue()).toString()),
                                Objects.requireNonNull(snapshot.child("explanation_" + getCurLang()).getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("correct_attempts").getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("total_attempts").getValue()).toString(),
                                snapshot.getKey(),
                                Integer.parseInt(Objects.requireNonNull(snapshot.child("key").getValue()).toString()));

                        result.add(new questionObject(obj.question,
                                obj.option1, obj.option2, obj.option3, obj.option4, obj.answer, obj.explanation, obj.correct_attempts, obj.total_attempts, obj.id, obj.key));
                    }
                    selectQuestionSet(result);

                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setQuestion() {
        timer.setText(String.valueOf(30));

        question.setText(selected_questions.get(0).getQuestion());
        option1_text.setText(selected_questions.get(0).getOption1());
        option2_text.setText(selected_questions.get(0).getOption2());
        option3_text.setText(selected_questions.get(0).getOption3());
        option4_text.setText(selected_questions.get(0).getOption4());

        qCount.setText(String.valueOf(1) + "/" + String.valueOf(selected_questions.size()));

        startTimer();

        quesNum = 0;
    }

    private void startTimer() {
        countDown = new CountDownTimer(32000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished < 30000)
                    timer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                changeQuestion();
            }
        };

        countDown.start();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.quiz_activity);

        question = findViewById(R.id.question_text);
        qCount = findViewById(R.id.quest_num);
        timer = findViewById(R.id.countdown);

        option1_text = findViewById(R.id.option_1_text);
        option2_text = findViewById(R.id.option_2_text);
        option3_text = findViewById(R.id.option_3_text);
        option4_text = findViewById(R.id.option_4_text);

        options[0] = findViewById(R.id.option_1_tile);
        options[0].setOnClickListener(this);
        options[1] = findViewById(R.id.option_2_tile);
        options[1].setOnClickListener(this);
        options[2] = findViewById(R.id.option_3_tile);
        options[2].setOnClickListener(this);
        options[3] = findViewById(R.id.option_4_tile);
        options[3].setOnClickListener(this);

        loadingDialog = new Dialog(quizActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        setDate = false;



        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        mPrefs = getPreferences(MODE_PRIVATE);
        editor = mPrefs.edit();

        Intent i = getIntent();

        String type = i.getStringExtra(TYPE);
        String date = i.getStringExtra(DATE);


        switch (date) {
            case DATE_YES:
                setDate = true;
                break;
            default:
                setDate = false;
                break;
        }

        switch (type) {
            case UPDATES:
                databaseReference = governmentReference;
                getSupportActionBar().setTitle(R.string.govt_updates_act);
                break;
            case GUIDELINES:
                databaseReference = guidelinesReference;
                getSupportActionBar().setTitle(R.string.guidelines_act);
                break;
            case MYTH:
                databaseReference = mythReference;
                getSupportActionBar().setTitle(R.string.myth_act);
                break;
            case FAQ:
                databaseReference = faqsReference;
                getSupportActionBar().setTitle(R.string.faqs_tile);
                break;
            case SUCCESS_STORIES:
                databaseReference = successStoriesReference;
                getSupportActionBar().setTitle(R.string.success_stories_tile);
                break;
            case TWEETS:
                databaseReference = tweetsReference;
                getSupportActionBar().setTitle(R.string.social_media_title);
                break;
            case QUIZ:
                databaseReference = quizReference;
                getSupportActionBar().setTitle(getString(R.string.quiz_title));
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                break;
            default:
                Logv(TAG, "Invalid Intent");
                break;
        }

//        result = new ArrayList<>();
//        result.add(new questionObject ("No Question Available", "No Option Available","No Option Available","No Option Available","No Option Available",0,"No Data Available","No Data Available","No Data Available", "No Data Available",0));

        setUpdates();

        score = 0;

    }

    @Override
    public void onClick(View view) {

        int selectedOption = 0;

        switch (view.getId()) {

            case R.id.option_1_tile:
                selectedOption = 1;
                break;
            case R.id.option_2_tile:
                selectedOption = 2;
                break;
            case R.id.option_3_tile:
                selectedOption = 3;
                break;
            case R.id.option_4_tile:
                selectedOption = 4;
                break;

            default:

        }
        countDown.cancel();
        checkAnswer(selectedOption, view);
    }

    private void checkAnswer(int selectedOption, View view) {

        if (selectedOption == selected_questions.get(quesNum).getAnswer()) {
            //Right Answer
            (view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            quizReference.child(selected_questions.get(quesNum).getId()).child("correct_attempts").setValue(String.valueOf(Integer.valueOf(selected_questions.get(quesNum).getCorrect_attempts()) + 1));
            quizReference.child(selected_questions.get(quesNum).getId()).child("total_attempts").setValue(String.valueOf(Integer.valueOf(selected_questions.get(quesNum).getTotal_attempts()) + 1));
            score++;

        } else {
            //Wrong Answer
            (view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            quizReference.child(selected_questions.get(quesNum).getId()).child("total_attempts").setValue(String.valueOf(Integer.valueOf(selected_questions.get(quesNum).getTotal_attempts()) + 1));

            switch (selected_questions.get(quesNum).getAnswer()) {
                case 1:
                    options[0].setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 2:
                    options[1].setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 3:
                    options[2].setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 4:
                    options[3].setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;

            }

        }

        Handler handler = new Handler();
        handler.postDelayed(() -> changeQuestion(), 2000);


    }

    private void changeQuestion() {
        if (quesNum < selected_questions.size() - 1) {
            quesNum++;

            playAnim(question, 0, 0);
            playAnim(option1_text, 0, 1);
            playAnim(option2_text, 0, 2);
            playAnim(option3_text, 0, 3);
            playAnim(option4_text, 0, 4);

            qCount.setText(String.valueOf(quesNum + 1) + "/" + String.valueOf(selected_questions.size()));

            timer.setText(String.valueOf(30));
            startTimer();

        } else {
            // Go to Score Activity
            Intent intent = new Intent(quizActivity.this, scoreActivity.class);
            intent.putExtra("SCORE", String.valueOf(score) + "/" + String.valueOf(selected_questions.size()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }


    }

    private void playAnim(final View view, final int value, final int viewNum) {

        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (value == 0) {
                            switch (viewNum) {
                                case 0:
                                    ((TextView) view).setText(selected_questions.get(quesNum).getQuestion());
                                    break;
                                case 1:
                                    ((TextView) view).setText(selected_questions.get(quesNum).getOption1());
                                    break;
                                case 2:
                                    ((TextView) view).setText(selected_questions.get(quesNum).getOption2());
                                    break;
                                case 3:
                                    ((TextView) view).setText(selected_questions.get(quesNum).getOption3());
                                    break;
                                case 4:
                                    ((TextView) view).setText(selected_questions.get(quesNum).getOption4());
                                    break;

                            }


                            if (viewNum != 0)
                                (view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E99C03")));


                            playAnim(view, 1, viewNum);

                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        countDown.cancel();
    }


    public void selectQuestionSet(ArrayList<questionObject> result) {

        try {
            selected_questions.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(result);
        for (questionObject object : result) {

                if (((selected_questions == null) ? 0 : selected_questions.size()) < 5 && checkUsage(object.key) && checkLimit()) {
                    //display the question
                    //append key to stored list
                    selected_questions.add(object);
                    seen_questions.add(object);
                    addInJSONArray ( object );

                } else if (selected_questions.size() < 5 && !checkLimit()) {
                    seen_questions.clear();
                    selectQuestionSet(result);
                }

                if (selected_questions.size() == 5) {
                    break;
                }





        }

        setQuestion();
    }

    public boolean checkUsage(int key) {
        //Check whether key already used or not here
        List<questionObject> arrayList = getList ();
        try{
            for (questionObject object : arrayList) {
                if (object.key == key) {
                    return false;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean checkLimit() {
        List<questionObject> arrayList = getList ();
        try{
            if (arrayList.size() > 95) {
                return false;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return true;
    }

    private void addInJSONArray(questionObject productToAdd){

        Gson gson = new Gson();

        String jsonSaved = mPrefs.getString(seen_questions_tag, "");
        String jsonNewproductToAdd = gson.toJson(productToAdd);

        JSONArray jsonArrayProduct= new JSONArray();

        try {
            if(jsonSaved.length()!=0){
                jsonArrayProduct = new JSONArray(jsonSaved);
            }
            jsonArrayProduct.put(new JSONObject (jsonNewproductToAdd));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //SAVE NEW ARRAY
        editor.putString(seen_questions_tag, gson.toJson(jsonArrayProduct));
        editor.commit();
    }

    public List<questionObject> getList(){
        List<questionObject> arrayItems = null;
        String serializedObject = mPrefs.getString(seen_questions_tag, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<questionObject>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

}
