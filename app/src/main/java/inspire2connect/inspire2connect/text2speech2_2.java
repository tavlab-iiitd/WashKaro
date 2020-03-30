package inspire2connect.inspire2connect;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.llollox.androidtoggleswitch.widgets.ToggleSwitch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class text2speech2_2 extends AppCompatActivity //implements Runnable
{
    private Button start_bt;
    boolean currently_paused = false;
    int current_time;
    private Button stop_bt;
    Switch toggleSwitch;
    private boolean already_clicked;
    TextView tot_time, curr_time;
    String key;
    //HashMap<String,Story_Details> hn=new HashMap<>();
    DatabaseReference ref;
    int position;
    boolean wasPlaying = false;
    MediaPlayer mediaPlayer, mMediaPlayer;
    TextView tv;
    MediaObserver observer;
    int relevant_num, irrelevant_num;
    String download_url, WHO_download_url;
    ProgressBar pb;
    String story_text;
    SeekBar mSeekBar;
    String news_text, WHO_text;//Variable to store the text in text view before swapping
    FloatingActionButton btn_play, btn_pause;

    public Boolean alread_clicked_checker(String key) {
        Log.d("already_clicked", "In function with " + key);
        boolean existence = false;
        String filename = "myfile";
        String currtext = "";
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
            currtext = sb.toString();
            Log.d("already_cl", "File read" + sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existence;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        //menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.red_color_icon));
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        /*if(mMediaPlayer!=null && mMediaPlayer.isPlaying())
        {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
        /*if(mMediaPlayer!=null && mMediaPlayer.isPlaying())
        {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer=null;
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about_us) {
            Intent i = new Intent(text2speech2_2.this, Improve.class);
            if (mediaPlayer != null) {
                clearMediaPlayer();
            }
            /*if(mMediaPlayer!=null)
            {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }*/
            i.putExtra("position", Integer.toString(position));
            startActivity(i);
            finish();
        } else if (id == R.id.privacy_policy) {
            Intent i = new Intent(text2speech2_2.this, privacy_policy.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toggle_button);

        final Toast toast = Toast.makeText(getApplicationContext(),
                "Press play button to start",
                Toast.LENGTH_LONG);

        toast.show();
        getSupportActionBar().hide();
        //final Button swap=(Button)(findViewById(R.id.WHO_text_swap_button));
        tot_time = (TextView) findViewById(R.id.total_time);
        curr_time = (TextView) findViewById(R.id.cur_time);
        observer = new MediaObserver();
        final ImageButton relevant_button = (ImageButton) findViewById(R.id.relevant_button);
        final ImageButton irrelevant_button = (ImageButton) findViewById(R.id.irrelevant_button);
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

                    ref.child("hindi").child(key).child("number_of_relevant_votes").setValue(relevant_num + 1);
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
                    ref.child("hindi").child(key).child("number_of_irrelevant_votes").setValue(irrelevant_num + 1);
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

//        if(swap.getText().toString().equalsIgnoreCase("WHO Report"))
//        {
//            relevant_button.setVisibility(View.INVISIBLE);
//            irrelevant_button.setVisibility(View.INVISIBLE);
//        }
        pb = (ProgressBar) findViewById(R.id.wait_state);
        pb.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.INVISIBLE);
        /*try
        {
            mMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.improve);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        mMediaPlayer.start();*/
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        final Thread t = new Thread(observer);
        t.start();

        mediaPlayer = new MediaPlayer();

        btn_play = (FloatingActionButton) findViewById(R.id.play_bt);
        btn_pause = (FloatingActionButton) findViewById(R.id.pause_bt);
        btn_pause.setImageDrawable(ContextCompat.getDrawable(text2speech2_2.this, android.R.drawable.ic_media_pause));
        btn_play.setImageDrawable(ContextCompat.getDrawable(text2speech2_2.this, android.R.drawable.ic_media_play));

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int x = (int) Math.ceil(progress / 1000f);
                double percent = progress / (double) seekBar.getMax();
                int offset = seekBar.getThumbOffset();
                int seekwidth = seekBar.getWidth();
                int val = (int) Math.round(percent * (seekwidth - 2 * offset));
                if (progress > 0 && mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    //clearMediaPlayer();
                    //fab.setImageDrawable(ContextCompat.getDrawable(Text2Speech2.this,android.R.drawable.ic_media_play));
                    text2speech2_2.this.mSeekBar.setProgress(current_time);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null && mediaPlayer.isPlaying())
                    mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mSeekBar.setProgress(mp.getCurrentPosition());
            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                float speed = 0.90f;
                mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed));
                mp.start();
                pb.setVisibility(View.INVISIBLE);
                Log.d("media_test", "prepared");
                mSeekBar.setMax(mediaPlayer.getDuration());
                Log.d("media_test", "prepared");
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!toggleSwitch.isChecked()) {
                /*if(mMediaPlayer!=null)
                {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    mMediaPlayer=null;
                }*/
                    Log.d("media_test", "btn_play");

                    if (mediaPlayer == null) {
                        mediaPlayer = new MediaPlayer();
                    } else if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.seekTo(current_time);
                        mediaPlayer.start();
                    }

                    try {
                        if (currently_paused == false)
                            mediaPlayer.reset();
                        //pb.setVisibility(View.VISIBLE);
                        Log.d("media_test", "before setting data source");
                        mediaPlayer.setDataSource(download_url);
                        mediaPlayer.prepare();
                        //mSeekBar.setMax(mediaPlayer.getDuration());
                        mediaPlayer.setVolume(0.5f, 0.5f);
                        mediaPlayer.setLooping(false);
                        //mSeekBar.setMax(mediaPlayer.getDuration());

                        //new Thread(observer).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (mediaPlayer != null && mediaPlayer.isPlaying() == false) {
                        pb.setVisibility(View.VISIBLE);
                    }
                    currently_paused = false;
                } else {
                    /*if(mMediaPlayer!=null)
                    {
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                        mMediaPlayer=null;
                    }*/
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.reset();
                        try {
                            mediaPlayer.setDataSource(WHO_download_url);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        mediaPlayer.seekTo(current_time);
                        mediaPlayer.start();
                    }
                }

            }
        });
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currently_paused = true;
                /*if(mMediaPlayer!=null)
                {
                    mediaPlayer.stop();
                    mMediaPlayer.release();
                    mMediaPlayer=null;
                }*/
                Log.d("media_test", "btn_pause");
                pb.setVisibility(View.INVISIBLE);
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    current_time = mediaPlayer.getCurrentPosition();
                }

            }
        });
        tv = (TextView) findViewById(R.id.display_text);
        tv.setMovementMethod(new ScrollingMovementMethod());
        ref = FirebaseDatabase.getInstance().getReference();

        Query lastQuery = ref.child("hindi").orderByKey();
        Intent i = getIntent();
        position = Integer.parseInt(i.getStringExtra("position"));
        WHO_text = "This is WHO report";
        toggleSwitch = (Switch) (findViewById(R.id.switch1));
        toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                // If ischecked means if it is ON the it will show true else
                // show false
                if (!isChecked) {

                    tv.setText(news_text);
                    relevant_button.setVisibility(View.INVISIBLE);
                    irrelevant_button.setVisibility(View.INVISIBLE);
                } else {
                    tv.setText(WHO_text);
                    if (!already_clicked) {
                        relevant_button.setVisibility(View.VISIBLE);
                        irrelevant_button.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
//        swap.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//
//                if(swap.getText().toString().equalsIgnoreCase("WHO Report"))
//                {
//                    swap.setText("News Article");
//                    //t1.start();
//                    //t.stop();
//                    tv.setText(WHO_text);
//                    if( !already_clicked)
//                    {
//                        relevant_button.setVisibility(View.VISIBLE);
//                        irrelevant_button.setVisibility(View.VISIBLE);
//                    }
//
//
//                }
//                else
//                {
//                    //t.start();
//                    //t1.stop();
//                    swap.setText("WHO Report");
//                    tv.setText(news_text);
//                    relevant_button.setVisibility(View.INVISIBLE);
//                    irrelevant_button.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
        lastQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                HashMap<String, Story_Details> hn = new HashMap<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Story_Details user = snapshot.getValue(Story_Details.class);
                    hn.put(snapshot.getKey(), user);
                }
                for (Map.Entry<String, Story_Details> it : hn.entrySet()) {
                    if (count == position) {
                        key = it.getKey();
                        Log.d("already_clicked_?", Boolean.toString(alread_clicked_checker(key)));
                        already_clicked = alread_clicked_checker(key);
                        story_text = it.getValue().getStory();
                        download_url = it.getValue().getUrl();
                        relevant_num = it.getValue().getNumber_of_relevant_votes();
                        irrelevant_num = it.getValue().getNumber_of_irrelevant_votes();
                        WHO_download_url = it.getValue().getWho_url();
                        Log.d("Story_Title", WHO_download_url);
                        WHO_text = it.getValue().getWho_article_text();
                        tv.setText(story_text);
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

    /*@Override
    public void run()
    {
        int currentPosition = mediaPlayer.getCurrentPosition();
        int total = mediaPlayer.getDuration();
        while (mediaPlayer != null && mediaPlayer.isPlaying() && currentPosition < total)
        {
            try
            {
                Thread.sleep(1000);
                currentPosition = mediaPlayer.getCurrentPosition();
            } catch (InterruptedException e)
            {
                return;
            }
            catch (Exception e)
            {
                return;
            }
            mSeekBar.setProgress(currentPosition);
        }
    }*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearMediaPlayer();
        /*if(mMediaPlayer!=null)
        {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }*/
    }

    private void clearMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private class MediaObserver implements Runnable {
        private AtomicBoolean stop = new AtomicBoolean(false);

        public void stop() {
            stop.set(true);
        }

        @Override
        public void run() {
            Log.d("Thread_Check", "Thread started.");
            while (!stop.get()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("boolean_check", Boolean.toString(currently_paused));
                        if (mediaPlayer != null && mediaPlayer.isPlaying() == false && currently_paused == false) {

                            pb.setVisibility(View.VISIBLE);
                        } else if (currently_paused)
                            pb.setVisibility(View.INVISIBLE);
                        if (mediaPlayer != null) {
                            Log.d("Get_Duration", "In here");
                            int max = mediaPlayer.getDuration();
                            Log.d("Get_Duration", Integer.toString(max));
                            int sec = max / 1000;
                            int min = sec / 60;
                            if (min <= 50) {
                                int secs = sec % 60;
                                tot_time.setText(min + ":" + secs);
                                max = mediaPlayer.getCurrentPosition();
                                sec = max / 1000;
                                min = sec / 60;
                                secs = sec % 60;
                                curr_time.setText(min + ":" + secs);
                                mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                            }
                        }
                    }
                });
                try {
                    if (mediaPlayer != null)
                        mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}