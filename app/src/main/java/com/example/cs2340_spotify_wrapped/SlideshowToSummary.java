package com.example.cs2340_spotify_wrapped;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SlideshowToSummary extends Fragment {

    public SlideshowToSummary() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SlideshowToSummary.
     */
    // TODO: Rename and change types and number of parameters
    public static SlideshowToSummary newInstance(String param1) {
        SlideshowToSummary fragment = new SlideshowToSummary();
        Bundle args = new Bundle();
        args.putString("param", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getActivity(), Wrapper.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Intent intent = new Intent(getActivity(), Wrapper.class);
        //startActivity(intent);
        return inflater.inflate(R.layout.fragment_slideshow_to_summary, container, false);
    }
}