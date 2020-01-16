package com.indicosmic.www.mypolicynow.adapter;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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

import com.indicosmic.www.mypolicynow.MainActivity;
import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.utils.CommonMethods;
import com.indicosmic.www.mypolicynow.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow.utils.MyValidator;
import com.indicosmic.www.mypolicynow.utils.UtilitySharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.indicosmic.www.mypolicynow.webservices.RestClient.ROOT_URL;


public class SignUpTab  extends Fragment implements View.OnClickListener {

    ProgressDialog myDialog;
    String DeviceId,IsVerifiedUser="0";
    Integer DeviceVersion;
    String StrUserId="",StrMobileNo="",StrEmailId="",StrName="",StrPassword="",SavedPassword="";
    Button sign_up;
    //Button signOutButton;
    Dialog DialogVerifyMobile,DialogVerifyEmail,DailogEnterPassword;
    EditText edt_verification_number;
    boolean IsGoogleSignIn = false;
    TelephonyManager mTelephonyManager;
    private static final String TAG = "GoogleSignInActivity";
    private static final int RC_SIGN_IN = 9001;

    String ClientName,ClientEmail;
    EditText edt_email_signup,edt_mobile_no_sign_up,edt_desired_password,edt_reenter_password;
    TextView hint_email,hint_mob,hint_password,hint_password_confirm;
    CheckBox check_box_tnc;
    View rootView;
    Button btn_forget;
    Button signIn;
    Button sign_in_button,sign_up_button;
    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.signup_layout_new, container, false);
        mContext = getContext();
        init();
        return rootView;
    }

    @SuppressLint("MissingPermission")
    private void init() {

        mTelephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        if (mTelephonyManager.getDeviceId() != null) {
            DeviceId = mTelephonyManager.getDeviceId();
        }else {
            DeviceId =  Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            String version = pInfo.versionName;
            DeviceVersion = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        edt_email_signup = (EditText)rootView.findViewById(R.id.edt_email_signup);
        edt_mobile_no_sign_up= (EditText)rootView.findViewById(R.id.edt_mobile_no_sign_up);
        edt_desired_password= (EditText)rootView.findViewById(R.id.edt_desired_password);
        edt_reenter_password= (EditText)rootView.findViewById(R.id.edt_reenter_password);

        hint_email= (TextView) rootView.findViewById(R.id.hint_email);
        hint_mob= (TextView)rootView.findViewById(R.id.hint_mob);
        hint_password= (TextView)rootView.findViewById(R.id.hint_password);
        hint_password_confirm= (TextView)rootView.findViewById(R.id.hint_password_confirm);


        check_box_tnc = (CheckBox)rootView.findViewById(R.id.check_box_tnc);


        sign_up = (Button)rootView.findViewById(R.id.sign_up);
        sign_up.setOnClickListener(this);


        myDialog = new ProgressDialog(mContext);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.sign_up) {

            if(isValidFeild()){
                hint_email.setVisibility(View.GONE);
                hint_mob.setVisibility(View.GONE);
                hint_password.setVisibility(View.GONE);
                hint_password_confirm.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_reenter_password.getWindowToken(), 0);
                StrMobileNo = edt_mobile_no_sign_up.getText().toString();
                StrPassword = edt_desired_password.getText().toString();
                StrEmailId =  edt_email_signup.getText().toString();
                StrName = "";
                UtilitySharedPreferences.setPrefs(mContext,"FinalSignUpPassword",StrPassword);
                IsVerifiedUser = "0";
                IsGoogleSignIn = false;
                registerUserAPI();
                //fetchUserDetailFromMobile();
            }

        }
    }




    private boolean isValidFeild() {
        boolean result = true;

        if (!MyValidator.isValidEmail(edt_email_signup)) {
            edt_email_signup.requestFocus();
            hint_email.setVisibility(View.VISIBLE);
            result = false;
        }

        if(!MyValidator.isValidMobile(edt_mobile_no_sign_up)) {
            edt_mobile_no_sign_up.requestFocus();
            hint_mob.setVisibility(View.VISIBLE);
            result = false;
        }

        if (!MyValidator.isValidPassword(edt_desired_password)) {
            edt_desired_password.requestFocus();
            edt_desired_password.setVisibility(View.VISIBLE);
            result = false;
        }

        if (!MyValidator.isValidPassword(edt_desired_password)) {
            edt_desired_password.requestFocus();
            hint_password.setVisibility(View.VISIBLE);
            result = false;
        }

        if(!MyValidator.isValidPassword(edt_reenter_password)) {
            edt_reenter_password.requestFocus();
            result = false;
        }

        if(!edt_desired_password.getText().toString().equalsIgnoreCase(edt_reenter_password.getText().toString())){
            edt_reenter_password.requestFocus();
            hint_password_confirm.setVisibility(View.VISIBLE);
            result = false;

        }


        return  result;
    }


    private void registerUserAPI() {

        myDialog.show();
        String Uiid_id = UUID.randomUUID().toString();
        String URL_FetchDetails = ROOT_URL + "user/registerUser";
        try {
            Log.d("URL_FetchDetails",URL_FetchDetails);

            ConnectionDetector cd = new ConnectionDetector(mContext);
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
                                    String message = jobj.getString("message");

                                    if (status) {

                                        JSONObject jsonObject = jobj.getJSONObject("data");
                                        StrUserId = jsonObject.getString("id");
                                        String password = jsonObject.getString("password");
                                        String MobileNo = jsonObject.getString("mobile_no");
                                        String mobile_otp = jsonObject.getString("mobile_otp");
                                        String is_mobile_verified = jsonObject.getString("is_mobile_verified");
                                        String is_verified_user = jsonObject.getString("is_verified_user");
                                        String is_password_set = jsonObject.getString("is_password_set");
                                        StrEmailId = jsonObject.getString("email_id");
                                        String email_pin = jsonObject.getString("email_pin");
                                        String is_email_verified = jsonObject.getString("is_email_verified");
                                        StrName = jsonObject.getString("name");
                                        if(StrName==null || StrName.equalsIgnoreCase("null")){
                                            StrName="";
                                        }
                                        if(is_verified_user!=null && is_verified_user.equalsIgnoreCase("1") && !password.equalsIgnoreCase("")){

                                            UtilitySharedPreferences.setPrefs(getActivity(), "UserMobile", StrMobileNo);
                                            UtilitySharedPreferences.setPrefs(getActivity(), "UserName", StrName);
                                            UtilitySharedPreferences.setPrefs(getActivity(), "UserId", StrUserId);
                                            UtilitySharedPreferences.setPrefs(getActivity(), "UserEmail", StrEmailId);

                                            Intent i = new Intent(getActivity(), MainActivity.class);
                                            startActivity(i);
                                            getActivity().overridePendingTransition(R.animator.move_left, R.animator.move_right);
                                            getActivity().finish();

                                        }else {
                                            if(!MobileNo.equalsIgnoreCase("") && is_mobile_verified!=null && is_mobile_verified.equalsIgnoreCase("0")) {
                                                CommonMethods.DisplayToastWarning(getActivity(),"MOBILE OTP TO VERIFY IS: "+mobile_otp);

                                                PopupToVerifyMobileWithOTP();
                                            }else if(!StrEmailId.equalsIgnoreCase("") && is_email_verified!=null && is_email_verified.equalsIgnoreCase("0")){
                                                CommonMethods.DisplayToastWarning(getActivity(),"EMAIL PIN TO VERIFY IS: "+email_pin);

                                                PopupToVerifyEmailWithPin();
                                            }else if(password.equalsIgnoreCase("") && is_password_set!=null && is_password_set.equalsIgnoreCase("0")){
                                                UtilitySharedPreferences.setPrefs(getActivity(), "UserMobile", StrMobileNo);
                                                UtilitySharedPreferences.setPrefs(getActivity(), "UserName", StrName);
                                                UtilitySharedPreferences.setPrefs(getActivity(), "UserId", StrUserId);
                                                UtilitySharedPreferences.setPrefs(getActivity(), "UserEmail", StrEmailId);

                                                UpdateProfileData();
                                                /*Intent i = new Intent(getActivity(), ProfileActivity.class);
                                                startActivity(i);
                                                getActivity().overridePendingTransition(R.animator.move_left, R.animator.move_right);
                                                getActivity().finish();*/

                                            }
                                        }


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

                    }
                }){

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("name",StrName);
                        params.put("email_id", StrEmailId);
                        params.put("mobile", StrMobileNo);
                        params.put("device_id", DeviceId);
                        params.put("password", StrPassword);
                        params.put("is_verified_user",IsVerifiedUser);
                        params.put("device_version", String.valueOf(DeviceVersion));
                        Log.d("ParrasOtpdata",params.toString() );

                        return params;
                    }



                } ;

                int socketTimeout = 50000; //30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                // RequestQueue requestQueue = Volley.newRequestQueue(mContext, new HurlStack(null, getSocketFactory()));
                RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                requestQueue.add(stringRequest);

            } else {
                if(myDialog!=null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                CommonMethods.DisplayToast(mContext, "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    private void PopupToVerifyEmailWithPin() {
        DialogVerifyEmail = new Dialog(mContext);
        DialogVerifyEmail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogVerifyEmail.setCanceledOnTouchOutside(false);
        DialogVerifyEmail.setContentView(R.layout.custom_dialog_for_entering_otp);
        DialogVerifyEmail.getWindow().getAttributes().width = ActionBar.LayoutParams.MATCH_PARENT;



        DialogVerifyEmail.show();
        TextView til_text = (TextView)DialogVerifyEmail.findViewById(R.id.til_text);
        til_text.setText("PIN to verify your email is sent on your Email");
        edt_verification_number  = (EditText) DialogVerifyEmail.findViewById(R.id.edt_verification_number);
        Button Verify_otp =(Button) DialogVerifyEmail.findViewById(R.id.Verify_otp);
        ImageView iv_close = (ImageView)DialogVerifyEmail.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DialogVerifyEmail!=null && DialogVerifyEmail.isShowing()){
                    DialogVerifyEmail.dismiss();
                }

            }
        });



        Verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_verification_number.getWindowToken(), 0);
                String OTP_Entered = edt_verification_number.getText().toString();

                PinVerifyApi(OTP_Entered);
            }
        });

    }


    private void PopupToVerifyMobileWithOTP() {
        DialogVerifyMobile = new Dialog(mContext);
        DialogVerifyMobile.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogVerifyMobile.setCanceledOnTouchOutside(false);
        DialogVerifyMobile.setContentView(R.layout.custom_dialog_for_entering_otp);
        DialogVerifyMobile.getWindow().getAttributes().width = ActionBar.LayoutParams.MATCH_PARENT;



        DialogVerifyMobile.show();
        TextView til_text = (TextView)DialogVerifyMobile.findViewById(R.id.til_text);
        til_text.setText("OTP to verify your mobile is sent on your Mobile No.");

        edt_verification_number  = (EditText) DialogVerifyMobile.findViewById(R.id.edt_verification_number);
        Button Verify_otp =(Button) DialogVerifyMobile.findViewById(R.id.Verify_otp);
        ImageView iv_close = (ImageView)DialogVerifyMobile.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DialogVerifyMobile!=null && DialogVerifyMobile.isShowing()){
                    DialogVerifyMobile.dismiss();
                }

            }
        });



        Verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_verification_number.getWindowToken(), 0);
                String OTP_Entered = edt_verification_number.getText().toString();

                OtpVerifyApi(OTP_Entered);
            }
        });

    }

    private void OtpVerifyApi(final String OTP_Entered) {
        String verifyOTP_Api = ROOT_URL + "/user/verifyUserMobile";
        try {

            ConnectionDetector cd = new ConnectionDetector(mContext);
            boolean isInternetPresent = cd.isConnectingToInternet();



            if (isInternetPresent) {
                Log.d("URL",verifyOTP_Api);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, verifyOTP_Api,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response",response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean status = jsonObject.getBoolean("status");
                                    if(status){
                                        edt_verification_number.setError(null);
                                        if(DialogVerifyMobile!=null && DialogVerifyMobile.isShowing()){
                                            DialogVerifyMobile.dismiss();
                                        }
                                        registerUserAPI();



                                    }else {
                                        edt_verification_number.setError("Invalid OTP.");


                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        }){

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        //params.put("Pan", StrClientPan);
                        params.put("Mobile", StrMobileNo);
                        params.put("OTP", OTP_Entered);
                        Log.d("ParrasOtpdata",params.toString() );

                        return params;
                    }



                };

                int socketTimeout = 50000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                requestQueue.add(stringRequest);



            } else {
                CommonMethods.DisplayToast(mContext, "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    private void PinVerifyApi(final String OTP_Entered) {
        String verifyOTP_Api = ROOT_URL + "user/verifyUserEmail";
        try {

            ConnectionDetector cd = new ConnectionDetector(mContext);
            boolean isInternetPresent = cd.isConnectingToInternet();



            if (isInternetPresent) {
                Log.d("URL",verifyOTP_Api);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, verifyOTP_Api,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response",response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean status = jsonObject.getBoolean("status");
                                    if(status){
                                        edt_verification_number.setError(null);
                                        if(DialogVerifyEmail!=null && DialogVerifyEmail.isShowing()){
                                            DialogVerifyEmail.dismiss();
                                        }

                                        registerUserAPI();



                                    }else {
                                        edt_verification_number.setError("Invalid OTP.");


                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        }){

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        //params.put("Pan", StrClientPan);
                        params.put("email", StrEmailId);
                        params.put("pin", OTP_Entered);
                        Log.d("ParrasOtpdata",params.toString() );

                        return params;
                    }



                };

                int socketTimeout = 50000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                requestQueue.add(stringRequest);



            } else {
                CommonMethods.DisplayToast(mContext, "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    private void UpdateProfileData() {

        myDialog.show();
        String Uiid_id = UUID.randomUUID().toString();
        String URL_FetchDetails = ROOT_URL + "user/updateProfileData";
        try {
            Log.d("URL_FetchDetails",URL_FetchDetails);

            ConnectionDetector cd = new ConnectionDetector(mContext);
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
                                    String message = jobj.getString("message");

                                    if (status) {

                                        Intent i = new Intent(getActivity(), MainActivity.class);
                                        startActivity(i);
                                        getActivity().overridePendingTransition(R.animator.move_left, R.animator.move_right);
                                        getActivity().finish();
                                    }else {
                                        CommonMethods.DisplayToastWarning(getActivity(),message);
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

                    }
                }){

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Id",StrUserId);
                        params.put("Name",StrName);
                        params.put("Email",StrEmailId);
                        params.put("Mobile",StrMobileNo);
                        params.put("Password",StrPassword);
                        params.put("UserId","");
                        Log.d("ParrasOtpdata",params.toString() );

                        return params;
                    }



                } ;

                int socketTimeout = 50000; //30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
                RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                requestQueue.add(stringRequest);

            } else {
                if(myDialog!=null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                CommonMethods.DisplayToast(mContext, "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}