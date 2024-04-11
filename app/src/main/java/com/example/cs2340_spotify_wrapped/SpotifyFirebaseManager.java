package com.example.cs2340_spotify_wrapped;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.json.JSONObject;

public class SpotifyFirebaseManager {

    private DatabaseReference mDatabase;

    public SpotifyFirebaseManager() {
        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void addArtist(JSONObject artist) {
        // Assuming 'artist' is a well-structured JSONObject containing relevant data
        // Generate a unique key for each new artist
        String artistId = mDatabase.child("artists").push().getKey();
        if (artistId != null) {
            // Set the artist JSONObject as a child of 'artists' node using the unique key
            mDatabase.child("artists").child(artistId).setValue(artist.toString());
        }
    }

    public void addTrack(JSONObject track) {
        // Similar to the addArtist method, generate a unique key for each new track
        String trackId = mDatabase.child("tracks").push().getKey();
        if (trackId != null) {
            // Set the track JSONObject as a child of 'tracks' node using the unique key
            mDatabase.child("tracks").child(trackId).setValue(track.toString());
        }
    }
}
