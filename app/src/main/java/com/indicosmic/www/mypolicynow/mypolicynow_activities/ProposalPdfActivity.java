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
import android.graphics.Bitmap;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.model.Helper;
import com.indicosmic.www.mypolicynow.utils.CommonMethods;
import com.indicosmic.www.mypolicynow.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow.utils.DownloadTask;
import com.indicosmic.www.mypolicynow.utils.FileDownloader;
import com.indicosmic.www.mypolicynow.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow.webservices.RestClient;

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
import java.util.HashMap;
import java.util.Map;

import static com.indicosmic.www.mypolicynow.webservices.RestClient.ROOT_URL2;

public class ProposalPdfActivity extends AppCompatActivity {

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
    String StrCustomerMobile = "",QuoteLink = "",PDF_URL="",quote_url="",proposal_no="",proposal_otp="",policy_no="";
    Button BtnShare,BtnEdit,BtnVerify_Buy;
    Dialog DialogVerifyPopup,DialogUploadPolicy;
    String StrPolicyType="";
    ImageView Iv_UploadedImg;
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
        webViewProposalPdf = findViewById(R.id.webViewProposalPdf);
        progressBar = findViewById(R.id.progressbar);
        myDialog = new ProgressDialog(ProposalPdfActivity.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
        try {

            JSONObject user_action_data = new JSONObject(StrUserActionData);

            StrPolicyType = user_action_data.getString("policy_type");

            JSONObject proposal_obj = new JSONObject(ProposalObjStr);

            Log.d("proposalobj",""+proposal_obj);
            proposal_no = proposal_obj.getString("proposal_no");
            proposal_otp = proposal_obj.getString("otp");
            policy_no = proposal_obj.getString("policy_no");
            QuoteLink = proposal_obj.getString("quote_link");
            quote_url = proposal_obj.getString("url");
            QuoteLink = QuoteLink.toUpperCase();
           //Toast.makeText(getApplicationContext(),"Customer Verification OTP :"+proposal_otp,Toast.LENGTH_LONG).show();

            ResendVerifyLink();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PDF_URL = ROOT_URL2+quote_url;
        Log.d("PDFURL",""+PDF_URL);
        download_file_url = PDF_URL;
        dest_file_path = QuoteLink.toUpperCase()+".pdf";
        LoadProposalPage();




        BtnShare= findViewById(R.id.BtnShare);
        BtnEdit= findViewById(R.id.BtnEdit);
        BtnVerify_Buy= findViewById(R.id.BtnVerify_Buy);


        if(!isVerifiedCustomerMobile){
            BtnVerify_Buy.setText("VERIFY");

        }else {
            BtnVerify_Buy.setText("BUY NOW");
        }

        BtnVerify_Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isVerifiedCustomerMobile){
                    verifyCustomerPopup();

                }else {
                    BUY_POLICY();
                    //BtnVerify_Buy.setText("BUY NOW");
                }



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
                        tv_loading = new TextView(ProposalPdfActivity.this);
                        tv_loading.setGravity(Gravity.CENTER);
                        tv_loading.setTypeface(null, Typeface.BOLD);
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

    private void BUY_POLICY() {

        CommonMethods.DisplayToastInfo(getApplicationContext(),"Redirected to Payment Gateway.");




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
                CheckAlreadyVerified();
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

    private void CheckAlreadyVerified() {
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
                            BtnVerify_Buy.setText("BUY NOW");
                            if(DialogVerifyPopup!=null && DialogVerifyPopup.isShowing()){
                                DialogVerifyPopup.dismiss();
                            }
                        }else {
                            CommonMethods.DisplayToastInfo(getApplicationContext(),"Please verify first using the link sent on registered Mobile no.");

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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
    }
}
