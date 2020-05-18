package com.indicosmic.mypolicynow_app.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.indicosmic.mypolicynow_app.R;
import com.indicosmic.mypolicynow_app.adapter.StepperAdapter;
import com.stepstone.stepper.StepperLayout;

public class CustomerPage_4 extends AppCompatActivity {

    StepperLayout mStepperLayout;
    String StrEmployeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_page);

        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this));


    }

}