package com.example.androidnotesapplication;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StartActivity extends AppCompatActivity {
    private static final String TAG = "StartActivity";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private SectionPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        viewPager=findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout=findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Login");
        tabLayout.getTabAt(1).setText("Register");

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter= new SectionPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new LoginFragment());
        adapter.AddFragment(new RegisterFragment());
        viewPager.setAdapter(adapter);

    }
}
