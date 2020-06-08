package inspire2connect.inspire2connect.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;

public class text2speech2_2Activity extends BaseActivity
{
    Switch toggleSwitch;
    String key;
    DatabaseReference ref;
    int position;
    TextView tv, tv_title;
    int relevant_num, irrelevant_num;
    String download_url, WHO_download_url;
    String story_text, story_title;
    String news_text, WHO_text, WHO_title;
    ImageButton btn_play;
    boolean isSpeaking;
    TextToSpeech mTTS;
    private boolean already_clicked;

    @Override
    protected void onPause() {
        super.onPause();
        if (mTTS.isSpeaking()) {
            mTTS.stop();
            isSpeaking = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isSpeaking = false;
        btn_play.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_34dp));
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.ERROR) {
                    isSpeaking = false;
                    mTTS.stop();
                    btn_play.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_34dp));
                } else if (status == TextToSpeech.SUCCESS) {
                    mTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            if (!isSpeaking) {
                                isSpeaking = true;
                                btn_play.setImageDrawable(getDrawable(R.drawable.ic_pause_black_34dp));
                            }
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            if (isSpeaking) {
                                isSpeaking = false;
                                mTTS.stop();
                                btn_play.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_34dp));
                            }
                        }

                        @Override
                        public void onError(String utteranceId) {

                        }
                    });
                }
            }
        });
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    public Boolean alread_clicked_checker(String key) {
        Log.d("already_clicked", "In function with " + key);
        boolean existence = false;
        String filename = "myfile";
        try {
            FileInputStream fileInputStream = openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuffer sb = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                Log.d("already_cl", "line read" + line);
                if (key.equalsIgnoreCase(line))
                    existence = true;
                sb.append(line);
                line = reader.readLine();
            }
            Log.d("already_cl", "File read" + sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existence;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toggle_button);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        final ImageButton relevant_button = findViewById(R.id.relevant_button);
        final ImageButton irrelevant_button = findViewById(R.id.irrelevant_button);
        relevant_button.setVisibility(View.INVISIBLE);
        irrelevant_button.setVisibility(View.INVISIBLE);
        relevant_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Keyofidis", key);
                String filename = "myfile";
                String currtext = "";
                if (!already_clicked) {
                    try {
                        FileInputStream fileInputStream = openFileInput(filename);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                        StringBuffer sb = new StringBuffer();
                        String line = reader.readLine();
                        while (line != null) {
                            sb.append(line + '\n');
                            line = reader.readLine();
                            Log.d("Keyofidis", "line read" + line);
                        }
                        currtext = sb.toString();
                        Log.d("Keyofidis", "File read" + sb.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ref.child(getCurLangKey().toLowerCase()).child(key).child("number_of_relevant_votes").setValue(relevant_num + 1);
                    String fileContents = currtext + '\n' + key;
                    Log.d("already_clicked", fileContents);
                    FileOutputStream outputStream;
                    try {
                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(fileContents.getBytes());
                        outputStream.close();
                        Log.d("Keyofidis", "Writing" + key);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    relevant_button.setVisibility(View.INVISIBLE);
                    irrelevant_button.setVisibility(View.INVISIBLE);
                    already_clicked = true;
                }
            }
        });
        irrelevant_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Keyofidis", key);
                String filename = "myfile";
                String currtext = "";
                if (!already_clicked) {
                    try {
                        FileInputStream fileInputStream = openFileInput(filename);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                        StringBuffer sb = new StringBuffer();
                        String line = reader.readLine();
                        while (line != null) {
                            sb.append(line + '\n');
                            line = reader.readLine();
                            Log.d("Keyofidis", "line read" + line);
                        }
                        currtext = sb.toString();
                        Log.d("Keyofidis", "File read" + sb.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ref.child(getCurLangKey().toLowerCase()).child(key).child("number_of_irrelevant_votes").setValue(irrelevant_num + 1);
                    String fileContents = currtext + '\n' + key;
                    Log.d("already_clicked", fileContents);
                    FileOutputStream outputStream;
                    try {
                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(fileContents.getBytes());
                        outputStream.close();
                        Log.d("Keyofidis", "Writing" + key);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    relevant_button.setVisibility(View.INVISIBLE);
                    irrelevant_button.setVisibility(View.INVISIBLE);
                    already_clicked = true;
                }
            }
        });

        btn_play = findViewById(R.id.play_bt);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!toggleSwitch.isChecked()) {

                    if (isSpeaking) {
                        isSpeaking = false;
                        mTTS.stop();
                        btn_play.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_34dp));
                    } else {
                        mTTS.speak(news_text, TextToSpeech.QUEUE_FLUSH, null);
                        isSpeaking = true;
                        btn_play.setImageDrawable(getDrawable(R.drawable.ic_pause_black_34dp));
                    }
                } else {
                    if (isSpeaking) {
                        isSpeaking = false;
                        mTTS.stop();
                        btn_play.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_34dp));
                    } else {
                        mTTS.speak(WHO_text, TextToSpeech.QUEUE_FLUSH, null);
                        isSpeaking = true;
                        btn_play.setImageDrawable(getDrawable(R.drawable.ic_pause_black_34dp));
                    }
                }
            }
        });
        tv = findViewById(R.id.display_text);
        tv_title = findViewById(R.id.display_title);
//        tv.setMovementMethod(new ScrollingMovementMethod());
        ref = FirebaseDatabase.getInstance().getReference();

        Query lastQuery = ref.child(getCurLangKey().toLowerCase()).orderByKey();
        Intent i = getIntent();
        position = Integer.parseInt(i.getStringExtra("position"));
        WHO_text = "This is WHO report";
        toggleSwitch = findViewById(R.id.switch1);
        toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isSpeaking) {
                    isSpeaking = false;
                    mTTS.stop();
                    btn_play.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_34dp));
                }

                // If ischecked means if it is ON the it will show true else
                // show false
                if (!isChecked) {
                    tv.setText(news_text);
                    tv_title.setText(story_title);
                    relevant_button.setVisibility(View.INVISIBLE);
                    irrelevant_button.setVisibility(View.INVISIBLE);
                } else {
                    tv.setText(WHO_text);
                    tv_title.setText(WHO_title);
                    if (!already_clicked) {
                        relevant_button.setVisibility(View.VISIBLE);
                        irrelevant_button.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        lastQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                HashMap<String, Story> hn = new HashMap<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Story user = snapshot.getValue(Story.class);
                    hn.put(snapshot.getKey(), user);
                }
                for (Map.Entry<String, Story> it : hn.entrySet()) {
                    if (count == position) {
                        key = it.getKey();
                        Log.d("already_clicked_?", Boolean.toString(alread_clicked_checker(key)));
                        already_clicked = alread_clicked_checker(key);
                        story_text = it.getValue().getStory();
                        story_title = it.getValue().getTitle();
                        download_url = it.getValue().getUrl();
                        relevant_num = it.getValue().getNumber_of_relevant_votes();
                        irrelevant_num = it.getValue().getNumber_of_irrelevant_votes();
                        WHO_download_url = it.getValue().getWho_url();
                        WHO_text = it.getValue().getWho_article_text();
                        WHO_title = it.getValue().getWho_summary();
                        tv.setText(story_text);
                        tv_title.setText(story_title);
                        news_text = story_text;
                        break;
                    }
                    count++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}