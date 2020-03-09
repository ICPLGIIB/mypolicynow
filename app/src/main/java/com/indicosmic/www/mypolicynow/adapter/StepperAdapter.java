package com.indicosmic.www.mypolicynow.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;


import com.indicosmic.www.mypolicynow.customer_info.OwnerDetailFragment;
import com.indicosmic.www.mypolicynow.customer_info.NomineeDetailsFragment;
import com.indicosmic.www.mypolicynow.customer_info.ContactDetailsFragment;
import com.indicosmic.www.mypolicynow.customer_info.VehicleDetailsFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;


public class StepperAdapter extends AbstractFragmentStepAdapter {

    private static final String CURRENT_STEP_POSITION_KEY = "messageResourceId";
    public StepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }
    @Override
    public Step createStep(int position) {
        switch (position){

            case 0:
                final OwnerDetailFragment step0 = new OwnerDetailFragment();
                Bundle b0 = new Bundle();
                b0.putInt(CURRENT_STEP_POSITION_KEY, position);
                step0.setArguments(b0);
                return step0;

            case 1:
                final NomineeDetailsFragment step1 = new NomineeDetailsFragment();
                Bundle b1 = new Bundle();
                b1.putInt(CURRENT_STEP_POSITION_KEY, position);
                step1.setArguments(b1);
                return step1;

            case 2:
                final ContactDetailsFragment step2 = new ContactDetailsFragment();
                Bundle b2 = new Bundle();
                b2.putInt(CURRENT_STEP_POSITION_KEY, position);
                step2.setArguments(b2);
                return step2;

            case 3:
                final VehicleDetailsFragment step3 = new VehicleDetailsFragment();
                Bundle b3 = new Bundle();
                b3.putInt(CURRENT_STEP_POSITION_KEY, position);
                step3.setArguments(b3);
                return step3;

          /*  case 4:
                final ReviewDetailsActivity step4 = new ReviewDetailsActivity();
                Bundle b4 = new Bundle();
                b4.putInt(CURRENT_STEP_POSITION_KEY, position);
                step4.setArguments(b4);
                return step4;*/




        }
        return null;
    }
    @Override
    public int getCount() {
        return 4;
    }
    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        switch (position){
            case 0:
                return new StepViewModel.Builder(context)
                        .setTitle("OWNER DETAILS")
                        .setEndButtonLabel("NOMINEE DETAILS")
                        .create();
            case 1:
                return new StepViewModel.Builder(context)
                        .setTitle("NOMINEE DETAILS")
                        .setBackButtonLabel("OWNER DETAILS")
                        .setEndButtonLabel("ADDRESS DETAILS")
                        .create();

            case 2:
                return new StepViewModel.Builder(context)
                        .setTitle("ADDRESS DETAILS")
                        .setBackButtonLabel("NOMINEE DETAILS")
                        .setEndButtonLabel("VEHICLE DETAILS")
                        .create();


            case 3:
                return new StepViewModel.Builder(context)
                        .setTitle("VEHICLE DETAILS")
                        .setBackButtonLabel("ADDRESS DETAILS")
                        .setEndButtonLabel("REVIEW DETAILS")
                        .create();

           /* case 4:
                return new StepViewModel.Builder(context)
                        .setTitle("REVIEW DETAILS")
                        .setBackButtonLabel("EDIT")
                        .setEndButtonLabel("GENERATE PROPOSAL")
                        .create();*/




        }
        return null;
    }
}
