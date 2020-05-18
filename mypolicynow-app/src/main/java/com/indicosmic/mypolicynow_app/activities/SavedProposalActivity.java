package com.indicosmic.mypolicynow_app.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.indicosmic.mypolicynow_app.R;
import com.indicosmic.mypolicynow_app.utils.CommonMethods;
import com.indicosmic.mypolicynow_app.utils.ConnectionDetector;
import com.indicosmic.mypolicynow_app.utils.MultipleSelectionSpinner;
import com.indicosmic.mypolicynow_app.utils.UtilitySharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.indicosmic.mypolicynow_app.webservices.RestClient.ROOT_URL2;
import static com.indicosmic.mypolicynow_app.webservices.RestClient.api_password;
import static com.indicosmic.mypolicynow_app.webservices.RestClient.api_user_name;
import static com.indicosmic.mypolicynow_app.webservices.RestClient.x_api_key;

public class SavedProposalActivity extends AppCompatActivity {

    MultipleSelectionSpinner mSpinner;
    List<String> list = new ArrayList<String>();
    TextView tv_filters;
    ProgressDialog myDialog;
    LinearLayout ll_parent_saved_proposal;
    String StrAgentId="",StrProposalNo="",StrRegistrationNo="",StrPolicyHolderMobileNo="",StrChassisNo="";
    Dialog DialogFilters,DialogPdfViewer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_proposal_screen_list);

        init();
    }

    private void init() {
        StrAgentId =   UtilitySharedPreferences.getPrefs(getApplicationContext(),"PosId");

        myDialog = new ProgressDialog(SavedProposalActivity.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);

        ImageView back_btn_toolbar = (ImageView)findViewById(R.id.back_btn_toolbar);
        back_btn_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView til_text = (TextView)findViewById(R.id.til_text);
        til_text.setText("SAVED PROPOSALS");

        tv_filters = (TextView)findViewById(R.id.tv_filters);
        tv_filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewFiltersDialog();
            }
        });

        ll_parent_saved_proposal = (LinearLayout)findViewById(R.id.ll_parent_saved_proposal);

        API_GET_SAVED_PROPOSAL();




    }

    private void API_GET_SAVED_PROPOSAL() {
        if(ll_parent_saved_proposal.getChildCount()>0 ) {
            ll_parent_saved_proposal.removeAllViews();
        }
        myDialog.show();

        String URL = ROOT_URL2+"front/myaccount/SavedProposal";

        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            Log.d("URL",""+ URL);
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    try {

                        Log.d("Response", "" + response);
                        JSONObject jsonresponse = new JSONObject(response);
                        JSONArray records_arry = jsonresponse.getJSONArray("records");
                        if(records_arry.length()>0) {
                            for (int i = 0; i < records_arry.length(); i++) {

                                JSONObject jsonObject = records_arry.getJSONObject(i);
                                String proposal_id = jsonObject.getString("id");
                                String proposal_no = jsonObject.getString("proposal_no");
                                String vehicle_reg_no = jsonObject.getString("vehicle_reg_no");
                                String ic_name = jsonObject.getString("ic_name");
                                String policy_holder_name = jsonObject.getString("policy_holder_name");
                                String policy_holder_mobile_no = jsonObject.getString("policy_holder_mobile_no");
                                String created = jsonObject.getString("created");

                                String proposal_status_id = jsonObject.getString("proposal_status_id");

                                String final_premium = jsonObject.getString("final_premium");
                                String quote_forward_link = jsonObject.getString("quote_forward_link");



                                LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View rowView1 = Objects.requireNonNull(inflater1).inflate(R.layout.saved_proposal_row, null);
                                rowView1.setPadding(10, 10, 10, 10);

                                TextView row_tv_proposal_id = (TextView) rowView1.findViewById(R.id.row_tv_proposal_id);
                                TextView row_tv_proposal_no = (TextView) rowView1.findViewById(R.id.row_tv_proposal_no);
                                TextView row_tv_policy_holder_name = (TextView) rowView1.findViewById(R.id.row_tv_policy_holder_name);
                                TextView row_tv_mobile_no = (TextView) rowView1.findViewById(R.id.row_tv_mobile_no);

                                TextView row_tv_ins_company = (TextView) rowView1.findViewById(R.id.row_tv_ins_company);
                                TextView row_tv_issue_date = (TextView) rowView1.findViewById(R.id.row_tv_issue_date);
                                TextView row_tv_reg_no = (TextView) rowView1.findViewById(R.id.row_tv_reg_no);



                                TextView row_tv_net_premium = (TextView) rowView1.findViewById(R.id.row_tv_net_premium);

                                TextView row_tv_quote_link = (TextView) rowView1.findViewById(R.id.row_tv_quote_link);
                                TextView row_tv_proposal_url = (TextView) rowView1.findViewById(R.id.row_tv_proposal_url);

                                Button btnCancel =  (Button) rowView1.findViewById(R.id.btnCancel);
                                Button btnQuoteForward =  (Button) rowView1.findViewById(R.id.btnQuoteForward);
                                Button btnDownload_viewProposal =  (Button) rowView1.findViewById(R.id.btnDownload_viewProposal);
                                Button btnBuyPolicy =  (Button) rowView1.findViewById(R.id.btnBuyPolicy);

                                row_tv_proposal_no.setText(proposal_no.toUpperCase());
                                row_tv_policy_holder_name.setText(policy_holder_name.toUpperCase());
                                row_tv_mobile_no.setText(policy_holder_mobile_no);
                                row_tv_ins_company.setText(ic_name);
                                row_tv_proposal_id.setText(proposal_id);

                                row_tv_issue_date.setText(created);
                                row_tv_reg_no.setText(vehicle_reg_no.toUpperCase());


                                row_tv_net_premium.setText("\u20B9 " + final_premium);


                                row_tv_quote_link.setText(quote_forward_link);
                                String quote_url = "downloadProposalPdf/"+quote_forward_link.toUpperCase();
                                row_tv_proposal_url.setText(quote_url);

                                btnCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CommonMethods.DisplayToastInfo(getApplicationContext(),"Cancel Proposal - "+row_tv_proposal_url.getText().toString());
                                    }
                                });

                                btnDownload_viewProposal.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String pdf_title = "PROPOSAL PDF";
                                        String PROPOSAL_URL = ROOT_URL2+row_tv_proposal_url.getText().toString();
                                        Intent intent = new Intent(getApplicationContext(),PdfViewer.class);
                                        intent.putExtra("pdf_url",PROPOSAL_URL);
                                        intent.putExtra("pdf_title",pdf_title);
                                        startActivity(intent);
                                        overridePendingTransition(R.animator.move_left,R.animator.move_right);



                                    }
                                });


                                btnBuyPolicy.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        JSONObject dataObj = new JSONObject();
                                        try {
                                            dataObj.put("proposal_no",row_tv_proposal_id.getText().toString());
                                            dataObj.put("otp","");
                                            dataObj.put("policy_no","");
                                            dataObj.put("quote_link",row_tv_quote_link.getText().toString());
                                            dataObj.put("url",row_tv_proposal_url.getText().toString());


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }



                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ProposalAry",dataObj.toString());
                                        Intent intent = new Intent(getApplicationContext(), ProposalPdfActivity_6.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.animator.move_left,R.animator.move_right);
                                    }
                                });

                                btnQuoteForward.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        API_Quote_Forward(row_tv_quote_link.getText().toString().toUpperCase());
                                    }
                                });

                                ll_parent_saved_proposal.addView(rowView1);

                            }
                        }else {

                            TextView textView = new TextView(getApplicationContext());
                            textView.setText("No Data found...");
                            textView.setPadding(10,10,10,10);

                            ll_parent_saved_proposal.addView(textView);

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
                    CommonMethods.DisplayToastInfo(getApplicationContext(), "Something went wrong. Please try again later.");
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("agent_id", StrAgentId);
                    map.put("proposal_no", StrProposalNo);
                    map.put("vehicle_reg_no", StrRegistrationNo);
                    map.put("policy_holder_mobile_no", StrPolicyHolderMobileNo);
                    map.put("chassis_no",StrChassisNo);
                    Log.d("SavedProposalParam",""+map);

                    return map;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("x-api-key",x_api_key);
                    headers.put("Authorization", "Basic "+CommonMethods.Base64_Encode(api_user_name + ":" + api_password));
                    return headers;
                }
            };


            int socketTimeout = 50000; //50 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }else {
            CommonMethods.DisplayToastInfo(getApplicationContext(), "Please check Internet Connection");

        }





    }


    private void API_Quote_Forward(String QuoteLink) {
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
                    map.put("quote_link", QuoteLink);
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
        final Dialog dialog = new Dialog(SavedProposalActivity.this);
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


    private void ViewFiltersDialog() {


        DialogFilters = new Dialog(SavedProposalActivity.this);
        DialogFilters.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogFilters.setCanceledOnTouchOutside(true);
        DialogFilters.setCancelable(true);
        DialogFilters.setContentView(R.layout.popup_filters);
        Objects.requireNonNull(DialogFilters.getWindow()).setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView title = (TextView)DialogFilters.findViewById(R.id.title) ;
        ImageView iv_close = (ImageView)DialogFilters.findViewById(R.id.iv_close);


        EditText Edt_FilterPolicyNo = (EditText)DialogFilters.findViewById(R.id.Edt_FilterPolicyNo);
        EditText Edt_FilterProposalNo = (EditText)DialogFilters.findViewById(R.id.Edt_FilterProposalNo);
        EditText Edt_FilterMobileNo = (EditText)DialogFilters.findViewById(R.id.Edt_FilterMobileNo);
        EditText Edt_FilterRegNo = (EditText)DialogFilters.findViewById(R.id.Edt_FilterRegNo);
        EditText Edt_FilterChassisNo = (EditText)DialogFilters.findViewById(R.id.Edt_FilterChassisNo);

        Edt_FilterPolicyNo.setVisibility(View.GONE);
        Edt_FilterProposalNo.setVisibility(View.VISIBLE);

        if(StrProposalNo!=null && !StrProposalNo.equalsIgnoreCase("")){
            Edt_FilterProposalNo.setText(StrProposalNo);
        }


        if(StrPolicyHolderMobileNo!=null && !StrPolicyHolderMobileNo.equalsIgnoreCase("")){
            Edt_FilterMobileNo.setText(StrPolicyHolderMobileNo);
        }


        if(StrRegistrationNo!=null && !StrRegistrationNo.equalsIgnoreCase("")){
            Edt_FilterRegNo.setText(StrRegistrationNo);
        }


        if(StrChassisNo!=null && !StrChassisNo.equalsIgnoreCase("")){
            Edt_FilterChassisNo.setText(StrChassisNo);
        }

        Edt_FilterProposalNo.setVisibility(View.GONE);
        Button BtnSearch = (Button)DialogFilters.findViewById(R.id.BtnSearch);

        DialogFilters.show();
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DialogFilters!=null && DialogFilters.isShowing()) {
                    DialogFilters.dismiss();
                }

            }
        });

        BtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Edt_FilterProposalNo.getText().toString()!=null && !Edt_FilterProposalNo.getText().toString().equalsIgnoreCase("")){
                    StrProposalNo = Edt_FilterProposalNo.getText().toString();
                }else {
                    StrProposalNo = "";
                }

                if(Edt_FilterMobileNo.getText().toString()!=null && !Edt_FilterMobileNo.getText().toString().equalsIgnoreCase("")){
                    StrPolicyHolderMobileNo = Edt_FilterMobileNo.getText().toString();
                }else {
                    StrPolicyHolderMobileNo = "";
                }

                if(Edt_FilterRegNo.getText().toString()!=null && !Edt_FilterRegNo.getText().toString().equalsIgnoreCase("")){
                    StrRegistrationNo = Edt_FilterRegNo.getText().toString();
                }else {
                    StrRegistrationNo = "";
                }

                if(Edt_FilterChassisNo.getText().toString()!=null && !Edt_FilterChassisNo.getText().toString().equalsIgnoreCase("")){
                    StrChassisNo = Edt_FilterChassisNo.getText().toString();
                }else {
                    StrChassisNo = "";
                }
                if(DialogFilters!=null && DialogFilters.isShowing()) {
                    DialogFilters.dismiss();
                }
                API_GET_SAVED_PROPOSAL();

            }
        });

    }

    private void ViewPdfFromUrl(String PDF_URL,String PDF_Type) {


        DialogPdfViewer = new Dialog(SavedProposalActivity.this);
        DialogPdfViewer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogPdfViewer.setCanceledOnTouchOutside(true);
        DialogPdfViewer.setCancelable(true);
        DialogPdfViewer.setContentView(R.layout.popup_pdf_viewer);
        Objects.requireNonNull(DialogPdfViewer.getWindow()).setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView title = (TextView)DialogPdfViewer.findViewById(R.id.title) ;
        ImageView iv_close = (ImageView)DialogPdfViewer.findViewById(R.id.iv_close);
        title.setText(PDF_Type);
     /*   PDFView pdfView = (PDFView)DialogPdfViewer.findViewById(R.id.pdfView);
        pdfView.fromUri(Uri.parse(PDF_URL)).load();
*/

        DialogPdfViewer.show();
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DialogPdfViewer!=null && DialogPdfViewer.isShowing()) {
                    DialogPdfViewer.dismiss();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity_1.class);
        startActivity(intent);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }
}
