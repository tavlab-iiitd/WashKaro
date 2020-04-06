package inspire2connect.inspire2connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
//import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.common.hash.HashingOutputStream;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import inspire2connect.inspire2connect.contactTracer.MainActivity;


public class Home_Activity extends AppCompatActivity implements View.OnClickListener {

    //    AdapterViewFlipper adapterViewFlipper;
//    FirebaseStorage firebaseStorage;
    DatabaseReference dRef;
    //    DatabaseReference d;
    private ViewFlipper viewFlipper;
    private DatabaseReference databaseReference;
    private List<SlideModel> slideLists;
    ConstraintLayout ll_but[] = new ConstraintLayout[10];
    ImageButton img_but[]=new ImageButton[10];
    ConstraintLayout ll_but1, ll_but2, ll_but3, ll_but4, ll_but5, ll_but6, ll_but7, ll_but8,ll_but9,ll_but10;
    int curr_lang = 2; //1 for eng , 2 for hindi
    String intentLangExtra = "hindi";
    //    DatabaseReference dref;
    ImageButton flip_left, flip_right;
    Animation anim_in, anim_out, anim1, anim2, anim3, anim4;
    TextView corona_helpline, live_data, mohfw_data1, mohfw_data2, mohfw_data3, mohfw_data4, mohfw_data5,
            mohfw_tv1, mohfw_tv2, mohfw_tv3, mohfw_tv4, mohfw_tv5;
    StorageReference storageReference;
    RelativeLayout data_tile;
    LayoutInflater inflater;
    LinearLayout layout;
    float downX, downY, upX, upY;

    public int w = 0, h = 0;
    private static final int MY_REQUEST_CODE = 2399;
    private PopupWindow p_window;
    String TAG = "MainActivity";
    //View flipper Zoom Variables.......................................................


    public void update_handle() {
        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, AppUpdateType.IMMEDIATE, Home_Activity.this, MY_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }

                } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, AppUpdateType.FLEXIBLE, Home_Activity.this, MY_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.e("UPDATE_STATUS", "Update flow failed! Result code: " + resultCode);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);
        dRef = FirebaseDatabase.getInstance().getReference().child("Infographic");
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        update_handle();
        initialize_view_flipper();

        slideLists = new ArrayList<>();
        slideLists = new ArrayList<>();
        ll_but[0] = findViewById(R.id.img_but_lay1);
        ll_but[1] = findViewById(R.id.img_but_lay2);
        ll_but[2] = findViewById(R.id.img_but_lay3);
        ll_but[3] = findViewById(R.id.img_but_lay4);
        ll_but[4]=findViewById(R.id.img_but_lay5);
        ll_but[5] = findViewById(R.id.img_but_lay6);
//        ll_but[6] = findViewById(R.id.img_but_lay7);
//        ll_but[7] = findViewById(R.id.img_but_lay8);
//        ll_but[8] = findViewById(R.id.img_but_lay9);
//        ll_but[9] = findViewById(R.id.img_but_lay10);


//        img_but[0] = findViewById(R.id.image_button1);
//        img_but[1] = findViewById(R.id.image_button2);
//        img_but[2] = findViewById(R.id.image_button3);
//        img_but[3] = findViewById(R.id.image_button4);
//        ll_but[4]=findViewById(R.id.img_but_lay5);
//        img_but[5] = findViewById(R.id.image_button6);
//        img_but[6] = findViewById(R.id.image_button7);
//        img_but[7] = findViewById(R.id.image_button8);
//        img_but[8] = findViewById(R.id.image_button9);
//        img_but[9] = findViewById(R.id.image_button10);

        int[] btnToAdd = new int[]{0, 1, 2, 3, 4,5};

        for (int i = 0; i < btnToAdd.length; i++)
        {
            //img_but[btnToAdd[i]].setOnClickListener(this);
            ll_but[btnToAdd[i]].setOnClickListener(this);
        }

        mohfw_data1 = (TextView) findViewById(R.id.mohfw_data1);
        mohfw_data2 = (TextView) findViewById(R.id.mohfw_data2);
        mohfw_data3 = (TextView) findViewById(R.id.mohfw_data3);
        mohfw_data4 = (TextView) findViewById(R.id.mohfw_data4);
        mohfw_data5 = (TextView) findViewById(R.id.mohfw_data5);
        mohfw_tv1 = (TextView) findViewById(R.id.mohfw_tv1);
        mohfw_tv2 = (TextView) findViewById(R.id.mohfw_tv2);
        mohfw_tv3 = (TextView) findViewById(R.id.mohfw_tv3);
        mohfw_tv4 = (TextView) findViewById(R.id.mohfw_tv4);
        mohfw_tv5 = (TextView) findViewById(R.id.mohfw_tv5);
        data_tile = (RelativeLayout) findViewById(R.id.data_tile);

        anim1 = AnimationUtils.loadAnimation(this, R.anim.anim1);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.anim2);
        anim3 = AnimationUtils.loadAnimation(this, R.anim.anim3);
        anim4 = AnimationUtils.loadAnimation(this, R.anim.anim4);
        flip_left = (ImageButton) findViewById(R.id.flipperLeft);
        flip_right = (ImageButton) findViewById(R.id.flipperRight);
        switchLang();
