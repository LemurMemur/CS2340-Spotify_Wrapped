package com.example.cs2340_spotify_wrapped;

import org.json.JSONObject;

import java.util.Date;

public class WrapperData {
    public JSONObject artists;
    public JSONObject tracks;
    public Date date;
    public WrapperData() {
        this.artists = new JSONObject();
        this.tracks = new JSONObject();
        this.date = new Date();
    }
    public WrapperData(JSONObject artists, JSONObject tracks, Date date) {
        this.artists = artists;
        this.tracks = tracks;
        this.date = date;
    }
}
