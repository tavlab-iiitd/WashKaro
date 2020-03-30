package inspire2connect.inspire2connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterViewFlipper;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class Home_Activity extends AppCompatActivity {

    AdapterViewFlipper adapterViewFlipper;
    FirebaseStorage firebaseStorage;
    DatabaseReference dRef;
    DatabaseReference d;
    private ViewFlipper viewFlipper;
    private DatabaseReference databaseReference;
    private List<SlideModel> slideLists;
    LinearLayout ll_but1,ll_but2,ll_but3,ll_but4,ll_but5,ll_but6,ll_but7,ll_but8;
    int curr_lang =2; //1 for eng , 2 for hindi
    DatabaseReference dref;
    ImageButton flip_left,flip_right;
    Animation anim_in,anim_out,anim1,anim2,anim3,anim4;
    TextView corona_helpline,live_data;
    StorageReference storageReference;

    public void get_hindi_live_data()
    {
        d=FirebaseDatabase.getInstance().getReference();
        dref = FirebaseDatabase.getInstance().getReference().child("Mohfw");
        dref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String guidelnines ="";
                int count=0;
                for(DataSnapshot snapshot :dataSnapshot.getChildren())
                {
                    count+=1;
//                    String active=snapshot.child("Active").getValue().toString();
//                    String airport=snapshot.child("Airport").getValue().toString();
//                    String deaths=snapshot.child("Deaths").getValue().toString();
//                    String discharged=snapshot.child("Discharged").getValue().toString();
//                    String info=snapshot.child("Info").getValue().toString();
//                    String migrated=snapshot.child("Migrated").getValue().toString();

//                    String tts="Total number of passengers screened at airport : "+airport+"\n" +
//                            "\n" +
//                            "Total number of Active COVID 2019 cases across India * : "+active+"\n" +
//                            "\n" +
//                            "Total number of Discharged/Cured COVID 2019 cases across India * : "+discharged+"\n" +
//                            "\n" +
//                            "Total number of Migrated COVID-19 Patient * : "+migrated+"\n" +
//                            "\n" +
//                            "Total number of Deaths due to COVID 2019 across India * : "+deaths+"\n" +
//                            "\n" +
//                            info;
                    String tts=snapshot.child("complete_info_hindi").getValue().toString();
                    live_data.setText(tts);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void get_english_live_data()
    {
        d=FirebaseDatabase.getInstance().getReference();
        dref = FirebaseDatabase.getInstance().getReference().child("Mohfw");
        dref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String guidelnines ="";
                int count=0;
                for(DataSnapshot snapshot :dataSnapshot.getChildren())
                {
                    count+=1;
//                    String active=snapshot.child("Active").getValue().toString();
//                    String airport=snapshot.child("Airport").getValue().toString();
//                    String deaths=snapshot.child("Deaths").getValue().toString();
//                    String discharged=snapshot.child("Discharged").getValue().toString();
//                    String info=snapshot.child("Info").getValue().toString();
//                    String migrated=snapshot.child("Migrated").getValue().toString();

//                    String tts="Total number of passengers screened at airport : "+airport+"\n" +
//                            "\n" +
//                            "Total number of Active COVID 2019 cases across India * : "+active+"\n" +
//                            "\n" +
//                            "Total number of Discharged/Cured COVID 2019 cases across India * : "+discharged+"\n" +
//                            "\n" +
//                            "Total number of Migrated COVID-19 Patient * : "+migrated+"\n" +
//                            "\n" +
//                            "Total number of Deaths due to COVID 2019 across India * : "+deaths+"\n" +
//                            "\n" +
//                            info;
                    String tts=snapshot.child("complete_info").getValue().toString();
                    live_data.setText(tts);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);
        dRef = FirebaseDatabase.getInstance().getReference().child("Infographic");
        storageReference=FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        initialize_view_flipper();
        switch_to_hindi();
        slideLists = new ArrayList<>();
        ll_but1=(LinearLayout)findViewById(R.id.img_but_lay1);
        ll_but2=(LinearLayout)findViewById(R.id.img_but_lay2);
        ll_but3=(LinearLayout)findViewById(R.id.img_but_lay3);
        ll_but4=(LinearLayout)findViewById(R.id.img_but_lay4);
        ll_but5=(LinearLayout)findViewById(R.id.img_but_lay5);
        ll_but6=(LinearLayout)findViewById(R.id.img_but_lay6);
        ll_but7=(LinearLayout)findViewById(R.id.img_but_lay7);
        ll_but8=(LinearLayout)findViewById(R.id.img_but_lay8);
        anim1 = AnimationUtils.loadAnimation(this,R.anim.anim1);
        anim2 = AnimationUtils.loadAnimation(this,R.anim.anim2);
        anim3 = AnimationUtils.loadAnimation(this,R.anim.anim3);
        anim4 = AnimationUtils.loadAnimation(this,R.anim.anim4);
        flip_left=(ImageButton)findViewById(R.id.flipperLeft);
        flip_right = (ImageButton)findViewById(R.id.flipperRight);
        corona_helpline = (TextView)findViewById(R.id.Corona_helpline_text);
        live_data=(TextView)findViewById(R.id.state_helpline_text);
        //init_pie_chart();
        image_button_1_llistner();
        image_button_2_llistner();
        image_button_3_llistner();
        image_button_4_llistner();
        image_button_5_llistner();
        image_button_6_llistner();
        image_button_7_llistner();
        image_button_8_llistner();
        onFlipperLeftListner();
        onFlipperRightListner();
        onCoronaHelpListner();
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

    public void onFlipperLeftListner(){

        flip_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.stopFlipping();
                viewFlipper.setInAnimation(anim2);
                viewFlipper.setOutAnimation(anim3);
                viewFlipper.showPrevious();
            }
        });

    }

    public void onFlipperRightListner(){

        flip_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.stopFlipping();
                viewFlipper.setInAnimation(anim1);
                viewFlipper.setOutAnimation(anim4);
                viewFlipper.showNext();
            }
        });

    }



    public void image_button_1_llistner(){
        ll_but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img1_clicked(view);
            }
        });

    }



    public void image_button_2_llistner(){
        ll_but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img2_clicked(view);

            }
        });

    }
    public void image_button_3_llistner(){
        ll_but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               img3_clicked(view);

            }
        });

    }
    public void image_button_4_llistner(){

        ll_but4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             img4_clicked(view);

            }
        });
    }
    public void image_button_5_llistner(){

        ll_but5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img5_clicked(view);

            }
        });
    }
    public void image_button_6_llistner(){
        ll_but6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img6_clicked(view);

            }
        });

    }
    public void image_button_7_llistner(){
        ll_but7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img7_clicked(view);

            }
        });

    }

    public void image_button_8_llistner(){
        ll_but8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img8_clicked(view);

            }
        });

    }

    public void img1_clicked(View view){
        Intent i = new Intent(Home_Activity.this,daily_guidelines.class);

        //Toast.makeText(Home_Activity.this,"Button Clicked 1 ",Toast.LENGTH_SHORT).show();
        startActivity(i);
    }


    public void img2_clicked(View view){

        Intent i = new Intent(Home_Activity.this,Government_Updates.class);
        startActivity(i);
    }
    public void img3_clicked(View view){
        Intent i = new Intent(Home_Activity.this,Myths.class);
        startActivity(i);

    }
    public void img4_clicked(View view){
        Intent i = new Intent(Home_Activity.this,daily_updates.class);
        startActivity(i);
    }
    public void img5_clicked(View view){
        Intent i = new Intent(Home_Activity.this,CardViewActivity.class);
        startActivity(i);
    }
    public void img6_clicked(View view){

        //call map activity here
        Intent i = new Intent(Home_Activity.this,map_activity.class);
        startActivity(i);
    }
    public void img7_clicked(View view){

        //call bot activity here

        String number="+919013151515";
        String url="https://api.whatsapp.com/send?phone="+number;
        Intent i=new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void img8_clicked(View view){

        //call bot activity here
        String number="+41798931892";
        String url="https://api.whatsapp.com/send?phone="+number;
        Intent i=new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.app_bar_settings ){
            //toggle language
            Intent i = new Intent(Home_Activity.this,CardViewActivity.class);
            startActivity(i);
        }
        else if(id == R.id.lang_togg_butt){
            Toast.makeText(Home_Activity.this,"Language Changed",Toast.LENGTH_SHORT).show();
            switch_language();


        }
        else if(id == R.id.Survey){
            Intent i = new Intent(Home_Activity.this,Male_Female.class);
            startActivity(i);
        }
        else if(id == R.id.developers){
            Intent i = new Intent(Home_Activity.this,about.class);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }

    public void switch_language(){
        if(curr_lang ==1){
            curr_lang=2;
            switch_to_hindi();
        }
        else{
            curr_lang=1;
            switch_to_eng();
        }
    }

    public void switch_to_hindi(){

        get_hindi_live_data();
        TextView t1 = (TextView)findViewById(R.id.imgbut_text1);
        TextView t2 = (TextView)findViewById(R.id.imgbut_text2);
        TextView t3 = (TextView)findViewById(R.id.imgbut_text3);
        TextView t4 = (TextView)findViewById(R.id.imgbut_text4);
        TextView t7 = (TextView)findViewById(R.id.imgbut_text5);
        TextView t5 = (TextView)findViewById(R.id.helpline_text);
        TextView t6 =(TextView)findViewById(R.id.imgbut_text6);
        t1.setText("दिशा निर्देश");
        t2.setText("सरकारी निर्देश");
        t3.setText("मिथक");
        t4.setText("दैनिक नवीनतम जानकारी");
        t7.setText("समाचार");
        t5.setText("कोरोना केंद्रीय हेल्पलाइन");
        t6.setText("MapMyIndia लाइव ट्रैकर");
        usingFirebaseDatabase();
    }
    public  void switch_to_eng(){
        get_english_live_data();
        TextView t1 = (TextView)findViewById(R.id.imgbut_text1);
        TextView t2 = (TextView)findViewById(R.id.imgbut_text2);
        TextView t3 = (TextView)findViewById(R.id.imgbut_text3);
        TextView t4 = (TextView)findViewById(R.id.imgbut_text4);
        TextView t5 = (TextView)findViewById(R.id.helpline_text);
        TextView t6 = (TextView)findViewById(R.id.imgbut_text6);
        TextView t7 = (TextView)findViewById(R.id.imgbut_text5);

        t1.setText("Guidelines");
        t2.setText("Gov updates");
        t3.setText("Myth Busters");
        t4.setText("Daily Information");
        t5.setText("Corona Central helpline");
        t6.setText("MapMyIndia Live Tracker");
        t7.setText("News");
        setEnglishFlipper();
    }

    public void setEndlishFlipper(){
        initialize_view_flipper();


        databaseReference.child("Infographic").child("English")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.exists())
                        {
                            slideLists.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
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




    public void onCoronaHelpListner(){
        corona_helpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callintent = new Intent(Intent.ACTION_DIAL);
                callintent.setData(Uri.parse("tel:"+corona_helpline.getText().toString()));
                startActivity(callintent);
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
