package com.example.cs2340_spotify_wrapped;

import org.json.JSONObject;

import java.util.ArrayList;

public class WrapperData {
    public ArrayList<JSONObject> artists;
    public ArrayList<JSONObject> tracks;
    public WrapperData() {
        this.artists = new ArrayList<>();
        this.tracks = new ArrayList<>();
    }
    public WrapperData(ArrayList<JSONObject> artists, ArrayList<JSONObject> tracks) {
        this.artists = artists;
        this.tracks = tracks;
    }
}
