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
import android.text.Html;
import android.text.Spanned;
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

public class Government_Updates extends AppCompatActivity {
    DatabaseReference dref;
    private RecyclerView recyclerView;
    DatabaseReference d;
    private Government_Updates_Adapter mAdapter;
    TextView centre;
    int curr_lang = 2;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
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

    private void setGuidelinesHindi() {
        curr_lang = 2;
        centre = (TextView) findViewById(R.id.centre_view_gov_updates);
        centre.setText("सरकारी जानकारी");
        FirebaseApp.initializeApp(this);
        d = FirebaseDatabase.getInstance().getReference();
        dref = FirebaseDatabase.getInstance().getReference().child("Coronavirus").child("Government");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<daily_update_single_object> result = new ArrayList<>();
                String guidelnines = "";
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    count += 1;
                    String date = snapshot.child("Date").getValue(String.class);
                    String hindi_title = snapshot.child("Title_hin").getValue(String.class);
                    String hindi_text = snapshot.child("Text_hin").getValue(String.class);
                    String sno = snapshot.child("Sno").getValue().toString();
                    String audio_url = snapshot.child("Audio").getValue(String.class);
                    String redirect_url = snapshot.child("Source").getValue(String.class);
                    String ttd = "<b>" + sno + ". " + date + "<br>" + hindi_title + "</b><br>" + hindi_text;
                    Spanned text = Html.fromHtml(ttd +
                            "<a href=" + redirect_url + "> स्रोत:" + redirect_url + "</a>");
                    result.add(new daily_update_single_object(text, Integer.toString(count), audio_url));
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
        centre = (TextView) findViewById(R.id.centre_view_gov_updates);
        centre.setTypeface(null, Typeface.BOLD);

        centre.setText("Government Updates");
        FirebaseApp.initializeApp(this);
        d = FirebaseDatabase.getInstance().getReference();
        dref = FirebaseDatabase.getInstance().getReference().child("Coronavirus").child("Government");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<daily_update_single_object> result = new ArrayList<>();
                String guidelnines = "";
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    count += 1;
                    String date = snapshot.child("Date").getValue(String.class);
                    String english_title = snapshot.child("Title_en").getValue(String.class);
                    String english_text = snapshot.child("Text_en").getValue(String.class);
                    String sno = snapshot.child("Sno").getValue().toString();
                    String audio_url = snapshot.child("Audio").getValue(String.class);
                    String redirect_url = snapshot.child("Source").getValue(String.class);
                    String ttd = "<b>" + sno + ". " + date + "<br>" + english_title + "</b><br>" + english_text;
                    Spanned text = Html.fromHtml(ttd +
                            "<a href=" + redirect_url + "> Source:" + redirect_url + "</a>");
                    result.add(new daily_update_single_object(text, Integer.toString(count), audio_url));
                }
                populate_recycler_view(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void populate_recycler_view(ArrayList<daily_update_single_object> result) {
        mAdapter = new Government_Updates_Adapter(result);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_government__updates);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_gov_updates);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = this.getIntent();
        String lan = i.getStringExtra("Language");
        if (lan.equalsIgnoreCase("hindi"))
            setGuidelinesHindi();
        else
            setGuidelinesEnglish();
//        lang_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
//            {
//                if(b)
//                {
//                    Toast.makeText(Government_Updates.this,"English selected",Toast.LENGTH_SHORT).show();
//                    centre.setText("Governemnt UPDATES");
//                    setGuidelinesEnglish();
//                }
//                else
//                {
//                    Toast.makeText(Government_Updates.this,"Hindi selected",Toast.LENGTH_SHORT).show();
//                    centre.setText("सरकारी जानकारी");
//                    setGuidelinesHindi();
//                }
//            }
//        });
        //populate_recycler_view(temp);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.lang_togg_butt) {
            Toast.makeText(Government_Updates.this, "Language Changed", Toast.LENGTH_SHORT).show();
            //switch_language();
            if (curr_lang == 2) {
                curr_lang = 1;
                setGuidelinesEnglish();
            } else {
                curr_lang = 2;
                setGuidelinesHindi();
            }
        } else if (id == R.id.Survey) {
            Intent i = new Intent(Government_Updates.this, Male_Female.class);
            if (curr_lang == 2)
                i.putExtra("Language", "hindi");
            else
                i.putExtra("Language", "english");
            startActivity(i);
        } else if (id == R.id.developers) {
            Intent i = new Intent(Government_Updates.this, about.class);
            startActivity(i);
        } else if (id == R.id.privacy_policy) {
            Intent i = new Intent(Government_Updates.this, privacy_policy.class);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }
}
