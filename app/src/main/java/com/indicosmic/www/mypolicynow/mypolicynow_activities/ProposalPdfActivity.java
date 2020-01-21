package com.indicosmic.www.mypolicynow.mypolicynow_activities;

import android.Manifest;
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
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    private static final int SELECT_FILE = 2243;
    private static final int REQUEST_CAMERA = 23424;


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
        webViewProposalPdf = (WebView) findViewById(R.id.webViewProposalPdf);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);


        try {

            JSONObject user_action_data = new JSONObject(StrUserActionData);

            StrPolicyType = user_action_data.getString("policy_type");


            JSONObject proposal_obj = new JSONObject(ProposalObjStr);
            proposal_no = proposal_obj.getString("proposal_no");
            proposal_otp = proposal_obj.getString("otp");
            policy_no = proposal_obj.getString("policy_no");
            QuoteLink = proposal_obj.getString("quote_link");
            quote_url = proposal_obj.getString("url");
            QuoteLink = QuoteLink.toUpperCase();
           //Toast.makeText(getApplicationContext(),"Customer Verification OTP :"+proposal_otp,Toast.LENGTH_LONG).show();



        } catch (JSONException e) {
            e.printStackTrace();
        }

        PDF_URL = ROOT_URL2+quote_url;
        Log.d("PDFURL",""+PDF_URL);

        WebSettings ws = webViewProposalPdf.getSettings();
        ws.setSupportZoom(true);
        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);
        String google_pdf_url = "https://docs.google.com/gview?embedded=true&url="+PDF_URL;
        webViewProposalPdf.loadUrl(google_pdf_url);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ECLAIR) {
            try {
                Log.d(TAG, "Enabling HTML5-Features");
                Method m1 = WebSettings.class.getMethod("setDomStorageEnabled", new Class[]{Boolean.TYPE});
                m1.invoke(ws, Boolean.TRUE);

                Method m2 = WebSettings.class.getMethod("setDatabaseEnabled", new Class[]{Boolean.TYPE});
                m2.invoke(ws, Boolean.TRUE);

                Method m3 = WebSettings.class.getMethod("setDatabasePath", new Class[]{String.class});
                m3.invoke(ws, "/data/data/" + getPackageName() + "/databases/");

                Method m4 = WebSettings.class.getMethod("setAppCacheMaxSize", new Class[]{Long.TYPE});
                m4.invoke(ws, 1024*1024*8);

                Method m5 = WebSettings.class.getMethod("setAppCachePath", new Class[]{String.class});
                m5.invoke(ws, "/data/data/" + getPackageName() + "/cache/");

                Method m6 = WebSettings.class.getMethod("setAppCacheEnabled", new Class[]{Boolean.TYPE});
                m6.invoke(ws, Boolean.TRUE);

                Log.d(TAG, "Enabled HTML5-Features");
            }
            catch (NoSuchMethodException e) {
                Log.e(TAG, "Reflection fail", e);
            }
            catch (InvocationTargetException e) {
                Log.e(TAG, "Reflection fail", e);
            }
            catch (IllegalAccessException e) {
                Log.e(TAG, "Reflection fail", e);
            }
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
        });



        myDialog = new ProgressDialog(ProposalPdfActivity.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);

        BtnShare= (Button)findViewById(R.id.BtnShare);
        BtnEdit= (Button)findViewById(R.id.BtnEdit);
        BtnVerify_Buy= (Button)findViewById(R.id.BtnVerify_Buy);


        if(!isVerifiedCustomerMobile){
            if(StrPolicyType!=null && !StrPolicyType.equalsIgnoreCase("")){
                if(StrPolicyType.equalsIgnoreCase("new")){
                    BtnVerify_Buy.setText("VERIFY");

                }else if(StrPolicyType.equalsIgnoreCase("renew") && !isUploadedPolicyDocument){
                    BtnVerify_Buy.setText("UPLOAD");
                }else if(StrPolicyType.equalsIgnoreCase("renew") && isUploadedPolicyDocument){
                    BtnVerify_Buy.setText("VERIFY");
                }
            }

        }else {
            BtnVerify_Buy.setText("BUY NOW");
        }

        BtnVerify_Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isVerifiedCustomerMobile){
                    if(StrPolicyType!=null && !StrPolicyType.equalsIgnoreCase("")){
                        if(StrPolicyType.equalsIgnoreCase("new")){
                            verifyCustomerPopup();
                        }else if(StrPolicyType.equalsIgnoreCase("renew") && !isUploadedPolicyDocument){
                            uploadPreviousPolicyDocumentPopup();
                        }else if(StrPolicyType.equalsIgnoreCase("renew") && isUploadedPolicyDocument){
                            verifyCustomerPopup();
                        }
                    }

                }else {
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
                                                    //new DownloadFile().execute(PDF_URL, QuoteLink+".pdf");
                                                    new DownloadTask(ProposalPdfActivity.this, PDF_URL);
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

    private void verifyCustomerPopup() {

        DialogVerifyPopup = new Dialog(ProposalPdfActivity.this);
        DialogVerifyPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogVerifyPopup.setCanceledOnTouchOutside(false);
        DialogVerifyPopup.setCancelable(true);
        DialogVerifyPopup.setContentView(R.layout.popup_upload_previous_policy_document);
        DialogVerifyPopup.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));


        ImageView iv_close = (ImageView) DialogVerifyPopup.findViewById(R.id.iv_close);
        Button btnAlreadyRegistered = (Button) DialogVerifyPopup.findViewById(R.id.btnAlreadyRegistered);
        Button btnVerify = (Button) DialogVerifyPopup.findViewById(R.id.btnVerify);

        TextView til_customer_mobile_no  = (TextView) DialogVerifyPopup.findViewById(R.id.til_customer_mobile_no);


        EditText edt_Check_Mobile_Otp = (EditText) DialogVerifyPopup.findViewById(R.id.edt_Check_Mobile_Otp);

        String tv_customer_no_text = "Click on 'Send Customer Signature and OTP'. Please enter OTP sent on following number to verify \n(Mobile Number: "+StrCustomerMobile+")";
        til_customer_mobile_no.setText(tv_customer_no_text);


        TextView txt_resend_mobile_otp = (TextView) DialogVerifyPopup.findViewById(R.id.txt_resend_mobile_otp);




        DialogVerifyPopup.show();

        //starCountdown();

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String input_text = edt_Check_Mobile_Otp.getText().toString();
                if (input_text.length() == 4) {

                    String MobileOtp = edt_Check_Mobile_Otp.getText().toString().trim();
                    VerifyOTP(MobileOtp);
                }
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

        txt_resend_mobile_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




        edt_Check_Mobile_Otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String input_text = charSequence.toString();
                if (input_text.length() == 4) {

                    String MobileOtp = edt_Check_Mobile_Otp.getText().toString().trim();
                    VerifyOTP(MobileOtp);



                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




    }

    private void uploadPreviousPolicyDocumentPopup() {

        DialogUploadPolicy = new Dialog(ProposalPdfActivity.this);
        DialogUploadPolicy.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogUploadPolicy.setCanceledOnTouchOutside(false);
        DialogUploadPolicy.setCancelable(true);
        DialogUploadPolicy.setContentView(R.layout.popup_upload_previous_policy_document);
        DialogUploadPolicy.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));


        ImageView iv_close = (ImageView) DialogUploadPolicy.findViewById(R.id.iv_close);
        Button upload_button = (Button) DialogUploadPolicy.findViewById(R.id.upload_button);
        Button captureImage_button = (Button) DialogUploadPolicy.findViewById(R.id.captureImage_button);

        Iv_UploadedImg = (ImageView)DialogUploadPolicy.findViewById(R.id.Iv_UploadedImg);
        Button btnUpload = (Button) DialogUploadPolicy.findViewById(R.id.btnUpload);



        DialogUploadPolicy.show();

        //starCountdown();

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryIntent();

            }
        });



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

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });





    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Str_Base64Image = "";
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (bm != null) {
            Iv_UploadedImg.setImageBitmap(bm);
            Iv_UploadedImg.setVisibility(View.VISIBLE);
            Str_Base64Image = CommonMethods.getEncoded64ImageStringFromBitmap(bm);
        }else {
            Iv_UploadedImg.setVisibility(View.GONE);
        }

    }

    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

        if (thumbnail != null) {
            Iv_UploadedImg.setImageBitmap(thumbnail);
            Iv_UploadedImg.setVisibility(View.VISIBLE);

            Str_Base64Image = CommonMethods.getEncoded64ImageStringFromBitmap(thumbnail);
        }else {
            Iv_UploadedImg.setVisibility(View.GONE);
        }



        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir(".SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);

            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        Iv_UploadedImg.setImageURI(contentUri);
        Iv_UploadedImg.setVisibility(View.VISIBLE);
        mediaScanIntent.setData(contentUri);
        ProposalPdfActivity.this.sendBroadcast(mediaScanIntent);
    }



    private void VerifyOTP(String mobileOtp) {

        if(mobileOtp!=null && !mobileOtp.equalsIgnoreCase("")){
            if(mobileOtp.equalsIgnoreCase(proposal_otp)){
                CommonMethods.DisplayToastSuccess(getApplicationContext(),"Customer Verified Successfully");
                isVerifiedCustomerMobile = true;
                BtnVerify_Buy.setText("BUY NOW");
                if(DialogVerifyPopup!=null && DialogVerifyPopup.isShowing()){
                    DialogVerifyPopup.dismiss();
                }
            }
        }

    }


    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "ags/proposal");
            folder.mkdir();


            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName + ".pdf");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //Log.e("pathOpen", file.getPath());

            Uri contentUri;
            contentUri = Uri.fromFile(file);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            if (Build.VERSION.SDK_INT >= 24) {

                Uri apkURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
                intent.setDataAndType(apkURI, "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            } else {

                intent.setDataAndType(contentUri, "application/pdf");
            }


           // FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }
    }

    private void API_Quote_Forward() {
        myDialog.show();
        String URL = ROOT_URL2+"quoteForward";
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
        TextView title = (TextView)dialog.findViewById(R.id.title) ;
        TextView Message_text = (TextView)dialog.findViewById(R.id.Message_text) ;

        ImageView iv_MessageImg = (ImageView)dialog.findViewById(R.id.iv_MessageImg);
        if(status) {
            iv_MessageImg.setImageDrawable(getDrawable(R.drawable.checked_green));
            iv_MessageImg.setVisibility(View.VISIBLE);
        }else {
            iv_MessageImg.setImageDrawable(getDrawable(R.drawable.alert_circle));
            iv_MessageImg.setVisibility(View.VISIBLE);
        }
        TextView tv_ok = (TextView)dialog.findViewById(R.id.tv_ok);
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
