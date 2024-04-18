package com.example.cs2340_spotify_wrapped;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class SecondPageFragment extends Fragment {

    private ConstraintLayout secondFragment;


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
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnimationDrawable animationDrawable = (AnimationDrawable) secondFragment.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();


        getActivity().runOnUiThread(() -> {
            initArtists();
            initPicture();
        });


    }
    private void initArtists() {
        try {
            JSONArray items = WrapperLoader.currWrapperData.artists.getJSONArray("items");

            LinearLayout artistList = getView().findViewById(R.id.topArtist_list);
            for (int i = 0; i < 3; i++) {
                String artist = "";
                if (i < items.length()) {
                    JSONObject item = items.getJSONObject(i);
                    artist = item.getString("name");
                    System.out.println(item.getString("name"));
                }
                try {
                    ((TextView) artistList.getChildAt(i)).setText(artist);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {};
    }

    private void initPicture() {
        try {

            JSONArray ja = WrapperLoader.currWrapperData.artists.getJSONArray("items").getJSONObject(0).getJSONArray("images");
            if (ja.length() > 0) {
                String url = ja.getJSONObject(0).getString("url");
                System.out.println(url);
                ImageView pfpIV = getView().findViewById(R.id.pfpImageView);
                Picasso.get().load(url).resize(400, 400).into(pfpIV);
            }
        } catch (Exception e) {

        }
    }
}
