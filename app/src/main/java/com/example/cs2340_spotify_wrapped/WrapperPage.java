package com.example.cs2340_spotify_wrapped;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cs2340_spotify_wrapped.ScreenSlidePagerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class WrapperPage extends AppCompatActivity {
    private ViewPager2 viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapper_page);
        viewPager = findViewById(R.id.viewPager);
        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Apply the page transformer
        viewPager.setPageTransformer(new ZoomOutPageTransformer());

    }


}
