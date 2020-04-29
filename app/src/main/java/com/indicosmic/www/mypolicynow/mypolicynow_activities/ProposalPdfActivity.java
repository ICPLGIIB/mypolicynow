package com.indicosmic.www.mypolicynow.mypolicynow_activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.indicosmic.www.mypolicynow.BuildConfig;
import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.model.Helper;
import com.indicosmic.www.mypolicynow.utils.CommonMethods;
import com.indicosmic.www.mypolicynow.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow.utils.DownloadTask;
import com.indicosmic.www.mypolicynow.utils.FileDownloader;
import com.indicosmic.www.mypolicynow.utils.FilePath;
import com.indicosmic.www.mypolicynow.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow.webservices.Common;
import com.indicosmic.www.mypolicynow.webservices.RestClient;
import com.interfaces.ClsPAXInf;
import com.interfaces.OnPaxPOSTransactionListerner;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.UUID;

import static com.indicosmic.www.mypolicynow.webservices.RestClient.Basic_auth;
import static com.indicosmic.www.mypolicynow.webservices.RestClient.ROOT_URL2;
import static com.indicosmic.www.mypolicynow.webservices.RestClient.api_password;
import static com.indicosmic.www.mypolicynow.webservices.RestClient.api_user_name;
import static com.indicosmic.www.mypolicynow.webservices.RestClient.get;
import static com.indicosmic.www.mypolicynow.webservices.RestClient.x_api_key;
import static io.fabric.sdk.android.Fabric.TAG;

