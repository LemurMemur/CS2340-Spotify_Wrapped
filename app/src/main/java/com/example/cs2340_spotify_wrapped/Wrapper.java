package com.example.cs2340_spotify_wrapped;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class Wrapper extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapper);

        initButtons();
        initArtistList();

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

    private void initArtistList() {
        if (MainActivity.mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a request to get the user profile
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/artists")
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
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    fillArtistText(jsonObject);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(Wrapper.this, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fillArtistText(JSONObject jo) {
        try {
            JSONArray items = jo.getJSONArray("items");
            LinearLayout artistList = findViewById(R.id.topArtist_list);
            for (int i = 0; i < 3 && i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                int finalI = i;
                runOnUiThread(() -> ((TextView)artistList.getChildAt(finalI)).setText(item.toString()));
                System.out.println(item.toString());
            }

        } catch (JSONException e) {
            Log.d("JSON", "Failed to parse data: " + e);
            Toast.makeText(Wrapper.this, "Failed to parse data, watch Logcat for more details",
                    Toast.LENGTH_SHORT).show();
        }
    }
}