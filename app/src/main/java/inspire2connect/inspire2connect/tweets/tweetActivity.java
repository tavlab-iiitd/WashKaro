package inspire2connect.inspire2connect.tweets;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.about.aboutActivity;
import inspire2connect.inspire2connect.home.Infographics;
import inspire2connect.inspire2connect.home.InfographicsActivity;
import inspire2connect.inspire2connect.home.Stats;
import inspire2connect.inspire2connect.home.homeActivity;
import inspire2connect.inspire2connect.mythGuidelineUpdates.UpdatesAdapter;
import inspire2connect.inspire2connect.mythGuidelineUpdates.guidelineViewActivity;
import inspire2connect.inspire2connect.mythGuidelineUpdates.guidelinesObject;
import inspire2connect.inspire2connect.mythGuidelineUpdates.updateObject;
import inspire2connect.inspire2connect.satyaChat.ChatActivity;
import inspire2connect.inspire2connect.survey.maleFemaleActivity;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;
import inspire2connect.inspire2connect.utils.urlActivity;

public class tweetActivity extends BaseActivity implements TextToSpeech.OnInitListener, View.OnClickListener {
    private static final int MY_REQUEST_CODE = 2399;
    ConstraintLayout[] ll_but = new ConstraintLayout[10];
    ImageButton flip_left, flip_right;
    Animation anim1, anim2, anim3, anim4;
    TextView mohfw_data2, mohfw_data3, mohfw_data4, mohfw_data5;
    TextView mohfw_tv2, mohfw_tv3, mohfw_tv4, mohfw_tv5;
    TextView mohfw_currency2, mohfw_currency3, mohfw_currency4, mohfw_currency5;
    ConstraintLayout[] statLayouts = new ConstraintLayout[4];
    LayoutInflater inflater;
    float downX, downY, upX, upY;
    private ViewFlipper viewFlipper;
    private List<tweetInfographics> slideLists;

