package inspire2connect.inspire2connect.mythGuidelineUpdates;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Serializable;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.urlActivity;

public class guidelineViewActivity extends BaseActivity implements Serializable {
    public TextView detailed_title;
    public TextView detailed_text;
    public ImageButton detailed_play_button, detailed_share_button;
    public ImageButton sourceBtn;
    boolean isSpeaking;
    TextToSpeech mTTS;
    private String title;
    private String content;
    private Context context;

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
        detailed_play_button.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_34dp));
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.ERROR) {
                    isSpeaking = false;
                    mTTS.stop();
                    detailed_play_button.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_34dp));
                } else if (status == TextToSpeech.SUCCESS) {
                    mTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            if (!isSpeaking) {
                                isSpeaking = true;
                                detailed_play_button.setImageDrawable(getDrawable(R.drawable.ic_pause_black_34dp));
                            }
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            if (isSpeaking) {
                                isSpeaking = false;
                                mTTS.stop();
                                detailed_play_button.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_34dp));
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_guideline_view);
        detailed_title = findViewById(R.id.detailed_title);
        detailed_title.setMovementMethod(LinkMovementMethod.getInstance());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        detailed_text = findViewById(R.id.detailed_text);
        detailed_text.setMovementMethod(new ScrollingMovementMethod());
        detailed_play_button = findViewById(R.id.detailed_play_button);
        detailed_share_button = findViewById(R.id.detailed_share);
        isSpeaking = false;
        final Intent i = getIntent();
        final String redirect_url = i.getStringExtra("redirect_url");

        title = i.getStringExtra("detailed_title");
        content = i.getStringExtra("detailed_text");

        detailed_title.setText(title);
        detailed_text.setText(content);

        context = this;

        sourceBtn = findViewById(R.id.detailed_source);
        sourceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, urlActivity.class);
                i.putExtra("url", redirect_url);
                i.putExtra("name", getString(R.string.information_link ));

                // Firebase Analytics
                Bundle bundle = new Bundle();
                bundle.putString("UID", firebaseUser.getUid());
                bundle.putString("ArticleTitle", title);
                bundle.putString("ArticleURL", redirect_url);
                firebaseAnalytics.logEvent("ArticleSourceChecked", bundle);

                startActivity(i);
            }
        });

        detailed_share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(i.getStringExtra("detailed_text"));
            }
        });

        detailed_play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Firebase Analytics
                Bundle bundle = new Bundle();
                bundle.putString("UID", firebaseUser.getUid());
                bundle.putString("ArticleTitle", title);
                bundle.putString("ArticleURL", redirect_url);

                if (isSpeaking) {
                    // Firebase Analytics
                    bundle.putString("ArticleAudioStatus", "Audio OFF");

                    isSpeaking = false;
                    mTTS.stop();
                    detailed_play_button.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_34dp));
                } else {
                    mTTS.speak(title + " " + content, TextToSpeech.QUEUE_FLUSH, null);
                    isSpeaking = true;
                    detailed_play_button.setImageDrawable(getDrawable(R.drawable.ic_pause_black_34dp));
                }

                // Firebase Analytics
                firebaseAnalytics.logEvent("ArticleAudio", bundle);

            }
        });

    }

    public void share(String toShare) {
        // Firebase Analytics
        Bundle bundle = new Bundle();
        bundle.putString("UID", firebaseUser.getUid());
        bundle.putString("ArticleTitle", title);
        firebaseAnalytics.logEvent("ArticleShared", bundle);

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        Log.d("sharing", toShare);
        Spanned shareBody = Html.fromHtml(toShare);
        String share = shareBody.toString();
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, share);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
