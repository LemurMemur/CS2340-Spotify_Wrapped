package com.example.cs2340_spotify_wrapped;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class FirstPageFragment extends Fragment {

    public FirstPageFragment() {
        // Required empty public constructor
    }

    public static FirstPageFragment newInstance(String param) {
        FirstPageFragment fragment = new FirstPageFragment();
        Bundle args = new Bundle();
        args.putString("param", param);
        fragment.setArguments(args);
        return fragment;
    }
    private ConstraintLayout firstFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_page, container, false);
        firstFragment = view.findViewById(R.id.firstFragment); // Make sure this ID exists in your layout
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = view.findViewById(R.id.textView);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        textView.startAnimation(fadeInAnimation);

        AnimationDrawable animationDrawable = (AnimationDrawable) firstFragment.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();
    }


}
