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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.List;
import java.util.Objects;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.TinyDB;
import inspire2connect.inspire2connect.home.homeActivity;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;

public class quizActivity extends BaseActivity implements View.OnClickListener {
    private static final int MY_REQUEST_CODE = 2399;
    TextView question, option1_text, option2_text, option3_text, option4_text, qCount;
    ConstraintLayout[] options = new ConstraintLayout[4];
    public static final String TAG = "QuizActivity";
    public static final String seen_questions_tag = "QuestionsShown";
    public ArrayList<questionObject> result;
    public static final ArrayList<questionObject> selected_questions = new ArrayList<>();
    public ArrayList<questionObject> seen_questions;
    DatabaseReference databaseReference;
    private Dialog loadingDialog;
//    private CountDownTimer countDown;
    private int quesNum;
    private int score;
    public static int[] selected_options;
    private boolean setDate;
    String currentUserID;



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
        quizReference.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                result = new ArrayList<>();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

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

                                Integer.parseInt(Objects.requireNonNull(snapshot.child("key").getValue()).toString()),
                                Objects.requireNonNull ( (snapshot.child("source").getValue ()).toString () ));

                        result.add(new questionObject(obj.question,
                                obj.option1, obj.option2, obj.option3, obj.option4, obj.answer, obj.explanation, obj.correct_attempts, obj.total_attempts, obj.id, obj.key, obj.source));

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
//        timer.setText(String.valueOf(30));

        question.setText(selected_questions.get(0).getQuestion());
        option1_text.setText(selected_questions.get(0).getOption1());
        option2_text.setText(selected_questions.get(0).getOption2());
        option3_text.setText(selected_questions.get(0).getOption3());
        option4_text.setText(selected_questions.get(0).getOption4());

        qCount.setText(String.valueOf(1) + "/" + String.valueOf(selected_questions.size()));



//        startTimer();

    }

//    private void startTimer() {
//        countDown = new CountDownTimer(32000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                if (millisUntilFinished < 30000)
//                    timer.setText(String.valueOf(millisUntilFinished / 1000));
//            }
//
//            @Override
//            public void onFinish() {
//                changeQuestion();
//            }
//
//
//        };
//
//        countDown.start();
//
//    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        countDown.start();
//    }
//
//    //When activity is destroyed then this will cancel the timer
//    @Override
//    protected void onStop() {
//        super.onStop();
//        countDown.cancel();
//    }
//
//    //This will pause the time
//    @Override
//    protected void onPause() {
//        super.onPause();
//        countDown.cancel();
//    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.quiz_activity);

        seen_questions = new ArrayList<>();
//        selected_questions = new ArrayList<>();
        result = new ArrayList<>();
        selected_options = new int[5];


        question = findViewById(R.id.question_text);
        qCount = findViewById(R.id.quest_num);
