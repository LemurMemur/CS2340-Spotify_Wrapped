package com.example.cs2340_spotify_wrapped;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class SecondPageFragment extends Fragment {

    private ConstraintLayout secondFragment;
    private LinearLayout artistList;
    private int currentArtistIndex = 0;

    public SecondPageFragment() {
        // Required empty public constructor
    }

    public static SecondPageFragment newInstance(String param) {
        SecondPageFragment fragment = new SecondPageFragment();
        Bundle args = new Bundle();
        args.putString("param", param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second_page, container, false);
        secondFragment = view.findViewById(R.id.secondFragment);
        artistList = view.findViewById(R.id.topArtist_list);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = view.findViewById(R.id.textView);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        textView.startAnimation(fadeInAnimation);
        Log.d("AnimationDebug", "Fade-in animation started for title.");

        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                startArtistAnimations();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        AnimationDrawable animationDrawable = (AnimationDrawable) secondFragment.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();



    }

    private void startArtistAnimations() {
        if (currentArtistIndex < artistList.getChildCount()) {
            View child = artistList.getChildAt(currentArtistIndex);
            child.setVisibility(View.VISIBLE); // Set visibility to VISIBLE right before animation starts
            Animation fadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            fadeInAnimation.setStartOffset(300 * currentArtistIndex); // Delay subsequent animation
            child.startAnimation(fadeInAnimation);
            fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    currentArtistIndex++;
                    startArtistAnimations(); // Recursively animate the next child
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        }
    }

}
