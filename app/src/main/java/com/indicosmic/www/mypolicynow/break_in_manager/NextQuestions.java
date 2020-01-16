package com.indicosmic.www.mypolicynow.break_in_manager;

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
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.adapter.NextQuestionAdapter;
import com.indicosmic.www.mypolicynow.model.Agentinfo;
import com.indicosmic.www.mypolicynow.model.NextQuestionModel;
import com.indicosmic.www.mypolicynow.utils.CommonMethods;
import com.indicosmic.www.mypolicynow.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow.utils.SharedPrefManager;
import com.indicosmic.www.mypolicynow.utils.SingletonClass;
import com.indicosmic.www.mypolicynow.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow.webservices.Common;


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


public class NextQuestions extends AppCompatActivity {

    private static final String TAG = "";
    private ListView listview;
    private ArrayList<NextQuestionModel> nextquestion_modelsarray;
    // Temp save listItem position
    int position;
    NextQuestionAdapter nextQuestionAdapter;
    NextQuestionModel nextQuestionModel;
    ArrayList<String> imageList = new ArrayList<>();
    ProgressDialog progressDialog;

    Agentinfo agent;
    String agent_id;
    LayoutInflater layoutinflater;
    boolean doubleBackToExitPressedOnce = false;
    RelativeLayout snackbar;
    Button submit;
    EditText edtOdomenter_reading;
    String str_Odometer_reading;

    String question_status,odo_status;
    String IC_NAME="";
    File mImageFolder;
    JSONObject jsonodo;
    private String pictureImagePath = "";

