package inspire2connect.inspire2connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class daily_guidelines extends AppCompatActivity
{
    DatabaseReference dref;
    private RecyclerView recyclerView;
    DatabaseReference d;
    private daily_guidelines_adapter mAdapter;
    TextView centre;
    int curr_lang = 2;//1 for eng , 2 for Hinf=di
    private RecyclerView.LayoutManager layoutManager;
    @Override
    public void onPause()
    {
        super.onPause();

    }
    @Override
    public void onStop()
    {
        super.onStop();
    }
    @Override
    public void onBackPressed()
    {
        ArrayList<custom_media_Class> media_player_list=new ArrayList<>();
        if(mAdapter!=null)
            media_player_list=mAdapter.getMedia_player_list();
        if(media_player_list!=null)
            for (int i=0;i<media_player_list.size();i++)
        {
            if(media_player_list.get(i).getMediaPlayer()!=null)
            {
                if(media_player_list.get(i).getMediaPlayer().isPlaying())
                {
                    media_player_list.get(i).getMediaPlayer().stop();
                    media_player_list.get(i).getMediaPlayer().seekTo( media_player_list.get(i).getMediaPlayer().getDuration());
                }
            }
        }
        finish();
        Log.d("Testing","Sizze of Media player list="+Integer.toString(media_player_list.size()));
    }
    private void setGuidelinesHindi()
    {
        TextView guid_view = (TextView)findViewById(R.id.centre_view);
        guid_view.setText("दिशा निर्देश");
        FirebaseApp.initializeApp(this);
        d=FirebaseDatabase.getInstance().getReference();
        dref = FirebaseDatabase.getInstance().getReference().child("Coronavirus").child("guidelines");
        dref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                ArrayList<guideline_sigle_object> result=new ArrayList<>();
                String guidelnines ="";
                int count=0;
                for(DataSnapshot snapshot :dataSnapshot.getChildren())
                {
                    count+=1;
                    String g_hindi = snapshot.child("Guideline_hin").getValue(String.class);
                    String hin_title=snapshot.child("Title_hin").getValue(String.class);
                    String sno = snapshot.child("Sno").getValue().toString();
                    String audio_url=snapshot.child("Audio").getValue(String.class);
                    String ttp="<b>"+sno+". "+hin_title+"</b><br />"+g_hindi;
                    //String ttp=sno+". "+hin_title+"\n"+g_hindi;
                    result.add(new guideline_sigle_object(ttp,Integer.toString(count),audio_url));
                }
                populate_recycler_view(result);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setGuidelinesEnglish()
    {
        TextView guid_view = (TextView)findViewById(R.id.centre_view);
        guid_view.setTypeface(null, Typeface.BOLD);
        guid_view.setText("Guidelines");
        FirebaseApp.initializeApp(this);
        d=FirebaseDatabase.getInstance().getReference();
        dref = FirebaseDatabase.getInstance().getReference().child("Coronavirus").child("guidelines");
        dref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                ArrayList<guideline_sigle_object> result=new ArrayList<>();
                String guidelnines ="";
                int count=0;
                for(DataSnapshot snapshot :dataSnapshot.getChildren())
                {
                    count+=1;
                    String en_title=snapshot.child("Title_en").getValue(String.class);
                    String g_english = snapshot.child("Guideline_en").getValue(String.class);
                    String sno = snapshot.child("Sno").getValue().toString();
                    String audio_url=snapshot.child("Audio").getValue(String.class);
                    String ttp="<b>"+sno+". "+en_title+"</b><br />"+g_english;
                    //String ttp=sno+". "+en_title+"\n"+g_english;
                    result.add(new guideline_sigle_object(ttp,Integer.toString(count),audio_url));
                }
                populate_recycler_view(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void populate_recycler_view(ArrayList<guideline_sigle_object> result)
    {
        mAdapter = new daily_guidelines_adapter(result);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_guidelines);
//        centre=(TextView)findViewById(R.id.centre_view);
//        centre.setText("दिशा निर्देश");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        setGuidelinesHindi();


        //populate_recycler_view(temp);
    }

    @Override
    public boolean onSupportNavigateUp() {
        ArrayList<custom_media_Class> media_player_list=new ArrayList<>();
        if(mAdapter!=null)
        media_player_list=mAdapter.getMedia_player_list();
        if(media_player_list!=null)
        for (int i=0;i<media_player_list.size();i++)
        {
            if(media_player_list.get(i).getMediaPlayer()!=null)
            {
                if(media_player_list.get(i).getMediaPlayer().isPlaying())
                {
                    media_player_list.get(i).getMediaPlayer().stop();
                    media_player_list.get(i).getMediaPlayer().seekTo( media_player_list.get(i).getMediaPlayer().getDuration());
                }
            }
        }
        finish();
        //return super.onSupportNavigateUp();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home_page_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.app_bar_settings ){
            Intent i = new Intent(daily_guidelines.this,CardViewActivity.class);
            startActivity(i);
        }
        else if(id == R.id.lang_togg_butt){
            Toast.makeText(daily_guidelines.this,"Language Changed",Toast.LENGTH_SHORT).show();
            //switch_language();
            if(curr_lang ==2){
                curr_lang=1;
                setGuidelinesEnglish();
            }
            else{
                curr_lang=2;
                setGuidelinesHindi();
            }
        }

        else if(id == R.id.Survey){
            Intent i = new Intent(daily_guidelines.this,Male_Female.class);
            startActivity(i);
        }
        else if(id == R.id.developers){
            Intent i = new Intent(daily_guidelines.this,about.class);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }


}
