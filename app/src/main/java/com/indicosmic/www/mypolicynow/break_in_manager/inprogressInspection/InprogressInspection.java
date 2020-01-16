package com.indicosmic.www.mypolicynow.break_in_manager.inprogressInspection;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.adapter.InprogressInspectionAdapter;
import com.indicosmic.www.mypolicynow.break_in_manager.Home;
import com.indicosmic.www.mypolicynow.model.Agentinfo;
import com.indicosmic.www.mypolicynow.model.InprogressInspectionModel;
import com.indicosmic.www.mypolicynow.utils.SharedPrefManager;
import com.indicosmic.www.mypolicynow.webservices.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class InprogressInspection extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout refreshLayout;
    private InprogressInspectionAdapter mInprogressInspectionAdapter;
    ArrayList<InprogressInspectionModel> mInprogressModel_list = new ArrayList<>();
    InprogressInspectionModel mInspectionModel;

    Agentinfo agentinfo;
    String pos_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inprogress_inspection);


        ImageView back_btn_toolbar = (ImageView)findViewById(R.id.back_btn_toolbar);
        back_btn_toolbar.setVisibility(View.VISIBLE);
        TextView til_text = (TextView)findViewById(R.id.til_text);

        back_btn_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        til_text.setText("Inprogress Inspections");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_home);
        recyclerView.setHasFixedSize(true);
        refreshLayout= (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //getting agent info
        agentinfo = SharedPrefManager.getInstance(this).getAgent();
        pos_id = agentinfo.id;


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mInprogressModel_list.clear();
                        showInprogressInspectiondata();
                        mInprogressInspectionAdapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                    }
                },1000);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        });

        showInprogressInspectiondata();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }

    private void showInprogressInspectiondata() {
        Log.d("URLINPROGRESSINSP",Common.URL_INPROGRESS_INSPECTION);

        StringRequest postrequest = new StringRequest(Request.Method.POST, Common.URL_INPROGRESS_INSPECTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    if (response != null && response.length() > 0) {
                        Log.d("URLINPROGRESSResp",""+response);


                        JSONObject jsonresponse = new JSONObject(response);

                        String status = jsonresponse.getString("status");

                        if (status.trim().contains("TRUE")) {

                            JSONArray customer_inspect_array = jsonresponse.getJSONArray("data");

                            for (int i = 0; i < customer_inspect_array.length(); i++) {

                                JSONObject customer_inspect_obj = customer_inspect_array.getJSONObject(i);

                                //Customer vehicle info
                                String inspection_id = customer_inspect_obj.getString("id");
                                String proposal_no = customer_inspect_obj.getString("proposal_no");
                                String registration_no = customer_inspect_obj.getString("vehicle_reg_no");
                                String ic_name = customer_inspect_obj.getString("ic_name");
                                String policy_start_date = customer_inspect_obj.getString("proposal_from_date");
                                String ic_id = customer_inspect_obj.getString("ic_id");

                                mInspectionModel = new InprogressInspectionModel();
                                mInspectionModel.setId(inspection_id);
                                mInspectionModel.setProposal_no(proposal_no);
                                mInspectionModel.setRegistration_no(registration_no);
                                mInspectionModel.setSub_inspec_date(policy_start_date);
                                mInspectionModel.setIc_name(ic_name);
                                mInprogressModel_list.add(mInspectionModel);

                            }

                        }
                        if (status.trim().contains("FALSE")) {
                            Toast.makeText(InprogressInspection.this, "Sorry No data is available", Toast.LENGTH_SHORT).show();
                        }
                        mInprogressInspectionAdapter = new InprogressInspectionAdapter(InprogressInspection.this, mInprogressModel_list, refreshLayout);
                        recyclerView.setAdapter(mInprogressInspectionAdapter);
                        mInprogressInspectionAdapter.notifyDataSetChanged();

                    }else {
                        Toast.makeText(InprogressInspection.this, "Something goes wrong. Please try again", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                //Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams () {
                Map<String, String> map = new HashMap<String, String>();
                map.put("pos_id", pos_id);
                if (SharedPrefManager.getInstance(getApplicationContext()).getAgent().business_id != null && !SharedPrefManager.getInstance(getApplicationContext()).getAgent().business_id.isEmpty()
                        && !SharedPrefManager.getInstance(getApplicationContext()).getAgent().business_id.equals("")) {
                    map.put("business_id",SharedPrefManager.getInstance(getApplicationContext()).getAgent().business_id);
                }else {
                    map.put("business_id","");
                }
                //     map.put("images",encodedString);
                Log.d("POSTDATA", "posting progressid: " + map.toString());
                return map;
            }
        } ;
        RequestQueue rQueue = Volley.newRequestQueue(InprogressInspection.this);
        rQueue.add(postrequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_inspections,menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        //    mInprogressInspectionAdapter.notifyDataSetChanged();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mInprogressInspectionAdapter != null){
                    mInprogressInspectionAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });

    }

}
