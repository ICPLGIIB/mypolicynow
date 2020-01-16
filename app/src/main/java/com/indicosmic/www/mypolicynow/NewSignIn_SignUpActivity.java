package com.indicosmic.www.mypolicynow;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.indicosmic.www.mypolicynow.adapter.SignInTab;
import com.indicosmic.www.mypolicynow.adapter.SignUpTab;
import com.indicosmic.www.mypolicynow.adapter.TabAdapter;

public class NewSignIn_SignUpActivity  extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    boolean swipeEnabled = true;
    TabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_signup);

        init();
    }

    private void init() {
        swipeEnabled = false;
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new SignInTab(), "SIGN IN");
        adapter.addFragment(new SignUpTab(), "SIGN UP");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
      /*  ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // disable swipe
                if(!swipeEnabled) {
                    if (viewPager.getAdapter().getCount()>1) {
                        viewPager.setCurrentItem(1);
                        viewPager.setCurrentItem(0);
                    }
                }
            }
            public void onPageScrollStateChanged(int state) {}
            public void onPageSelected(int position) {}
        };
        viewPager.addOnPageChangeListener(onPageChangeListener);*/


    }



    public void navigateFragment(int position) {
        viewPager.setCurrentItem(position, true);
    }
}
