package com.example.cs2340_spotify_wrapped;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class ThirdPageFragment extends Fragment {

    private ConstraintLayout thirdFragment;

    public ThirdPageFragment() {
        // Required empty public constructor
    }

    public static ThirdPageFragment newInstance(String param) {
        ThirdPageFragment fragment = new ThirdPageFragment();
        Bundle args = new Bundle();
        args.putString("param", param);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third_page, container, false);
        thirdFragment = view.findViewById(R.id.thirdFragment);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnimationDrawable animationDrawable = (AnimationDrawable) thirdFragment.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        getActivity().runOnUiThread(() -> {
            initTracks();
            initPicture();
        });

    }
    private void initTracks() {
        try {
            JSONArray items = WrapperLoader.currWrapperData.tracks.getJSONArray("items");

            LinearLayout trackList = getView().findViewById(R.id.topSong_list);
            for (int i = 0; i < 3; i++) {
                String track = "";
                if (i < items.length()) {
                    JSONObject item = items.getJSONObject(i);
                    track = item.getString("name");
                    System.out.println(item.getString("name"));
                }
                try {
                    ((TextView) trackList.getChildAt(i)).setText(track);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {};
    }



    private void initPicture() {
        try {
            JSONArray array = WrapperLoader.currWrapperData.tracks.getJSONArray("items");
            JSONArray ja = WrapperLoader.currWrapperData.tracks.getJSONArray("items").getJSONObject(0).getJSONObject("album").getJSONArray("images");
            if (ja.length() > 0) {
                String url = ja.getJSONObject(0).getString("url");
                System.out.println(url);
                ImageView albumCoverIV = getView().findViewById(R.id.albumCoverImageView);
                Picasso.get().load(url).resize(400, 400).into(albumCoverIV);
            }
        } catch (Exception e) {

        }
    }



}
