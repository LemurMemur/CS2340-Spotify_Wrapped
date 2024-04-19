package com.example.cs2340_spotify_wrapped;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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


public class History extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<WrapperData> history;
    WrapperData returnData;
    boolean gotArtists = false, gotTracks = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initHistory();

    }

    private void initHistory() {
        history = new ArrayList<>();
        history.add(null);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference artistsRef = database.getReference("artists");
        DatabaseReference tracksRef = database.getReference("tracks");

        tracksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 1;
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
                if (gotTracks && gotArtists) {
                    initHistorySelectionSpinner();
                    initButtons();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(History.this, "Failed",Toast.LENGTH_SHORT).show();
            }
        });

        artistsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 1;
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
                if (gotTracks && gotArtists) {
                    initHistorySelectionSpinner();
                    initButtons();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(History.this, "Failed",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initHistorySelectionSpinner() {
        Spinner historySelectSpinner = (Spinner)findViewById(R.id.historySelectSpinner);
        String[] lst = new String[history.size()];
        for (int i = 1; i < lst.length; ++i) {
            lst[i] = "" + i;
        }
        lst[0] = "None";
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                History.this,
                android.R.layout.simple_spinner_item,
                lst
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        historySelectSpinner.setAdapter(adapter);
        historySelectSpinner.setOnItemSelectedListener(this);
        historySelectSpinner.setSelection(0);
        historySelectSpinner.setVisibility(View.VISIBLE);
    }
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item is selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos).
        returnData = history.get(pos);
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
    }
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback.
    }
    private void initButtons() {
        ImageButton settings = findViewById(R.id.settingsGoBack);
        settings.setOnClickListener((v) -> {
            WrapperLoader.currWrapperData = null;
            Intent intent = new Intent(getApplicationContext(), WrapperLoader.class);
            startActivity(intent);
        });
        Button game = findViewById(R.id.timeTravelButton);
        game.setOnClickListener((v) -> {
            WrapperLoader.currWrapperData = returnData;
            Intent intent = new Intent(getApplicationContext(), WrapperLoader.class);
            startActivity(intent);
        });
    }

}