package com.indicosmic.www.mypolicynow.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.break_in_manager.inprogressInspection.InprogressInspection;
import com.indicosmic.www.mypolicynow.break_in_manager.newInspection.NewInspection;
import com.indicosmic.www.mypolicynow.break_in_manager.referbackInspection.ReferbackInspection;
import com.indicosmic.www.mypolicynow.model.Agentinfo;
import com.indicosmic.www.mypolicynow.utils.SharedPrefManager;
import com.indicosmic.www.mypolicynow.utils.SingletonClass;
import com.indicosmic.www.mypolicynow.utils.StoreCounter;
import com.indicosmic.www.mypolicynow.webservices.Common;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewVehicleFragment extends Fragment implements View.OnClickListener{

    SwipeRefreshLayout refreshLayout_fragment;
    TextView badge_new_notification,badge_inprogress_notification,badge_completed_notification,badge_approved_notification;

    TextView txtName,txtAddress,txtEmail,txtMobile,txtDob;
    ImageView newinspection,inprogress,referback,profilepic;
    SpotsDialog spotsDialog;

    Integer newInspection_count,inprogress_count,referback_count;
    Agentinfo agent;
    String pos_id;
    StoreCounter mStoreCounter;
    String pening_counter,inprogress_counter,referback_counter;

    public NewVehicleFragment() {
        // Required empty public constructor
    }


    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_inspection, container, false);

        //getting agent info
        agent = SharedPrefManager.getInstance(getContext()).getAgent();
        pos_id = SharedPrefManager.getInstance(getActivity()).getAgent().getId();
        spotsDialog = new SpotsDialog(getActivity());
        mStoreCounter = new StoreCounter(getContext());
        HashMap<String, String> counterValue = mStoreCounter.getCounterValue();

        refreshLayout_fragment = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout_fragment);

        badge_new_notification = view.findViewById(R.id.badge_new_notification);
        badge_inprogress_notification = view.findViewById(R.id.badge_inprogress_notification);
        badge_completed_notification = view.findViewById(R.id.badge_referback_notification);
        //badge_approved_notification = view.findViewById(R.id.badge_approved_notification);

        newInspection_count = Integer.parseInt(counterValue.get(StoreCounter.KEY_PENDING));
      //  newInspection_count = SingletonClass.getinstance().newInspection_count;
        badge_new_notification.setText(String.format("%02d",newInspection_count));

        inprogress_count = Integer.parseInt(counterValue.get(StoreCounter.KEY_INPROGRESS));
       // inprogress_count = SingletonClass.getinstance().inprogress_count;
        badge_inprogress_notification.setText(String.format("%02d", inprogress_count));

        referback_count = Integer.parseInt(counterValue.get(StoreCounter.KEY_REFERBACK));
        //referback_count = SingletonClass.getinstance().referback_count;
        badge_completed_notification.setText(String.format("%02d", referback_count));

        fetchCounter();


        refreshLayout_fragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fetchCounter();
                        getActivity().finish();
                        getActivity().overridePendingTransition(0, 0);
                        getActivity().startActivity(getActivity().getIntent());
                        getActivity().overridePendingTransition(0, 0);
                        refreshLayout_fragment.setRefreshing(false);
                    }
                },3000);
                //  refreshLayout.setItemAnimator(new DefaultItemAnimator());
            }
        });


        profilepic = (ImageView) view.findViewById(R.id.profilepic);
        txtName = (TextView)view.findViewById(R.id.txtName);
        txtAddress = (TextView)view.findViewById(R.id.txtAddress);
        txtEmail = (TextView)view.findViewById(R.id.txtEmail);
        txtMobile = (TextView)view.findViewById(R.id.txtMobile);
        txtDob = (TextView)view.findViewById(R.id.txtDob);

        //fragments
        newinspection = (ImageView) view.findViewById(R.id.newinspection);
        inprogress= (ImageView) view.findViewById(R.id.inprogress);
        referback = (ImageView) view.findViewById(R.id.referback);

        newinspection.setOnClickListener(this);
        inprogress.setOnClickListener(this);
        referback.setOnClickListener(this);

        //getting agent info
        agent = SharedPrefManager.getInstance(getActivity()).getAgent();

        //Glide.with(getContext()).load("").placeholder(R.drawable.placeholder).into(profilepic);
        profilepic.setImageDrawable(getResources().getDrawable(R.drawable.placeholder));

        if(agent.getFull_name()!=null && !agent.getFull_name().equalsIgnoreCase("")){
            txtName.setText(agent.getFull_name());

        }else {
            txtName.setText("");

        }


        if(agent.getEmail()!=null && !agent.getEmail().equalsIgnoreCase("")){
            txtEmail.setText(agent.getEmail());

        }else {
            txtEmail.setText("");

        }

        if(agent.getMobile_number()!=null && !agent.getMobile_number().equalsIgnoreCase("")){
            txtMobile.setText(agent.getMobile_number());

        }else {
            txtMobile.setText("");

        }
        if(agent.getDob()!=null && (!agent.getDob().equalsIgnoreCase("") || !agent.getDob().equalsIgnoreCase("null"))){

            if(agent.getDob()!=null && !agent.getDob().equalsIgnoreCase("")){
                String DisplayDate = Common.DateDisplayedFormat(agent.getDob());
                txtDob.setText(DisplayDate);
            }else {
                txtDob.setText("");
            }



        }

        if(agent.getAddress()!=null && (!agent.getAddress().equalsIgnoreCase("") || !agent.getAddress().equalsIgnoreCase("null"))){
            txtAddress.setText(agent.getAddress());
        }else{
            txtAddress.setText("");
        }

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.w(TAG, "onResume: ");
        HashMap<String, String> counterValue = mStoreCounter.getCounterValue();

        newInspection_count = Integer.parseInt(counterValue.get(StoreCounter.KEY_PENDING));
        //  newInspection_count = SingletonClass.getinstance().newInspection_count;
        badge_new_notification.setText(String.format("%02d",newInspection_count));

        inprogress_count = Integer.parseInt(counterValue.get(StoreCounter.KEY_INPROGRESS));
        // inprogress_count = SingletonClass.getinstance().inprogress_count;
        badge_inprogress_notification.setText(String.format("%02d", inprogress_count));

        referback_count = Integer.parseInt(counterValue.get(StoreCounter.KEY_REFERBACK));
        //referback_count = SingletonClass.getinstance().referback_count;
        badge_completed_notification.setText(String.format("%02d", referback_count));

    }

    @Override
    public void onClick(View view) {
        if (view == newinspection){
            Intent i = new Intent(getActivity(), NewInspection.class);
            startActivity(i);
        }if (view == inprogress){
            Intent i = new Intent(getActivity(), InprogressInspection.class);
            startActivity(i);
        }if (view == referback){
            Intent i = new Intent(getActivity(), ReferbackInspection.class);
            startActivity(i);
        }
    }



    public void fetchCounter() {
        spotsDialog = new SpotsDialog(getActivity());
        spotsDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Common.URL_INSPECTION_COUNTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(spotsDialog!=null && spotsDialog.isShowing()) {
                    spotsDialog.dismiss();
                }
                try {

                    Log.d("Response_FetchCounter",""+response);
                    JSONObject jsonresponse = new JSONObject(response);

                    String status = jsonresponse.getString("status");

                    if (status.trim().contains("TRUE")) {
                        JSONObject jsonObject = jsonresponse.getJSONObject("data");
                        pening_counter = jsonObject.getString("Pending");
                        SingletonClass.getinstance().newInspection_count = Integer.parseInt(pening_counter);

                        inprogress_counter = jsonObject.getString("inprocess");
                        SingletonClass.getinstance().inprogress_count = Integer.parseInt(inprogress_counter);

                        referback_counter = jsonObject.getString("referback");
                        SingletonClass.getinstance().referback_count = Integer.parseInt(referback_counter);

                        mStoreCounter.storeCounterValue(pening_counter, inprogress_counter, referback_counter);

                    }

                } catch (Exception e) {
                    if(spotsDialog!=null && spotsDialog.isShowing()) {
                        spotsDialog.dismiss();
                    }
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(spotsDialog!=null && spotsDialog.isShowing()) {
                    spotsDialog.dismiss();
                }
                Toast.makeText(getContext(), "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();

                //Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                if(SingletonClass.getinstance().pos_id==null){
                    SingletonClass.getinstance().pos_id =  SharedPrefManager.getInstance(getActivity()).getAgent().id;
                }

                map.put("pos_id",SingletonClass.getinstance().pos_id);
                if (SharedPrefManager.getInstance(getContext()).getAgent().business_id == null) {
                    map.put("business_id","");
                }else {
                    map.put("business_id",SharedPrefManager.getInstance(getContext()).getAgent().business_id);
                }

                //     map.put("images",encodedString);
                Log.d("POSTDATA", "posting completedid: "+map.toString());
                return map;
            }
        };


        int socketTimeout = 50000; //30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

}
