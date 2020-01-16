package com.indicosmic.www.mypolicynow;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.indicosmic.www.mypolicynow.utils.CommonMethods;
import com.indicosmic.www.mypolicynow.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow.utils.RuntimePermissions;
import com.indicosmic.www.mypolicynow.utils.SharedPrefManager;
import com.indicosmic.www.mypolicynow.utils.SingletonClass;
import com.indicosmic.www.mypolicynow.utils.UtilitySharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.indicosmic.www.mypolicynow.webservices.RestClient.ROOT_URL;


public class SplashActivity extends RuntimePermissions {

    private static int SPLASH_TIME_OUT = 2000;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 24;
    Thread splashTread;
    private static final String TAG = "SplashActivity";
    String StrPosUserName,StrClientMobile,Force_Update_flag="0";
    int YourApkVersionCode;
    String StrIsActiveAccount,  DeviceId;
    TelephonyManager mTelephonyManager;
    private int    timeoutMillis       = 5000;
    ImageView iv_logo;
    /** The time when this {@link AppCompatActivity} was created. */

    private long  startTimeMillis     = 0;
    private static final int REQUEST_PERMISSIONS = 20;
    SharedPrefManager sharedPrefManager;
    ArrayList<String> questionlist;
    /** The code used when requesting permissions */

