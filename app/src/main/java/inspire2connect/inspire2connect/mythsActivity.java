package inspire2connect.inspire2connect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;

public class mythsActivity extends BaseActivity {
    public ArrayList<myth_single_object> result;
    TextView centre;
    DatabaseReference dref;
    DatabaseReference d;
    private RecyclerView recyclerView;
    private myths_adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void setGuidelinesHindi() {
        //centre = (TextView) findViewById(R.id.centre_view);
        //centre.setText("मिथक(WHO के द्वारा)");
        FirebaseApp.initializeApp(this);
        d = FirebaseDatabase.getInstance().getReference();
        dref = FirebaseDatabase.getInstance().getReference().child("Coronavirus").child("Myth");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<myth_single_object> result = new ArrayList<>();
                String guidelnines = "";
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    count += 1;
                    String g_hindi = snapshot.child("Myth_hin").getValue(String.class);
                    String sno = snapshot.child("Sno").getValue().toString();
                    String audio_url = snapshot.child("Audio").getValue(String.class);
                    String hin_title = snapshot.child("Title_hin").getValue(String.class);
                    String redirect_url = snapshot.child("Source").getValue(String.class);
                    hin_title = sno + ". " + hin_title + "<br>";//<a href=" + redirect_url + ">स्रोत" + "</a>";
                    //String ttp = "<b>" + sno + ". " + hin_title + "</b><br />" + g_hindi;
                    String ttp = g_hindi;
                    result.add(new myth_single_object(hin_title, ttp, Integer.toString(count), audio_url, redirect_url));
                }
                populate_recycler_view(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setGuidelinesEnglish() {
        //TextView guid_view = (TextView) findViewById(R.id.centre_view);
        //guid_view.setTypeface(null, Typeface.BOLD);
        //guid_view.setText("Myth Busters(By WHO)");
        FirebaseApp.initializeApp(this);
        d = FirebaseDatabase.getInstance().getReference();
        dref = FirebaseDatabase.getInstance().getReference().child("Coronavirus").child("Myth");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<myth_single_object> result = new ArrayList<>();
                String guidelnines = "";
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    count += 1;
                    String g_hindi = snapshot.child("Myth_en").getValue(String.class);
                    String sno = snapshot.child("Sno").getValue().toString();
                    String audio_url = snapshot.child("Audio").getValue(String.class);
                    String hin_title = snapshot.child("Title_en").getValue(String.class);
                    String redirect_url = snapshot.child("Source").getValue(String.class);
                    hin_title = sno + ". " + hin_title + "</b><br>";//<a href=" + redirect_url + ">Source" + "</a>";
                    String ttp = g_hindi;
                    result.add(new myth_single_object(hin_title, ttp, Integer.toString(count), audio_url, redirect_url));
                }
                populate_recycler_view(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void populate_recycler_view(ArrayList<myth_single_object> result) {
        mAdapter = new myths_adapter(this, result);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));

        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_guidelines);
        result = new ArrayList<>();
        result.add(new myth_single_object("Under Maintainence", "Under Maintainence", "1", "Under", "under"));
        mAdapter = new myths_adapter(this, result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.myth_act);
        recyclerView = findViewById(R.id.recyclerView);

        switch (getCurLang()) {
            case englishCode:
                setGuidelinesEnglish();
                break;
            case hindiCode:
                setGuidelinesHindi();
                break;
        }

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
            toggleLang(this);
        } else if (id == R.id.Survey) {
            Intent i = new Intent(mythsActivity.this, maleFemaleActivity.class);
            startActivity(i);
        } else if (id == R.id.developers) {
            Intent i = new Intent(mythsActivity.this, aboutActivity.class);
            startActivity(i);
        } else if (id == R.id.privacy_policy) {
            openPrivacyPolicy(this);
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
        Log.d("Testing", "Sizze of Media player list=" + media_player_list.size());
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

    public void share(String toShare) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        Log.d("sharing", toShare);
        Spanned shareBody = Html.fromHtml(toShare);
        String share = shareBody.toString();
        //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, share);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setOnItemClickListener(new myths_adapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.d("Testing", " Clicked on Item gov_updates " + position);
                Intent i = new Intent(mythsActivity.this, detailedViewActivity.class);
                //Log.d("Testing",result.get(position).getTitle());
                ArrayList<myth_single_object> result_from_adapter = mAdapter.getResult();
                Log.d("Testing", result_from_adapter.get(position).getTitle());
                /*if(mMediaPlayer!=null)
                {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    mMediaPlayer=null;
                }*/
                ArrayList<myth_single_object> single = new ArrayList<>();
                single.add(result_from_adapter.get(position));
                Log.d("Testing", single.get(0).getTitle());
                i.putExtra("detailed_title", single.get(0).getTitle());
                i.putExtra("detailed_text", single.get(0).getMyth());
                i.putExtra("url", single.get(0).getAudio_url());
                i.putExtra("redirect_url", single.get(0).getRedirect_url());
                //i.putExtra("result_list",single);
//                if(v==findViewById(R.id.share_button))
//                {
//                    Log.d("sharing",single.get(0).getTitle());
//                    share(single.get(0).getTitle());
//                }
//                else
                startActivity(i);
            }
        });
    }
}