public class ProposalPdfActivity extends AppCompatActivity implements OnPaxPOSTransactionListerner {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final int PICK_IMAGE_REQUEST = 2;
    private static final int CAPTURE_IMAGE_REQUEST = 231;

    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    ProgressDialog myDialog;
    private int STORAGE_PERMISSION_CODE = 23;
    private static final String TAG = "ProposalPDF";
    WebView webViewProposalPdf;
    ProgressBar progressBar;
    String StrAgentId="",StrMpnData,StrUserActionData;
    WebViewClient webViewProposalPdfClient;
    boolean isVerifiedCustomerMobile = false;
    boolean isUploadedPolicyDocument = false;
    String StrCustomerMobile = "",QuoteLink = "",PDF_URL="",quote_url="",proposal_no="",proposal_otp="",policy_no="",IsPrePolicyUploaded="",IsPaCoverUploaded="false";
    Button BtnShare,BtnEdit,BtnVerify,BtnUploadPACover,BtnUploadPreviousPolicy,BtnBuyPolicy;
    Dialog DialogVerifyPopup,DialogUploadPolicy;
    String StrPolicyType="";
    ImageView Iv_UploadedImg;
    Button btnPreviousPolicyUpload;
    Uri outputFileUri;
    boolean ServerStatus;
    String Str_Base64Image;
    String download_file_url;
    String dest_file_path = "";
    int downloadedSize = 0, totalsize;
    float per = 0;
    private static final int SELECT_FILE = 2243;
    private static final int REQUEST_CAMERA = 23424;
    TextView tv_loading;
    String have_motor_policy,have_pa_policy;
    Uri fileUri;
    File myFileToUpload = null;
    String PreviousPolicyFileName;
    byte[] bbytesFile;
    String merchant_id="",terminal_id="",total_premium_payable="1";
    String file_base;
    String filePath,picturePath,b64,filename,file_extension;
    String Quote_Link="";
    EditText EdtChooseFile;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    File imageFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal_page_pdf_view);

        init();

    }

    private void init() {

        StrAgentId =   UtilitySharedPreferences.getPrefs(getApplicationContext(),"PosId");
        StrMpnData = UtilitySharedPreferences.getPrefs(getApplicationContext(),"MpnData");
        StrUserActionData = UtilitySharedPreferences.getPrefs(getApplicationContext(),"UserActionData");
        String ProposalObjStr =  UtilitySharedPreferences.getPrefs(getApplicationContext(),"ProposalAry");
        StrCustomerMobile = UtilitySharedPreferences.getPrefs(getApplicationContext(),"CustomerMobile");
        IsPrePolicyUploaded =  UtilitySharedPreferences.getPrefs(getApplicationContext(),"IsPrePolicyUploaded");
        webViewProposalPdf = findViewById(R.id.webViewProposalPdf);
        progressBar = findViewById(R.id.progressbar);

        BtnShare= (Button) findViewById(R.id.BtnShare);
        BtnEdit= (Button)  findViewById(R.id.BtnEdit);
        BtnVerify= (Button) findViewById(R.id.BtnVerify);
        BtnUploadPACover = (Button)findViewById(R.id.BtnUploadPACover);
        BtnUploadPreviousPolicy= (Button) findViewById(R.id.BtnUploadPreviousPolicy);
        BtnBuyPolicy= (Button) findViewById(R.id.BtnBuyPolicy);

        myDialog = new ProgressDialog(ProposalPdfActivity.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
        tv_loading = new TextView(ProposalPdfActivity.this);
        tv_loading.setGravity(Gravity.CENTER);
        tv_loading.setTypeface(null, Typeface.BOLD);
        try {

            JSONObject user_action_data = new JSONObject(StrUserActionData);

            have_motor_policy = user_action_data.getString("have_motor_policy");
            have_pa_policy = user_action_data.getString("have_pa_policy");

            StrPolicyType = user_action_data.getString("policy_type");

            JSONObject proposal_obj = new JSONObject(ProposalObjStr);

            Log.d("proposalobj",""+proposal_obj);
            proposal_no = proposal_obj.getString("proposal_no");
            proposal_otp = proposal_obj.getString("otp");
            policy_no = proposal_obj.getString("policy_no");
            QuoteLink = proposal_obj.getString("quote_link");
            quote_url = proposal_obj.getString("url");
            QuoteLink = QuoteLink.toUpperCase();
            PDF_URL = ROOT_URL2+quote_url;
            Log.d("PDFURL",""+PDF_URL);
            download_file_url = PDF_URL;
            dest_file_path = QuoteLink.toUpperCase()+".pdf";

            //Toast.makeText(getApplicationContext(),"Customer Verification OTP :"+proposal_otp,Toast.LENGTH_LONG).show();
            CheckAlreadyVerified(true);

        } catch (JSONException e) {
            e.printStackTrace();
        }




        BtnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isVerifiedCustomerMobile){
                    verifyCustomerPopup();
                }
            }
        });


        BtnUploadPreviousPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDocumentPopup(false,"previous_policy_doc");
            }
        });

        BtnUploadPACover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDocumentPopup(false,"pa_cover");
            }
        });

        BtnBuyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BUY_POLICY();
            }
        });


        BtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        final PopupMenu menu = new PopupMenu(this, BtnShare);
        menu.getMenu().add("QUOTE FORWARD");
        menu.getMenu().add("DOWNLOAD PDF");
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    // insert your code here
                    Log.d("menu title: ", item.getTitle().toString());
                    if(item.getTitle().toString().equalsIgnoreCase("QUOTE FORWARD")){
                        API_Quote_Forward();
                    }else if(item.getTitle().toString().equalsIgnoreCase("DOWNLOAD PDF")){

                        downloadAndOpenPDF();



                    }
                    return true;
                }
            }
        );


        BtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.show();
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void LoadProposalPage() {
        WebSettings ws = webViewProposalPdf.getSettings();
        ws.setSupportZoom(true);
        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);

        String google_pdf_url = "https://docs.google.com/gview?embedded=true&url="+PDF_URL;
        webViewProposalPdf.loadUrl(google_pdf_url);
        //webViewProposalPdf.loadUrl("http://uat.mypolicynow.com/proposal/b1f9e248e2373439343430");

        try {
            Log.d(TAG, "Enabling HTML5-Features");
            Method m1 = WebSettings.class.getMethod("setDomStorageEnabled", Boolean.TYPE);
            m1.invoke(ws, Boolean.TRUE);

            Method m2 = WebSettings.class.getMethod("setDatabaseEnabled", Boolean.TYPE);
            m2.invoke(ws, Boolean.TRUE);

            Method m3 = WebSettings.class.getMethod("setDatabasePath", String.class);
            m3.invoke(ws, "/data/data/" + getPackageName() + "/databases/");

            Method m4 = WebSettings.class.getMethod("setAppCacheMaxSize", Long.TYPE);
            m4.invoke(ws, 1024*1024*8);

            Method m5 = WebSettings.class.getMethod("setAppCachePath", String.class);
            m5.invoke(ws, "/data/data/" + getPackageName() + "/cache/");

            Method m6 = WebSettings.class.getMethod("setAppCacheEnabled", Boolean.TYPE);
            m6.invoke(ws, Boolean.TRUE);

            Log.d(TAG, "Enabled HTML5-Features");
        }
        catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(TAG, "Reflection fail", e);
        }

        webViewProposalPdf.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(google_pdf_url);
                //view.loadUrl("http://uat.mypolicynow.com/proposal/b1f9e248e2373439343430");

                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                LoadProposalPage();
            }
        });



    }

    private void CheckAlreadyVerified(boolean send_mesg) {
        myDialog.show();
        String URL = ROOT_URL2+"checkalreadyverify";
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            Log.d("URL", "" + URL);
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    try {

                        Log.d("Response", "" + response);
                        JSONObject jsonresponse = new JSONObject(response);

                        boolean status = jsonresponse.getBoolean("status");

                        if(status){
                            CommonMethods.DisplayToastSuccess(getApplicationContext(),"Customer Already Verified");
                            isVerifiedCustomerMobile = true;

                            if(DialogVerifyPopup!=null && DialogVerifyPopup.isShowing()){
                                DialogVerifyPopup.dismiss();
                            }
                        }else {
                            CommonMethods.DisplayToastInfo(getApplicationContext(),"Please verify first using the link sent on registered Mobile no.");
                            if(send_mesg){

                                ResendVerifyLink();
                                //downloadAndOpenPDF();
                            }
                        }
                        LoadProposalPage();
                        DisplayBtnsAsPerFlowConditions();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    CommonMethods.DisplayToastInfo(getApplicationContext(),"Something went wrong. Please try again later.");

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("quote_link", QuoteLink.toUpperCase());
                    map.put("agent_id", StrAgentId);

                    Log.d("Params",""+map);
                    return map;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //  Authorization: Basic $auth
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //headers.put("Content-Type", "application/x-www-form-urlencoded");
                    //headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("x-api-key",x_api_key);
                    headers.put("Authorization", "Basic "+CommonMethods.Base64_Encode(api_user_name + ":" + api_password));
                    return headers;
                }
            };


            int socketTimeout = 50000; //30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }else {
            CommonMethods.DisplayToast(getApplicationContext(),"Please check Internet Connection");
        }
    }

    private void DisplayBtnsAsPerFlowConditions() {

        if(isVerifiedCustomerMobile){
            BtnVerify.setVisibility(View.GONE);

            if(have_motor_policy!=null && have_pa_policy!=null){

                if(have_motor_policy.equalsIgnoreCase("yes") || have_pa_policy.equalsIgnoreCase("yes")){

                    if(IsPaCoverUploaded!=null && IsPaCoverUploaded.equalsIgnoreCase("false")) {
                        BtnUploadPACover.setVisibility(View.VISIBLE);
                        BtnUploadPreviousPolicy.setVisibility(View.GONE);
                        BtnBuyPolicy.setVisibility(View.GONE);
                    }else {
                        BtnUploadPACover.setVisibility(View.GONE);
                        if(StrPolicyType.equalsIgnoreCase("renew")) {

                            if(IsPrePolicyUploaded!=null && !IsPrePolicyUploaded.equalsIgnoreCase("")){
                                if(IsPrePolicyUploaded.equalsIgnoreCase("false")){

                                    BtnUploadPreviousPolicy.setVisibility(View.VISIBLE);
                                    BtnBuyPolicy.setVisibility(View.GONE);
                                }else if(IsPrePolicyUploaded.equalsIgnoreCase("true")){

                                    BtnUploadPreviousPolicy.setVisibility(View.GONE);
                                    BtnBuyPolicy.setVisibility(View.VISIBLE);
                                }
                            }
                        }else{
                            BtnUploadPACover.setVisibility(View.GONE);
                            BtnUploadPreviousPolicy.setVisibility(View.GONE);
                            BtnBuyPolicy.setVisibility(View.VISIBLE);
                        }
                    }

                }else if(StrPolicyType.equalsIgnoreCase("renew")) {

                    if(IsPrePolicyUploaded!=null && !IsPrePolicyUploaded.equalsIgnoreCase("")){
                        if(IsPrePolicyUploaded.equalsIgnoreCase("false")){
                            BtnUploadPACover.setVisibility(View.GONE);
                            BtnUploadPreviousPolicy.setVisibility(View.VISIBLE);
                            BtnBuyPolicy.setVisibility(View.GONE);
                        }else if(IsPrePolicyUploaded.equalsIgnoreCase("true")){
                            BtnUploadPACover.setVisibility(View.GONE);
                            BtnUploadPreviousPolicy.setVisibility(View.GONE);
                            BtnBuyPolicy.setVisibility(View.VISIBLE);
                        }
                    }
                }else{
                    BtnUploadPACover.setVisibility(View.GONE);
                    BtnUploadPreviousPolicy.setVisibility(View.GONE);
                    BtnBuyPolicy.setVisibility(View.VISIBLE);
                }
            }
        }else {

            BtnVerify.setVisibility(View.VISIBLE);
            BtnUploadPreviousPolicy.setVisibility(View.GONE);
            BtnUploadPACover.setVisibility(View.GONE);
            BtnBuyPolicy.setVisibility(View.GONE);

        }

    }

    private void ResendVerifyLink() {
        myDialog.show();
        String URL = ROOT_URL2+"resendverifylink";
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            Log.d("URL", "" + URL);
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    try {

                        Log.d("Response", "" + response);
                        JSONObject jsonresponse = new JSONObject(response);


                        JSONObject msgObj = jsonresponse.getJSONObject("message");

                        boolean is_error = msgObj.getBoolean("is_error");

                        if(!is_error){
                            CommonMethods.DisplayToast(getApplicationContext(),"Verification Link send on registered Mobile no.");
                        }else {
                            String error_message = msgObj.getString("error_message");
                            CommonMethods.DisplayToastError(getApplicationContext(),error_message);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    CommonMethods.DisplayToastInfo(getApplicationContext(),"Something went wrong. Please try again later.");

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("quote_link", QuoteLink.toUpperCase());
                    map.put("agent_id", StrAgentId);
                    map.put("proposal_no",proposal_no);
                    map.put("mobile_no",StrCustomerMobile);
                    Log.d("Params",""+map);
                    return map;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //  Authorization: Basic $auth
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //headers.put("Content-Type", "application/x-www-form-urlencoded");
                    //headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("x-api-key",x_api_key);
                    headers.put("Authorization", "Basic "+CommonMethods.Base64_Encode(api_user_name + ":" + api_password));
                    return headers;
                }
            };


            int socketTimeout = 50000; //30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }else {
            CommonMethods.DisplayToast(getApplicationContext(),"Please check Internet Connection");
        }
    }

    private void verifyCustomerPopup() {

        DialogVerifyPopup = new Dialog(ProposalPdfActivity.this);
        DialogVerifyPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogVerifyPopup.setCanceledOnTouchOutside(false);
        DialogVerifyPopup.setCancelable(true);
        DialogVerifyPopup.setContentView(R.layout.popup_verify_mobile_otp);
        DialogVerifyPopup.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));


        ImageView iv_close = DialogVerifyPopup.findViewById(R.id.iv_close);
        Button btnAlreadyVerified = DialogVerifyPopup.findViewById(R.id.btnAlreadyVerified);
        Button btnResendVerifyLink = DialogVerifyPopup.findViewById(R.id.btnResendVerifyLink);

        TextView til_customer_mobile_no  = DialogVerifyPopup.findViewById(R.id.til_customer_mobile_no);


        //EditText edt_Check_Mobile_Otp = (EditText) DialogVerifyPopup.findViewById(R.id.edt_Check_Mobile_Otp);

        String tv_customer_no_text = "Click on 'Send Customer Signature and OTP'. Please verify from link \n(Mobile Number: "+StrCustomerMobile+")";
        til_customer_mobile_no.setText(tv_customer_no_text);

        DialogVerifyPopup.show();

        //starCountdown();

        btnResendVerifyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResendVerifyLink();

            }
        });

        btnAlreadyVerified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckAlreadyVerified(false);
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DialogVerifyPopup!=null && DialogVerifyPopup.isShowing()) {
                    DialogVerifyPopup.dismiss();
                }
            }
        });
    }

    /*Start Upload Previous Year Policy*/
    private void uploadDocumentPopup(boolean show_skip_btn, String document_type) {

        DialogUploadPolicy = new Dialog(ProposalPdfActivity.this);
        DialogUploadPolicy.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogUploadPolicy.setCanceledOnTouchOutside(false);
        DialogUploadPolicy.setCancelable(true);
        DialogUploadPolicy.setContentView(R.layout.popup_upload_previous_policy_document);
        Objects.requireNonNull(DialogUploadPolicy.getWindow()).setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView title = (TextView)DialogUploadPolicy.findViewById(R.id.title);

        if(document_type.equalsIgnoreCase("previous_policy_doc")){
            title.setText("UPLOAD PREVIOUS POLICY");
        }else if(document_type.equalsIgnoreCase("pa_cover")){
            title.setText("UPLOAD PA COVER DOCUMENT");
        }

        ImageView iv_close = (ImageView) DialogUploadPolicy.findViewById(R.id.iv_close);
        Button captureImage_button = (Button) DialogUploadPolicy.findViewById(R.id.captureImage_button);

        Iv_UploadedImg = (ImageView)DialogUploadPolicy.findViewById(R.id.Iv_UploadedImg);
        btnPreviousPolicyUpload = (Button) DialogUploadPolicy.findViewById(R.id.btnUpload);
        Button btnViewProposal = (Button) DialogUploadPolicy.findViewById(R.id.btnViewProposal);

        if(show_skip_btn){
            btnViewProposal.setVisibility(View.VISIBLE);
        }else {
            btnViewProposal.setVisibility(View.GONE);
        }

        if(DialogUploadPolicy!=null && !DialogUploadPolicy.isShowing()) {
            DialogUploadPolicy.show();
        }




        captureImage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cameraIntent();

            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DialogUploadPolicy!=null && DialogUploadPolicy.isShowing()) {
                    DialogUploadPolicy.dismiss();
                }
            }
        });

        btnPreviousPolicyUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageFile!=null && !imageFile.getAbsolutePath().toString().equalsIgnoreCase("")) {
                    API_UPLOAD_PREVIOUS_YEAR_POLICY(document_type);
                }else {
                    CommonMethods.DisplayToastError(getApplicationContext(),"It seems that you have not captured the image to upload. Please capture the photo and then try to upload.");
                }
            }
        });

    }

    private void cameraIntent(){
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        PreviousPolicyFileName = QuoteLink+"_"+ timeStamp;
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            try {

                File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/AGS");
                if (!direct.exists()) {
                    direct.mkdirs();
                }
                imageFile = File.createTempFile(
                        PreviousPolicyFileName,  // prefix
                        ".jpg",         // suffix
                        direct      // directory
                );

                mCurrentPhotoPath = "file:" + imageFile.getAbsolutePath();
                Log.d("ImagePath",""+mCurrentPhotoPath);

                outputFileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileprovider", imageFile);
                if (imageFile != null) {
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    cameraIntent.putExtra("take_type", 1);
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (Exception ex) {
                // Error occurred while creating the File
                //startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                Log.i(TAG, "Exception");
                Log.d("Error", ""+ex.getMessage());
                //CommonMethods.DisplayToast(getApplicationContext(),"It seems to be some technical Issue. Please try again later.");
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {

        try {

            if(imageFile!=null && imageFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                Iv_UploadedImg.setImageBitmap(myBitmap);
                Iv_UploadedImg.setVisibility(View.VISIBLE);
                btnPreviousPolicyUpload.setVisibility(View.VISIBLE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void API_UPLOAD_PREVIOUS_YEAR_POLICY(String document_type){


            String UPLOAD_URL = ROOT_URL2 + "uploaddocumentproposal";
            Log.d("Upload_URL", UPLOAD_URL);
             myDialog.show();

            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(getApplicationContext(), uploadId, UPLOAD_URL)

                        .addFileToUpload(imageFile.getAbsolutePath(), "other_document") //
                        // Adding file
                        .addHeader("x-api-key",x_api_key)
                        .addHeader("Authorization","Basic "+CommonMethods.Base64_Encode(api_user_name + ":" + api_password))
                        .addParameter("document_name", document_type)
                        .addParameter("quote_forward_link", Quote_Link)

                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(UploadInfo uploadInfo) {

                            }

                            @Override
                            public void onError(UploadInfo uploadInfo, Exception e) {
                                if (myDialog != null && myDialog.isShowing()) {
                                    myDialog.dismiss();
                                }
                                CommonMethods.DisplayToastError(getApplicationContext(), "Error in Uploading. Please upload it again.");

                            }

                            @Override
                            public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                                if (myDialog != null && myDialog.isShowing()) {
                                    myDialog.dismiss();
                                }
                                Log.d("serverRespGetHttpCode",""+serverResponse.getHttpCode());
                                if(serverResponse.getHttpCode()==200) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());
                                        Log.d("uploadResponse",""+jsonObject);
                                        JSONObject resultObj = jsonObject.getJSONObject("result");
                                        boolean upload_status = resultObj.getBoolean("status");
                                        if(upload_status){
                                            CommonMethods.DisplayToastSuccess(getApplicationContext(), "Document Uploaded Successfully.");
                                            UtilitySharedPreferences.setPrefs(getApplicationContext(), "IsPrePolicyUploaded", "true");


                                            if(document_type.equalsIgnoreCase("previous_policy_doc")){
                                                IsPrePolicyUploaded = "true";
                                            }

                                            if(document_type.equalsIgnoreCase("pa_cover")) {
                                                IsPaCoverUploaded = "true";
                                            }

                                            DisplayBtnsAsPerFlowConditions();

                                            if (DialogUploadPolicy != null && DialogUploadPolicy.isShowing()) {
                                                DialogUploadPolicy.dismiss();
                                            }
                                        }else {
                                            CommonMethods.DisplayToastError(getApplicationContext(), "Error in Uploading. Please upload it again.");
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }

                            }

                            @Override
                            public void onCancelled(UploadInfo uploadInfo) {
                                if (myDialog != null && myDialog.isShowing()) {
                                    myDialog.dismiss();
                                }
                                CommonMethods.DisplayToastError(getApplicationContext(), "Error in Uploading. Please upload it again.");
                            }
                        })
                        .startUpload();


            } catch (Exception exc) {
                Log.d("UploadException",""+ exc.getMessage());
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
            }


    }

    /*End Upload Previous Year Policy*/

    //QUOTE FORWARD
    private void API_Quote_Forward() {
        myDialog.show();
        String URL = ROOT_URL2+"quoteforward";
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            Log.d("URL", "" + URL);
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    try {

                        Log.d("Response", "" + response);
                        JSONObject jsonresponse = new JSONObject(response);

                        boolean status = jsonresponse.getBoolean("status");

                        if(status) {
                            String message = jsonresponse.getString("message");
                            MessagePopUp(status,message);
                        }else {
                            String message = "Failed to send Quote";
                            MessagePopUp(false,message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    CommonMethods.DisplayToastInfo(getApplicationContext(),"Something went wrong. Please try again later.");

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("quote_link", QuoteLink.toUpperCase());
                    map.put("agent_id", StrAgentId);

                    Log.d("Params",""+map);
                    return map;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //  Authorization: Basic $auth
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //headers.put("Content-Type", "application/x-www-form-urlencoded");
                    //headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("x-api-key",x_api_key);
                    headers.put("Authorization", "Basic "+CommonMethods.Base64_Encode(api_user_name + ":" + api_password));
                    return headers;
                }
            };


            int socketTimeout = 50000; //30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }else {
            CommonMethods.DisplayToast(getApplicationContext(),"Please check Internet Connection");
        }

    }

    public void MessagePopUp(boolean status, String message) {
        final Dialog dialog = new Dialog(ProposalPdfActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.pop_up_info_message);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView title = dialog.findViewById(R.id.title);
        TextView Message_text = dialog.findViewById(R.id.Message_text);

        ImageView iv_MessageImg = dialog.findViewById(R.id.iv_MessageImg);
        if(status) {
            iv_MessageImg.setImageDrawable(getDrawable(R.drawable.checked_green));
            iv_MessageImg.setVisibility(View.VISIBLE);
        }else {
            iv_MessageImg.setImageDrawable(getDrawable(R.drawable.alert_circle));
            iv_MessageImg.setVisibility(View.VISIBLE);
        }
        TextView tv_ok = dialog.findViewById(R.id.tv_ok);
        tv_ok.setText("OK");

        title.setText(R.string.app_name);
        Message_text.setText(Html.fromHtml(message));

        dialog.show();
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* DOWNLOAD PDF */
    private void downloadAndOpenPDF() {
        myDialog.show();
        new Thread(new Runnable() {
            public void run() {
                //Uri path = Uri.fromFile(downloadFile(download_file_url));
               // if(path==null) {
                  Uri  path = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileprovider", downloadFile(download_file_url));
                //}
                try {
                    if(myDialog!=null && myDialog.isShowing()){
                        myDialog.dismiss();
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(Intent.createChooser(intent, "Open File Using..."));
                } catch (ActivityNotFoundException e) {
                    if(myDialog!=null && myDialog.isShowing()){
                        myDialog.dismiss();
                    }
                    tv_loading.setError("PDF Reader application is not installed in your device");
                }
            }
        }).start();
    }

    File downloadFile(String dwnload_file_path) {
        File file = null;
        try {

            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            // connect
            urlConnection.connect();

            // set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            // create a new file, to save the downloaded file
            file = new File(SDCardRoot, dest_file_path);

            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // this is the total size of the file which we are
            // downloading
            totalsize = urlConnection.getContentLength();
            setText("Starting PDF download...");

            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                per = ((float) downloadedSize / totalsize) * 100;
                setText("Total PDF File size  : "
                        + (totalsize / 1024)
                        + " KB\n\nDownloading PDF " + (int) per
                        + "% complete");
            }
            // close the output stream when complete //
            fileOutput.close();
            setText("Download Complete. Open PDF Application installed in the device.");

        } catch (final MalformedURLException e) {
            setTextError("Some error occured. Press back and try again.",
                    Color.RED);
        } catch (final IOException e) {
            setTextError("Some error occured. Press back and try again.",
                    Color.RED);
        } catch (final Exception e) {
            setTextError(
                    "Failed to download image. Please check your internet connection.",
                    Color.RED);
        }
        return file;
    }

    void setTextError(final String message, final int color) {
        runOnUiThread(new Runnable() {
            public void run() {
                tv_loading.setTextColor(color);
                tv_loading.setText(message);
            }
        });

    }

    void setText(final String txt) {
        runOnUiThread(new Runnable() {
            public void run() {
                tv_loading.setText(txt);
            }
        });

    }

    /*END OF DOWNLOAD PDF*/

    private void BUY_POLICY() {

         merchant_id =  UtilitySharedPreferences.getPrefs(getApplicationContext(),"MerchantId");
         terminal_id =  UtilitySharedPreferences.getPrefs(getApplicationContext(),"TerminalId");

        total_premium_payable =  UtilitySharedPreferences.getPrefs(getApplicationContext(),"total_premium_payable");

        Log.d("total_premium_payable",""+total_premium_payable);
        Log.d("terminal_id",""+terminal_id);
        Log.d("merchant_id",""+merchant_id);

        ClsPAXInf objClsPAXInf=new ClsPAXInf(this);
        objClsPAXInf.sale(total_premium_payable,"Tip","Extra_field", terminal_id,merchant_id, "AddPrivateDataprint");

    }

    private void PostPaymentAPI(String ResponseResult) {
        String WS_P_ID,TID,PGID,Premium,Response = "";


        String FinalPaymentResponse = ResponseResult;


        String[] separated = FinalPaymentResponse.split("|");
        if(separated.length==1){
            Response = separated[0];
        }else if(separated.length==8 || separated.length==9){
            Response = separated[8];

        }

        JSONObject responseObj = new JSONObject();

        try {
            responseObj.put("WS_P_ID","TP444357");
            responseObj.put("TID","123251572592696");
            responseObj.put("PGID","9291344799");
            responseObj.put("Premium","16083.00");
            responseObj.put("Response",Response);
            responseObj.put("return_url","https://www.mypolicynow.com/future/privatecar/transaction_status");

        } catch (JSONException e) {
            e.printStackTrace();
        }



        String URL = ROOT_URL2+"updatepaymentinfo";
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            Log.d("URL", "" + URL);
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    try {

                        Log.d("Response", "" + response);
                        JSONObject jsonresponse = new JSONObject(response);

                        boolean status = jsonresponse.getBoolean("status");

                        if(status) {
                            String message = jsonresponse.getString("message");

                            MessagePopUp(status,message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    CommonMethods.DisplayToastInfo(getApplicationContext(),"Something went wrong. Please try again later.");

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("quote_link", QuoteLink.toUpperCase());
                    map.put("agent_id", StrAgentId);
                    map.put("msg",responseObj.toString());
                    Log.d("Params",""+map);
                    return map;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //  Authorization: Basic $auth
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //headers.put("Content-Type", "application/x-www-form-urlencoded");
                    //headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("x-api-key",x_api_key);
                    headers.put("Authorization", "Basic "+CommonMethods.Base64_Encode(api_user_name + ":" + api_password));
                    return headers;
                }
            };


            int socketTimeout = 50000; //30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }else {
            CommonMethods.DisplayToast(getApplicationContext(),"Please check Internet Connection");
        }




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
    }


    @Override
    public void OnError(String s) {
        String StrPaymentResponse_failure = "Result";
        StrPaymentResponse_failure = s;

        Log.d("failure-payment-string",s);
        //PostPaymentAPI(StrPaymentResponse_failure);
    }

    @Override
    public void OnSuccess(String s) {

        String StrPaymentResponse_success = "Masked_Pan|Card_Holder_Name|Card_Type|Auth_Code|RRN|Stan|Batch_Number|Result|IS_SUCCESS|IS_PIN_ENTERED";
        StrPaymentResponse_success = s;

        Log.d("success-payment-string",s);
       // PostPaymentAPI(StrPaymentResponse_success);
    }
}
