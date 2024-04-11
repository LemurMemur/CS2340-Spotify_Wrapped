package com.example.cs2340_spotify_wrapped;

import org.json.JSONObject;

import java.util.ArrayList;

public class WrapperData {
    public JSONObject artists;
    public JSONObject tracks;
    public WrapperData() {
        this.artists = new JSONObject();
        this.tracks = new JSONObject();
    }
    public WrapperData(JSONObject artists, JSONObject tracks) {
        this.artists = artists;
        this.tracks = tracks;
    }
}
