package com.indicosmic.www.mypolicynow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import com.indicosmic.www.mypolicynow.utils.UtilitySharedPreferences;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;

public class SettingsActivity extends AppCompatActivity {

    ImageView back_btn,edit_btn_toolbar;
    TextView til_text;
    ProgressDialog myDialog;
    String StrMobileNo,StrUserName,StrUserId,StrUserEmail,StrUniqueUserId,StrVehicleId="",StrPucReminder="0";
    CardView CardNotificationSettings,CardDeletedVehicles;
    ToggleSwitch toggleBtnBackgroundScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        init();
    }

    private void init() {

        back_btn = (ImageView) findViewById(R.id.back_btn_toolbar);
        back_btn.setVisibility(View.VISIBLE);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        

        til_text = (TextView) findViewById(R.id.til_text);
        til_text.setText("SETTINGS");

        myDialog = new ProgressDialog(SettingsActivity.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);

        StrMobileNo = UtilitySharedPreferences.getPrefs(getApplicationContext(), "UserMobile");
        StrUserName = UtilitySharedPreferences.getPrefs(getApplicationContext(), "UserName");
        StrUserId = UtilitySharedPreferences.getPrefs(getApplicationContext(), "UserId");
        StrUserEmail = UtilitySharedPreferences.getPrefs(getApplicationContext(), "UserEmail");
        StrUniqueUserId = UtilitySharedPreferences.getPrefs(getApplicationContext(), "UniqueUserId");


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.animator.left_right, R.animator.right_left);
        finish();
    }
}
