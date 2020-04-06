package inspire2connect.inspire2connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class detailed_view extends AppCompatActivity implements Serializable
{
    public TextView detailed_title;
    public TextView detailed_text;
    public ImageButton detailed_play_button,detailed_share_button;
    public SeekBar detailed_seekBar;
    public MediaPlayer mediaPlayer;
    int current_time;
    boolean currently_paused;
    @Override
    public void onBackPressed()
    {
        mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        detailed_title=(TextView)findViewById(R.id.detailed_title);
        detailed_title.setMovementMethod(LinkMovementMethod.getInstance());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        detailed_text=(TextView)findViewById(R.id.detailed_text);
        detailed_text.setMovementMethod(new ScrollingMovementMethod());
        detailed_play_button=(ImageButton)findViewById(R.id.detailed_play_button);
        detailed_seekBar=(SeekBar)findViewById(R.id.detailed_seekBar);
        detailed_share_button=(ImageButton)findViewById(R.id.detailed_share);
        currently_paused=false;
        final Intent i=getIntent();
        //ArrayList<myth_single_object> single=(ArrayList<myth_single_object>) i.getSerializableExtra("result_list");
        detailed_title.setText(Html.fromHtml(i.getStringExtra("detailed_title")));
        detailed_text.setText(Html.fromHtml(i.getStringExtra("detailed_text")));
        mediaPlayer=new MediaPlayer();

        detailed_share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(i.getStringExtra("detailed_text"));
            }
        });
        detailed_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer!=null && fromUser)
                  mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                detailed_seekBar.setProgress(mp.getCurrentPosition());
                mediaPlayer.seekTo(0);
                current_time=0;
                currently_paused=true;
                detailed_play_button.setImageResource(R.drawable.ic_play_arrow_black_34dp);
                mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);

            }
        });

        detailed_play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if(currently_paused)
                {
                    if(mediaPlayer!=null)
                    {
                        mediaPlayer.seekTo(current_time);
                        mediaPlayer.start();
                        detailed_play_button.setImageResource(R.drawable.ic_pause_black_34dp);
                        currently_paused=false;
                        mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
                    }
                }
                else
                {
                    if(mediaPlayer.isPlaying())
                    {
                        mediaPlayer.pause();
                        current_time=mediaPlayer.getCurrentPosition();
                        detailed_play_button.setImageResource(R.drawable.ic_play_arrow_black_34dp);
                        mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
                        currently_paused=true;
                    }
                    else
                    {
                        try
                        {
                            mediaPlayer.setDataSource(i.getStringExtra("url"));
                            mediaPlayer.prepare();
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp)
                                {
                                    mp.start();
                                    Log.d("testing","Prepared");
                                    detailed_seekBar.setMax(mp.getDuration());
                                    mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
                                    detailed_play_button.setImageResource(R.drawable.ic_pause_black_34dp);
                                    currently_paused=false;
                                }
                            });
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }
    public void share(String toShare)
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        Log.d("sharing",toShare);
        Spanned shareBody = Html.fromHtml(toShare);
        String share=shareBody.toString();
        //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, share);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
    @Override
    public boolean onSupportNavigateUp() {

        //finish();
        mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        finish();
        //return super.onSupportNavigateUp();
        return true;
    }
    private Handler mSeekbarUpdateHandler = new Handler();
    private Runnable mUpdateSeekbar = new Runnable() {
        @Override
        public void run() {
            detailed_seekBar.setProgress(mediaPlayer.getCurrentPosition());
            mSeekbarUpdateHandler.postDelayed(this, 50);
        }
    };
}
