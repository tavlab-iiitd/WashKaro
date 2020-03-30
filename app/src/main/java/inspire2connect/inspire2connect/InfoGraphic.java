package inspire2connect.inspire2connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class InfoGraphic extends AppCompatActivity {

    TextView mythbusters, Guidlines, dailyUpdates;
    ToggleButton langToggleButton;
    Button gotoNews;
    DatabaseReference dref;
    ScrollView parentScrollView;
    ScrollView childScrollView1;
    ScrollView childScrollView2;
    ScrollView childScrollView3;
    private float downX, downY, upX, upY;
    ViewFlipper vf;
    static final int MIN_DISTANCE = 30;
    Animation anim_in, anim_out, anim1, anim2, anim3, anim4;
    LinearLayout daily_updates_layout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_info_graphic);


        parentScrollView = (ScrollView) findViewById(R.id.parent_scroll);
        childScrollView1 = (ScrollView) findViewById(R.id.child1);
        childScrollView2 = (ScrollView) findViewById(R.id.child2);
        childScrollView3 = (ScrollView) findViewById(R.id.child3);
        //daily_updates_layout=(LinearLayout)findViewById(R.id.daily_updates_layout);
        mythbusters = (TextView) findViewById(R.id.mythbusters);
        Guidlines = (TextView) findViewById(R.id.guidlines);
        dailyUpdates = (TextView) findViewById(R.id.dailyupdates);
        langToggleButton = (ToggleButton) findViewById(R.id.langtogg);
        vf = (ViewFlipper) findViewById(R.id.info_grap_flipper);
        anim_in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        anim_out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        anim1 = AnimationUtils.loadAnimation(this, R.anim.anim1);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.anim2);
        anim3 = AnimationUtils.loadAnimation(this, R.anim.anim3);
        anim4 = AnimationUtils.loadAnimation(this, R.anim.anim4);


        parentScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("scroll_test", "PARENT TOUCH");

                findViewById(R.id.child1).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                findViewById(R.id.child2).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                findViewById(R.id.child3).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        childScrollView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("scroll_test", "CHILD TOUCH1");

                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        childScrollView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("scroll_test", "CHILD TOUCH2");

                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        childScrollView3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("scroll_test", "CHILD TOUCH3");

                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;


            }
        });

        setViewFlipper();


        setGuidelinesHindi();
        setMythBustersHindi();
        setDailyUpdatesEng();


        //toggle button listner
        langToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //set to hindi
                    setGuidelinesEng();
                    setMythBustersEng();
                    Toast.makeText(InfoGraphic.this, "English selected", Toast.LENGTH_SHORT).show();

                    //headlines change

                    TextView temp = (TextView) findViewById(R.id.guid_text);
                    temp.setText("Guidelines");
                    temp = (TextView) findViewById(R.id.myth_text);
                    temp.setText("Mythbusters");
                    temp = (TextView) findViewById(R.id.daily_text);
                    temp.setText("Daily Updates");

                } else {

                    setGuidelinesHindi();
                    setMythBustersHindi();
                    Toast.makeText(InfoGraphic.this, "Hindi selected", Toast.LENGTH_SHORT).show();
                    //set to english

                    TextView temp = (TextView) findViewById(R.id.guid_text);
                    temp.setText("दिशा निर्देश");
                    temp = (TextView) findViewById(R.id.myth_text);
                    temp.setText("मिथक");
                    temp = (TextView) findViewById(R.id.daily_text);
                    temp.setText("दैनिक नवीनतम जानकारी");
                }

            }
        });


        onGotoNewsistner();
        vf.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                //view.getParent().requestDisallowInterceptTouchEvent(true);
                //return false;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        downX = event.getX();
                        downY = event.getY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        upX = event.getX();
                        upY = event.getY();
                        float deltaX = downX - upX;
                        float deltaY = downY - upY;

                        // swipe horizontal?
                        if (Math.abs(deltaX) > MIN_DISTANCE) {
                            // left or right
                            vf.stopFlipping();
                            if (deltaX < 0) {
                                onLeftToRightSwipe();
                                vf.startFlipping();
                                return true;
                            }
                            if (deltaX > 0) {
                                onRightToLeftSwipe();

                                return true;
                            }

                        } else {
                            if (Math.abs(deltaX) < 15) {
                                onClickEvent();
                            }
                            //Log.i(logTag, "Swipe was only " + Math.abs(deltaX)
                            //        + " long, need at least " + MIN_DISTANCE);
                        }

                        // swipe vertical?
