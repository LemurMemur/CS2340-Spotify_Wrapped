package com.example.cs2340_spotify_wrapped;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class WrapperLoader extends AppCompatActivity {
    private String[] urls = {
            "https://api.spotify.com/v1/me/top/artists?limit=5",
            "https://api.spotify.com/v1/me/top/tracks?limit=5"
    };
    private String[] timeFrames = {
            "&time_range=short_term",
            "&time_range=medium_term",
            "&time_range=long_term"
    };
    public static int currentTimeFrame = 1;
    public static WrapperData currWrapperData = null;
    public static boolean viewingHistory = false;
    public static boolean gotArtists = false, gotTracks = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapper_loader);
        if (currWrapperData == null) {
            viewingHistory = false;
            currWrapperData = new WrapperData();
            retrieveData(0);
            retrieveData(1); // will auto call goToSlideshow on the async call
        } else {
            viewingHistory = true;
            goToSlideshow();
        }
    }

    private void retrieveData (int mode) {
        if (MainActivity.mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            goToMain();
        }

        // Create a request to get the user profile
        final Request request = new Request.Builder()
                .url(urls[mode] + timeFrames[currentTimeFrame])
                .addHeader("Authorization", "Bearer " + MainActivity.mAccessToken)
                .build();
        Call mCall = MainActivity.mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jo = new JSONObject(response.body().string());
                    loadData(jo, mode);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                }
            }
        });
    }

    private void loadData(JSONObject jo, int mode) {
        try {
            JSONArray items = jo.getJSONArray("items");
            switch (mode) {
                case 0: // artist
                    gotArtists = true;
                    currWrapperData.artists = jo;
                    break;
                case 1: // songs
                    gotTracks = true;
                    currWrapperData.tracks = jo;
                    break;
                default:
            }

        } catch (JSONException e) {
            Log.d("JSON", "Failed to parse data: " + e);
        }
        if (gotArtists && gotTracks) {
            goToSlideshow();
        }
    }
    private void goToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void goToSlideshow() {
        Intent intent = new Intent(getApplicationContext(), WrapperPage.class);
        startActivity(intent);
        finish();
    }
}