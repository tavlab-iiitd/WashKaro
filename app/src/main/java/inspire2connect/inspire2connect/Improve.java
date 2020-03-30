package inspire2connect.inspire2connect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.Manifest;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Improve extends AppCompatActivity {
    private static String fileName;
    int position;
    final private int STORAGE_PERMISSION = 1;
    final private int MIC_PERMISSION = 2;
    MediaPlayer mMediaPlayer, mMediaPlayer2;
    private static final String LOG_TAG = "AudioRecordTest";
    private MediaRecorder recorder;
    private Button rec_bt;
    FirebaseStorage storage;
    private DatabaseReference mDatabaseReference;
    StorageReference storageReference;
    private String curr_col = "RED";
    Uri file;
    StorageReference improve_ref, storage_ref;

    /*private void upload()
    {
        if(fileName!=null)
        {
            Uri file = Uri.fromFile(new File(fileName));
            uploadTask = improve_ref.putFile(file);
        }
    }*/
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Improve.this, new String[]{Manifest.permission.RECORD_AUDIO}, MIC_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Improve.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (mMediaPlayer2 != null && mMediaPlayer2.isPlaying()) {
            mMediaPlayer2.stop();
            mMediaPlayer2.release();
            mMediaPlayer2 = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (mMediaPlayer2 != null && mMediaPlayer2.isPlaying()) {
            mMediaPlayer2.stop();
            mMediaPlayer2.release();
            mMediaPlayer2 = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(Improve.this, "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(Improve.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case MIC_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(Improve.this, "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(Improve.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        upload();
        float speed = 0.75f;
        try {
            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.registered);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent i = new Intent(Improve.this, Text2Speech2.class);
        i.putExtra("position", Integer.toString(position));
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (mMediaPlayer2 != null) {
            mMediaPlayer2.stop();
            mMediaPlayer2.release();
            mMediaPlayer2 = null;
        }
        startActivity(i);
    }

    private void upload() {

        if (file != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("improve/" + UUID.randomUUID().toString());
            ref.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d("Download URL", uri.toString());
                                    mDatabaseReference.child("audio_improve").push().setValue(new audio_improve(uri.toString(), position + 1));
                                }
                            });
                            Toast.makeText(Improve.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Improve.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improve);
        requestPermission();
        mMediaPlayer = new MediaPlayer();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mMediaPlayer2 = new MediaPlayer();
        try {
            mMediaPlayer2 = MediaPlayer.create(getApplicationContext(), R.raw.startstop);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMediaPlayer2.start();
        Intent i = getIntent();
        position = Integer.parseInt(i.getStringExtra("position"));
        storage = FirebaseStorage.getInstance();
        storage_ref = storage.getReference();
        improve_ref = storage_ref.child("improve");
        rec_bt = (Button) findViewById(R.id.record_button);
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/recordaudio_improve.3gp";
        file = Uri.fromFile(new File(fileName));
        Log.d(LOG_TAG, fileName);
        rec_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer2 != null) {
                    mMediaPlayer2.stop();
                    mMediaPlayer2.release();
                    mMediaPlayer2 = null;
                }
                if (curr_col.equalsIgnoreCase("RED")) {
                    startRecording();
                    rec_bt.setBackgroundResource(R.drawable.green_record);
                    curr_col = "GREEN";
                } else {
                    stopRecording();
                    rec_bt.setBackgroundResource(R.drawable.red_record);
                    curr_col = "RED";
                }
            }
        });
    }
}