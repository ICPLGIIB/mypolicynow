package com.indicosmic.www.mypolicynow.break_in_manager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iceteck.silicompressorr.SiliCompressor;
import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.utils.AppUtill;
import com.indicosmic.www.mypolicynow.utils.SingletonClass;
import com.indicosmic.www.mypolicynow.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow.webservices.RestClient;


import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.File;
import java.net.URISyntaxException;
import java.util.UUID;

import static com.indicosmic.www.mypolicynow.webservices.RestClient.ROOT_URL_BREAKIN;


public class UploadInspectionVideo_6 extends AppCompatActivity implements View.OnClickListener {

    TextView videoImageView;

    VideoView compressVideoView;


    LinearLayout layout_upload;

    Button btnUploadVideo;

     TextView homeImageView;

    LinearLayout layout_home;

    ProgressDialog progressDialog;
    String IC_NAME="";

    Uri capturedUri = null;
    String BreakInCaseId="";
    private static final int RESQUEST_TAKE_VIDEO = 200;
    private static final String TAG = "UploadInspectionVideo_6";

    String compressedVideo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_uploading);


        init();
    }

    private void init() {

        ImageView back_btn_toolbar = (ImageView)findViewById(R.id.back_btn_toolbar);
        back_btn_toolbar.setVisibility(View.VISIBLE);
        TextView til_text = (TextView)findViewById(R.id.til_text);

        back_btn_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        til_text.setText("Inspection Video");

        videoImageView = (TextView)findViewById(R.id.videoImageView);
        compressVideoView = (VideoView)findViewById(R.id.compressVideoView);
        layout_upload = (LinearLayout)findViewById(R.id.layout_upload);
        btnUploadVideo  = (Button) findViewById(R.id.btnUploadVideo);
        homeImageView  = (TextView) findViewById(R.id.homeImageView);
        layout_home = (LinearLayout)findViewById(R.id.layout_home);
        videoImageView.setOnClickListener(this);
        btnUploadVideo.setOnClickListener(this);
        IC_NAME= UtilitySharedPreferences.getPrefs(getApplicationContext(),"IC_NAME");
        BreakInCaseId = UtilitySharedPreferences.getPrefs(getApplicationContext(),"BreakInCaseId");
        SingletonClass.getinstance().break_in_case_id = BreakInCaseId;


        if(IC_NAME.contains("Shriram")||IC_NAME.contains("SHRIRAM")){
            layout_home.setVisibility(View.GONE);
        }else {
            layout_home.setVisibility(View.VISIBLE);
        }

        layout_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    private void captureVideo() {

        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {

            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            capturedUri = Uri.fromFile(AppUtill.createVideoPath());
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedUri);
            Log.d(TAG, "VideoUri: " + capturedUri.toString());
            startActivityForResult(takeVideoIntent, RESQUEST_TAKE_VIDEO);

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //verify if the image was gotten successfully
        if (requestCode == RESQUEST_TAKE_VIDEO && resultCode == RESULT_OK) {
//                if (capturedUri != null) {
            //create destination directory
            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/ClaimVideo");

            if (f.mkdirs() || f.isDirectory()) {
                //compress and output new video specs
                new VideoCompressAsyncTask(this).execute(capturedUri.toString(), f.getPath());
            }

            //    }
        }

    }


    class VideoCompressAsyncTask extends AsyncTask<String, String, String> {

        Context mContext;

        public VideoCompressAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... paths) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(mContext).compressVideo(Uri.parse(paths[0]), paths[1]);
                Log.d(TAG, "doInBackground: " + filePath);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return filePath;

        }


        @Override
        protected void onPostExecute(String compressedFilePath) {
            super.onPostExecute(compressedFilePath);
            progressDialog.dismiss();

            File imageFile = new File(compressedFilePath);

            compressedVideo = imageFile.getPath();

            compressVideoView.setVisibility(View.VISIBLE);

            Log.d(TAG, "onPostExecute: " + imageFile);

            compressVideoView.setVideoPath(imageFile.getPath());
            compressVideoView.setVisibility(View.VISIBLE);
            layout_upload.setVisibility(View.VISIBLE);
            compressVideoView.start();

//            MediaController mc = new MediaController(getApplicationContext());
//            compressVideoView.setMediaController(mc);
//
//            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            lp.gravity = Gravity.BOTTOM;
//            mc.setLayoutParams(lp);
//
//            ((ViewGroup) mc.getParent()).removeView(mc);
//
//            ((FrameLayout) findViewById(R.id.controllerAnchor)).addView(mc);


//            float length = imageFile.length() / 1024f; // Size in KB
//            String value;
//            if(length >= 1024)
//                value = length/1024f+" MB";
//            else
//                value = length+" KB";
//            String text = String.format(Locale.US, "%s\nName: %s\nSize: %s", getString(R.string.video_compression_complete), imageFile.getName(), value);
//            compressionMsg.setVisibility(View.GONE);
//            picDescription.setVisibility(View.VISIBLE);
//            picDescription.setText(text);
            //   Log.i("Silicompressor", "Path: "+compressedFilePath);
        }
    }


    private void UploadVideoApi(String compressedVideoPath) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        String URL_UPLOAD_VIDEO = ROOT_URL_BREAKIN+"updateBreakInVideo";
        Toast.makeText(this, "" + compressedVideoPath, Toast.LENGTH_LONG).show();
        if (compressedVideoPath != null) {

            progressDialog.setMessage("Uploading your Video");

            Log.d("Uploading File", "Ur here");
            Log.d("break_in_case_id", "--->"+SingletonClass.getinstance().break_in_case_id);
            Log.d("VideoUploadUrl",URL_UPLOAD_VIDEO);
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, URL_UPLOAD_VIDEO)
                        .addFileToUpload(compressedVideoPath, "file") //Adding file
                        .addParameter("break_in_case_id", BreakInCaseId) // BreakInCaseId
                        //.setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(UploadInfo uploadInfo) {

                            }

                            @Override
                            public void onError(UploadInfo uploadInfo, Exception e) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(getApplicationContext(), "Error Uploading Video. Please select it from gallery and upload it again.", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                                Log.d("ServerUploadingResponse", ""+serverResponse);
                                Log.d("ServerUploadingResponse", ""+serverResponse.getHttpCode());
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                if(serverResponse.getHttpCode()==200) {
                                    Toast.makeText(getApplicationContext(), "Video Uploaded Successfully", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getApplicationContext(), Home.class);
                                    startActivity(i);
                                    finish();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Error in Uploading Video", Toast.LENGTH_LONG).show();
                                }
                                //Toast.makeText(.this, "Your data has been successfully uploaded to our server", Toast.LENGTH_SHORT).show();


                            }

                            @Override
                            public void onCancelled(UploadInfo uploadInfo) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(getApplicationContext(), "Uploading Cancelled. Please select it from gallery and upload it again.", Toast.LENGTH_LONG).show();
                            }
                        })
                        .startUpload(); //Starting the upload

            } catch (Exception exc) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.d("Uploading Exception", "" + exc.getMessage());
                //Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }


            //Toast.makeText(VideoCapturingActivity.this, "Time spent: " + l, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.videoImageView) {
            captureVideo();
        } else if (id == R.id.btnUploadVideo) {
            UploadVideoApi(compressedVideo);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),Home.class);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }
}





