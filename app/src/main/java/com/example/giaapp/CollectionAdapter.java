package com.example.giaapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CollectionAdapter extends FragmentStateAdapter {


    public CollectionAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle args = new Bundle();
        Fragment fragment = new Fragment();

        switch (position) {
            case 0:
                fragment = new TimerTab();
                return fragment;
            case 1:
                fragment = new TaskTab();
                return fragment;
            case 2:
                fragment = new SettingsTab();
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
