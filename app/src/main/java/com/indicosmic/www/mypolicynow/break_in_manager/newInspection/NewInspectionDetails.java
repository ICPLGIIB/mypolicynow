package com.indicosmic.www.mypolicynow.break_in_manager.newInspection;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.break_in_manager.Home;
import com.indicosmic.www.mypolicynow.break_in_manager.QuestionActivityListView;
import com.indicosmic.www.mypolicynow.utils.SingletonClass;
import com.indicosmic.www.mypolicynow.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow.webservices.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class NewInspectionDetails extends AppCompatActivity {

    String proposal_list_id,str_proposal_no,str_registration_no,str_ic_name,str_mobile_no;
    TextView txtcustomer_name,txtcustomer_address,registration_date;
    ImageView vehicle_thumbnail;
    TextView btnStartInspection,btnCall;
    TextView regno,proposal_no,txtmake,txtmodel,txtvarient,txtengineno,txtchassisno,txtnildep,txtncb,txtregistration_year,txticname;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_inspection_details);


        inits();
    }

    private void inits() {
        ImageView back_btn_toolbar = (ImageView)findViewById(R.id.back_btn_toolbar);
        back_btn_toolbar.setVisibility(View.VISIBLE);
        TextView til_text = (TextView)findViewById(R.id.til_text);

        back_btn_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        til_text.setText("New Inspection Details");

        vehicle_thumbnail = (ImageView) findViewById(R.id.vehicle_thumbnail);
        txtcustomer_name = (TextView)findViewById(R.id.customer_name);
        txtcustomer_address = (TextView)findViewById(R.id.customer_address);
        proposal_no = (TextView)findViewById(R.id.proposal_no);
        regno = (TextView)findViewById(R.id.regno);
        txtmake = (TextView)findViewById(R.id.make);
        txtmodel = (TextView)findViewById(R.id.model);
        txtvarient = (TextView)findViewById(R.id.varient);
        txtengineno = (TextView)findViewById(R.id.engineno);
        txtchassisno = (TextView)findViewById(R.id.chassisno);
        txtnildep = (TextView)findViewById(R.id.nildep);
        txtncb = (TextView)findViewById(R.id.ncb);
        txtregistration_year = (TextView)findViewById(R.id.registration_year);
        txticname = (TextView)findViewById(R.id.ic_name);
        registration_date = (TextView) findViewById(R.id.registration_date);


        btnStartInspection = (TextView) findViewById(R.id.btnStartInspection);

        btnCall = (TextView) findViewById(R.id.btnCall);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);



        btnStartInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Intent i = new Intent(NewInspectionDetails.this, NextQuestions.class);
                startActivity(i);
                finish();*/


                Intent i = new Intent(NewInspectionDetails.this, QuestionActivityListView.class);
                startActivity(i);
                finish();

            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(NewInspectionDetails.this).create();
                alertDialog.setMessage("Call " + str_mobile_no);

                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Call", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + str_mobile_no));

                        Intent callIntent = new Intent(Intent.ACTION_VIEW);
                        callIntent.setData(Uri.parse("tel:"+str_mobile_no));
                        startActivity(callIntent);

                        /*if (ActivityCompat.checkSelfPermission(NewInspectionDetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling

                        }*/
                        //startActivity(callIntent);
                        dialog.dismiss();
                    }
                });

                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            Glide.with(getApplicationContext()).load(R.drawable.car).into(vehicle_thumbnail);
            proposal_list_id = bundle.getString("proposal_list_id");
            SingletonClass.getinstance().proposal_list_id = proposal_list_id;
            str_proposal_no = bundle.getString("proposal_no");
            str_registration_no = bundle.getString("registration_no");
            str_ic_name = bundle.getString("ic_name");
            Log.d( "onCreate: ",proposal_list_id);
            UtilitySharedPreferences.setPrefs(getApplicationContext(),"IC_NAME",str_ic_name);
            Log.d( "IC_NAME: ","-->"+str_ic_name);

        }

        showNewInspectionDetails();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), NewInspection.class);
        startActivity(intent);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            Glide.with(getApplicationContext()).load(R.drawable.car).into(vehicle_thumbnail);
            proposal_list_id = bundle.getString("proposal_list_id");
            SingletonClass.getinstance().proposal_list_id = proposal_list_id;
            str_proposal_no = bundle.getString("proposal_no");
            str_registration_no = bundle.getString("registration_no");
            str_ic_name = bundle.getString("ic_name");
        }

        Log.d( "onResume: ",proposal_list_id);
    }

    private void showNewInspectionDetails() {
        progressDialog.show();

        StringRequest postrequest = new StringRequest(Request.Method.POST, Common.URL_NEWINSPECTION_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Details",""+response);
                try {
                    if(progressDialog!=null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    response = response.trim();
                    JSONObject jsonresponse = new JSONObject(response);

                    String status = jsonresponse.getString("status");

                    if (status.trim().contains("True")) {

                        JSONObject customer_inspect_obj = jsonresponse.getJSONObject("data");

                        //Customer vehicle info
                        String st_proposal_no = customer_inspect_obj.getString("proposal_no");
                        String customer_name = customer_inspect_obj.getString("customer_name");

                        String customer_address = customer_inspect_obj.getString("address");
                        str_mobile_no = customer_inspect_obj.getString("customer_mobile");
                        String make = customer_inspect_obj.getString("make");
                        String model = customer_inspect_obj.getString("model");
                        String varient = customer_inspect_obj.getString("varient_name");
                        String engine_number = customer_inspect_obj.getString("engine_number");
                        String chassis_number = customer_inspect_obj.getString("chassis_number");
                        String registration_year = customer_inspect_obj.getString("manf_year");
                        String str_registration_date = customer_inspect_obj.getString("manf_date");
                        String nill_deep_status = customer_inspect_obj.getString("nill_deep_status");
                        String ncb_status = customer_inspect_obj.getString("ncb_status");
                        String ic_id = customer_inspect_obj.getString("ic_id");

                        SingletonClass.getinstance().ic_id = ic_id;

                        txtcustomer_name.setText(customer_name.toUpperCase());
                        txtcustomer_address.setText(customer_address.toUpperCase());
                        txticname.setText(str_ic_name.toUpperCase());
                        proposal_no.setText(st_proposal_no.toUpperCase());
                        regno.setText(str_registration_no.toUpperCase());
                        txtregistration_year.setText(registration_year);
                        registration_date.setText(str_registration_date);
                        txtmake.setText(make.toUpperCase());
                        txtmodel.setText(model.toUpperCase());
                        txtvarient.setText(varient.toUpperCase());
                        txtengineno.setText(engine_number.toUpperCase());
                        txtchassisno.setText(chassis_number.toUpperCase());
                        txtnildep.setText(nill_deep_status.toUpperCase().toUpperCase());
                        txtncb.setText(ncb_status.toUpperCase());
                      //  btnStartInspection.setEnabled(true);
                        //btnStartInspection.getBackground().setColorFilter(null);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    if(progressDialog!=null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(progressDialog!=null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams () {
                Map<String, String> map = new HashMap<String, String>();
                map.put("proposal_list_id", proposal_list_id);
                //     map.put("images",encodedString);
                Log.d("POSTDATA", "proposal_list_id: " + map.toString());
                return map;
            }
        } ;
        RequestQueue rQueue = Volley.newRequestQueue(NewInspectionDetails.this);
        rQueue.add(postrequest);

    }


}
