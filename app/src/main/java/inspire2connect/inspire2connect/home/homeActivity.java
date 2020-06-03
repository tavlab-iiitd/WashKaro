package inspire2connect.inspire2connect.home;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import inspire2connect.inspire2connect.satyaChat.ChatActivity;
import inspire2connect.inspire2connect.symptomTracker.QuestionsActivity;
import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.about.aboutActivity;
import inspire2connect.inspire2connect.contactTracer.ContactTracerActivity;
import inspire2connect.inspire2connect.survey.maleFemaleActivity;
import inspire2connect.inspire2connect.news.onAIrActivity;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;
import inspire2connect.inspire2connect.utils.urlActivity;

public class homeActivity extends BaseActivity implements View.OnClickListener {

    private static final int MY_REQUEST_CODE = 2399;
    ConstraintLayout[] ll_but = new ConstraintLayout[10];
    ImageButton flip_left, flip_right;
    Animation anim1, anim2, anim3, anim4;
//    TextView mohfw_data1
    TextView mohfw_data2, mohfw_data3, mohfw_data4, mohfw_data5;
    ConstraintLayout[] statLayouts = new ConstraintLayout[4];
    LayoutInflater inflater;
    float downX, downY, upX, upY;
    private ViewFlipper viewFlipper;
    private List<Infographics> slideLists;

    public void update_handle() {
        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, AppUpdateType.IMMEDIATE, homeActivity.this, MY_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }

                } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, AppUpdateType.FLEXIBLE, homeActivity.this, MY_REQUEST_CODE);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);
        update_handle();
        initialize_view_flipper();

        slideLists = new ArrayList<>();
        slideLists = new ArrayList<>();