    String captionString,text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_questions);

        init();
    }

    private void init() {
        listview = (ListView) findViewById(R.id.listview);
        submit = (Button) findViewById(R.id.submit);
        nextquestion_modelsarray = new ArrayList<NextQuestionModel>();
        layoutinflater = getLayoutInflater();
        // getSupportActionBar().setTitle("Next Inspection Check Points");
        ImageView back_btn_toolbar = (ImageView)findViewById(R.id.back_btn_toolbar);
        back_btn_toolbar.setVisibility(View.INVISIBLE);
        TextView til_text = (TextView)findViewById(R.id.til_text);
        til_text.setText("Inspection Images");

        snackbar = (RelativeLayout)findViewById(R.id.snackbar);

        agent = SharedPrefManager.getInstance(this).getAgent();
        agent_id = agent.getId();
        IC_NAME= UtilitySharedPreferences.getPrefs(getApplicationContext(),"IC_NAME");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while images are being uploaded");
        progressDialog.setCancelable(false);

        // Add a footer to the ListView
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.listview_footer,listview,false);
        listview.addHeaderView(header);

        edtOdomenter_reading = (EditText) header.findViewById(R.id.edtOdomenter_reading);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_Odometer_reading = edtOdomenter_reading.getText().toString().trim();

                // Log.d(TAG, "imageclick "+SingletonClass.getinstance().nextimage_counter);
                if (str_Odometer_reading.isEmpty() && str_Odometer_reading.equals("")) {
                    Snackbar snack = Snackbar.make(snackbar, "Please Enter odometer reading", Snackbar.LENGTH_LONG);
                    view = snack.getView();
                    TextView tv = (TextView) view.findViewById(R.id.snackbar_text);
                    tv.setTextColor(Color.RED);
                    snack.show();
                    return;
                }else {
                    if (!nextQuestionAdapter.CheckImageExist()){
                        Snackbar snack = Snackbar.make(snackbar, "You have not uploaded all required images", Snackbar.LENGTH_LONG);
                        view = snack.getView();
                        TextView tv = (TextView) view.findViewById(R.id.snackbar_text);
                        tv.setTextColor(Color.RED);
                        snack.show();
                        return;

                    }else {

                        sendOdometerReading();
                    }
                }
            }
        });

        showdata();
    }

    private void sendTosever() {

        progressDialog.show();


        //String URL_EXIT = ROOT_URL + "post/exit/capture";
        try {
            Log.d("URL", Common.URL_NEXT_QUESTION_SUBMIT);

            ConnectionDetector cd = new ConnectionDetector(this);
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,Common.URL_NEXT_QUESTION_SUBMIT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response: ", ""+response);
                        if(progressDialog!=null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                               String question_status = jsonObject.getString("status");

                                if (question_status.trim().contains("TRUE")){

                                    deleteFolder();
                                    Toast.makeText(NextQuestions.this, "Your data has been successfully uploaded to our server", Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(getApplicationContext(),UploadInspectionVideo_6.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    CommonMethods.DisplayToast(getApplicationContext(),"Something went wrong. Please try again.");

                                    //CommonMethods.DisplayToast(getApplicationContext(),"Please check the credentials and try again.");
                                }

                        }catch(Exception e){
                            e.printStackTrace();
                            if(progressDialog!=null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), "Some error occurred. Please try again later. ", Toast.LENGTH_LONG).show();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog!=null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), "Some error occurred. Please try again later. ", Toast.LENGTH_LONG).show();

                        Log.e("LOG", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        final JSONObject final_object = new JSONObject();
                        try {
                            final_object.put("question_answer", NextQuestionAdapter.jrray2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("user_answers", final_object.toString());
                        //map.put("images",encodedString);
                        Log.d("POSTDATA", "Inspection images params: " + map.toString());
                        return map;
                    }

                };

                int socketTimeout = 50000; //30 seconds - change to what you want
                RequestQueue requestQueue = Volley.newRequestQueue(NextQuestions.this);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
                requestQueue.add(jsonObjectRequest);

            } else {
                if(progressDialog!=null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                CommonMethods.DisplayToast(this, "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    /*private void sendTosever() {

        progressDialog.show();



        Log.d("URL",Common.URL_NEXT_QUESTION_SUBMIT);
        try {
           // Log.d("URL_EXIT",URL_EXIT);

            ConnectionDetector cd = new ConnectionDetector(this);
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {
                try {
                    final_object.put("question_answer", NextQuestionAdapter.jrray2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject params = new JSONObject();
                params.put("user_answers",  final_object.toString());


                final String requestBody = params.toString();
                Log.d("RequestBody",""+requestBody);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Common.URL_NEXT_QUESTION_SUBMIT, requestBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response: ", String.valueOf(response));
                        if(progressDialog!=null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try{
                            if (response != null) {
                                //JSONObject jsonObject = new JSONObject(response);
                                String question_status = response.getString("status");

                                if (question_status.trim().contains("TRUE")){

                                    deleteFolder();
                                    Toast.makeText(NextQuestions.this, "Your data has been successfully uploaded to our server", Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(getApplicationContext(),UploadInspectionVideo_6.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    CommonMethods.DisplayToast(getApplicationContext(),"Something went wrong. Please try again.");

                                    //CommonMethods.DisplayToast(getApplicationContext(),"Please check the credentials and try again.");
                                }
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog!=null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        CommonMethods.DisplayToast(getApplicationContext(),"Something went wrong. Please try again.");

                        Log.e("LOG", error.toString());
                    }
                });

                int socketTimeout = 50000; //30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(0, -1, 0);
                jsonObjectRequest.setRetryPolicy(policy);
                // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(jsonObjectRequest);

            } else {
                if(progressDialog!=null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                CommonMethods.DisplayToast(this, "Please check your internet connection");
            }
        } catch (Exception e) {
            if(progressDialog!=null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            e.printStackTrace();

        }

    }
*/


    private void showdata() {

        StringRequest request = new StringRequest(Request.Method.POST, Common.URL_NEXT_QUESTION_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("next question list",""+response);
                    String status = jsonObject.getString("status");

                    if (status.trim().contains("TRUE")){

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject ques_object = jsonArray.getJSONObject(i);

                            String main_question = ques_object.getString("name");
                            String str_is_mand  = ques_object.getString("is_mand");

                            nextQuestionModel = new NextQuestionModel();

                            nextQuestionModel.setQuestion(main_question);
                            nextQuestionModel.setIs_mand(str_is_mand);

                            nextQuestionModel.setImagePath("Null");

                            nextquestion_modelsarray.add(nextQuestionModel);

                            nextQuestionAdapter = new NextQuestionAdapter(nextquestion_modelsarray, NextQuestions.this);

                            listview.setAdapter(nextQuestionAdapter);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                //Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
            }
        });

//        RequestQueue rQueue = Volley.newRequestQueue(NextQuestions.this);
//        rQueue.add(request);

        Volley.newRequestQueue(getApplicationContext()).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    public void captureImage(int pos, String imageName) {

        position = pos;
        //imageTempName = imageName;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageTempName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        mImageFolder = new File(Environment.getExternalStorageDirectory()+"ClaimPic");
        mImageFolder = new File(storageDir, "ClaimPic");
        if (!mImageFolder.exists()){
            mImageFolder.mkdirs();
        }

        pictureImagePath = mImageFolder.getAbsolutePath() + "/" + imageTempName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, 90);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == 90) {

                File imgFile = new File(pictureImagePath);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                    captionString = sdf.format(new Date());
                    text = "Date:" +captionString+ "\nLat:" + SingletonClass.getinstance().latitude+ "\nLong:" +SingletonClass.getinstance().longitude+ "\nIMEI:"+agent.getImei();
                    String[] str = text.split("\n");

                    if (myBitmap != null) {
                        Bitmap dest = null;
                        try {
                            dest = myBitmap.copy(myBitmap.getConfig(), true);
                        } catch (OutOfMemoryError e1) {
                            Log.e("Exception", e1.getMessage());
                            e1.printStackTrace();
                        } catch (Error e) {
                            Log.e("Exception", e.getMessage());
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
                            cs.drawText(Oneline, 20f, (index + 1) * textHeight + 25f,
                                    tPaint);
                            index++;
                        }

                        try {
                            @SuppressLint("SimpleDateFormat")
                            String timeStamp = new SimpleDateFormat("MMdd_HHmmss").format(new Date());
                            String imageTempName = timeStamp + ".png";

                            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/ClaimPic", imageTempName));
                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ClaimPic" + "/" + imageTempName);

                            nextQuestionAdapter.setImageInItem(position, dest, file.getAbsolutePath());
                            // Toast.makeText(this, "path"+fos.toString(), Toast.LENGTH_LONG).show();
                            dest.compress(Bitmap.CompressFormat.JPEG, 25, fos);
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

        if (doubleBackToExitPressedOnce){
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to back", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void sendOdometerReading() {

        StringRequest request = new StringRequest(Request.Method.POST, Common.URL_SUBMIT_ODOMETER_READING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //progressDialog.dismiss();
                try {

                    JSONObject jsonObject = new JSONObject(response);

                     odo_status = jsonObject.getString("status");
                    if (odo_status.trim().contains("TRUE")){
                        /*JSONArray question_array = NextQuestionAdapter.jrray2;
                        Log.d("Array",""+question_array);
                */
                        sendTosever();
                     //   Toast.makeText(NextQuestions.this, "Success", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Some error occurred. Please try again later. ", Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
               // progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Some error occurred. Please try again later. ", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                try {
                    jsonodo = new JSONObject();
                    JSONObject odojson = new JSONObject();
                    odojson.put("odometer", str_Odometer_reading);
                    odojson.put("pos_id", SingletonClass.getinstance().agent_id);
                    odojson.put("break_in_case_id", SingletonClass.getinstance().break_in_case_id);
                    jsonodo.put("odometer_reading",odojson);

                    Log.d("odoadterjson",jsonodo.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Map<String, String> map = new HashMap<String, String>();
                map.put("odometer_reading",jsonodo.toString());
                //map.put("images",encodedString);
                Log.d("POSTDATA", "odoparams params: " + map.toString());
                return map;
            }

        };
        Volley.newRequestQueue(getApplicationContext()).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

//        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        RequestQueue rQueue = Volley.newRequestQueue(NextQuestions.this);
//        rQueue.add(request);

    }




    public void deleteFolder(){
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/ClaimPic");

     //   Toast.makeText(this, "dir="+dir, Toast.LENGTH_LONG).show();
        Log.d("deleteFolder: ",dir.toString());

        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
    }
}
