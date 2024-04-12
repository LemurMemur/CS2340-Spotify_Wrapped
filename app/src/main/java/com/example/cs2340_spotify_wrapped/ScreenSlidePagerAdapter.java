package com.example.cs2340_spotify_wrapped;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 4; // Increase as needed

    public ScreenSlidePagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FirstPageFragment();
            case 1:
                return new SecondPageFragment();
            case 2:
                return new ThirdPageFragment();
            case 3:
                return new FourthPageFragment();
            default:
                return new FirstPageFragment(); // Default to existing page or handle uniquely
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
