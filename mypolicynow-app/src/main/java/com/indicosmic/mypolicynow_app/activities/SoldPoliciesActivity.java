package com.indicosmic.mypolicynow_app.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.indicosmic.mypolicynow_app.utils.CommonMethods.ucFirst;
import static com.indicosmic.mypolicynow_app.webservices.RestClient.ROOT_URL2;
import static com.indicosmic.mypolicynow_app.webservices.RestClient.api_password;
import static com.indicosmic.mypolicynow_app.webservices.RestClient.api_user_name;
import static com.indicosmic.mypolicynow_app.webservices.RestClient.x_api_key;

public class SoldPoliciesActivity extends AppCompatActivity {

    MultipleSelectionSpinner mSpinner;
    List<String> list = new ArrayList<String>();
    TextView tv_filters;
    ProgressDialog myDialog;
    LinearLayout ll_parent_sold_policies;
    String StrAgentId="",StrPolicyNo="",StrRegistrationNo="",StrPolicyHolderMobileNo="",StrChassisNo="";
    Dialog DialogFilters,DialogPdfViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sold_policies_screen_list);

        init();
    }

    private void init() {

        StrAgentId =   UtilitySharedPreferences.getPrefs(getApplicationContext(),"PosId");

        myDialog = new ProgressDialog(SoldPoliciesActivity.this);
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
        til_text.setText("SOLD POLICIES");

        tv_filters = (TextView)findViewById(R.id.tv_filters);
        tv_filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewFiltersDialog();
            }
        });

        ll_parent_sold_policies = (LinearLayout)findViewById(R.id.ll_parent_sold_policies);

        API_GET_SOLD_POLICIES();




    }

    private void API_GET_SOLD_POLICIES() {
        if( ll_parent_sold_policies.getChildCount()>0 ) {
            ll_parent_sold_policies.removeAllViews();
        }
        myDialog.show();

        String URL = ROOT_URL2+"front/myaccount/SoldPolicy";

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

                                String customer_name = jsonObject.getString("customer_name");
                                String mobile_no = jsonObject.getString("mobile_no");
                                String ic_name = jsonObject.getString("ic_name");
                                String policy_no = jsonObject.getString("policy_no");
                                String vehicle_reg_no = jsonObject.getString("vehicle_reg_no");
                                String created_at = jsonObject.getString("created_at");
                                String vehicle_type = jsonObject.getString("vehicle_type");

                                String net_od = jsonObject.getString("net_od");
                                String total_addon_premium = jsonObject.getString("total_addon_premium");
                                String pa_owner_driver_premium = jsonObject.getString("pa_owner_driver_premium");
                                String ll_paid_driver_premium = jsonObject.getString("ll_paid_driver_premium");
                                String gst_value = jsonObject.getString("gst_value");
                                String gross_premium = jsonObject.getString("gross_premium");
                                String policy_id = jsonObject.getString("policy_id");
                                String policy_lite_pdf = ROOT_URL2+"lite-download-policy/"+policy_id;
                                String policy_pdf = ROOT_URL2+"downloadPolicyPdf/"+policy_id;

                                LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View rowView1 = Objects.requireNonNull(inflater1).inflate(R.layout.sold_policies_row, null);
                                rowView1.setPadding(10, 10, 10, 10);

                                TextView row_policy_issuer_name = (TextView) rowView1.findViewById(R.id.row_policy_issuer_name);
                                TextView row_tv_mobile_no = (TextView) rowView1.findViewById(R.id.row_tv_mobile_no);

                                TextView row_tv_ins_company = (TextView) rowView1.findViewById(R.id.row_tv_ins_company);
                                TextView row_tv_policy_id = (TextView) rowView1.findViewById(R.id.row_tv_policy_id);
                                TextView row_tv_policy_no = (TextView) rowView1.findViewById(R.id.row_tv_policy_no);
                                TextView row_tv_issue_date = (TextView) rowView1.findViewById(R.id.row_tv_issue_date);
                                TextView row_tv_vehicle_type = (TextView) rowView1.findViewById(R.id.row_tv_vehicle_type);
                                TextView row_tv_reg_no = (TextView) rowView1.findViewById(R.id.row_tv_reg_no);



                                TextView row_tv_od_premium = (TextView) rowView1.findViewById(R.id.row_tv_od_premium);
                                TextView row_tv_add_on_premium = (TextView) rowView1.findViewById(R.id.row_tv_add_on_premium);

                                LinearLayout third_party_layout = (LinearLayout) rowView1.findViewById(R.id.third_party_layout);
                                TextView row_tv_third_party = (TextView) rowView1.findViewById(R.id.row_tv_third_party);

                                TextView row_tv_pa_od = (TextView) rowView1.findViewById(R.id.row_tv_pa_od);
                                TextView row_tv_ll_pd = (TextView) rowView1.findViewById(R.id.row_tv_ll_pd);
                                TextView row_tv_total_gross_premium = (TextView) rowView1.findViewById(R.id.row_tv_total_gross_premium);
                                TextView row_tv_gst = (TextView) rowView1.findViewById(R.id.row_tv_gst);
                                TextView row_tv_net_premium = (TextView) rowView1.findViewById(R.id.row_tv_net_premium);

                                TextView row_tv_quote_link = (TextView) rowView1.findViewById(R.id.row_tv_quote_link);
                                TextView row_tv_proposal_url = (TextView) rowView1.findViewById(R.id.row_tv_proposal_url);

                                TextView row_tv_policy_url = (TextView) rowView1.findViewById(R.id.row_tv_policy_url);
                                TextView row_tv_policy_lite_url = (TextView) rowView1.findViewById(R.id.row_tv_policy_lite_url);

                                Button btnCancel =  (Button) rowView1.findViewById(R.id.btnCancel);
                                Button btnProposal =  (Button) rowView1.findViewById(R.id.btnProposal);
                                Button btnPolicy =  (Button) rowView1.findViewById(R.id.btnPolicy);
                                Button btnPolicyPdfLite =  (Button) rowView1.findViewById(R.id.btnPolicyPdfLite);

                                row_policy_issuer_name.setText(customer_name.toUpperCase());
                                row_tv_mobile_no.setText(mobile_no);
                                row_tv_ins_company.setText(ic_name);
                                row_tv_policy_id.setText(policy_id);
                                row_tv_policy_no.setText(policy_no);
                                row_tv_issue_date.setText(created_at);
                                row_tv_vehicle_type.setText(vehicle_type);
                                row_tv_reg_no.setText(vehicle_reg_no.toUpperCase());

                                row_tv_od_premium.setText("\u20B9 " + net_od);
                                row_tv_add_on_premium.setText("\u20B9 " + total_addon_premium);

                                row_tv_pa_od.setText("\u20B9 " + pa_owner_driver_premium);
                                row_tv_ll_pd.setText("\u20B9 " + ll_paid_driver_premium);
                                row_tv_gst.setText("\u20B9 " + gst_value);
                                row_tv_net_premium.setText("\u20B9 " + gross_premium);


                                row_tv_policy_url.setText(policy_pdf);
                                row_tv_policy_lite_url.setText(policy_lite_pdf);

                                btnCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CommonMethods.DisplayToastInfo(getApplicationContext(),"Cancel Policy - "+row_tv_policy_no.getText().toString());
                                    }
                                });

                                btnProposal.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String pdf_title = "PROPOSAL PDF";

                                        Intent intent = new Intent(getApplicationContext(),PdfViewer.class);
                                        intent.putExtra("pdf_url",row_tv_proposal_url.getText().toString());
                                        intent.putExtra("pdf_title",pdf_title);
                                        startActivity(intent);
                                        overridePendingTransition(R.animator.move_left,R.animator.move_right);



                                    }
                                });


                                btnPolicy.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String pdf_title = "DETAILED POLICY PDF";

                                        Intent intent = new Intent(getApplicationContext(),PdfViewer.class);
                                        intent.putExtra("pdf_url",row_tv_policy_url.getText().toString());
                                        intent.putExtra("pdf_title",pdf_title);
                                        startActivity(intent);
                                        overridePendingTransition(R.animator.move_left,R.animator.move_right);
                                    }
                                });

                                btnPolicyPdfLite.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String pdf_title = "POLICY LITE VERSION";

                                        Intent intent = new Intent(getApplicationContext(),PdfViewer.class);
                                        intent.putExtra("pdf_url",row_tv_policy_lite_url.getText().toString());
                                        intent.putExtra("pdf_title",pdf_title);
                                        startActivity(intent);
                                        overridePendingTransition(R.animator.move_left,R.animator.move_right);
                                    }
                                });

                                ll_parent_sold_policies.addView(rowView1);

                            }
                        }else {

                            TextView textView = new TextView(getApplicationContext());
                            textView.setText("No Data found...");
                            textView.setPadding(10,10,10,10);

                            ll_parent_sold_policies.addView(textView);

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
                    map.put("policy_no", StrPolicyNo);
                    map.put("vehicle_reg_no", StrRegistrationNo);
                    map.put("policy_holder_mobile_no", StrPolicyHolderMobileNo);
                    map.put("chassis_no",StrChassisNo);
                    Log.d("SoldPolicyParam",""+map);

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

    private void ViewFiltersDialog() {


        DialogFilters = new Dialog(SoldPoliciesActivity.this);
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


        if(StrPolicyNo!=null && !StrPolicyNo.equalsIgnoreCase("")){
            Edt_FilterPolicyNo.setText(StrPolicyNo);
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

                if(Edt_FilterPolicyNo.getText().toString()!=null && !Edt_FilterPolicyNo.getText().toString().equalsIgnoreCase("")){
                    StrPolicyNo = Edt_FilterPolicyNo.getText().toString();
                }else {
                    StrPolicyNo = "";
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
                API_GET_SOLD_POLICIES();

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
