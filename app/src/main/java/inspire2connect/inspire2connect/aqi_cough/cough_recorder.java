package inspire2connect.inspire2connect.aqi_cough;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import java.util.Calendar;
import java.util.Date;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.utils.BaseActivity;

public class cough_recorder extends BaseActivity {
    Button recordButton1, stopButton1, playFileButton1, stopFileButton1, submitButton1;
    String pathSave1 = "";
    MediaRecorder mediaRecorder1;
    MediaPlayer mediaPlayer1;
    UploadTask uploadTask;

    final int REQUEST_PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cough_recorder);
        setStatusBarGradiant(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if(!checkPermissionFromDevice())
            requestPermission();

        recordButton1 = (Button) findViewById( R.id.recordButton1);
        stopButton1 = (Button) findViewById(R.id.stopButton1);
        playFileButton1 = (Button) findViewById(R.id.playFileButton1);
        stopFileButton1 = (Button) findViewById(R.id.stopFileButton1);

        submitButton1 = (Button) findViewById(R.id.submitButton1);

        submitButton1.setOnClickListener(new android.view.View.OnClickListener(){

            @Override
            public void onClick(android.view.View view) {

//                FirebaseStorage storage = FirebaseStorage.getInstance();
//
//                StorageReference storageRef = storage.getReference();

                //System.out.println("PATHSAVEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE" + pathSave1);

                Uri file = Uri.fromFile(new File(pathSave1));
                Date currentTime = Calendar.getInstance().getTime();

//                StorageReference coughRef = storageRef.child("coughAudio/" + currentTime + file.getLastPathSegment());

//                uploadTask = coughRef.putFile(file);

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener () {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot> () {
                    @Override
                    public void onSuccess(UploadTask    .TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                    }
                });

//                Intent youtubeVideo = new Intent(cough_recorder.this, YoutubePlayVid.class);
//                startActivity(youtubeVideo);
            }

        });


        recordButton1.setOnClickListener(new android.view.View.OnClickListener(){
            @Override
            public void onClick(android.view.View view) {
                System.out.println("GOING IN");
                if(checkPermissionFromDevice()){
                    //pathSave1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                    //        .getAbsolutePath()+"/"
                    //        + UUID.randomUUID().toString()+"_audio_record.3gp"; //CHANGE THIS

                    pathSave1 = getExternalFilesDir(pathSave1).getAbsolutePath() + "/" + UUID.randomUUID().toString()+"_audio_record.3gp";

                    System.out.println("HOLAAAAAAAAAAAAAAAAAAAAAAAAA " + pathSave1);
                    setupMediaRecorder();
                    try{
                        mediaRecorder1.prepare();
                        mediaRecorder1.start();
                        stopButton1.setEnabled(true);
                        playFileButton1.setEnabled(false);
                        stopFileButton1.setEnabled(false);
                        recordButton1.setEnabled(false);
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }

                    Toast.makeText(cough_recorder.this, "Please COUGH!", Toast.LENGTH_SHORT).show();
                }
                else{
                    requestPermission();
                }

            }
        });

        stopButton1.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                mediaRecorder1.stop();
                stopButton1.setEnabled(false);
                playFileButton1.setEnabled(true);
                stopFileButton1.setEnabled(false);
            }
        });

        playFileButton1.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(android.view.View view) {
                stopFileButton1.setEnabled(true);
                stopButton1.setEnabled(false);
                recordButton1.setEnabled(false);

                mediaPlayer1 = new MediaPlayer();

                try{
                    mediaPlayer1.setDataSource(pathSave1);
                    mediaPlayer1.prepare();
                }catch (IOException e){
                    e.printStackTrace();
                }
                Toast.makeText(cough_recorder.this, "Playing...", Toast.LENGTH_SHORT).show();

            }
        });

        stopFileButton1.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                stopFileButton1.setEnabled(false);
                recordButton1.setEnabled(true);
                stopButton1.setEnabled(false);
                playFileButton1.setEnabled(true);

                if(mediaPlayer1 != null){
                    mediaPlayer1.stop();
                    mediaPlayer1.release();
                    setupMediaRecorder();
                }
            }
        });



    }

    private void setupMediaRecorder() {
        mediaRecorder1= new MediaRecorder();
        mediaRecorder1.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder1.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder1.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder1.setOutputFile(pathSave1);

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
        },REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        switch(requestCode){
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();

            }
        }
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;


    }

}

