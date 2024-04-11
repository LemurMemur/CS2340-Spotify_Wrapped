package com.example.cs2340_spotify_wrapped;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.MoreObjects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<WrapperData> history;
    WrapperData returnData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initHistory();
        initHistorySelectionSpinner();
        initButtons();
        TextView[] trackTextViews = new TextView[3];
        trackTextViews[0] = findViewById(R.id.artist_display);
        trackTextViews[1] = findViewById(R.id.textView2);
        trackTextViews[2] = findViewById(R.id.textView3);

        TextView[] artists = new TextView[3];
        artists[0] = findViewById(R.id.textView4);
        artists[1] = findViewById(R.id.textView5);
        artists[2] = findViewById(R.id.textView6);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference artistsRef = database.getReference("artists");
        DatabaseReference tracksRef = database.getReference("tracks");

        tracksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                // Iterate through each child node
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (i >= 3) break; // Exit loop after 3 tracks are displayed
                    // Get the track data as a String
                    String trackData = snapshot.getValue(String.class);
                    // Convert the String data to a JSONObject
                    try {
                        JSONObject trackJSON = new JSONObject(trackData);
                        // Now you have the track data as a JSONObject, you can use it as needed
                        // For example, you can display it in a TextView or add it to a list
                        // Display the track name in the corresponding TextView
                        trackTextViews[i].setText(trackJSON.optString("name"));
                        i++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Settings.this, "Failed",Toast.LENGTH_SHORT).show();
            }
        });

        artistsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                // Iterate through each child node
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (i >= 3) break; // Exit loop after 3 tracks are displayed
                    // Get the track data as a String
                    String artistData = snapshot.getValue(String.class);
                    // Convert the String data to a JSONObject
                    try {
                        JSONObject artistJSON = new JSONObject(artistData);
                        // Now you have the track data as a JSONObject, you can use it as needed
                        // For example, you can display it in a TextView or add it to a list
                        // Display the track name in the corresponding TextView
                        artists[i].setText(artistJSON.optString("name"));
                        i++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Settings.this, "Failed",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initHistory() {
        history = new ArrayList<>();
        history.add(Wrapper.currWrapperData);
    }

    private void initHistorySelectionSpinner() {
        Spinner historySelectSpinner = (Spinner)findViewById(R.id.historySelectSpinner);
        String[] lst = new String[history.size()];
        for (int i = 0; i < lst.length; ++i) {
            lst[i] = "" + i;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                Settings.this,
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
        Button game = findViewById(R.id.timeTravelButton);
        game.setOnClickListener((v) -> {
            Wrapper.currWrapperData = returnData;
            Intent intent = new Intent(getApplicationContext(), Wrapper.class);
            startActivity(intent);
        });
    }


}