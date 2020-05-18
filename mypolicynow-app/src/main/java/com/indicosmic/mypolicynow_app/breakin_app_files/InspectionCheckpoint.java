package com.indicosmic.mypolicynow_app.breakin_app_files;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.indicosmic.mypolicynow_app.R;
import com.indicosmic.mypolicynow_app.model.Agentinfo;
import com.indicosmic.mypolicynow_app.utils.GPSTracker;
import com.indicosmic.mypolicynow_app.utils.JSONClass;
import com.indicosmic.mypolicynow_app.utils.UtilitySharedPreferences;
import com.indicosmic.mypolicynow_app.webservices.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class InspectionCheckpoint extends AppCompatActivity {

//    private static final String TAG = "";
    private ListView listview;
    private ArrayList<CheckPointListModel> question_modelsarray;
    int position;
    CheckpointListAdapter checkpointListAdapter;
    CheckPointListModel question_model;
    ArrayList<String> imageList = new ArrayList<>();
    ProgressDialog progressDialog;
    boolean doubleBackToExitPressedOnce = false;
    RelativeLayout snackbar;
    GPSTracker gps;
    double latitude;
    double longitude;
    Agentinfo agent;
    String agent_id,QuoteLink;
    File mImageFolder;

    LayoutInflater layoutinflater;
    private String pictureImagePath = "";
    Button submit;

    String text;

    String captionString;

    @SuppressLint({"MissingPermission", "HardwareIds"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list_view);




        gps = new GPSTracker(InspectionCheckpoint.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            UtilitySharedPreferences.setPrefs(getApplicationContext(),"Latitude",String.valueOf(latitude));
            UtilitySharedPreferences.setPrefs(getApplicationContext(),"Longitude",String.valueOf(longitude));



        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        listview = (ListView) findViewById(R.id.listview);
        snackbar = (RelativeLayout)findViewById(R.id.snackbar);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        question_modelsarray = new ArrayList<CheckPointListModel>();
        layoutinflater = getLayoutInflater();

        ImageView back_btn_toolbar = (ImageView)findViewById(R.id.back_btn_toolbar);
        back_btn_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView til_text = (TextView)findViewById(R.id.til_text);
        til_text.setText("Inspection Check Points");

        agent_id = UtilitySharedPreferences.getPrefs(getApplicationContext(),"PosId");
        QuoteLink  = UtilitySharedPreferences.getPrefs(getApplicationContext(),"QuoteLink");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while data is being uploaded");
        progressDialog.setCancelable(false);

        showdata();

        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                   if (!checkpointListAdapter.getAnswer()){
                    Snackbar snack = Snackbar.make(snackbar, "Please answer all the questions", Snackbar.LENGTH_LONG);
                    view = snack.getView();
                    TextView tv = (TextView) view.findViewById(R.id.snackbar_text);
                    tv.setTextColor(Color.RED);
                    snack.show();

                }else {
                    sendTosever();
                }

           }
        });
    }

    private void sendTosever() {
        progressDialog.show();
        Log.d("SubmitReview", Common.URL_SUBMIT_QUESTION);
        StringRequest request = new StringRequest(Request.Method.POST, Common.URL_SUBMIT_QUESTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progressDialog!=null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                try {
                    Log.d("response", response);
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.trim().contains("TRUE")){
                        String break_in_case_id = jsonObject.getString("break_in_case_id");

                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"BreakInCaseId",break_in_case_id);


                        Intent i = new Intent(getApplicationContext(), ActivityInspectionImages.class);
                        startActivity(i);
                        overridePendingTransition(R.animator.move_left,R.animator.move_right);
                        finish();


                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                    if(progressDialog!=null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(progressDialog!=null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                volleyError.printStackTrace();
                Toast.makeText(getApplicationContext(), "Some error occurred. Please try again later. ", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                final JSONObject final_object = new JSONObject();
                try {
                    final_object.put("question_answer", JSONClass.jrray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Map<String, String> map = new HashMap<String, String>();
                map.put("user_answers", final_object.toString());
                Log.d("POSTDATA", "question params: " + map.toString());
                return map;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(InspectionCheckpoint.this);
        rQueue.add(request);
    }


    private void showdata() {
                 progressDialog.show();
                StringRequest request = new StringRequest(Request.Method.POST, Common.URL_QUESTION_LIST, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(progressDialog!=null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.trim().contains("TRUE")){

                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject ques_object = jsonArray.getJSONObject(i);

                                String main_question = ques_object.getString("question");
                                JSONObject question_object = ques_object.getJSONObject("answers_obj");

                                String safe = question_object.getString("0");
                                String scratch = question_object.getString("1");
                                String pressed = question_object.getString("2");
                                String broken = question_object.getString("3");

                                question_model = new CheckPointListModel();

                                question_model.setQuestion(main_question.toUpperCase());
                                question_model.setSafe("Safe");
                                question_model.setScrach("Scratch");
                                question_model.setPressed("Pressed");
                                question_model.setBroken("Broken");
                                question_model.setAnswer("Null");
                                question_model.setId(i);

                                question_modelsarray.add(question_model);

                                checkpointListAdapter = new CheckpointListAdapter(question_modelsarray, InspectionCheckpoint.this);

                                listview.setAdapter(checkpointListAdapter);
                            }
                          }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(progressDialog!=null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), "Some error occurred. Please try again later. ", Toast.LENGTH_LONG).show();
                    }
                });

               Volley.newRequestQueue(getApplicationContext()).add(request);
                request.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            }


    public void captureImage(int pos, String imageName) {

        position = pos;
        //imageTempName = imageName;,
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageTempName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        mImageFolder = new File(storageDir, "ClaimPic");
        if (!mImageFolder.exists()){
            mImageFolder.mkdirs();
        }
        pictureImagePath = mImageFolder.getAbsolutePath() + "/" + imageTempName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 0);
        startActivityForResult(cameraIntent, 100);

    }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode != Activity.RESULT_CANCELED) {
                if (requestCode == 100) {
                    //onCaptureImageResult(data);
                    File imgFile = new File(pictureImagePath);
                    if (imgFile.exists()) {


                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                    captionString = sdf.format(new Date());
                    text = "Date:" +captionString+ "\nLat:" +latitude+ "\nLong:" +longitude+ "\nQuote No.:"+QuoteLink;
                    String[] str = text.split("\n");



                    if (myBitmap != null) {
                        Bitmap dest = null;
                        try {
                            dest = myBitmap.copy(myBitmap.getConfig(), true);
                        } catch (OutOfMemoryError e1) {
                           // Log.e("Exception", e1.getMessage());
                            e1.printStackTrace();
                        } catch (Error e) {
                           // Log.e("Exception", e.getMessage());
                            e.printStackTrace();
                        }


                        Canvas cs = new Canvas(dest);
                        Typeface tf = Typeface.create("Verdana", Typeface.BOLD);
                        Paint tPaint = new Paint();
                        tPaint.setAntiAlias(true);
                        tPaint.setTextSize(60);
                        tPaint.setTextAlign(Paint.Align.LEFT);
                        tPaint.setTypeface(tf);
                        tPaint.setColor(Color.RED);
                        tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                        cs.drawBitmap(myBitmap, 0f, 0f, null);
                        float textHeight = tPaint.measureText("yY");
                        int index = 0;
                        for (String Oneline : str) {
                            cs.drawText(Oneline, 20f, (index + 1) * textHeight + 25f, tPaint);
                            index++;
                        }

                        try {

                            String timeStamp = new SimpleDateFormat("MMdd_HHmmss").format(new Date());
                            String imageTempName = timeStamp + ".png";

                            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/ClaimPic", imageTempName));
                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ClaimPic" + "/" + imageTempName);


                            checkpointListAdapter.setImageInItem(position, dest, file.getAbsolutePath());
                            // Toast.makeText(this, "path"+fos.toString(), Toast.LENGTH_LONG).show();
                            dest.compress(Bitmap.CompressFormat.JPEG, 40, fos);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                  }

                }
            }
        }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }
}