//                        if (Math.abs(deltaY) > MIN_DISTANCE) {
//                            // top or down
//                            if (deltaY < 0) {
//                                onTopToBottomSwipe();
//                                return true;
//                            }
//                            if (deltaY > 0) {
//                                onBottomToTopSwipe();
//                                return true;
//                            }
//                        } else {
//                            Log.i("viewflipper", "Swipe was only " + Math.abs(deltaX)
//                                    + " long, need at least " + MIN_DISTANCE);
//                        }
                        return true;
                }

                return false;
            }
        });

        homeButtonListner();
        scrollUpListner();
        scrollDownListner();

    }

    private void setViewFlipper() {


        vf.setInAnimation(anim1);
        vf.setOutAnimation(anim4);
        vf.setFlipInterval(4000);
        vf.startFlipping();
    }


    public void onRightToLeftSwipe() {
        Log.i("viewflipper", "right to left swipe");
        vf.setInAnimation(anim2);
        vf.setOutAnimation(anim3);
        vf.showNext();
        vf.setInAnimation(anim1);
        vf.setOutAnimation(anim4);

    }

    public void onLeftToRightSwipe() {
        Log.i("viewflipper", "left to rigth swipe");
        vf.setInAnimation(anim1);
        vf.setOutAnimation(anim4);
        vf.showPrevious();
    }

    public void onTopToBottomSwipe() {
        Log.i("viewflipper", "onTopToBottomSwipe!");
        vf.setInAnimation(anim2);
        vf.setOutAnimation(anim3);
        vf.showNext();
        vf.setInAnimation(anim1);
        vf.setOutAnimation(anim4);
        // activity.doSomething();
    }

    public void onBottomToTopSwipe() {
        Log.i("viewflipper", "onBottomToTopSwipe!");
        vf.setInAnimation(anim1);
        vf.setOutAnimation(anim4);
        vf.showPrevious();
        // activity.doSomething();
    }

    public void onClickEvent() {
        Log.i("viewflipper", "On clicked");
    }


    private void setMythBustersEng() {
        dref = FirebaseDatabase.getInstance().getReference().child("Coronavirus").child("Myth");

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String guidelnines = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String g_hindi = snapshot.child("Myth_en").getValue(String.class);
                    String sno = snapshot.child("Sno").getValue().toString();
                    if (sno.equals("1"))
                        guidelnines += sno + ". " + g_hindi;
                    else
                        guidelnines += "\n\n" + sno + ". " + g_hindi;
                }
                mythbusters.setText(guidelnines);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setMythBustersHindi() {
        dref = FirebaseDatabase.getInstance().getReference().child("Coronavirus").child("Myth");

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String guidelnines = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String g_hindi = snapshot.child("Myth_hin").getValue(String.class);
                    String sno = snapshot.child("Sno").getValue().toString();
                    if (sno.equals("1"))
                        guidelnines += sno + ". " + g_hindi;
                    else
                        guidelnines += "\n\n" + sno + ". " + g_hindi;
                }
                mythbusters.setText(guidelnines);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setGuidelinesHindi() {
        dref = FirebaseDatabase.getInstance().getReference().child("Coronavirus").child("guidelines");

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String guidelnines = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String g_hindi = snapshot.child("Guideline_hin").getValue(String.class);
                    String sno = snapshot.child("Sno").getValue().toString();
                    if (sno.equals("1"))
                        guidelnines += sno + ". " + g_hindi;
                    else
                        guidelnines += "\n\n" + sno + ". " + g_hindi;
                }
                Guidlines.setText(guidelnines);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setGuidelinesEng() {

        dref = FirebaseDatabase.getInstance().getReference().child("Coronavirus").child("guidelines");

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String guidelnines = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String g_eng = snapshot.child("Guideline_en").getValue(String.class);
                    String sno = snapshot.child("Sno").getValue().toString();
                    if (sno.equals("1"))
                        guidelnines += sno + ". " + g_eng;
                    else
                        guidelnines += "\n\n" + sno + ". " + g_eng;
                }
                Guidlines.setText(guidelnines);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void setDailyUpdatesEng() {
        dref = FirebaseDatabase.getInstance().getReference().child("Coronavirus").child("DailyUpdate");

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String guidelnines = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    String g = snapshot.child("Summary").getValue(String.class);
                    String g_url = snapshot.child("url").getValue(String.class);
                    String sno = snapshot.child("Sno").getValue().toString();
                    if (sno.equals("1"))
                        guidelnines += sno + ". " + g + "\n" + g_url;
                    else
                        guidelnines += "\n" + sno + ". " + g + "\n" + g_url;


                    //trying to create textview , for each update

//                    TextView txtview = new TextView(InfoGraphic.this);
//                    txtview.setText(guidelnines);
//                    txtview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                    daily_updates_layout.addView(txtview);
//
//                    TextView urlview = new TextView(InfoGraphic.this);
//                    urlview.setText(g_url);
//                    urlview.setTextColor(Color.rgb(0,0,255));
//                    //urlview.setMovementMethod(LinkMovementMethod.getInstance());
//
//                    urlview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                    daily_updates_layout.addView(urlview);


                }


                dailyUpdates.setText(guidelnines);
                //Linkify.addLinks(dailyUpdates, Linkify.WEB_URLS);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void onGotoNewsistner() {
        gotoNews = (Button) findViewById(R.id.gotonews);
        gotoNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoGraphic.this, CardViewActivity.class);
                startActivity(intent);
            }
        });
    }


    public void homeButtonListner() {
        ImageButton home = (ImageButton) findViewById(R.id.home_button);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gototwebsite
//                String url ="https://tavlab-iiitd.github.io/CoronaActionIndia";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);

                Intent i = new Intent(InfoGraphic.this, Home_Activity.class);
                startActivity(i);

            }
        });
    }

    public void scrollUpListner() {
        Button scrollup = (Button) findViewById(R.id.scroll_up);
        scrollup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childScrollView3.scrollTo((int) childScrollView3.getScrollX(), (int) childScrollView3.getScrollY() - 50);
            }
        });
    }

    public void scrollDownListner() {
        Button scrolldown = (Button) findViewById(R.id.scroll_down);
        scrolldown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childScrollView3.scrollTo((int) childScrollView3.getScrollX(), (int) childScrollView3.getScrollY() + 50);
            }
        });
    }

}