//        corona_helpline = (TextView)findViewById(R.id.Corona_helpline_text);
//        live_data=(TextView)findViewById(R.id.state_helpline_text);

        inflater = (LayoutInflater) Home_Activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //layout = inflater.inflate(R.layout.zoom_in, null);


        flip_left.setOnClickListener(this);
        flip_right.setOnClickListener(this);
//        corona_helpline.setOnClickListener(this);

        flipper_single_tap();
        fetchset_MOHFW_data();


    }

    @Override
    protected void onStart() {
        super.onStart();
        //usingFirebaseDatabase();
    }

    private void initialize_view_flipper() {
        viewFlipper = findViewById(R.id.viewFlipper_slide_show);
        viewFlipper.removeAllViews();
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.loading_image);
        viewFlipper.addView(imageView);
    }

    private void usingFirebaseDatabase() {
        databaseReference.child("Infographic").child("Hindi")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (dataSnapshot.exists()) {
                            slideLists.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                SlideModel model = snapshot.getValue(SlideModel.class);

                                Long sno = Long.parseLong(snapshot.child("Sno").getValue().toString());
                                String imageUrl = snapshot.child("InfoURL").getValue(String.class);
                                model.setImageUrl(imageUrl);
                                model.setName(sno);

                                slideLists.add(model);
                            }
                            //Toast.makeText(Home_Activity.this, "All data fetched", Toast.LENGTH_SHORT).show();
                            viewFlipper.removeAllViews();
                            usingFirebaseImages(slideLists);
                        } else {
                            Toast.makeText(Home_Activity.this, "No images in firebase", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Home_Activity.this, "NO images found \n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void usingFirebaseImages(List<SlideModel> slideLists) {
        for (int i = 0; i < slideLists.size(); i++) {
            String downloadImageUrl = slideLists.get(i).getImageUrl();
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
        if (view == flip_left) {
            viewFlipper.stopFlipping();
            viewFlipper.setInAnimation(anim2);
            viewFlipper.setOutAnimation(anim3);
            viewFlipper.showPrevious();
        }
        if (view == flip_right) {
            viewFlipper.stopFlipping();
            viewFlipper.setInAnimation(anim1);
            viewFlipper.setOutAnimation(anim4);
            viewFlipper.showNext();
        }

        if (view == ll_but[0] )
        {
            Intent i = new Intent(Home_Activity.this,Government_Updates.class);
            i.putExtra("Language", intentLangExtra);
            //Toast.makeText(Home_Activity.this,"Button Clicked 1 ",Toast.LENGTH_SHORT).show();
            startActivity(i);
        }
        if (view == ll_but[1] ) {
            Intent i = new Intent(Home_Activity.this,symptom_activity.class);
            i.putExtra("Language", intentLangExtra);
            startActivity(i);
        }
        if (view == ll_but[2]) {
            //Put contact tracer here
            Intent i = new Intent(Home_Activity.this, MainActivity.class);
            i.putExtra("Language", intentLangExtra);
            startActivity(i);
        }
        if (view == ll_but[3] ) {
            Intent i = new Intent(Home_Activity.this, CardViewActivity.class);
            i.putExtra("Language", intentLangExtra);
            startActivity(i);
        }
        if (view == ll_but[4] ) {
            Intent i = new Intent(Home_Activity.this,select_chatbot_activity.class);
            startActivity(i);
        }
        if (view == ll_but[5] ) {
            Intent i = new Intent(Home_Activity.this,select_misc_activity.class);
            startActivity(i);

        }


//        if (view == ll_but[6] ) {
//            String nmbr = "+919013151515";
//            openWhatsapp(nmbr);
//        }
//        if (view == ll_but[7]  || view==img_but[7]) {
//            String nmbr = "+41798931892";
//            openWhatsapp(nmbr);
//        }
//        if(view==ll_but[8]  || view==img_but[8])
//        {
//            Intent i = new Intent(Home_Activity.this,CardViewActivity.class);
//            i.putExtra("Language", intentLangExtra);
//            startActivity(i);
//        }
//        if(view==ll_but[9]  || view==img_but[9])
//        {
//            Intent i = new Intent(Home_Activity.this,symptom_activity.class);
//            i.putExtra("Language", intentLangExtra);
//            startActivity(i);
//        }
//        if (view == corona_helpline) {
//            Intent callintent = new Intent(Intent.ACTION_DIAL);
//            callintent.setData(Uri.parse("tel:" + corona_helpline.getText().toString()));
//            startActivity(callintent);
//        }
    }

    private void openWhatsapp(String nmbr) {
        String url = "https://api.whatsapp.com/send?phone=" + nmbr;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.lang_togg_butt) {
            Toast.makeText(Home_Activity.this, "Language Changed", Toast.LENGTH_SHORT).show();
            switch_language();

        } else if (id == R.id.Survey) {
            Intent i = new Intent(Home_Activity.this, Male_Female.class);
            if (curr_lang == 2)
                i.putExtra("Language", "hindi");
            else
                i.putExtra("Language", "english");
            startActivity(i);
        } else if (id == R.id.developers) {
            Intent i = new Intent(Home_Activity.this, about.class);
            startActivity(i);
        }
        else if(id==R.id.privacy_policy)
        {
            Intent i=new Intent(Home_Activity.this,privacy_policy.class);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }

    public void switch_language() {
        if (curr_lang == 1) {
            curr_lang = 2;
            intentLangExtra = "hindi";
        } else {
            curr_lang = 1;
            intentLangExtra = "english";
        }
        switchLang();
    }

    public void switchLang() {

        if (curr_lang == 1) {
            //get_english_live_data();
            TextView t1 = (TextView) findViewById(R.id.imgbut_text1);
            TextView t2 = (TextView) findViewById(R.id.imgbut_text2);
            TextView t3 = (TextView) findViewById(R.id.imgbut_text3);
            TextView t4 = (TextView) findViewById(R.id.imgbut_text4);
            TextView t5 = (TextView)findViewById(R.id.imgbut_text5);
            TextView t6 = (TextView) findViewById(R.id.imgbut_text6);
            //TextView t7 = (TextView)findViewById(R.id.imgbut_text5);
//            TextView t9 = (TextView) findViewById(R.id.imgbut_text9);
//            TextView t10 = (TextView) findViewById(R.id.imgbut_text10);

            t1.setText("Government Advisories");
            t2.setText("Symptom Tracker");
            t3.setText("Contact Tracer");
            t4.setText("AI News");
            t5.setText("Chatbots");
            t6.setText("Miscellaneous");
//            t9.setText("News");
////            t10.setText("Symptom Tracker");
            //t7.setText("News");
            mohfw_tv1.setText("Passesgers screened at airport");
            mohfw_tv2.setText("Active COVID19\n Cases");
            mohfw_tv3.setText("Cured/Discharged Cases");
            mohfw_tv4.setText("Death \nCases");
            mohfw_tv5.setText("Migrated\nPatient");
            setEnglishFlipper();
        } else {
            //get_hindi_live_data();
            TextView t1 = (TextView) findViewById(R.id.imgbut_text1);
            TextView t2 = (TextView) findViewById(R.id.imgbut_text2);
            TextView t3 = (TextView) findViewById(R.id.imgbut_text3);
            TextView t4 = (TextView) findViewById(R.id.imgbut_text4);
            TextView t5 = (TextView)findViewById(R.id.imgbut_text5);
            TextView t6 = (TextView) findViewById(R.id.imgbut_text6);
//            TextView t9 = (TextView) findViewById(R.id.imgbut_text9);
//            TextView t10 = (TextView) findViewById(R.id.imgbut_text10);
            t1.setText("सरकारी निर्देश");
            t2.setText("लक्षण ट्रैकर");
            t3.setText("Contact Tracer");
            t4.setText("समाचार");
            t5.setText("Chatbots");
            t6.setText("Miscellaneous");
//            t9.setText("समाचार");
//            t10.setText("लक्षण ट्रैकर");
            //t6.setText("MapMyIndia लाइव ट्रैकर");
            mohfw_tv1.setText("हवाई अड्डे पर यात्री जांच");
            mohfw_tv2.setText("सक्रिय COVID19 रोगी");
            mohfw_tv3.setText("कुल ठीक व्यक्ति ");
            mohfw_tv4.setText("कुल मौत");
            mohfw_tv5.setText("प्रव्रजनित व्यक्ति");
            usingFirebaseDatabase();
        }
    }

    public void setEnglishFlipper() {
        initialize_view_flipper();

        databaseReference.child("Infographic").child("English")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            slideLists.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                SlideModel model = snapshot.getValue(SlideModel.class);

                                Long sno = Long.parseLong(snapshot.child("Sno").getValue().toString());
                                String imageUrl = snapshot.child("InfoURL").getValue(String.class);
                                model.setImageUrl(imageUrl);
                                model.setName(sno);
                                slideLists.add(model);
                            }
                            //Toast.makeText(Home_Activity.this, "All data fetched", Toast.LENGTH_SHORT).show();
                            viewFlipper.removeAllViews();
                            usingFirebaseImages(slideLists);
                        } else {
                            Toast.makeText(Home_Activity.this, "No images in firebase", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Home_Activity.this, "NO images found \n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void flipper_single_tap()
    {
        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        downX = motionEvent.getX();
                        downY = motionEvent.getY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        upX = motionEvent.getX();
                        upY = motionEvent.getY();
                        float deltaX = downX - upX;
                        float deltaY = downY - upY;
                        if (deltaX == 0 && deltaY == 0)
                        {
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

        String url = slideLists.get(i).getImageUrl();
        Intent intnt = new Intent(Home_Activity.this,Infographics.class);
        intnt.putExtra("image",url);
        startActivity(intnt);
    }

        //call new infographics activity here

//        viewFlipper.stopFlipping();
//        int i = viewFlipper.indexOfChild(viewFlipper.getCurrentView());
//        Log.d("Zoomintest", "Index of child" + Integer.toString(i));
//        String url = slideLists.get(i).getImageUrl();
//        Log.d("Zoomintest", "Url of child" + url);
//        Picasso.get().load(url).into(zoo_image);
//
//        zoo_image.setBackground(new ColorDrawable(Color.TRANSPARENT));
//
//        // Log.d("Zoomintest",Integer.toString(w)+" "+Integer.toString(h));
//        PopupWindow window = new PopupWindow(layout, 800, 1400, true);
//
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        window.setOutsideTouchable(true);
//        window.setElevation(60);
//
//        window.showAtLocation(layout, Gravity.CENTER, 10, 10);

//


    public void fetchset_MOHFW_data() {

        databaseReference.child("Mohfw").child("Data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String active_cases = dataSnapshot.child("Active").getValue().toString();
                String airport_check = dataSnapshot.child("Airport").getValue().toString();
                String noof_deaths = dataSnapshot.child("Deaths").getValue().toString();
                String noof_discharged = dataSnapshot.child("Discharged").getValue().toString();
                String noof_migrated = dataSnapshot.child("Migrated").getValue().toString();
                mohfw_data1.setText(airport_check);
                mohfw_data2.setText(active_cases);
                mohfw_data3.setText(noof_discharged);
                mohfw_data4.setText(noof_deaths);
                mohfw_data5.setText(noof_migrated);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }



}
//    private void populateViewFlipper() {
//        Toast.makeText(Home_Activity.this,"PLease wait",Toast.LENGTH_LONG).show();
//        adapterViewFlipper = (AdapterViewFlipper)findViewById(R.id.adapterViewFlipper);
//        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),names, images);
//        adapterViewFlipper.setAdapter(customAdapter);
//        adapterViewFlipper.setFlipInterval(3000);
//        adapterViewFlipper.setAutoStart(true);
//        Toast.makeText(Home_Activity.this,"Done",Toast.LENGTH_SHORT).show();
//    }

//    public void init_pie_chart(){
//        pieChartView = findViewById(R.id.chart);
//        pieData.add(new SliceValue(15, Color.BLUE).setLabel("France:"+"180"));
//        pieData.add(new SliceValue(25, Color.GRAY).setLabel("China"+"1200"));
//        pieData.add(new SliceValue(10, Color.RED).setLabel("India:"+"150"));
//        pieData.add(new SliceValue(60, Color.MAGENTA).setLabel("Italy:"+"6000"));
//
//        PieChartData pieChartData = new PieChartData(pieData);
//
//        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
//        pieChartData.setHasCenterCircle(true).setCenterText1("Cases of COVID19").setCenterText1FontSize(16);
//        //pieChartData.setHasCenterCircle(true).setCenterText1("Sales in million").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
//        pieChartView.setPieChartData(pieChartData);
//    }