    public void update_handle() {
        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo> () {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, AppUpdateType.IMMEDIATE, tweetActivity.this, MY_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }

                } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, AppUpdateType.FLEXIBLE, tweetActivity.this, MY_REQUEST_CODE);
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

    public static final String TAG = "TweetActivity";
    public ArrayList<tweetObject> result;
    DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private tweetRecyclerViewAdapter mAdapter;
    private TextToSpeech tts;
    private boolean setDate;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    private void setUpdates() {
        tweetsReference.child("tweets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                result = new ArrayList<>();
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    count += 1;
                    tweetObject obj = new tweetObject(snapshot.child("tweet_"+ getCurLang()).getValue().toString(),snapshot.child("url").getValue().toString(),Integer.toString(count) );

                    result.add(new tweetObject (obj.text,
                            obj.source,Integer.toString(count)));
                }
                populate_recycler_view(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void populate_recycler_view(ArrayList<tweetObject> result) {
        mAdapter = new tweetRecyclerViewAdapter (this, result);
        mAdapter.setTTS(tts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator ());
        recyclerView.addItemDecoration(new DividerItemDecoration (this, 0));

        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_analysis);
        update_handle();
        initialize_view_flipper();

        slideLists = new ArrayList<>();
        ll_but[0] = findViewById(R.id.tweet_data_tile1);
        ll_but[1] = findViewById(R.id.tweet_data_tile2);
        ll_but[2] = findViewById(R.id.tweet_data_tile3);
        ll_but[3] = findViewById(R.id.tweet_data_tile4);


        int[] btnToAdd = new int[]{0, 1, 2, 3};

        for (int i = 0; i < btnToAdd.length; i++) {
            ll_but[btnToAdd[i]].setOnClickListener(this);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mohfw_tv2 = findViewById(R.id.tweet_title1);
        mohfw_tv3 = findViewById(R.id.tweet_title2);
        mohfw_tv4 = findViewById(R.id.tweet_title3);
        mohfw_tv5 = findViewById(R.id.tweet_title4);

        mohfw_data2 = findViewById(R.id.tweet_data1);
        mohfw_data3 = findViewById(R.id.tweet_data2);
        mohfw_data4 = findViewById(R.id.tweet_data3);
        mohfw_data5 = findViewById(R.id.tweet_data4);

        mohfw_currency2 = findViewById(R.id.tweet_Currencydata1);
        mohfw_currency3 = findViewById(R.id.tweet_Currencydata2);
        mohfw_currency4 = findViewById(R.id.tweet_Currencydata3);
        mohfw_currency5 = findViewById(R.id.tweet_Currencydata4);

        anim1 = AnimationUtils.loadAnimation(this, R.anim.anim1);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.anim2);
        anim3 = AnimationUtils.loadAnimation(this, R.anim.anim3);
        anim4 = AnimationUtils.loadAnimation(this, R.anim.anim4);
        flip_left = findViewById(R.id.tweetFlipperLeft);
        flip_right = findViewById(R.id.tweetFlipperRight);

        inflater = (LayoutInflater) tweetActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        flip_left.setOnClickListener(this);
        flip_right.setOnClickListener(this);

        flipper_single_tap();
        fetchset_facts ();

        setInfographicFlipper();

        setDate = false;

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
                getSupportActionBar().setTitle( R.string.social_media_title);
                break;
            default:
                Logv(TAG, "Invalid Intent");
                break;
        }

        result = new ArrayList<>();
        result.add(new tweetObject ("No Tweet Available", "No Tweet","1"));
        mAdapter = new tweetRecyclerViewAdapter(this, result);
        recyclerView = findViewById(R.id.tweets_recycler_view);

        setUpdates();
    }


    public void flipper_single_tap() {
        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = motionEvent.getX();
                        downY = motionEvent.getY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        upX = motionEvent.getX();
                        upY = motionEvent.getY();
                        float deltaX = downX - upX;
                        float deltaY = downY - upY;
                        if (deltaX == 0 && deltaY == 0) {
                            try {
                                onFlipperClicked();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        return true;
                }
                return false;
            }
        });
    }

    public void onFlipperClicked() throws Exception {

        int i = viewFlipper.indexOfChild(viewFlipper.getCurrentView());

        String url = slideLists.get(i).tweetURL;
        String code = slideLists.get(i).tweetCode;
        Intent intnt = new Intent(tweetActivity.this, tweetInfoActivity.class);

        // Firebase Analytics
        Bundle bundle = new Bundle();
        bundle.putString("UID", firebaseUser.getUid());
        bundle.putString("Tweet_Code", code);
        firebaseAnalytics.logEvent("Infographic_Selected", bundle);

        intnt.putExtra("image", url);
        startActivity(intnt);
    }

    private void initialize_view_flipper() {
        viewFlipper = findViewById(R.id.tweetViewFlipper_slide_show);
        viewFlipper.removeAllViews();
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.loading_image);
        viewFlipper.addView(imageView);
    }
    private void usingFirebaseImages(List<tweetInfographics> slideLists) {
        for (int i = 0; i < slideLists.size(); i++) {
            String downloadImageUrl = slideLists.get(i).tweetURL;
            flipImages(downloadImageUrl);
        }
    }

    public void flipImages(String imageUrl) {
        ImageView imageView = new ImageView(this);
        Picasso.get().load(imageUrl).into(imageView);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(3500);
        viewFlipper.setAutoStart(true);
        viewFlipper.setDisplayedChild(0);
        viewFlipper.startFlipping();
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }

    public void fetchset_facts() {

        tweetsReference.child ("facts").child(getCurLangKey().toLowerCase()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Tweets tweets = dataSnapshot.getValue (Tweets.class );

                mohfw_tv2.setText ( tweets.Fact1 );
                mohfw_tv3.setText ( tweets.Fact2 );
                mohfw_tv4.setText ( tweets.Fact3 );
                mohfw_tv5.setText ( tweets.Fact4 );

                mohfw_data2.setText ( tweets.Fact1_value );
                mohfw_data3.setText ( tweets.Fact2_value );
                mohfw_data4.setText ( tweets.Fact3_value );
                mohfw_data5.setText ( tweets.Fact4_value );

                mohfw_currency2.setText ( tweets.Fact1_currency );
                mohfw_currency3.setText ( tweets.Fact2_currency );
                mohfw_currency4.setText ( tweets.Fact3_currency );
                mohfw_currency5.setText ( tweets.Fact4_currency );

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }



    public void setInfographicFlipper() {
        initialize_view_flipper();

        tweetsReference.child ("images").child(getCurLangKey().toLowerCase())
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            slideLists.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                tweetInfographics graphic = snapshot.getValue(tweetInfographics.class);

                                slideLists.add(graphic);
                            }
                            viewFlipper.removeAllViews();
                            usingFirebaseImages(slideLists);
                        } else {
                            Toast.makeText(tweetActivity.this, "No images in firebase", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(tweetActivity.this, "NO images found \n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        tts = new TextToSpeech(this, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
//            button
        } else {
            Loge(TAG, "Can't speak");
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i = null;
        switch (id){
            case R.id.lang_togg_butt:
                // Firebase Analytics
                Bundle bundle = new Bundle();
                bundle.putString("UID", firebaseUser.getUid());
                if(Locale.getDefault().getLanguage().equals("en"))
                    bundle.putString("Current_Language", "Hindi");
                else if(Locale.getDefault().getLanguage().equals("hi"))
                    bundle.putString("Current_Language", "English");

                bundle.putString("Language_Change_Activity", "Tweet Activity");
                firebaseAnalytics.logEvent("Language_Toggle", bundle);

                toggleLang(this);
                break;
            case R.id.Survey:
                i = new Intent( tweetActivity.this, maleFemaleActivity.class);
                startActivity(i);
                break;
            case R.id.developers:
                i = new Intent(tweetActivity.this, aboutActivity.class);
                startActivity(i);
                break;
            case R.id.privacy_policy:
                openPrivacyPolicy(this);
                break;
            default:
                i = null;
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setOnItemClickListener( (position, v) -> {
            Intent i = new Intent(tweetActivity.this, urlActivity.class);
            ArrayList<tweetObject> result_from_adapter = mAdapter.getResult();
            i.putExtra("url", result_from_adapter.get(position).getSource ());
            i.putExtra("name", getString(R.string.information_link ));
            startActivity(i);
        } );
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        Intent i = null;

        switch (view.getId()){
            case R.id.tweetFlipperLeft:
                viewFlipper.stopFlipping();
                viewFlipper.setInAnimation(anim1);
                viewFlipper.setOutAnimation(anim4);
                viewFlipper.showNext();
                //Firebase Analytics
                Bundle bundle = new Bundle();
                bundle.putString("UID", firebaseUser.getUid());
                bundle.putString("InfographicScroll", "Scrolled Left");
                firebaseAnalytics.logEvent("ScrollingInfographics", bundle);
                break;
            case R.id.tweetFlipperRight:
                viewFlipper.stopFlipping();
                viewFlipper.setInAnimation(anim2);
                viewFlipper.setOutAnimation(anim3);
                viewFlipper.showPrevious();
                //Firebase Analytics
                Bundle bundle2 = new Bundle();
                bundle2.putString("UID", firebaseUser.getUid());
                bundle2.putString("InfographicScroll", "Scrolled Right");
                firebaseAnalytics.logEvent("ScrollingInfographics", bundle2);
                break;

            default:
                i = null;
                break;
        }
    }
}

