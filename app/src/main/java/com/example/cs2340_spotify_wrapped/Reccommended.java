package com.example.cs2340_spotify_wrapped;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Reccommended extends AppCompatActivity {
    ConstraintLayout main;

    private static final String TAG = "MainActivity";
    private static final String API_URL = "https://api.spotify.com/v1/recommendations?limit=10&seed_artists=4NHQUGzhtTLFvgF5SZesLK";

    private TextView recommendationsTextView;
    private TextView textview1;
    private TextView textview2;
    private ImageView pfpImageView;
    private ImageView pfpImageView1;
    private ImageView pfpImageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reccommended);

        recommendationsTextView = findViewById(R.id.recommendationsTextView);
        textview1 = findViewById(R.id.textView1);
        textview2 = findViewById(R.id.textView2);
        pfpImageView = findViewById(R.id.pfpImageView);
        pfpImageView1 = findViewById(R.id.pfpImageView1);
        pfpImageView2 = findViewById(R.id.pfpImageView2);

        new FetchRecommendationsTask().execute();
        main = findViewById(R.id.main);
        AnimationDrawable animationDrawable = (AnimationDrawable) main.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();
        ImageButton home = findViewById(R.id.home);
        home.setOnClickListener((v) -> {
            Intent intent = new Intent(getApplicationContext(), Wrapper.class);
            startActivity(intent);
        });
    }

    private class FetchRecommendationsTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Add authorization header with token
                String token = MainActivity.mAccessToken;
                connection.setRequestProperty("Authorization", "Bearer " + token);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    return response.toString();
                } else {
                    return null;
                }
            } catch (IOException e) {
                Log.e(TAG, "Error fetching recommendations", e);
                return null;
            }
        }
        private void initPicture(String imageUrl, int position) {
            new AsyncTask<String, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... strings) {
                    String url = strings[0];
                    try {
                        InputStream inputStream = new URL(url).openStream();
                        return BitmapFactory.decodeStream(inputStream);
                    } catch (IOException e) {
                        Log.e(TAG, "Error loading image from URL", e);
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    if (bitmap != null) {
                        // Set the bitmap to the respective ImageView
                        if (position == 0) {
                            pfpImageView.setImageBitmap(bitmap);
                        } else if (position == 1) {
                            pfpImageView1.setImageBitmap(bitmap);
                        } else if (position == 2) {
                            pfpImageView2.setImageBitmap(bitmap);
                        }
                    } else {
                        // Handle error loading image
                    }
                }
            }.execute(imageUrl);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray tracksArray = jsonObject.getJSONArray("tracks");

                    StringBuilder recommendations = new StringBuilder();
                    StringBuilder recommendations1 = new StringBuilder();
                    StringBuilder recommendations2 = new StringBuilder();
                    for (int i = 0; i < 3; i++) {
                        JSONObject trackObject = tracksArray.getJSONObject(i);
                        String imageUrl = trackObject.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");
                        initPicture(imageUrl, i);
                        String trackName = trackObject.getString("name");
                        String artistName = trackObject.getJSONArray("artists").getJSONObject(0).getString("name");
//                        recommendations.append(trackName).append(" by ").append(artistName).append("\n").append("\n");

                        if (i == 0) {
                            recommendations.append(trackName).append(" by ").append(artistName).append("\n");
                        }
                        if (i == 1) {
                            recommendations1.append(trackName).append(" by ").append(artistName).append("\n");
                        }
                        if (i == 2) {
                            recommendations2.append(trackName).append(" by ").append(artistName).append("\n");
                        }

                    }

                    recommendationsTextView.setText(recommendations.toString());
                    textview1.setText(recommendations1.toString());
                    textview2.setText(recommendations2.toString());
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                }
            } else {
                recommendationsTextView.setText("Error fetching recommendations");
                textview1.setText("Error fetching recommendations");
                textview2.setText("Error fetching recommendations");
            }
        }
    }
}