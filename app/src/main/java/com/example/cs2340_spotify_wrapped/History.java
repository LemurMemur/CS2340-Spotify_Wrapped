package com.example.cs2340_spotify_wrapped;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class History extends AppCompatActivity {

    ArrayList<WrapperData> history;
    ArrayList<String> dates;
    //WrapperData returnData;
    boolean gotArtists = false, gotTracks = false, gotDates = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        gotArtists = false;
        gotTracks = false;
        gotDates = false;
        initHistory();
    }

    void initListItems() {
        ListView listView = findViewById(R.id.historyListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                WrapperLoader.currWrapperData = history.get(position);
                goToWrapperLoader();
            }
        });
    }

    private void initHistory() {
        history = new ArrayList<>();
        dates = new ArrayList<>();
        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(SpotifyFirebaseManager.GetUserID());
        DatabaseReference artistsRef = userDatabase.child("artists");
        DatabaseReference tracksRef = userDatabase.child("tracks");
        DatabaseReference datesRef = userDatabase.child("dates");

        tracksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                gotTracks = true;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String trackData = snapshot.getValue(String.class);
                    // Convert the String data to a JSONObject
                    try {
                        JSONObject trackJSON = new JSONObject(trackData);
                        if (i >= history.size()) {
                            history.add(new WrapperData());
                        }
                        history.get(i).tracks = trackJSON;
                        ++i;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                continueInit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(History.this, "Failed",Toast.LENGTH_SHORT).show();
            }
        });

        artistsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                gotArtists = true;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String artistData = snapshot.getValue(String.class);
                    // Convert the String data to a JSONObject
                    try {
                        JSONObject artistJSON = new JSONObject(artistData);
                        if (i >= history.size()) {
                            history.add(new WrapperData());
                        }
                        history.get(i).artists = artistJSON;
                        ++i;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                continueInit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(History.this, "Failed",Toast.LENGTH_SHORT).show();
            }
        });
        datesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i = 0;
                gotDates = true;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String date = snapshot.getValue(String.class);

                    if (i >= dates.size()) {
                        dates.add("");
                    }
                    dates.set(i, date);
                    ++i;

                }
                continueInit();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(History.this, "Failed",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void continueInit() {
        if (gotTracks && gotArtists && gotDates) {
            initHistoryList();
            initButtons();
            initListItems();
        }
    }


    private void initHistoryList() {
        ListView listView = (ListView) findViewById(R.id.historyListView);
        String[] lst = new String[history.size()];
        for (int i = 0; i < lst.length; ++i) {
            lst[i] = dates.get(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                History.this,
                android.R.layout.simple_list_item_1,
                lst
        );

        listView.setAdapter(adapter);
        /*
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        historySelectSpinner.setAdapter(adapter);
        historySelectSpinner.setOnItemSelectedListener(this);
        historySelectSpinner.setSelection(0);
        historySelectSpinner.setVisibility(View.VISIBLE);
         */
    }
    private void initButtons() {
        ImageButton back = findViewById(R.id.historyGoBack);
        back.setOnClickListener((v) -> {
            //WrapperLoader.currWrapperData = null;
            Intent intent = new Intent(getApplicationContext(), Wrapper.class);
            startActivity(intent);
            finish();
        });

    }

    void goToWrapperLoader() {
        Intent intent = new Intent(getApplicationContext(), WrapperLoader.class);
        startActivity(intent);
        finish();
    }


}
