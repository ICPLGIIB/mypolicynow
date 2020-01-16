package com.indicosmic.www.mypolicynow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.indicosmic.www.mypolicynow.model.Agentinfo;
import com.indicosmic.www.mypolicynow.utils.CommonMethods;
import com.indicosmic.www.mypolicynow.utils.SharedPrefManager;
import com.indicosmic.www.mypolicynow.utils.SingletonClass;
import com.indicosmic.www.mypolicynow.webservices.RestClient;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class OtpActivity extends AppCompatActivity {

    SmsVerifyCatcher smsVerifyCatcher;
    public static EditText inputOtp;
    Button btn_verify_otp;
    TextView txt_edit_mobile;

    ProgressDialog progressDialog;

    private static final String TAG = "OtpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        inputOtp = (EditText) findViewById(R.id.inputOtp);
        btn_verify_otp = (Button) findViewById(R.id.btn_verify_otp);
        txt_edit_mobile = (TextView) findViewById(R.id.txt_edit_mobile);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");


        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                inputOtp.setText(code);//set code in edit text
                //then you can send verification code to server
            }
        });

        starCountdown();
        btn_verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  String message = getIntent().getStringExtra("MESSAGE");

                String otp = inputOtp.getText().toString().trim();
                SingletonClass.getinstance().strOtp = otp;

                Log.d(TAG, "strotp: "+SingletonClass.getinstance().strOtp);

                if(otp.isEmpty() || otp.equals(""))
                {
                    inputOtp.setError("Enter OTP");
                    inputOtp.requestFocus();
                    return;
                }
                if (SingletonClass.getinstance().otp != null && !SingletonClass.getinstance().otp.isEmpty() && !SingletonClass.getinstance().otp.equals("")){
                    if(otp.contains(SingletonClass.getinstance().otp))
                    {
                        SuccessDeviceChanged();
                    }
                    if (!otp.contains(SingletonClass.getinstance().otp)){
                        inputOtp.setError("Invalid Otp");
                        inputOtp.requestFocus();
                        return;
                    }
                }

          }

      });

    }


    private void starCountdown() {
        CountDownTimer cT =  new CountDownTimer(100000, 1000) {

            public void onTick(long millisUntilFinished) {


                String v = String.format("%02d", millisUntilFinished/60000);
                int va = (int)( (millisUntilFinished%60000)/1000);
                txt_edit_mobile.setText("Resend OTP? " +v+":"+String.format("%02d",va));
                txt_edit_mobile.setClickable(false);
            }

            public void onFinish() {
                txt_edit_mobile.setText("Resend OTP");
                txt_edit_mobile.setClickable(true);
                txt_edit_mobile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(OtpActivity.this, "OTP has been sent !", Toast.LENGTH_SHORT).show();
                        //AlertOTP();
                        starCountdown();
                        resendOTP();
                    }
                });

            }
        };
        cT.start();
    }


    private void resendOTP() {
        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, RestClient.ROOT_URL_BREAKIN+"mobile_otp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                          progressDialog.dismiss();
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");

                            if(status.trim().contains("TRUE")){

                                JSONObject pos_login_data = jsonResponse.getJSONObject("pos_login_data");

                                String str_otp = pos_login_data.getString("otp");
                                SingletonClass.getinstance().otp = str_otp;


                                Toast.makeText(OtpActivity.this, "Otp has been sent to your Mobile", Toast.LENGTH_LONG).show();

                            }else {

                                Toast.makeText(OtpActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                     pdg.dismiss();
                        progressDialog.dismiss();
                        Toast.makeText(OtpActivity.this, "Server Problem", Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("reg_id", SingletonClass.getinstance().pos_id);
                params.put("user_name", SingletonClass.getinstance().mobile_no);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    private void SuccessDeviceChanged() {
        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, RestClient.ROOT_URL_BREAKIN+"check_otp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");

                            if(status.trim().contains("TRUE")){

                                JSONObject pos_info_obj = jsonResponse.getJSONObject("data");



                                //creating a new user object
                                Agentinfo agent = new Agentinfo(

                                        pos_info_obj.getString("id"),
                                        pos_info_obj.getString("full_name"),
                                        pos_info_obj.getString("email"),
                                        pos_info_obj.getString("mobile_number"),
                                        pos_info_obj.getString("address"),
                                        pos_info_obj.getString("dob"),
                                        pos_info_obj.getString("imei"),
                                        pos_info_obj.getString("business_id")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).agentLogin(agent);


                                Intent login = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(login);

                                Toasty.success(getApplicationContext(),"Your Otp is successfully verified",Toast.LENGTH_LONG).show();
                                finish();
                            }else {

                                Toast.makeText(OtpActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      progressDialog.dismiss();
                        Toast.makeText(OtpActivity.this, "Server Problem", Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                if(SingletonClass.getinstance().mobile_no==null){
                    SingletonClass.getinstance().mobile_no =  SharedPrefManager.getInstance(getApplicationContext()).getAgent().mobile_number;
                }

                if(SingletonClass.getinstance().imei_no==null){
                    SingletonClass.getinstance().imei_no =  SharedPrefManager.getInstance(getApplicationContext()).getAgent().imei;
                }

                if(SingletonClass.getinstance().pos_id==null){
                    SingletonClass.getinstance().pos_id =  SharedPrefManager.getInstance(getApplicationContext()).getAgent().id;
                }

                if (SingletonClass.getinstance().mobile_no != null && !SingletonClass.getinstance().mobile_no.isEmpty() && !SingletonClass.getinstance().mobile_no.equals("")) {
                    params.put("username", SingletonClass.getinstance().mobile_no);
                    params.put("reg_id", SingletonClass.getinstance().pos_id);
                    params.put("imei", SingletonClass.getinstance().imei_no);
                    params.put("business_id", "");
                    params.put("otp", SingletonClass.getinstance().otp);
                }else{
                    params.put("username", SingletonClass.getinstance().emailId);
                    params.put("reg_id", SingletonClass.getinstance().pos_id);
                    params.put("imei", SingletonClass.getinstance().imei_no);
                    params.put("business_id", SingletonClass.getinstance().businessId);
                    params.put("otp", SingletonClass.getinstance().otp);

                    Log.d(TAG, "getParams: "+params);
                }

                Log.d("chagepassword",params.toString());
                // Log.wtf("Network", "Link : " + "http://192.168.1.138/mypolicynow.com-new/Api/check_otp" + params);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }

    /**
     * Parse verification code
     *
     * @param message sms message
     * @return only four numbers from massage string
     */
    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }


    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