//        timer = findViewById(R.id.countdown);

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

        quesNum = 0;


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


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


        //Firebase Analytics
        if(firebaseUser != null) {
            currentUserID = firebaseUser.getUid();
        }
        Bundle bundle = new Bundle();
        bundle.putString("UID", currentUserID);
        bundle.putString("Screen", "Quiz Activity");
        FirebaseAnalytics.getInstance(this).logEvent("CurrentScreen", bundle);

        Bundle bundle2 = new Bundle();
        bundle2.putString("UID", currentUserID);
        bundle2.putString("Quiz", "Quiz Started");
        FirebaseAnalytics.getInstance(this).logEvent("QuizStatus", bundle2);

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
//        countDown.cancel();
        selected_options[quesNum] = selectedOption;
        checkAnswer(selectedOption, view);
    }

    private void checkAnswer(int selectedOption, View view) {

        if (selectedOption == selected_questions.get(quesNum).getAnswer()) {
            //Right Answer
            (view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            quizReference.child(selected_questions.get(quesNum).getId()).child("correct_attempts").setValue(String.valueOf(Integer.valueOf(selected_questions.get(quesNum).getCorrect_attempts()) + 1));
            quizReference.child(selected_questions.get(quesNum).getId()).child("total_attempts").setValue(String.valueOf(Integer.valueOf(selected_questions.get(quesNum).getTotal_attempts()) + 1));
            score++;
            correctDialog();

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

            wrongDialog ();

        }


    }

    private void changeQuestion() {

        resetColor ();

//      Firebase Analytics
        Bundle bundle1 = new Bundle();
        bundle1.putString("UID", currentUserID);
        bundle1.putString("QuizQuestionStatus", "Q" + quesNum + " Attempted");
        FirebaseAnalytics.getInstance(this).logEvent("QuizStatus", bundle1);

        if (quesNum < selected_questions.size() - 1) {

            quesNum++;

            playAnim(question, 0, 0);
            playAnim(option1_text, 0, 1);
            playAnim(option2_text, 0, 2);
            playAnim(option3_text, 0, 3);
            playAnim(option4_text, 0, 4);

            qCount.setText(String.valueOf(quesNum + 1) + "/" + String.valueOf(selected_questions.size()));

//            timer.setText(String.valueOf(30));
//            startTimer();

        } else {
            // Go to Score Activity
            Intent intent = new Intent(quizActivity.this, scoreActivity.class);
            intent.putExtra("SCORE", String.valueOf(score) + "/" + String.valueOf(selected_questions.size()));
//            intent.putExtra("QUESTIONS", selected_questions);
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

    public void resetColor() {
        options[0].setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        options[1].setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        options[2].setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        options[3].setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, homeActivity.class);
        startActivity(intent);
        finish();
//        countDown.cancel();
    }

    public void wrongDialog() {
        final Dialog dialogWrong = new Dialog(quizActivity.this);
        dialogWrong.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialogWrong.getWindow() != null) {
            ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
            dialogWrong.getWindow().setBackgroundDrawable(colorDrawable);
        }
        dialogWrong.setContentView(R.layout.dialog_wrong);
        dialogWrong.setCancelable(false);
        dialogWrong.show();

        //Since the dialog is show to user just pause the timer in background
//        onPause();


//      Firebase Analytics
        Bundle bundle1 = new Bundle();
        bundle1.putString("UID", currentUserID);
        bundle1.putString("QuizAnswerStatus", "Wrong Answer");
        FirebaseAnalytics.getInstance(this).logEvent("QuizStatus", bundle1);

        TextView wrongText = (TextView) dialogWrong.findViewById(R.id.wrongText);
        Button buttonNext = (Button) dialogWrong.findViewById(R.id.dialogNext);

        //OnCLick listener to go next que
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This will dismiss the dialog
                dialogWrong.dismiss();

                //reset the color of buttons back to white
                resetColor();

                //Change question
                changeQuestion();


            }
        });
    }

    public void correctDialog() {
        final Dialog dialogCorrect = new Dialog(quizActivity.this);
        dialogCorrect.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialogCorrect.getWindow() != null) {
            ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
            dialogCorrect.getWindow().setBackgroundDrawable(colorDrawable);
        }
        dialogCorrect.setContentView(R.layout.dialog_correct);
        dialogCorrect.setCancelable(false);
        dialogCorrect.show();

        //Since the dialog is show to user just pause the timer in background
//        onPause();


//      Firebase Analytics
        Bundle bundle1 = new Bundle();
        bundle1.putString("UID", currentUserID);
        bundle1.putString("QuizAnswerStatus", "Correct Answer");
        FirebaseAnalytics.getInstance(this).logEvent("QuizStatus", bundle1);

        TextView correctText = (TextView) dialogCorrect.findViewById(R.id.correctText);
        Button buttonNext = (Button) dialogCorrect.findViewById(R.id.dialogNext);

        //OnCLick listener to go next que
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This will dismiss the dialog
                dialogCorrect.dismiss();

                //reset the color of buttons back to white
                resetColor();

                //it will increment the question number
                changeQuestion();


            }
        });
    }


    public void selectQuestionSet(ArrayList<questionObject> result) {

        Collections.sort(result);


        try {
            selected_questions.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }


        for (questionObject object : result) {

            if (selected_questions.isEmpty() && checkLimit() && checkUsage(object.key)) {

                selected_questions.add(object);
                seen_questions.add(object);
                addInJSONArray(seen_questions);

            } else if (selected_questions.size() < 5 && checkUsage(object.key) && checkLimit()) {

                selected_questions.add(object);
                seen_questions.add(object);
                addInJSONArray(seen_questions);

            } else if (selected_questions.size() < 5 && !checkLimit()) {
                seen_questions.clear();
//                selectQuestionSet(result);
            } else if (selected_questions.size() == 5) {
                break;
            }

        }
        setQuestion();
    }

    public boolean checkUsage(int key) {
        //Check whether key already used or not here
        List<questionObject> arrayList = getList();
        try {
            for (questionObject object : arrayList) {
                if (object.key == key) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean checkLimit() {
        List<questionObject> arrayList = getList();
        try {
            if (arrayList.size() > 95) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private void addInJSONArray(ArrayList<questionObject> questions_seen) {


        Gson gson = new Gson();

        TinyDB tinydb = new TinyDB(this);

        tinydb.checkForNullKey( seen_questions_tag );

        ArrayList<String> objStrings = new ArrayList<String>();

        for(questionObject question: questions_seen){
            objStrings.add(gson.toJson(question));
        }

        tinydb.putListString(seen_questions_tag, objStrings);
    }

    public ArrayList<questionObject> getList() {

        Gson gson = new Gson();

        TinyDB tinydb = new TinyDB(this);

        ArrayList<String> objStrings = tinydb.getListString(seen_questions_tag);
        ArrayList<questionObject> questionList =  new ArrayList<questionObject>();

        for(String jObjString : objStrings){
            questionObject question  = gson.fromJson(jObjString,  questionObject.class);
            questionList.add(question);
        }
        return questionList;
    }

}
