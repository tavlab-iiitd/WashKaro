package inspire2connect.inspire2connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
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

public class Myths extends AppCompatActivity {
    TextView centre;
    DatabaseReference dref;
    private RecyclerView recyclerView;
    DatabaseReference d;
    private daily_guidelines_adapter mAdapter;
    int curr_lang = 2;  // 2 for hindi  1 for eng

    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void setGuidelinesHindi() {
        curr_lang = 2;
        centre = (TextView) findViewById(R.id.centre_view);
        centre.setText("मिथक(WHO के द्वारा)");
        FirebaseApp.initializeApp(this);
        d = FirebaseDatabase.getInstance().getReference();
        dref = FirebaseDatabase.getInstance().getReference().child("Coronavirus").child("Myth");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<guideline_sigle_object> result = new ArrayList<>();
                String guidelnines = "";
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    count += 1;
                    String g_hindi = snapshot.child("Myth_hin").getValue(String.class);
                    String sno = snapshot.child("Sno").getValue().toString();
                    String audio_url = snapshot.child("Audio").getValue(String.class);
                    String hin_title = snapshot.child("Title_hin").getValue(String.class);
                    String ttp = "<b>" + sno + ". " + hin_title + "</b><br />" + g_hindi;
                    result.add(new guideline_sigle_object(ttp, Integer.toString(count), audio_url));
                }
                populate_recycler_view(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setGuidelinesEnglish() {
        curr_lang = 1;
        TextView guid_view = (TextView) findViewById(R.id.centre_view);
        guid_view.setTypeface(null, Typeface.BOLD);
        guid_view.setText("Myth Busters(By WHO)");

        FirebaseApp.initializeApp(this);
        d = FirebaseDatabase.getInstance().getReference();
        dref = FirebaseDatabase.getInstance().getReference().child("Coronavirus").child("Myth");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<guideline_sigle_object> result = new ArrayList<>();
                String guidelnines = "";
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    count += 1;
                    String g_hindi = snapshot.child("Myth_en").getValue(String.class);
                    String sno = snapshot.child("Sno").getValue().toString();
                    String audio_url = snapshot.child("Audio").getValue(String.class);
                    String hin_title = snapshot.child("Title_en").getValue(String.class);
                    String ttp = "<b>" + sno + ". " + hin_title + "</b><br />" + g_hindi;
                    result.add(new guideline_sigle_object(ttp, Integer.toString(count), audio_url));
                }
                populate_recycler_view(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void populate_recycler_view(ArrayList<guideline_sigle_object> result) {
        mAdapter = new daily_guidelines_adapter(result);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_guidelines);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        Intent i = this.getIntent();
        String lan = i.getStringExtra("Language");
        if (lan.equalsIgnoreCase("hindi"))
            setGuidelinesHindi();
        else
            setGuidelinesEnglish();


        //populate_recycler_view(temp);
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
            //switch_language();
            if (curr_lang == 2) {
                curr_lang = 1;
                setGuidelinesEnglish();
            } else {
                curr_lang = 2;
                setGuidelinesHindi();
            }
        } else if (id == R.id.Survey) {
            Intent i = new Intent(Myths.this, Male_Female.class);
            if (curr_lang == 2)
                i.putExtra("Language", "hindi");
            else
                i.putExtra("Language", "english");
            startActivity(i);
        } else if (id == R.id.developers) {
            Intent i = new Intent(Myths.this, about.class);
            startActivity(i);
        } else if (id == R.id.privacy_policy) {
            Intent i = new Intent(Myths.this, privacy_policy.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        ArrayList<custom_media_Class> media_player_list = new ArrayList<>();
        if (mAdapter != null)
            media_player_list = mAdapter.getMedia_player_list();
        if (media_player_list != null)

            for (int i = 0; i < media_player_list.size(); i++) {
                if (media_player_list.get(i).getMediaPlayer() != null) {
                    if (media_player_list.get(i).getMediaPlayer().isPlaying()) {
                        media_player_list.get(i).getMediaPlayer().stop();
                        media_player_list.get(i).getMediaPlayer().seekTo(media_player_list.get(i).getMediaPlayer().getDuration());
                    }
                }
            }
        finish();
        Log.d("Testing", "Sizze of Media player list=" + Integer.toString(media_player_list.size()));
    }


    @Override
    public boolean onSupportNavigateUp() {
        ArrayList<custom_media_Class> media_player_list = new ArrayList<>();
        if (mAdapter != null)
            media_player_list = mAdapter.getMedia_player_list();
        if (media_player_list != null)
            for (int i = 0; i < media_player_list.size(); i++) {
                if (media_player_list.get(i).getMediaPlayer() != null) {
                    if (media_player_list.get(i).getMediaPlayer().isPlaying()) {
                        media_player_list.get(i).getMediaPlayer().stop();
                        media_player_list.get(i).getMediaPlayer().seekTo(media_player_list.get(i).getMediaPlayer().getDuration());
                    }
                }
            }
        finish();
        //return super.onSupportNavigateUp();
        return true;
    }


}
