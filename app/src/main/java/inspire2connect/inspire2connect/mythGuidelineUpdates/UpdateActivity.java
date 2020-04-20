package inspire2connect.inspire2connect.mythGuidelineUpdates;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.about.aboutActivity;
import inspire2connect.inspire2connect.survey.maleFemaleActivity;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;

public class UpdateActivity extends BaseActivity {
    public ArrayList<guidelinesObject> result;
    DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private Government_Updates_Adapter mAdapter;

    public static final String TAG = "UpdateActivity";

    private boolean setDate;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

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
        Log.d("Testing", "Sizze of Media player list=" + media_player_list.size());
    }

    private void setUpdates() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                result = new ArrayList<>();
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    count += 1;
                    updateObject obj = new updateObject();
                    if(setDate) {
                        obj.date = snapshot.child("date").getValue().toString();
                    }
                    obj.title = snapshot.child("title_" + getCurLang()).getValue().toString();
                    obj.content = snapshot.child("content_" + getCurLang()).getValue().toString();
                    obj.source = snapshot.child("source").getValue().toString();
                    String audio_url = "";

                    result.add(new guidelinesObject(obj.title,
                            obj.content, Integer.toString(count), audio_url, obj.source));
                }
                populate_recycler_view(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void populate_recycler_view(ArrayList<guidelinesObject> result) {
        mAdapter = new Government_Updates_Adapter(this, result);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));

        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updates);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setDate = false;

        Intent i = getIntent();

        String type = i.getStringExtra(TYPE);
        String date = i .getStringExtra(DATE);

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
            default:
                Logv(TAG, "Invalid Intent");
                break;
        }

        result = new ArrayList<>();
        result.add(new guidelinesObject("Under Maintainence", "Under Maintainence", "1", "Under", "under"));
        mAdapter = new Government_Updates_Adapter(this, result);
        recyclerView = findViewById(R.id.recyclerView_gov_updates);

        setUpdates();
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
            toggleLang(this);
        } else if (id == R.id.Survey) {
            Intent i = new Intent(UpdateActivity.this, maleFemaleActivity.class);
            startActivity(i);
        } else if (id == R.id.developers) {
            Intent i = new Intent(UpdateActivity.this, aboutActivity.class);
            startActivity(i);
        } else if (id == R.id.privacy_policy) {
            openPrivacyPolicy(this);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setOnItemClickListener(new Government_Updates_Adapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.d("Testing", " Clicked on Item gov_updates " + position);
                Intent i = new Intent(UpdateActivity.this, detailedViewActivity.class);
                //Log.d("Testing",result.get(position).getTitle());
                ArrayList<guidelinesObject> result_from_adapter = mAdapter.getResult();
                Log.d("Testing", result_from_adapter.get(position).getTitle());
                /*if(mMediaPlayer!=null)
                {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    mMediaPlayer=null;
                }*/
                ArrayList<guidelinesObject> single = new ArrayList<>();
                single.add(result_from_adapter.get(position));
                //Log.d("Testing",single.get(0).getTitle());
                i.putExtra("detailed_title", single.get(0).getTitle());
                i.putExtra("detailed_text", single.get(0).getMyth());
                i.putExtra("url", single.get(0).getAudio_url());
                i.putExtra("redirect_url", single.get(0).getRedirect_url());
                //i.putExtra("result_list",single);
                startActivity(i);
            }
        });
    }
}