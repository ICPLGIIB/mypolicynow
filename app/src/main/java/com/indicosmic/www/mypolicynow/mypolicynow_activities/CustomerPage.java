package com.indicosmic.www.mypolicynow.mypolicynow_activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.adapter.StepperAdapter;
import com.stepstone.stepper.StepperLayout;

public class CustomerPage extends AppCompatActivity {

    StepperLayout mStepperLayout;
    String StrEmployeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_page);

        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this));


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to go back?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CustomerPage.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }
}