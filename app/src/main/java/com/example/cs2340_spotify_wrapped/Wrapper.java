package com.example.cs2340_spotify_wrapped;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class Wrapper extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String[] urls = {
            "https://api.spotify.com/v1/me/top/artists?limit=5",
            "https://api.spotify.com/v1/me/top/tracks?limit=5"
    };
    private String[] timeFrames = {
            "&time_range=short_term",
            "&time_range=medium_term",
            "&time_range=long_term"
    };

    private Spinner timeSelectSpinner;
    private static final String[] paths = {"1 Month", "6 Months", "12 Months"};
    private static int currentTimeFrame = 0;
    private static WrapperData currWrapperData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapper);

        currWrapperData = new WrapperData();
        initButtons();
        initTimeSelect();
        initList(0);
        initList(1);
    }

    private void initButtons() {
        ImageButton profile = findViewById(R.id.profile);
        profile.setOnClickListener((v) -> {
            //TODO add redirect to profile page
        });
        Button game = findViewById(R.id.miniGame_btn);
        profile.setOnClickListener((v) -> {
            //TODO add redirect to game page
        });
        Button recommendation = findViewById(R.id.recom_btn);
        profile.setOnClickListener((v) -> {
            //TODO add redirect to recommendation page
        });
    }

    private void initTimeSelect() {
        timeSelectSpinner = (Spinner)findViewById(R.id.timeSelectSpinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(
                Wrapper.this,
                android.R.layout.simple_spinner_item,
                paths
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSelectSpinner.setAdapter(adapter);
        timeSelectSpinner.setOnItemSelectedListener(this);
        timeSelectSpinner.setSelection(currentTimeFrame);
    }
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item is selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos).
        if (pos != currentTimeFrame) {
            currentTimeFrame = pos;
            finish();
            startActivity(getIntent());
        }
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback.
    }



    private void initList (int mode) {
        if (MainActivity.mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
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
                Toast.makeText(Wrapper.this, "Failed to fetch data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jo = new JSONObject(response.body().string());
                    fillText(jo, mode);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(Wrapper.this, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fillText(JSONObject jo, int mode) {
        runOnUiThread(() -> {
            try {
                JSONArray items = jo.getJSONArray("items");
                switch (mode) {
                    case 0: // artist
                        System.out.println(jo);
                        LinearLayout artistList = findViewById(R.id.topArtist_list);
                        HashMap<String, Integer> genreList;
                        for (int i = 0; i < 3; i++) {
                            String artist = "";
                            if (i < items.length()) {
                                JSONObject item = items.getJSONObject(i);
                                System.out.println(item.getString("name"));
                                artist = item.getString("name");
                            }
                            try {
                                ((TextView) artistList.getChildAt(i)).setText(artist);
                            } catch (Exception e) {}
                        }
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject item = items.getJSONObject(i);
                            System.out.println(item.getString("name"));
                        }
                        break;
                    case 1: // songs
                        System.out.println(jo);
                        LinearLayout songList = findViewById(R.id.topSong_list);
                        for (int i = 0; i < 3; i++) {
                            String song = "";
                            if (i < items.length()) {
                                JSONObject item = items.getJSONObject(i);
                                System.out.println(item.getString("name"));
                                song = item.getString("name");
                            }
                            try {
                                ((TextView) songList.getChildAt(i)).setText(song);
                            } catch (Exception e) {}
                        }
                        break;

                    default:
                }

            } catch (JSONException e) {
                Log.d("JSON", "Failed to parse data: " + e);
                Toast.makeText(Wrapper.this, "Failed to parse data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}