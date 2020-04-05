package inspire2connect.inspire2connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
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
    public ImageButton detailed_play_button;
    public SeekBar detailed_seekBar;
    public MediaPlayer mediaPlayer;
    int current_time;
    boolean currently_paused;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        detailed_title=(TextView)findViewById(R.id.detailed_title);
        detailed_text=(TextView)findViewById(R.id.detailed_text);
        detailed_play_button=(ImageButton)findViewById(R.id.detailed_play_button);
        detailed_seekBar=(SeekBar)findViewById(R.id.detailed_seekBar);
        currently_paused=false;
        final Intent i=getIntent();
        //ArrayList<myth_single_object> single=(ArrayList<myth_single_object>) i.getSerializableExtra("result_list");
        detailed_title.setText(Html.fromHtml(i.getStringExtra("detailed_title")));
        detailed_text.setText(Html.fromHtml(i.getStringExtra("detailed_text")));
        mediaPlayer=new MediaPlayer();
        detailed_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
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
                    detailed_view.this.detailed_seekBar.setProgress(current_time);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                if (mediaPlayer!= null && mediaPlayer.isPlaying())
                    mediaPlayer.seekTo(seekBar.getProgress());
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
                detailed_play_button.setImageResource(R.drawable.play_icon);
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
                        detailed_play_button.setImageResource(R.drawable.pause_icon);
                        currently_paused=false;
                    }
                }
                else
                {
                    if(mediaPlayer.isPlaying())
                    {
                        mediaPlayer.pause();
                        current_time=mediaPlayer.getCurrentPosition();
                        detailed_play_button.setImageResource(R.drawable.play_icon);
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
                                    detailed_play_button.setImageResource(R.drawable.pause_icon);
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
}
