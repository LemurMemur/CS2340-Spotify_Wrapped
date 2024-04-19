package com.example.cs2340_spotify_wrapped;

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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class TimeFrameSelection extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner timeSelectSpinner;
    private static final String[] paths = {"1 Month", "6 Months", "12 Months"};
    ConstraintLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_frame_selection);
        initButtons();
        initTimeSelect();
        relativeLayout = findViewById(R.id.timeFrame);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();
    }

    void initButtons() {
        Button generateButton = findViewById(R.id.generateButton);
        generateButton.setOnClickListener((v) -> {
            WrapperLoader.currWrapperData = null;
            Intent intent = new Intent(getApplicationContext(), WrapperLoader.class);
            startActivity(intent);
            finish();
        });
    }

    private void initTimeSelect() {
        timeSelectSpinner = (Spinner) findViewById(R.id.timeSelectSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                TimeFrameSelection.this,
                android.R.layout.simple_spinner_item,
                paths
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSelectSpinner.setAdapter(adapter);
        timeSelectSpinner.setOnItemSelectedListener(this);
        timeSelectSpinner.setSelection(WrapperLoader.currentTimeFrame);
        timeSelectSpinner.setVisibility(View.VISIBLE);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (pos != WrapperLoader.currentTimeFrame) {
            WrapperLoader.currentTimeFrame = pos;
        }
        if (parent != null && parent.getChildAt(0) != null) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
