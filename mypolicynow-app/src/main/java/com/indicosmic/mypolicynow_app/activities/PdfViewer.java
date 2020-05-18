package com.indicosmic.mypolicynow_app.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.indicosmic.mypolicynow_app.R;
import com.indicosmic.mypolicynow_app.utils.UtilitySharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.indicosmic.mypolicynow_app.webservices.RestClient.ROOT_URL2;

public class PdfViewer extends AppCompatActivity {

    private static final String TAG = "ProposalPDF";
    WebView webViewProposalPdf;
    SwipeRefreshLayout MySwipeRefreshLayout;
    ProgressBar progressBar;
    String StrAgentId="",StrMpnData,StrUserActionData;
    WebViewClient webViewProposalPdfClient;
    TextView tv_loading;
    ProgressDialog myDialog;
    String PDF_URL="";
    String PDF_TITLE="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if(bundle.getString("pdf_url") != null) {
                PDF_URL = bundle.getString("pdf_url");
                Log.d("PDFURL",""+PDF_URL);
            }

            if(bundle.getString("pdf_title") != null){
                PDF_TITLE = bundle.getString("pdf_title");
            }
        } else {
            PDF_URL = "";
            PDF_TITLE="";
        }



        init();

    }

    private void init() {

        ImageView back_btn_toolbar = (ImageView)findViewById(R.id.back_btn_toolbar);
        back_btn_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView til_text = (TextView)findViewById(R.id.til_text);
        til_text.setText(PDF_TITLE);


        StrAgentId =   UtilitySharedPreferences.getPrefs(getApplicationContext(),"PosId");

        webViewProposalPdf = findViewById(R.id.webViewProposalPdf);


        MySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        MySwipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // This method performs the actual data-refresh operation.
                // The method calls setRefreshing(false) when it's finished.
                LoadProposalPage();
            }
        });

        progressBar = findViewById(R.id.progressbar);



        myDialog = new ProgressDialog(PdfViewer.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
        tv_loading = new TextView(PdfViewer.this);
        tv_loading.setGravity(Gravity.CENTER);
        tv_loading.setTypeface(null, Typeface.BOLD);


        LoadProposalPage();
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
                if(MySwipeRefreshLayout.isRefreshing()){
                    MySwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                LoadProposalPage();
            }
        });



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }

}
