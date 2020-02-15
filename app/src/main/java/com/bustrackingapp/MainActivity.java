package com.bustrackingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;

    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     tabLayout=findViewById(R.id.tablelayout);
     viewPager=findViewById(R.id.viewpager);

     Intent intent=getIntent();
     String selectedBusValue=intent.getStringExtra("route_value");

     ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),
             tabLayout.getTabCount(),selectedBusValue);

     viewPager.setAdapter(viewPagerAdapter);
     viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
     tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
         @Override
         public void onTabSelected(TabLayout.Tab tab) {
             viewPager.setCurrentItem(tab.getPosition());
         }

         @Override
         public void onTabUnselected(TabLayout.Tab tab) {

         }

         @Override
         public void onTabReselected(TabLayout.Tab tab) {

         }
     });



    }
}
