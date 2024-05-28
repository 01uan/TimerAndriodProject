package com.example.giaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.swipe);

        CollectionAdapter collectionAdapter = new CollectionAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(collectionAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Timer");
                            break;
                        case 1:
                            tab.setText("Task");
                            break;
                        case 2:
                            tab.setText("Settings");
                            break;
                    }
                }
        ).attach();
    }
}