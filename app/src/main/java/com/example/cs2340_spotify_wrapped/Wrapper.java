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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class Wrapper extends AppCompatActivity {


    private SpotifyFirebaseManager spotifyFirebaseManager;

    //public static WrapperData currWrapperData = null;
    int[] numMonths = {1, 6, 12};

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

        //Spinner dd = findViewById(R.id.timeSelectSpinner);
            //dd.setVisibility(View.INVISIBLE);
        fillText(WrapperLoader.currWrapperData.artists, 0);
        fillText(WrapperLoader.currWrapperData.tracks, 1);

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
        ImageButton home = findViewById(R.id.home);
        home.setOnClickListener((v) -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            //Intent intent = new Intent(getApplicationContext(), TimeFrameSelection.class);
            startActivity(intent);
            finish();
        });

        ImageButton settings = findViewById(R.id.setting);
        settings.setOnClickListener((v) -> {
            Intent intent = new Intent(getApplicationContext(), Settings.class);
            startActivity(intent);
        });
        Button game = findViewById(R.id.miniGame_btn);
        game.setOnClickListener((v) -> {
            Intent intent = new Intent(getApplicationContext(), QuizHome.class);
            startActivity(intent);
        });
        Button recommendation = findViewById(R.id.recom_btn);
        recommendation.setOnClickListener((v) -> {
            Intent intent = new Intent(getApplicationContext(), Reccommended.class);
            startActivity(intent);
        });
        Button saveToDatabase = findViewById(R.id.save_wrapper_database);
        if (WrapperLoader.viewingHistory) {
            saveToDatabase.setVisibility(View.GONE);
        } else {
            saveToDatabase.setOnClickListener((v) -> {
                saveDataToFirebase();
            });
        }
        Button history = findViewById(R.id.history);
        history.setOnClickListener((v) -> {
            Intent intent = new Intent(getApplicationContext(), History.class);
            startActivity(intent);
        });

    }


    private void fillText(JSONObject jo, int mode) {
        runOnUiThread(() -> {
            try {
                JSONArray items = jo.getJSONArray("items");
                switch (mode) {
                    case 0: // artist
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
        });
    }
    private void saveDataToFirebase() {
        spotifyFirebaseManager.addWrapperData(WrapperLoader.currWrapperData);
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusMonths(numMonths[WrapperLoader.currentTimeFrame]);
        spotifyFirebaseManager.addDates(start, end);
        Toast.makeText(Wrapper.this, "Summary saved.\n Click view history to view past Summaries.", Toast.LENGTH_LONG).show();
    }
}