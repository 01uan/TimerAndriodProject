package com.example.giaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * MainActivity class that handles the main functionality of the app.
 * This activity sets up the tab layout and view pager for navigating between different fragments.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains
     *                           the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the TabLayout and ViewPager2
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.swipe);

        // Set the adapter for ViewPager2
        CollectionAdapter collectionAdapter = new CollectionAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(collectionAdapter);

        // Attach the TabLayout and ViewPager2 using TabLayoutMediator
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Timer"); // Set text for Timer tab
                            break;
                        case 1:
                            tab.setText("Task"); // Set text for Task tab
                            break;
                        case 2:
                            tab.setText("Settings"); // Set text for Settings tab
                            break;
                    }
                }
        ).attach();
    }
}