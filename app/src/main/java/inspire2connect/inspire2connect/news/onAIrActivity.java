package inspire2connect.inspire2connect.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.about.aboutActivity;
import inspire2connect.inspire2connect.survey.maleFemaleActivity;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;

public class onAIrActivity extends BaseActivity {
    boolean flag = false;
    DatabaseReference ref;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i;
        switch (id){
            case R.id.lang_togg_butt:
                // Firebase Analytics
                Bundle bundle = new Bundle();
                bundle.putString("UID", firebaseUser.getUid());
                if(Locale.getDefault().getLanguage().equals("en"))
                    bundle.putString("Current_Language", "Hindi");
                else if(Locale.getDefault().getLanguage().equals("hi"))
                    bundle.putString("Current_Language", "English");

                bundle.putString("Language_Change_Activity", "onAIr Activity");
                firebaseAnalytics.logEvent("Language_Toggle", bundle);

                toggleLang(this);
                break;
            case R.id.Survey:
                i = new Intent(onAIrActivity.this, maleFemaleActivity.class);
                startActivity(i);
                break;
            case R.id.developers:
                i = new Intent(onAIrActivity.this, aboutActivity.class);
                startActivity(i);
                break;
            case R.id.privacy_policy:
                openPrivacyPolicy(this);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_air);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.onair_tile);
        }

        final ArrayList<newsObject> results = new ArrayList<>();

        final ProgressBar cpd = findViewById(R.id.circularprogressbar);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(results);
        mRecyclerView.setAdapter(mAdapter);

        ref = FirebaseDatabase.getInstance().getReference();
        Query lastQuery = ref.child(getCurLangKey().toLowerCase()).orderByKey();
        lastQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!flag) {
                    int count = 0;
                    ArrayList<newsObject> results2 = new ArrayList<>();
                    HashMap<String, Story> hn = new HashMap<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Story user = snapshot.getValue(Story.class);
                        hn.put(snapshot.getKey(), user);
                    }

                    for (Map.Entry<String, Story> it : hn.entrySet()) {
                        Logd("Database", it.getValue().getTitle());
                        newsObject obj = new newsObject(it.getValue().getTitle(), it.getValue().getStory());
                        results2.add(obj);
                        count++;
                    }

                    if (count == 0) {
                        mAdapter = new MyRecyclerViewAdapter(results);
                    } else {

                        mAdapter = new MyRecyclerViewAdapter(results2);
                    }
                    mRecyclerView.setAdapter(mAdapter);
                    cpd.setVisibility(View.INVISIBLE);
                    flag = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent i = new Intent(onAIrActivity.this, text2speech2_2Activity.class);
                i.putExtra("position", Integer.toString(position));
                startActivity(i);
            }
        });
    }
}
