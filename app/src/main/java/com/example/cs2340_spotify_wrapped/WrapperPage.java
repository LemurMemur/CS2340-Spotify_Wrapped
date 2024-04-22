package com.example.cs2340_spotify_wrapped;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        viewPager.setCurrentItem(0, true);

        //pageViewSetup();

    }
/*
    private void pageViewSetup() {
        ViewPager pageView = findViewById(R.id.viewPager);
        pageView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // This method will be invoked when the current page is scrolled,
                // but we don't need to do anything here.
            }

            @Override
            public void onPageSelected(int position) {
                // This method will be invoked when a new page becomes selected.
                if (position == YOUR_DESIRED_PAGE_INDEX) {
                    // Put your code here that you want to run when you scroll to the desired page.
                    // For example, you can show a toast message:
                    Toast.makeText(getApplicationContext(), "Scrolled to desired page!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // This method will be invoked when the scroll state changes.
            }

        });
    }
*/


}
