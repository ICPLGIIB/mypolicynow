package com.indicosmic.www.mypolicynow;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.indicosmic.www.mypolicynow.model.Agentinfo;
import com.indicosmic.www.mypolicynow.utils.CommonMethods;
import com.indicosmic.www.mypolicynow.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow.utils.MyValidator;
import com.indicosmic.www.mypolicynow.utils.SharedPrefManager;
import com.indicosmic.www.mypolicynow.utils.SingletonClass;
import com.indicosmic.www.mypolicynow.utils.UtilitySharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.indicosmic.www.mypolicynow.webservices.RestClient.ROOT_URL_BREAKIN;
import static com.indicosmic.www.mypolicynow.webservices.RestClient.URLUAT;

public class LoginActivity extends AppCompatActivity {


    EditText EdtEmail_Mobile, EdtPassword;
    TextView Tv_InvalidLogin, Tv_ForgetPassword;
    LinearLayout LayoutLogin;
    String StrEmail_Mobile, StrPassword;
    ProgressDialog myDialog;
    ImageView iv_logo;

    String IMEI;
    TelephonyManager mTelephonyManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {

        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        //Glide.with(this).load(R.drawable.logo_spinner).asGif().into(iv_logo);
        /*Glide.with(this)
                .load("https://www.mypolicynow.com/assets/images/logo_spinner.gif")
                .placeholder(R.drawable.logo_spinner)
                .into(iv_logo);*/

        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        IMEI = mTelephonyManager.getDeviceId();
        if (IMEI == null) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
            }
            IMEI = mTelephonyManager.getSubscriberId();
        }

        SingletonClass.getinstance().imei_no = IMEI;

        EdtEmail_Mobile = (EditText)findViewById(R.id.EdtEmail_Mobile);
        EdtEmail_Mobile.setText("8169972611");
        EdtPassword = (EditText)findViewById(R.id.EdtPassword);
        EdtPassword.setText("8169972611");

        Tv_InvalidLogin = (TextView)findViewById(R.id.Tv_InvalidLogin);
        Tv_ForgetPassword = (TextView)findViewById(R.id.Tv_ForgetPassword);

        myDialog = new ProgressDialog(this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);

        LayoutLogin = (LinearLayout) findViewById(R.id.LayoutLogin);
        LayoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.animator.move_left,R.animator.move_right);
                finish();*/

                if(isValidFields()){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(EdtPassword.getWindowToken(), 0);
                    StrEmail_Mobile = EdtEmail_Mobile.getText().toString();
                    StrPassword = EdtPassword.getText().toString();

                    requestforLogin();
                }
            }
        });

    }



    private boolean isValidFields() {

        boolean result = true;

        if (!MyValidator.isValidField(EdtEmail_Mobile)) {
            EdtEmail_Mobile.requestFocus();
            CommonMethods.DisplayToastWarning(getApplicationContext(),"Please Enter Valid Email Id or Mobile No.");
            result = false;
        }

       

        if (!MyValidator.isValidField(EdtPassword)) {
            EdtPassword.requestFocus();
            CommonMethods.DisplayToastWarning(getApplicationContext(),"Please Enter Valid Password. " +getResources().getString(R.string.error_password));
            result = false;
        }

       
        return  result;
        
    }

    private void requestforLogin() {
        //dialog.show();
        myDialog.show();

        StrEmail_Mobile = EdtEmail_Mobile.getText().toString();
        StrPassword = EdtPassword.getText().toString();

        String LOGIN_URL = ROOT_URL_BREAKIN+"login_pos";

        Log.d("LOGIN_URL",""+LOGIN_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,LOGIN_URL ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(myDialog!=null && myDialog.isShowing()){
                            myDialog.dismiss();
                        }
                        try {

                            Log.d("Response",""+response);
                            JSONObject jsonObject = new JSONObject(response);


                            //JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String status = jsonObject.getString("status");
                            String first_time_login = jsonObject.getString("message");


                            if (status.trim().contains("TRUE")) {

                                JSONObject pos_info_obj = jsonObject.getJSONObject("pos_login_data");

                                //creating a new user object
                                Agentinfo agent = new Agentinfo(
                                        pos_info_obj.getString("id"),
                                        pos_info_obj.getString("full_name"),
                                        pos_info_obj.getString("email"),
                                        pos_info_obj.getString("mobile_number"),
                                        pos_info_obj.getString("address"),
                                        pos_info_obj.getString("dob"),
                                        pos_info_obj.getString("imei"),
                                        pos_info_obj.getString("business_id"));



                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).agentLogin(agent);


                                UtilitySharedPreferences.setPrefs(getApplicationContext(),"UserName",  pos_info_obj.getString("full_name"));
                                UtilitySharedPreferences.setPrefs(getApplicationContext(),"UserId",  pos_info_obj.getString("id"));
                                UtilitySharedPreferences.setPrefs(getApplicationContext(),"UserEmail",  pos_info_obj.getString("email"));
                                UtilitySharedPreferences.setPrefs(getApplicationContext(),"UserMobile",  pos_info_obj.getString("mobile_number"));
                                UtilitySharedPreferences.setPrefs(getApplicationContext(),"UserAddress",  pos_info_obj.getString("address"));
                                UtilitySharedPreferences.setPrefs(getApplicationContext(),"UserDob",  pos_info_obj.getString("dob"));
                                UtilitySharedPreferences.setPrefs(getApplicationContext(),"UserDob",  pos_info_obj.getString("dob"));
                                CommonMethods.DisplayToastSuccess(LoginActivity.this, "Login Successful");

                                //Tv_InvalidLogin.setVisibility(View.GONE);

                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.animator.move_left, R.animator.move_right);
                                finish();

                            }else if(status.trim().contains("FALSE")){
                                JSONObject pos_info_obj = jsonObject.getJSONObject("data");

                                //creating a new user object
                                Agentinfo agent = new Agentinfo(
                                        pos_info_obj.getString("id"),
                                        pos_info_obj.getString("full_name"),
                                        pos_info_obj.getString("email"),
                                        pos_info_obj.getString("mobile_number"),
                                        pos_info_obj.getString("address"),
                                        pos_info_obj.getString("dob"),
                                        pos_info_obj.getString("imei"),
                                        pos_info_obj.getString("business_id"));

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).agentLogin(agent);


                                UtilitySharedPreferences.setPrefs(getApplicationContext(),"UserName",  pos_info_obj.getString("full_name"));
                                UtilitySharedPreferences.setPrefs(getApplicationContext(),"UserId",  pos_info_obj.getString("id"));
                                UtilitySharedPreferences.setPrefs(getApplicationContext(),"UserEmail",  pos_info_obj.getString("email"));
                                UtilitySharedPreferences.setPrefs(getApplicationContext(),"UserMobile",  pos_info_obj.getString("mobile_number"));
                                UtilitySharedPreferences.setPrefs(getApplicationContext(),"UserAddress",  pos_info_obj.getString("address"));
                                UtilitySharedPreferences.setPrefs(getApplicationContext(),"UserDob",  pos_info_obj.getString("dob"));
                                UtilitySharedPreferences.setPrefs(getApplicationContext(),"UserDob",  pos_info_obj.getString("dob"));

                                //Tv_InvalidLogin.setVisibility(View.GONE);
                                CommonMethods.DisplayToastSuccess(LoginActivity.this, "Login Successful");

                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.animator.move_left, R.animator.move_right);
                                finish();

                            }else if (status.trim().contains("Invalid")){

                                CommonMethods.DisplayToastError(LoginActivity.this, "You have entered an invalid username or password");
                                return;
                            }
                        } catch(JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if(myDialog!=null && myDialog.isShowing()){
                            myDialog.dismiss();
                        }

                        CommonMethods.DisplayToastWarning(getApplicationContext(),"Something went wrong. Please try after sometime.");
                        //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("username", StrEmail_Mobile);
                map.put("password", StrPassword);
                map.put("imei",IMEI);
                Log.d("", "posting params: "+map.toString());
                return map;
            }
        };

        int socketTimeout = 50000; //30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void API_LOGIN() {


        myDialog.show();

        StrEmail_Mobile = EdtEmail_Mobile.getText().toString();
        StrPassword = EdtPassword.getText().toString();

        String URL_FetchDetails = URLUAT + "signin";
        try {
            Log.d("URL_FetchDetails",URL_FetchDetails);

            ConnectionDetector cd = new ConnectionDetector(this);
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FetchDetails,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (myDialog != null && myDialog.isShowing()) {
                                    myDialog.dismiss();
                                }
                                try {
                                    Log.d("mainResponse", response);

                                    JSONObject jobj = new JSONObject(response);
                                    boolean status = jobj.getBoolean("status");
                                    //String message = jobj.getString("message");

                                    if (status) {
                                        /*JSONObject jsonObject = jobj.getJSONObject("data");
                                        String id = jsonObject.getString("id");
                                        String user_id = jsonObject.getString("user_id");
                                        String name = jsonObject.getString("name");
                                        String mobile_no = jsonObject.getString("mobile_no");
                                        String email_id = jsonObject.getString("email_id");
                                        String is_password_set = jsonObject.getString("is_password_set");
                                                */

                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"UserName",StrEmail_Mobile);

                                        //Tv_InvalidLogin.setVisibility(View.GONE);

                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(i);
                                        overridePendingTransition(R.animator.move_left, R.animator.move_right);
                                        finish();


                                    }else {
                                        CommonMethods.DisplayToastWarning(getApplicationContext(),"Invalid User / User Credentials!!");
                                    }

                                }catch (Exception e ){
                                    e.printStackTrace();
                                    if(myDialog!=null && myDialog.isShowing()){
                                        myDialog.dismiss();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (myDialog != null && myDialog.isShowing()) {
                            myDialog.dismiss();
                        }
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();
                        CommonMethods.DisplayToastWarning(getApplicationContext(),"Something went wrong. Please try again later.");
                    }
                }){

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username",StrEmail_Mobile);
                        params.put("password",StrPassword);

                        Log.d("ParrasOtpdata",params.toString() );

                        return params;
                    }



                } ;

                int socketTimeout = 50000; //30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

            } else {
                if(myDialog!=null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                CommonMethods.DisplayToast(this, "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }







    }

}
