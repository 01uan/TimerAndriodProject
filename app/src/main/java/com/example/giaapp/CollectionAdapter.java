package com.example.giaapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * CollectionAdapter is a FragmentStateAdapter that manages the fragments in the ViewPager.
 * It creates and returns the fragment corresponding to the position in the ViewPager.
 */
public class CollectionAdapter extends FragmentStateAdapter {

    /**
     * Constructor for CollectionAdapter.
     *
     * @param fragmentManager The FragmentManager that will interact with this adapter's fragments.
     * @param lifecycle       The lifecycle of the fragments.
     */
    public CollectionAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    /**
     * Creates and returns the Fragment corresponding to the specified position.
     * @param position The position in the ViewPager.
     * @return The Fragment corresponding to the specified position.
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Create a new Bundle to hold arguments for the fragment
        Bundle args = new Bundle();
        // Create a new Fragment
        Fragment fragment = new Fragment();

        // Determine which fragment to create based on the position
        switch (position) {
            case 0:
                // Create a new TimerTab fragment for position 0
                fragment = new TimerTab();
                return fragment;
            case 1:
                // Create a new TaskTab fragment for position 1
                fragment = new TaskTab();
                return fragment;
            case 2:
                // Create a new SettingsTab fragment for position 2
                fragment = new SettingsTab();
        }
        return fragment;
    }

    /**
     * Returns the total number of items in the ViewPager.
     * @return The total number of items in the ViewPager.
     */
    @Override
    public int getItemCount() {
        // There are 3 items in the ViewPager
        return 3;
    }
}