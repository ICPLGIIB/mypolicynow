package com.indicosmic.mypolicynow_app.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.indicosmic.mypolicynow_app.R;
import com.indicosmic.mypolicynow_app.utils.CommonMethods;
import com.indicosmic.mypolicynow_app.utils.MyValidator;
import com.indicosmic.mypolicynow_app.utils.UtilitySharedPreferences;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.indicosmic.mypolicynow_app.utils.CommonMethods.md5;
import static com.indicosmic.mypolicynow_app.webservices.RestClient.ROOT_URL2;


public class LoginActivity extends AppCompatActivity {


    EditText EdtEmail_Mobile;
    TextView Tv_InvalidLogin, Tv_ForgetPassword;
    LinearLayout LayoutLogin;
    String StrEmail_Mobile, StrPassword;
    ProgressDialog myDialog;
    ImageView iv_logo;
    String POS_TOKEN="";
    String StrMobile,StrEmail;

    String IMEI;
    TelephonyManager mTelephonyManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {


        EdtEmail_Mobile = (EditText)findViewById(R.id.EdtEmail_Mobile);
        EdtEmail_Mobile.setText("8169972611");

        Tv_InvalidLogin = (TextView)findViewById(R.id.Tv_InvalidLogin);
        Tv_ForgetPassword = (TextView)findViewById(R.id.Tv_ForgetPassword);

        myDialog = new ProgressDialog(LoginActivity.this);
        myDialog.setMessage("Please wait...");


        LayoutLogin = (LinearLayout) findViewById(R.id.LayoutLogin);
        LayoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(getApplicationContext(), MainActivity_1.class);
                startActivity(i);
                overridePendingTransition(R.animator.move_left,R.animator.move_right);
                finish();*/

                if(isValidFields()){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(EdtEmail_Mobile.getWindowToken(), 0);
                    }
                    StrEmail_Mobile = EdtEmail_Mobile.getText().toString();

                    requestforLogin();
                }
            }
        });

    }

    boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidFields() {

        boolean result = true;

        if (!MyValidator.isValidEmail_Mobile(EdtEmail_Mobile)) {
            EdtEmail_Mobile.requestFocus();
            CommonMethods.DisplayToastWarning(getApplicationContext(),"Please Enter Valid Email Id or Mobile No.");
            result = false;
        }

        return  result;
        
    }

    private void requestforLogin() {
        //dialog.show();
        myDialog.setCancelable(true);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
        StrEmail_Mobile = EdtEmail_Mobile.getText().toString();

        if(isEmailValid(StrEmail_Mobile)){
            StrEmail = StrEmail_Mobile;
            StrMobile = "8169972611";
        }else {
            StrMobile = StrEmail_Mobile;
            StrEmail = "";
        }

        StrMobile = "8169972611";



        String LOGIN_URL = ROOT_URL2+"getpassword";

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
                            JSONObject jsonresponse = new JSONObject(response);
                            JSONObject data_obj = jsonresponse.getJSONObject("data");

                            POS_TOKEN  = data_obj.getString("token");
                            UtilitySharedPreferences.setPrefs(getApplicationContext(),"POS_TOKEN",POS_TOKEN);

                            /*UtilitySharedPreferences.setPrefs(getApplicationContext(),"QuotationFor","Bike");
                            if(POS_TOKEN!=null && !POS_TOKEN.equalsIgnoreCase("")){
                                Intent i = new Intent(getApplicationContext(), QuotationActivity_2.class);
                                i.putExtra("pos_token", POS_TOKEN);
                                startActivity(i);
                                finish();
                            }*/

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
                map.put("mobile_number", StrMobile);
                map.put("email_id", StrEmail);
                if(StrMobile!=null && !StrMobile.equalsIgnoreCase("")){
                    map.put("access_key", md5(StrMobile));
                }else if(StrEmail!=null && !StrEmail.equalsIgnoreCase("")){
                    map.put("access_key", md5(StrEmail));
                }
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


}