    private static final int    PERMISSIONS_REQUEST = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        init();

    }

    private void init() {

        startTimeMillis = System.currentTimeMillis();
        iv_logo = (ImageView)findViewById(R.id.iv_logo);

        /*Glide.with(this)
                .load("https://www.mypolicynow.com/assets/images/logo_spinner.gif")
                .placeholder(R.drawable.logo_spinner)
                .into(iv_logo);*/

        SingletonClass.initinstance();
        questionlist = new ArrayList<String>();
        sharedPrefManager = new SharedPrefManager(this);

        if (Build.VERSION.SDK_INT >= 21) {
            SplashActivity.super.requestAppPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION},
                    R.string.runtime_permissions_txt, REQUEST_PERMISSIONS);
        } else {
            redirect();

        }

       /*if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions();
        } else {
            //force_update();
            redirect();

        }*/
    }


    @Override
    public void onPermissionsGranted(int requestCode) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.grey));


        redirect();

    }





    /*@TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            checkPermissions();
        }
    }



    private void checkPermissions() {
        String[] ungrantedPermissions = requiredPermissionsStillNeeded();
        if (ungrantedPermissions.length == 0) {
            //force_update();

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(ungrantedPermissions, PERMISSIONS_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private String[] requiredPermissionsStillNeeded() {

        Set<String> permissions = new HashSet<String>();
        for (String permission : getRequiredPermissions()) {
            permissions.add(permission);
        }
        for (Iterator<String> i = permissions.iterator(); i.hasNext();) {
            String permission = i.next();
            if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                Log.d(SplashActivity.class.getSimpleName(),
                        "Permission: " + permission + " already granted.");
                i.remove();
            } else {
                Log.d(SplashActivity.class.getSimpleName(),
                        "Permission: " + permission + " not yet granted.");
            }
        }
        return permissions.toArray(new String[permissions.size()]);
    }

    public String[] getRequiredPermissions() {
        String[] permissions = null;
        try {
            permissions = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_PERMISSIONS).requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (permissions == null) {
            return new String[0];
        } else {
            return permissions.clone();
        }
    }
*/



    private void force_update() {


        StrPosUserName = UtilitySharedPreferences.getPrefs(getApplicationContext(),"UserName");

        int currentVersionCode = getCurrentVersion();
        //Log.d("Current version = ",currentVersionCode);
        String Uiid_id = UUID.randomUUID().toString();
        final String get_latest_version_info = ROOT_URL +"appupdate/getLatestUpdateVersion?_"+Uiid_id;
        Log.d("URL --->", get_latest_version_info);
        try {
            ConnectionDetector cd = new ConnectionDetector(this);
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, get_latest_version_info, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Response",""+response);
                            String latestVersion = "";
                            JSONObject Jobj = new JSONObject(response);
                            String data = Jobj.getString("data");
                            JSONObject jobject = new JSONObject(data);

                            String Id = jobject.getString("id");
                            String VersionCode = jobject.getString("version_code");
                            String VersionName = jobject.getString("version_name");
                            Force_Update_flag = jobject.getString("is_force_update");
                            update_dialog(VersionCode);

                            Log.d("Latest version:",latestVersion);
                        } catch (Exception e) {
                            Log.d("Exception",e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        Log.d("Vollley Err", volleyError.toString());
                        if(volleyError.toString().equalsIgnoreCase("com.android.volley.ServerError")){
                            CommonMethods.DisplayToastWarning(getApplicationContext(),"App under maintenance");
                        }
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                int socketTimeout = 50000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(20),0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                requestQueue.add(stringRequest);
            }else {
                CommonMethods.DisplayToastWarning(this,"No Internet Connection");
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private int getCurrentVersion(){
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;
        try {
            pInfo =  pm.getPackageInfo(this.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        String currentVersion = pInfo.versionName;
        YourApkVersionCode = pInfo.versionCode;
        Log.d("YourVersionCode",""+YourApkVersionCode);
        String version_code = String.valueOf(YourApkVersionCode);
        return YourApkVersionCode;
    }


    private void update_dialog(String versionCode) {
        if(versionCode!="" || versionCode!=null){
            int v_code = Integer.valueOf(versionCode);
            if((YourApkVersionCode < v_code) && Force_Update_flag.equalsIgnoreCase("1")){
                Log.d("version code",""+v_code);
                Log.d("Your APK CODE ",""+YourApkVersionCode);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog dialog = new Dialog(SplashActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.pop_up_app_update);
                        dialog.getWindow().setBackgroundDrawable(
                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        TextView title = (TextView)dialog.findViewById(R.id.title) ;
                        TextView Upgrade_text = (TextView)dialog.findViewById(R.id.Upgrade_text) ;
                        TextView tv_ok = (TextView)dialog.findViewById(R.id.tv_ok);
                        dialog.show();
                        tv_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                UtilitySharedPreferences.clearPref(getApplicationContext());
                                CommonMethods.deleteCache(getApplicationContext());
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                                finish();
                            }
                        });
                    }
                }, SPLASH_TIME_OUT);
            }else
            {
                /*UtilitySharedPreferences.clearPref(this);*/
                redirect();

            }
        }else{
            redirect();
        }
    }

    private void redirect() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){

                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    overridePendingTransition(R.animator.move_left,R.animator.move_right);
                    finish();
                }else {
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.animator.move_left,R.animator.move_right);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }

    private void getUserCurrentStateStatus() {

        //myDialog.show();

        String URL_check_user_status = ROOT_URL+"User/getUserByMobile_Email?email="+StrPosUserName+"&mobile_no="+StrClientMobile;
        try {
            Log.d("URL",URL_check_user_status);

            ConnectionDetector cd = new ConnectionDetector(this);
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        URL_check_user_status,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("dataResponse",response);
                                try {

                                    JSONObject jObj = new JSONObject(response);

                                    boolean status = jObj.getBoolean("status");

                                    JSONObject jsonData = jObj.getJSONObject("data");

                                    if (status) {

                                        String client_id = jsonData.getString("id");
                                        String client_name = jsonData.getString("name");
                                        String client_mobile = jsonData.getString("mobile_no");
                                        String client_email = jsonData.getString("email_id");
                                        String is_verified_user = jsonData.getString("is_verified_user");
                                        String is_password_set = jsonData.getString("is_password_set");
                                        String is_active = jsonData.getString("is_active");



                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ClientId",client_id);
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ClientName",client_name);
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ClientMobile",client_mobile);
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ClientEmail",client_email);
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"active",is_active);
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"IsPasswordSet",is_password_set);
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"IsVerifiedUser",is_verified_user);


                                        if (is_active!=null && is_active.equalsIgnoreCase("1")){
                                            //portal
                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                            overridePendingTransition(R.animator.move_left,R.animator.move_right);
                                            finish();

                                        }else {
                                            CommonMethods.DisplayToastWarning(getApplicationContext(),"Account Is no more Active. Please contact System Administrator.");
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                            overridePendingTransition(R.animator.move_left,R.animator.move_right);
                                            finish();
                                        }


                                    }


                                } catch (JSONException e) {

                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        overridePendingTransition(R.animator.move_left,R.animator.move_right);
                        finish();
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();

                    }
                }) {


                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {

                        return super.parseNetworkResponse(response);
                    }


                };
                int socketTimeout = 50000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);


            } else {

                CommonMethods.DisplayToast(this, "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }


    }





    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
