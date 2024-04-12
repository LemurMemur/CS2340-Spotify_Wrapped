package com.example.cs2340_spotify_wrapped;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class FourthPageFragment extends Fragment {

    private ConstraintLayout fourthFragment;
    private Button wrapperSummaryButton;

    public FourthPageFragment() {
        // Required empty public constructor
    }

    public static FourthPageFragment newInstance(String param) {
        FourthPageFragment fragment = new FourthPageFragment();
        Bundle args = new Bundle();
        args.putString("param", param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fourth_page, container, false);
        fourthFragment = view.findViewById(R.id.fourthFragment);
        wrapperSummaryButton = view.findViewById(R.id.wrapper_summary);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnimationDrawable animationDrawable = (AnimationDrawable) fourthFragment.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        // Set up the button click listener to navigate to Wrapper activity
        wrapperSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Wrapper.class);
                startActivity(intent);
            }
        });
        getActivity().runOnUiThread(() -> {
            initGenre();
        });

    }
    private void initGenre() {
        try {
            JSONArray items = WrapperLoader.currWrapperData.artists.getJSONArray("items");
            HashMap<String, Integer> genreList = new HashMap<>();
            String topGenre = "None";
            int topGenreCount = 0;
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONArray genres = item.getJSONArray("genres");
                for (int g = 0; g < genres.length(); ++g) {
                    String genre = genres.getString(g);
                    System.out.println(item.getString("name"));
                    genreList.put(genre, genreList.getOrDefault(genre, 0) + 1);

                    if (genreList.getOrDefault(genre, -1) > topGenreCount) {
                        topGenreCount = genreList.getOrDefault(genre, -1);
                        topGenre = genre;
                    }
                }
            }
            TextView genreText = getView().findViewById(R.id.topGenre);
            genreText.setText(topGenre);

        } catch (Exception e) {};
    }
}
