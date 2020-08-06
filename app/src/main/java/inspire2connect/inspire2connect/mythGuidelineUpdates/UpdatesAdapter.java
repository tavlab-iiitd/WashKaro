package inspire2connect.inspire2connect.mythGuidelineUpdates;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import inspire2connect.inspire2connect.R;

import static inspire2connect.inspire2connect.utils.BaseActivity.firebaseAnalytics;
import static inspire2connect.inspire2connect.utils.BaseActivity.firebaseUser;

public class UpdatesAdapter extends RecyclerView.Adapter<UpdatesAdapter.MyViewHolder> {
    private static MyClickListener myClickListener;
    Context context;

    private TextToSpeech tts;

    private ArrayList<guidelinesObject> List;

    boolean isSpeaking = false;
    private ImageView curPlaying = null;

    public UpdatesAdapter(Context context, ArrayList<guidelinesObject> List) {
        this.context = context;
        this.List = List;
    }

    public void setTTS(TextToSpeech tts) {
        this.tts = tts;
    }

    public ArrayList<guidelinesObject> getResult() {
        return List;
    }

    public void share(String toShare) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        Log.d("sharing", toShare);
        Spanned shareBody = Html.fromHtml(toShare);
        String share = shareBody.toString();
        //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, share);

        // Firebase Analytics
        Bundle bundle = new Bundle();
        bundle.putString("UID", firebaseUser.getUid());
        bundle.putString("ArticleTitle", share);
        firebaseAnalytics.logEvent("ArticleShared", bundle);

        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        UpdatesAdapter.myClickListener = myClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.updates_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final guidelinesObject movie = List.get(position);
        holder.title.setText(movie.getTitle());
        holder.actual_text.setText(movie.getContent());
        holder.play_pause.setImageDrawable(context.getDrawable(R.drawable.ic_play_arrow_black_34dp));
        holder.share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(movie.getTitle());
            }
        });
        isSpeaking = false;
        holder.play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Firebase Analytics
                Bundle bundle = new Bundle();
                bundle.putString("UID", firebaseUser.getUid());
                bundle.putString("ArticleTitle", movie.getTitle());

                if(isSpeaking) {
                    // Firebase Analytics
                    bundle.putString("ArticleAudioStatus", "Audio OFF");
                    if(tts.isSpeaking()) {
                        if(curPlaying!=null) {
                            curPlaying.setImageDrawable(context.getDrawable(R.drawable.ic_play_arrow_black_34dp));
                        }
                        tts.stop();
                        isSpeaking = false;
                        holder.play_pause.setImageDrawable(context.getDrawable(R.drawable.ic_play_arrow_black_34dp));
                    } else {
                        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                            @Override
                            public void onDone(String utteranceId) {
                                isSpeaking = false;
                                if(curPlaying!=null) {
                                    curPlaying.setImageDrawable(context.getDrawable(R.drawable.ic_play_arrow_black_34dp));
                                }
                                holder.play_pause.setImageDrawable(context.getDrawable(R.drawable.ic_play_arrow_black_34dp));
                            }

                            @Override
                            public void onError(String utteranceId) {
                                isSpeaking = false;
                                if(curPlaying!=null) {
                                    curPlaying.setImageDrawable(context.getDrawable(R.drawable.ic_play_arrow_black_34dp));
                                }
                                holder.play_pause.setImageDrawable(context.getDrawable(R.drawable.ic_play_arrow_black_34dp));
                            }

                            @Override
                            public void onStart(String utteranceId) {
                            }
                        });
                    }
                    if(curPlaying!=holder.play_pause) {
                        // Firebase Analytics
                        bundle.putString("ArticleAudioStatus", "Audio ON");

                        holder.play_pause.setImageDrawable(context.getDrawable(R.drawable.ic_pause_black_34dp));
                        tts.speak(movie.getTitle(), TextToSpeech.QUEUE_FLUSH, null);
                        curPlaying = holder.play_pause;
                        isSpeaking = true;

                        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                            @Override
                            public void onDone(String utteranceId) {
                                isSpeaking = false;
                                if(curPlaying!=null) {
                                    curPlaying.setImageDrawable(context.getDrawable(R.drawable.ic_play_arrow_black_34dp));
                                }
                                holder.play_pause.setImageDrawable(context.getDrawable(R.drawable.ic_play_arrow_black_34dp));
                            }

                            @Override
                            public void onError(String utteranceId) {
                                isSpeaking = false;
                                if(curPlaying!=null) {
                                    curPlaying.setImageDrawable(context.getDrawable(R.drawable.ic_play_arrow_black_34dp));
                                }
                                holder.play_pause.setImageDrawable(context.getDrawable(R.drawable.ic_play_arrow_black_34dp));
                            }

                            @Override
                            public void onStart(String utteranceId) {
                            }
                        });
                    }
                } else {
                    // Firebase Analytics
                    bundle.putString("ArticleAudioStatus", "Audio ON");

                    holder.play_pause.setImageDrawable(context.getDrawable(R.drawable.ic_pause_black_34dp));
                    tts.speak(movie.getTitle(), TextToSpeech.QUEUE_FLUSH, null);
                    curPlaying = holder.play_pause;
                    isSpeaking = true;

                    tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onDone(String utteranceId) {
                            isSpeaking = false;
                            if(curPlaying!=null) {
                                curPlaying.setImageDrawable(context.getDrawable(R.drawable.ic_play_arrow_black_34dp));
                            }
                            holder.play_pause.setImageDrawable(context.getDrawable(R.drawable.ic_play_arrow_black_34dp));
                        }

                        @Override
                        public void onError(String utteranceId) {
                            isSpeaking = false;
                            if(curPlaying!=null) {
                                curPlaying.setImageDrawable(context.getDrawable(R.drawable.ic_play_arrow_black_34dp));
                            }
                            holder.play_pause.setImageDrawable(context.getDrawable(R.drawable.ic_play_arrow_black_34dp));
                        }

                        @Override
                        public void onStart(String utteranceId) {
                        }
                    });
                }

                // Firebase Analytics
                firebaseAnalytics.logEvent("ArticleAudio", bundle);
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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView actual_text;
        public LinearLayout main_layout;
        public ImageView play_pause, share_button;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.myth_title);
            actual_text = view.findViewById(R.id.actual_text);
            title.setMovementMethod(LinkMovementMethod.getInstance());
            play_pause = view.findViewById(R.id.play_pause_myth);
            main_layout = view.findViewById(R.id.main_layout);
            cardView = view.findViewById(R.id.cardView);
            title.setOnClickListener(this);
            actual_text.setOnClickListener(this);
            share_button = view.findViewById(R.id.share_button);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}