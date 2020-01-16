package com.indicosmic.www.mypolicynow.adapter;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import com.indicosmic.www.mypolicynow.NewSignIn_SignUpActivity;
import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.utils.CommonMethods;
import com.indicosmic.www.mypolicynow.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow.utils.MyValidator;
import com.indicosmic.www.mypolicynow.utils.UtilitySharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.indicosmic.www.mypolicynow.webservices.RestClient.ROOT_URL;

public class SignInTab  extends Fragment implements View.OnClickListener{


    ProgressDialog myDialog;
    String DeviceId,IsVerifiedUser="0";
    Integer DeviceVersion;
    String StrUserId="",StrMobileNo="",StrEmailId="",StrName="",StrPassword="",SavedPassword="";

    Dialog DialogVerifyMobile,DialogVerifyEmail,DailogEnterPassword;
    EditText edt_verification_number;
    boolean IsGoogleSignIn = false;
    TelephonyManager mTelephonyManager;
    private static final String TAG = "GoogleSignInActivity";

    View rootView;
    EditText edt_user_mobile,edt_password;
    TextView error_mobile,error_password;
    Button btn_forget;
    Button signIn;
    Button sign_in_button,sign_up_button;
    Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.signin_layout_new, container, false);

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


        edt_user_mobile = (EditText)rootView.findViewById(R.id.edt_user_mobile);
        edt_password= (EditText)rootView.findViewById(R.id.edt_password);


        error_mobile= (TextView) rootView.findViewById(R.id.error_mobile);
        error_password= (TextView)rootView.findViewById(R.id.error_password);

        btn_forget = (Button)rootView.findViewById(R.id.btn_forget);
        btn_forget.setOnClickListener(this);

        //Relative Layout Btn
        signIn = (Button) rootView.findViewById(R.id.signIn);
        signIn.setOnClickListener(this);


        sign_up_button = (Button) rootView.findViewById(R.id.sign_up_button);
        sign_up_button.setOnClickListener(this);


        // [END build_client]








        myDialog = new ProgressDialog(mContext);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.signIn) {

            if(isValidFeild()){
                error_mobile.setVisibility(View.GONE);
                error_password.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_password.getWindowToken(), 0);
                StrMobileNo = edt_user_mobile.getText().toString();
                StrPassword = edt_password.getText().toString();

                StrName = "";
                StrEmailId = "";
                IsVerifiedUser = "0";
                IsGoogleSignIn = false;
                fetchUserDetails();
                //fetchUserDetailFromMobile();
            }

        }else if(id== R.id.btn_forget){
            CommonMethods.DisplayToastWarning(getActivity(),"Forgot Password");
        }else if(id==R.id.sign_up_button){
            ((NewSignIn_SignUpActivity)getActivity()).navigateFragment(1);
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










    private void PopupVerifyPassword() {
        String msg= "Please Enter your Password.";

        DailogEnterPassword = new Dialog(mContext);
        DailogEnterPassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DailogEnterPassword.setCanceledOnTouchOutside(false);
        DailogEnterPassword.setContentView(R.layout.custom_dialog_for_password);
        DailogEnterPassword.getWindow().getAttributes().width = ActionBar.LayoutParams.MATCH_PARENT;



        DailogEnterPassword.show();

        TextView text= (TextView)DailogEnterPassword.findViewById(R.id.text);
        text.setText(msg);
        TextView text1 = (TextView)DailogEnterPassword.findViewById(R.id.text1);
        text1.setVisibility(View.GONE);

        ImageView iv_close = (ImageView) DailogEnterPassword.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DailogEnterPassword!=null && DailogEnterPassword.isShowing()){
                    DailogEnterPassword.dismiss();
                }

            }
        });

        TextView tv_user_detail = (TextView)DailogEnterPassword.findViewById(R.id.tv_user_detail);
        tv_user_detail.setText("Mobile:"+StrMobileNo);

        EditText EdtPassword = (EditText)DailogEnterPassword.findViewById(R.id.etPassword);
        TextView verify=(TextView)DailogEnterPassword.findViewById(R.id.verify);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EdtPassword.getText().toString()!=null && !EdtPassword.getText().toString().equalsIgnoreCase("")){

                    String md_HashPassword = CommonMethods.md5(EdtPassword.getText().toString());
                    Log.d("md5 Hash Psss",md_HashPassword);
                    if(CommonMethods.md5(EdtPassword.getText().toString()).equalsIgnoreCase(SavedPassword)){
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(EdtPassword.getWindowToken(), 0);
                        fetchUserDetails();


                    }else {

                        EdtPassword.setError("Password does not match. Please try again.");
                    }

                }
            }
        });
      /*  TextView forget_password = (TextView)DailogEnterPassword.findViewById(R.id.forget_password);
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DailogEnterPassword.dismiss();
                //GetAPI_ForgotPassword();
            }
        });*/

    }

   /* private void GetAPI_ForgotPassword() {
        String forgotPassword_URL = ROOT_URL + "/user/VerifyPanuserData?pan="+StrClientPan;
        try {
            ConnectionDetector cd = new ConnectionDetector(mContext);
            boolean isInternetPresent = cd.isConnectingToInternet();

            if (isInternetPresent) {
                Log.d("forgotPassword_URL",forgotPassword_URL);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, forgotPassword_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response",response.toString());
                                try {
                                    JSONObject jObj = new JSONObject(response);

                                    boolean status = jObj.getBoolean("status");

                                    JSONObject jsonData = jObj.getJSONObject("data");

                                    String mobile_otp = jsonData.getString("mobile_otp");
                                    if(status){

                                        pop_up_reset_password_verification(mobile_otp);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Toast.makeText(MainActivity.mContext,error.toString(),Toast.LENGTH_LONG).show();
                            }
                        });
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
    }*/

   /* private void pop_up_reset_password_verification(final String mobile_otp) {
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder1 = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        LayoutInflater inflater = mContext.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_for_entering_otp, nullParent);
        dialogBuilder1.setView(dialogView);
        dialogBuilder1.setCancelable(false);
        alert1 = dialogBuilder1.create();

        edt_verification_number  = (EditText) dialogView.findViewById(R.id.edt_verification_number);

        ImageView iv_close = (ImageView)dialogView.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myDialog!=null && myDialog.isShowing()){
                    myDialog.dismiss();
                }
                alert1.dismiss();
            }
        });


        TextView Verify_otp=(TextView)dialogView.findViewById(R.id.Verify_otp);
        Verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_verification_number.getWindowToken(), 0);
                String OTP_Entered = edt_verification_number.getText().toString();

                OtpVerifyApi(OTP_Entered);
            }
        });
        alert1.show();
    }

    private void OtpVerifyApi(final String OTP_Entered) {
        String verifyOTP_Api = RestClient.ROOT_URL1 + "/user/verifyUserMobile";
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
                                    Boolean status = jsonObject.getBoolean("status");
                                    if(status){
                                        alert1.dismiss();

                                        pop_up_reset_password();


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
                        params.put("Pan", StrClientPan);
                        params.put("Mobile", client_mobile);
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

    private void pop_up_reset_password() {

        androidx.appcompat.app.AlertDialog.Builder dialogBuilder1 = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        LayoutInflater inflater = mContext.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_for_reset_password, nullParent);
        dialogBuilder1.setView(dialogView);
        dialogBuilder1.setCancelable(false);
        alert2 = dialogBuilder1.create();

        TextView tv_pan_no = (TextView) dialogView.findViewById(R.id.tv_pan_no);
        tv_pan_no.setText(StrClientPan);
        final EditText etNewPassword  = (EditText) dialogView.findViewById(R.id.etNewPassword);
        final EditText etConfirmPassword  = (EditText) dialogView.findViewById(R.id.etConfirmPassword);



        ImageView iv_close = (ImageView)dialogView.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myDialog!=null && myDialog.isShowing()){
                    myDialog.dismiss();
                }
                alert1.dismiss();
            }
        });


        Button btn_reset=(Button) dialogView.findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etConfirmPassword.getWindowToken(), 0);


                if((etNewPassword.getText().toString()!=null && etNewPassword.getText().toString().length()!=0) && (etConfirmPassword.getText().toString()!=null && etConfirmPassword.getText().toString().length()!=0)){

                    String StrNewPassword = etNewPassword.getText().toString();
                    String StrConfirmPassword = etConfirmPassword.getText().toString();
                    if(StrNewPassword.equalsIgnoreCase(StrConfirmPassword)){

                        ResetPasswordApi(StrNewPassword,StrConfirmPassword);

                    }else {

                        etConfirmPassword.setError("New Password & Confirm Password does not Match.");
                    }

                }


            }
        });
        alert2.show();

    }

    private void ResetPasswordApi(final String strNewPassword, String strConfirmPassword) {

        String reset_password_Api = RestClient.ROOT_URL1 + "/dashboard/ChangeForgotPassword";
        try {

            ConnectionDetector cd = new ConnectionDetector(mContext);
            boolean isInternetPresent = cd.isConnectingToInternet();



            if (isInternetPresent) {
                Log.d("URL",reset_password_Api);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, reset_password_Api,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response",response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Boolean status = jsonObject.getBoolean("status");
                                    if(status){
                                        alert2.dismiss();
                                        UtilitySharedPreferences.setPrefs(getActivity()(),"Password",strNewPassword);
                                        password = strNewPassword;
                                        ShowEnterPasswordDialog();

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
                        params.put("Pan", StrClientPan);
                        params.put("New_password", strNewPassword);
                        Log.d("Parraresetdata",params.toString() );

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
*/


    private void fetchUserDetailFromEmail() {

        myDialog.show();
        String Uiid_id = UUID.randomUUID().toString();
        String URL_FetchDetails = ROOT_URL + "user/fetchUserDetailFromEmail?_"+Uiid_id+"&email="+StrEmailId;
        try {
            Log.d("URL_FetchDetails",URL_FetchDetails);

            ConnectionDetector cd = new ConnectionDetector(mContext);
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_FetchDetails,
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
                                        StrMobileNo = jsonObject.getString("mobile_no");
                                        String mobile_otp = jsonObject.getString("mobile_otp");
                                        String is_mobile_verified = jsonObject.getString("is_mobile_verified");
                                        String is_verified_user = jsonObject.getString("is_verified_user");
                                        String is_password_set = jsonObject.getString("is_password_set");
                                        StrEmailId = jsonObject.getString("email_id");
                                        String email_pin = jsonObject.getString("email_pin");
                                        String is_email_verified = jsonObject.getString("is_email_verified");
                                        StrName = jsonObject.getString("name");



                                        if(is_verified_user!=null && is_verified_user.equalsIgnoreCase("1") && !password.equalsIgnoreCase("")){

                                            UtilitySharedPreferences.setPrefs(getActivity(), "UserMobile", StrMobileNo);
                                            UtilitySharedPreferences.setPrefs(getActivity(), "UserName", StrName);
                                            UtilitySharedPreferences.setPrefs(getActivity(), "UserId", StrUserId);
                                            UtilitySharedPreferences.setPrefs(getActivity(), "UserEmail", StrEmailId);

                                            Intent i = new Intent(getActivity(), MainActivity.class);
                                            startActivity(i);
                                            getActivity().overridePendingTransition(R.animator.move_left, R.animator.move_right);
                                            //getActivity().finish();

                                        }else {
                                            if(!StrMobileNo.equalsIgnoreCase("") && is_mobile_verified!=null && is_mobile_verified.equalsIgnoreCase("0")) {
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
                                                //getActivity().finish();

                                            }

                                        }
                                    }else {
                                        registerUserAPI();
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
                }) ;

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


    private void fetchUserDetails() {


        myDialog.show();

        final String StrMobile = edt_user_mobile.getText().toString();
        final String StrPassword = edt_password.getText().toString();

        String URL_FetchDetails = ROOT_URL + "user/login";
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
                                        String id = jsonObject.getString("id");
                                        String user_id = jsonObject.getString("user_id");
                                        String name = jsonObject.getString("name");
                                        String mobile_no = jsonObject.getString("mobile_no");
                                        String email_id = jsonObject.getString("email_id");
                                        String is_password_set = jsonObject.getString("is_password_set");


                                        UtilitySharedPreferences.setPrefs(getActivity(),"UserMobile",mobile_no);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"UserName",name);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"UserId",id);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"UserEmail",email_id);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"UniqueUserId",user_id);
                                        //Tv_InvalidLogin.setVisibility(View.GONE);
                                        if(DailogEnterPassword!=null && DailogEnterPassword.isShowing()){
                                            DailogEnterPassword.dismiss();
                                        }

                                        if(is_password_set!=null && !is_password_set.equalsIgnoreCase("") && is_password_set.equalsIgnoreCase("0")){
                                           /* Intent i = new Intent(getActivity(), ProfileActivity.class);
                                            startActivity(i);
                                            getActivity().overridePendingTransition(R.animator.move_left, R.animator.move_right);
                                            getActivity().finish();*/
                                        }else if(is_password_set!=null && !is_password_set.equalsIgnoreCase("") && is_password_set.equalsIgnoreCase("1")){
                                            Intent i = new Intent(getActivity(), MainActivity.class);
                                            startActivity(i);
                                            getActivity().overridePendingTransition(R.animator.move_left, R.animator.move_right);
                                            getActivity().finish();
                                        }


                                    }else {
                                        CommonMethods.DisplayToastWarning(getActivity(),"Invalid User / User Credentials!!");
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
                        CommonMethods.DisplayToastWarning(getActivity(),"Something went wrong. Please try again later.");
                    }
                }){

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Mobile",StrMobile);
                        params.put("Password",StrPassword);

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



    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap mIcon = null;
            try {
                InputStream in = new URL(urls[0]).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                Log.d(TAG,"IMG BItmap: "+result);
                /*ImageView mImageView = new ImageView(getActivity()());
                mImageView.getLayoutParams().width = (getResources().getDisplayMetrics().widthPixels / 100) * 24;
                mImageView.setImageBitmap(result);*/
            }
        }
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
                                        StrMobileNo = jsonObject.getString("mobile_no");
                                        String mobile_otp = jsonObject.getString("mobile_otp");
                                        String is_mobile_verified = jsonObject.getString("is_mobile_verified");
                                        String is_verified_user = jsonObject.getString("is_verified_user");
                                        String is_password_set = jsonObject.getString("is_password_set");
                                        StrEmailId = jsonObject.getString("email_id");
                                        String email_pin = jsonObject.getString("email_pin");
                                        String is_email_verified = jsonObject.getString("is_email_verified");
                                        StrName = jsonObject.getString("name");

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
                                            if(!StrMobileNo.equalsIgnoreCase("") && is_mobile_verified!=null && is_mobile_verified.equalsIgnoreCase("0")) {
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



    private boolean isValidFeild() {
        boolean result = true;

        if (!MyValidator.isValidMobile(edt_user_mobile)) {
            edt_user_mobile.requestFocus();
            error_mobile.setVisibility(View.VISIBLE);
            result = false;
        }

        if(MyValidator.isValidMobile(edt_user_mobile)) {

            error_mobile.setVisibility(View.GONE);
            result = true;
        }

        if (!MyValidator.isValidPassword(edt_password)) {
            edt_password.requestFocus();
            error_password.setVisibility(View.VISIBLE);
            result = false;
        }

        if(MyValidator.isValidPassword(edt_password)) {

            error_password.setVisibility(View.GONE);
            result = true;
        }


        return  result;
    }




}