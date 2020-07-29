package inspire2connect.inspire2connect.mythGuidelineUpdates;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
import inspire2connect.inspire2connect.home.homeActivity;
import inspire2connect.inspire2connect.survey.maleFemaleActivity;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;

public class UpdateActivity extends BaseActivity implements TextToSpeech.OnInitListener {
    public static final String TAG = "UpdateActivity";
    public ArrayList<guidelinesObject> result;
    DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private UpdatesAdapter mAdapter;
    private TextToSpeech tts;
    private boolean setDate;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
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
                    if (setDate) {
                        obj.date = snapshot.child("date").getValue().toString();
                        obj.title = snapshot.child("title_" + getCurLang()).getValue().toString() + " | " + obj.date;
                    } else {
                        obj.title = snapshot.child("title_" + getCurLang()).getValue().toString();
                    }
                    obj.title = snapshot.child("title_" + getCurLang()).getValue().toString();
                    obj.content = snapshot.child("content_" + getCurLang()).getValue().toString();
                    obj.source = snapshot.child("source").getValue().toString();

                    result.add(new guidelinesObject(obj.title,
                            obj.content, Integer.toString(count), obj.source));
                }
                populate_recycler_view(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void populate_recycler_view(ArrayList<guidelinesObject> result) {
        mAdapter = new UpdatesAdapter(this, result);
        mAdapter.setTTS(tts);
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
                getSupportActionBar().setTitle( R.string.tweets_tile);
                break;
            default:
                Logv(TAG, "Invalid Intent");
                break;
        }

        result = new ArrayList<>();
        result.add(new guidelinesObject("Under Maintainence", "Under Maintainence", "1", "Under"));
        mAdapter = new UpdatesAdapter(this, result);
        recyclerView = findViewById(R.id.recyclerView_gov_updates);

        setUpdates();
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
                toggleLang(this);
                break;
            case R.id.Survey:
                i = new Intent(UpdateActivity.this, maleFemaleActivity.class);
                startActivity(i);
                break;
            case R.id.developers:
                i = new Intent(UpdateActivity.this, aboutActivity.class);
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
        mAdapter.setOnItemClickListener(new UpdatesAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent i = new Intent(UpdateActivity.this, guidelineViewActivity.class);
                ArrayList<guidelinesObject> result_from_adapter = mAdapter.getResult();
                i.putExtra("detailed_title", result_from_adapter.get(position).getTitle());
                i.putExtra("detailed_text", result_from_adapter.get(position).getContent());
                i.putExtra("url", "");
                i.putExtra("redirect_url", result_from_adapter.get(position).getSource());
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}