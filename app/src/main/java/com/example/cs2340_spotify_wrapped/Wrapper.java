package com.example.cs2340_spotify_wrapped;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
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
    private SpotifyFirebaseManager spotifyFirebaseManager;

    private Spinner timeSelectSpinner;
    private static final String[] paths = {"1 Month", "6 Months", "12 Months"};
    private static int currentTimeFrame = 1;
    public static WrapperData currWrapperData = null;
    public static boolean gotArtists = false, gotTracks = false;
    public static boolean slideshowRedirect = false;

    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapper);
        initButtons();
        spotifyFirebaseManager = new SpotifyFirebaseManager();
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("messsage");
//        myRef.setValue("Hello, World");

        if (currWrapperData == null) { // if regular opening
            currWrapperData = new WrapperData();
            initTimeSelect();
            initList(0);
            initList(1); // redirect to wrapper page in here if redirect is true
        } else {
            //TODO change time frame text to date of original
            //TODO hide dropdown
            Spinner dd = findViewById(R.id.timeSelectSpinner);
            dd.setVisibility(View.INVISIBLE);
            fillText(currWrapperData.artists, 0);
            fillText(currWrapperData.tracks, 1);
        }

        relativeLayout = findViewById(R.id.relativelayout);
        Button save_wrapper = findViewById(R.id.save_wrapper);
        save_wrapper.setOnClickListener(v->{
            saveImage();
        });

        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

    }

    private void saveImage() {

        relativeLayout.setDrawingCacheEnabled(true);
        relativeLayout.buildDrawingCache();
        relativeLayout.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = relativeLayout.getDrawingCache();
        
        save(bitmap);

    }

    private void save(Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(root+"/Download");
        String fileName = "saved_wrapper.jpg";
        File myFile = new File(file, fileName);

        if(myFile.exists()){
            myFile.delete();
        }

        try{
            FileOutputStream fileOutputStream = new FileOutputStream(myFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            relativeLayout.setDrawingCacheEnabled(false);


        } catch (Exception e){
            Toast.makeText(this, "Error : "+e.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    private void initButtons() {
        ImageButton history = findViewById(R.id.history);
        history.setOnClickListener((v) -> {
            Intent intent = new Intent(getApplicationContext(), History.class);
            startActivity(intent);
        });
        ImageButton profile = findViewById(R.id.profile);
        profile.setOnClickListener((v) -> {
            //TODO add redirect to profile page
        });
        ImageButton settings = findViewById(R.id.setting);
        settings.setOnClickListener((v) -> {
        });
        Button game = findViewById(R.id.miniGame_btn);
        game.setOnClickListener((v) -> {
            //TODO add redirect to game page
        });
        Button recommendation = findViewById(R.id.recom_btn);
        recommendation.setOnClickListener((v) -> {
            //TODO add redirect to recommendation page
        });
        Button saveToDatabase = findViewById(R.id.save_wrapper_database);
        saveToDatabase.setOnClickListener((v) -> {
            spotifyFirebaseManager.addWrapperData(currWrapperData);
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
        timeSelectSpinner.setVisibility(View.VISIBLE);
    }
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item is selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos).
        if (pos != currentTimeFrame) {
            currentTimeFrame = pos;
            currWrapperData = null;
            finish();
            startActivity(getIntent());
        }
        if (parent != null && parent.getChildAt(0) != null) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        }
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
                        gotArtists = true;
                        currWrapperData.artists = jo;
                        if (slideshowRedirect) break;
                        HashMap<String, Integer> genreList = new HashMap<>();
                        String topGenre = "None";
                        int topGenreCount = 0;
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject item = items.getJSONObject(i);
                            JSONArray genres = item.getJSONArray("genres");
                            for (int g = 0; g < genres.length(); ++g) {
                                String genre = genres.getString(g);
                                System.out.println(item.getString("name"));
                                genreList.put(genre, genreList.getOrDefault(genre, 0) + 1);

                                if (genreList.getOrDefault(genre, -1) > topGenreCount) {
                                    topGenreCount = genreList.getOrDefault(genre, -1);
                                    topGenre = genre;
                                }
                            }
                        }
                        TextView genreText = findViewById(R.id.topGenre);
                        genreText.setText(topGenre);

                        LinearLayout artistList = findViewById(R.id.topArtist_list);
                        JSONObject artists = new JSONObject();
                        for (int i = 0; i < 3; i++) {
                            String artist = "";
                            if (i < items.length()) {
                                JSONObject item = items.getJSONObject(i);
                                System.out.println(item.getString("name"));
                                artist = item.getString("name");
                                artists = items.getJSONObject(i);
                                artists.put("artist", "The artist");
                            }
                            try {
                                ((TextView) artistList.getChildAt(i)).setText(artist);
                            } catch (Exception e) {}
                        }
                        break;
                    case 1: // songs
                        gotTracks = true;
                        currWrapperData.tracks = jo;
                        if (slideshowRedirect) break;
                        JSONObject tracks = new JSONObject();
                        LinearLayout songList = findViewById(R.id.topSong_list);
                        for (int i = 0; i < 3; i++) {
                            String song = "";
                            if (i < items.length()) {
                                JSONObject item = items.getJSONObject(i);
                                System.out.println(item.getString("name"));
                                song = item.getString("name");
                                tracks = items.getJSONObject(i);
                                tracks.put("title", "Track title");
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
            if (gotArtists && gotTracks && slideshowRedirect) {
                slideshowRedirect = false;
                Intent intent = new Intent(getApplicationContext(), WrapperPage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}