//        ll_but[0] = findViewById(R.id.advisories_tile);
//        ll_but[1] = findViewById(R.id.symptom_tracker_tile);
        ll_but[0] = findViewById(R.id.contact_tracer_tile);
        ll_but[1] = findViewById(R.id.onair_tile);
        ll_but[2] = findViewById(R.id.guidelines_tile);
        ll_but[3] = findViewById(R.id.myth_busters_tile);

        int[] btnToAdd = new int[]{0, 1, 2, 3};

        for (int i = 0; i < btnToAdd.length; i++) {
            ll_but[btnToAdd[i]].setOnClickListener(this);
        }

        mohfw_data2 = findViewById(R.id.mohfw_data2);
        mohfw_data2 = findViewById(R.id.mohfw_data2);
        mohfw_data3 = findViewById(R.id.mohfw_data3);
        mohfw_data4 = findViewById(R.id.mohfw_data4);
        mohfw_data5 = findViewById(R.id.mohfw_data5);

        statLayouts[0] = findViewById(R.id.mohfw_ll2);
        statLayouts[1] = findViewById(R.id.mohfw_ll3);
        statLayouts[2] = findViewById(R.id.mohfw_ll4);
        statLayouts[3] = findViewById(R.id.mohfw_ll5);

        for (int i=0; i<statLayouts.length; i++) {
            statLayouts[i].setOnClickListener(this);
        }

        anim1 = AnimationUtils.loadAnimation(this, R.anim.anim1);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.anim2);
        anim3 = AnimationUtils.loadAnimation(this, R.anim.anim3);
        anim4 = AnimationUtils.loadAnimation(this, R.anim.anim4);
        flip_left = findViewById(R.id.flipperLeft);
        flip_right = findViewById(R.id.flipperRight);

        inflater = (LayoutInflater) homeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        flip_left.setOnClickListener(this);
        flip_right.setOnClickListener(this);

        flipper_single_tap();
        fetchset_MOHFW_data();

        setInfographicFlipper();

    }

    private void initialize_view_flipper() {
        viewFlipper = findViewById(R.id.viewFlipper_slide_show);
        viewFlipper.removeAllViews();
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.loading_image);
        viewFlipper.addView(imageView);
    }

    private void usingFirebaseImages(List<Infographics> slideLists) {
        for (int i = 0; i < slideLists.size(); i++) {
            String downloadImageUrl = slideLists.get(i).InfoURL;
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

    @Override
    public void onClick(View view) {
        Intent i = null;
        switch (view.getId()){
            case R.id.flipperLeft:
                viewFlipper.stopFlipping();
                viewFlipper.setInAnimation(anim1);
                viewFlipper.setOutAnimation(anim4);
                viewFlipper.showNext();
                break;
            case R.id.flipperRight:
                viewFlipper.stopFlipping();
                viewFlipper.setInAnimation(anim2);
                viewFlipper.setOutAnimation(anim3);
                viewFlipper.showPrevious();
                break;
//            case R.id.mohfw_ll2:
//                i = new Intent(this, urlActivity.class);
//                i.putExtra("url", getString(R.string.covid_map));
//                i.putExtra("name", getString(R.string.india_covid_map_tile));
//                startActivity(i);
//                break;
//            case R.id.mohfw_ll3:
//                i = new Intent(this, urlActivity.class);
//                i.putExtra("url", getString(R.string.covid_map));
//                i.putExtra("name", getString(R.string.india_covid_map_tile));
//                startActivity(i);
//                break;
//            case R.id.mohfw_ll4:
//                i = new Intent(this, urlActivity.class);
//                i.putExtra("url", getString(R.string.covid_map));
//                i.putExtra("name", getString(R.string.india_covid_map_tile));
//                startActivity(i);
//                break;
//            case R.id.mohfw_ll5:
//                i = new Intent(this, urlActivity.class);
//                i.putExtra("url", getString(R.string.covid_map));
//                i.putExtra("name", getString(R.string.india_covid_map_tile));
//                startActivity(i);
//                break;
            case R.id.contact_tracer_tile:
                i = new Intent(homeActivity.this, ContactTracerActivity.class);
                startActivity(i);
                break;
            case R.id.onair_tile:
                i = new Intent(homeActivity.this, onAIrActivity.class);
                startActivity(i);
                break;
            case R.id.guidelines_tile:
                i = getGuidelinesIntent(this);
                startActivity(i);
                break;
            case R.id.myth_busters_tile:
                i = getMythIntent(this);
                startActivity(i);
                break;
            default:
                i = null;
                break;
        }

//        if (view == flip_right) {
//            viewFlipper.stopFlipping();
//            viewFlipper.setInAnimation(anim2);
//            viewFlipper.setOutAnimation(anim3);
//            viewFlipper.showPrevious();
//        }
//        if (view == flip_left) {
//            viewFlipper.stopFlipping();
//            viewFlipper.setInAnimation(anim1);
//            viewFlipper.setOutAnimation(anim4);
//            viewFlipper.showNext();
//        }

//        Intent i = null;

//        for(int z=0; z<statLayouts.length; z++) {
//            if(view == statLayouts[z]) {
//                i = new Intent(this, urlActivity.class);
//                i.putExtra("url", getString(R.string.covid_map));
//                i.putExtra("name", getString(R.string.india_covid_map_tile));
//            }
//        }




//        if (view == ll_but[0]) {
//            i = getGovernmentIntent(this);
//        } else if (view == ll_but[1]) {
//            i = new Intent(homeActivity.this, QuestionsActivity.class);
//        } else
//        if (view == ll_but[0]) {
//            i = new Intent(homeActivity.this, ContactTracerActivity.class);
//        } else  if (view == ll_but[1]) {
//            i = new Intent(homeActivity.this, onAIrActivity.class);
//        }else if (view == ll_but[2]) {
//            i = new Intent(homeActivity.this, selectChatBotActivity.class);
//        }else if (view == ll_but[3]) {
//            i = new Intent(homeActivity.this, selectMiscActivity.class);
//        }
//
//        if(i!=null) {
//            startActivity(i);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        Drawable drawable = menu.findItem(R.id.lang_togg_butt).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this,R.color.app_pink));
        menu.findItem(R.id.lang_togg_butt).setIcon(drawable);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = null;
        int id = item.getItemId();
        switch (id){
            case R.id.lang_togg_butt:
                toggleLang(this);
                break;
            case R.id.Survey:
                i = new Intent(homeActivity.this, maleFemaleActivity.class);
                startActivity(i);
                break;
            case R.id.developers:
                i = new Intent(homeActivity.this, aboutActivity.class);
                startActivity(i);
                break;
            case R.id.privacy_policy:
                openPrivacyPolicy(this);
                break;
            default:
                startActivity(i);
                break;
        }

//        if (id == R.id.lang_togg_butt) {
//            toggleLang(this);
//        } else if (id == R.id.Survey) {
//            Intent i = new Intent(homeActivity.this, maleFemaleActivity.class);
//            startActivity(i);
//        } else if (id == R.id.developers) {
//            Intent i = new Intent(homeActivity.this, aboutActivity.class);
//            startActivity(i);
//        } else if (id == R.id.privacy_policy) {
//            openPrivacyPolicy(this);
//        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    public void setInfographicFlipper() {
        initialize_view_flipper();

        infographicReference.child(getCurLangKey().toLowerCase())
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            slideLists.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                Infographics graphic = snapshot.getValue(Infographics.class);

                                slideLists.add(graphic);
                            }
                            viewFlipper.removeAllViews();
                            usingFirebaseImages(slideLists);
                        } else {
                            Toast.makeText(homeActivity.this, "No images in firebase", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(homeActivity.this, "NO images found \n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

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
                            onFlipperClicked();
                        }

                        return true;
                }
                return false;
            }
        });
    }

    public void onFlipperClicked() {

        int i = viewFlipper.indexOfChild(viewFlipper.getCurrentView());

        String url = slideLists.get(i).InfoURL;
        Intent intnt = new Intent(homeActivity.this, InfographicsActivity.class);
        intnt.putExtra("image", url);
        startActivity(intnt);
    }

    public void fetchset_MOHFW_data() {

        statsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Stats stats = dataSnapshot.getValue(Stats.class);
//                mohfw_data1.setText(stats.Airport);
                mohfw_data2.setText(stats.Active);
                mohfw_data3.setText(stats.Discharged);
                mohfw_data4.setText(stats.Deaths);
                mohfw_data5.setText(stats.Migrated);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}