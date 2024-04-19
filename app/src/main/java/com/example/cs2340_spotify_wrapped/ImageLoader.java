package com.example.cs2340_spotify_wrapped;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;

public class ImageLoader {
    public static String getImageURL(int mode, int index) {

        try {
            JSONArray ja = new JSONArray();
            switch (mode) { //0 is for artist, 1 is for track;
                case 0:
                    System.out.println("Getting image for" + WrapperLoader.currWrapperData.artists.getJSONArray("items").getJSONObject(index).getString("name"));
                    ja = WrapperLoader.currWrapperData.artists.getJSONArray("items").getJSONObject(index).getJSONArray("images");
                    break;
                case 1:
                    System.out.println("Getting image for" + WrapperLoader.currWrapperData.tracks.getJSONArray("items").getJSONObject(index).getJSONObject("album").getString("name"));
                    ja = WrapperLoader.currWrapperData.tracks.getJSONArray("items").getJSONObject(index).getJSONObject("album").getJSONArray("images");
                    break;
            }

            if (ja.length() > 0) {
                String url = ja.getJSONObject(index).getString("url");
                System.out.println(url);
                return url;
                //Picasso.get().load(url).resize(400, 400).into(IV);
            }
        } catch (Exception e) {

        }
        return "";
    }
}
