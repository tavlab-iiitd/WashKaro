package inspire2connect.inspire2connect;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class daily_guidelines_adapter extends RecyclerView.Adapter<daily_guidelines_adapter.MyViewHolder> {
    private static MyClickListener myClickListener;
    private ArrayList<Boolean> play_pause_list = new ArrayList<Boolean>();
    private ArrayList<custom_media_Class> media_player_list = new ArrayList<>();
    private ArrayList<guideline_sigle_object> List;

    public daily_guidelines_adapter() {
    }

    public daily_guidelines_adapter(ArrayList<guideline_sigle_object> List) {
        this.List = List;
    }

    public ArrayList<custom_media_Class> getMedia_player_list() {
        return media_player_list;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        daily_guidelines_adapter.myClickListener = myClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_guideline, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        guideline_sigle_object movie = List.get(position);
        holder.title.setText(Html.fromHtml(movie.getGuideline()));
        holder.play_pause.setBackgroundResource(R.drawable.ic_play_arrow_black_34dp);
        play_pause_list.add(false);
        media_player_list.add(new custom_media_Class(null, true));
        holder.play_pause.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        Log.d("Testing", "x=" + motionEvent.getX() + "y=" + motionEvent.getY());
//                        if(motionEvent.getX()>820.0) {
//                        Log.d("Testing", "Button Touched");
                        for (int i = 0; i < media_player_list.size(); i++) {
                            if (i != position) {
                                MediaPlayer temp = media_player_list.get(i).getMediaPlayer();
                                if (temp != null && temp.isPlaying()) {
                                    temp.seekTo(temp.getDuration());
                                }
                            }
                        }
                        if (media_player_list.get(position).getPaused()) {
                            holder.play_pause.setImageDrawable(null);
                            holder.play_pause.setBackgroundResource(R.drawable.ic_pause_black_34dp);
                            //media_player_list.get(position).setPaused(false);
                            MediaPlayer temp = media_player_list.get(position).getMediaPlayer();
                            try {
                                if (false) {
                                } else {
                                    if (temp == null) {
                                        temp = new MediaPlayer();
                                        media_player_list.get(position).setMediaPlayer(temp);
                                        Log.d("Testing", "Step1");
                                        temp.setDataSource(List.get(position).getAudio_url());
                                        temp.prepare();
                                        temp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                            @Override
                                            public void onPrepared(MediaPlayer mediaPlayer) {
                                                mediaPlayer.start();
                                                media_player_list.get(position).setPaused(false);
                                            }
                                        });
                                        temp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            @Override
                                            public void onCompletion(MediaPlayer mediaPlayer) {
                                                Log.d("Testing", "Media Player finished");
                                                media_player_list.get(position).setPaused(true);
                                                holder.play_pause.setBackgroundResource(R.drawable.ic_play_arrow_black_34dp);
                                                mediaPlayer = null;
                                            }
                                        });
                                    } else {
                                        Log.d("Testing", "In here not null");
                                        if (media_player_list.get(position).getPaused()) {
                                            temp.seekTo(media_player_list.get(position).getCurrent_time());
                                            temp.start();
                                            media_player_list.get(position).setPaused(false);
                                        } else {
                                            temp.setDataSource(List.get(position).getAudio_url());
                                            temp.prepare();
                                            temp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                @Override
                                                public void onPrepared(MediaPlayer mediaPlayer) {
                                                    mediaPlayer.start();
                                                    media_player_list.get(position).setPaused(false);
                                                }
                                            });
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("Testing", "Exception", e);
                                e.printStackTrace();
                            }
                        } else {
                            holder.play_pause.setBackgroundResource(R.drawable.ic_play_arrow_black_34dp);
                            //play_pause_list.set(position,false);
                            //media_player_list.get(position).setPaused(true);
                            MediaPlayer temp = media_player_list.get(position).getMediaPlayer();
                            if (temp != null) {
                                if (temp.isPlaying()) {
                                    temp.pause();
                                    Log.d("Testing", "Step2");
                                    media_player_list.get(position).setPaused(true);
                                    media_player_list.get(position).setCurrent_time(temp.getCurrentPosition());
                                }
                            }
                        }
//                        }
                        return true;
                }
                return false;
            }

        });
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ConstraintLayout constraintLayout;
        public ImageView play_pause;

        //public CardView guideline_cv;
        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.guideline_view);
            play_pause = view.findViewById(R.id.play_pause);
            constraintLayout = itemView.findViewById(R.id.constraint);

        }
    }
}