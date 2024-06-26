package com.example.cs2340_spotify_wrapped;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpotifyFirebaseManager {
    private static final String TAG = "FirebaseDatabase";
    private static DatabaseReference mDatabase;

    private static String UserID = "";

    public SpotifyFirebaseManager() {
        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static String GetUserID() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference test = mDatabase.child("users").child(UserID);
        return UserID;
    }

    public void addWrapperData(WrapperData wd) {
        addArtists(wd.artists);
        addTracks(wd.tracks);
    }

    public void addArtists(JSONObject artists) {
        // Assuming 'artist' is a well-structured JSONObject containing relevant data
        // Generate a unique key for each new artist
        String artistId = mDatabase.child("users").child(UserID).child("artists").push().getKey();
        if (artistId != null) {
            // Set the artist JSONObject as a child of 'artists' node using the unique key
            mDatabase.child("users").child(UserID).child("artists").child(artistId).setValue(artists.toString());
        }
    }

    public void addTracks(JSONObject tracks) {
        // Similar to the addArtist method, generate a unique key for each new track
        String trackId = mDatabase.child("users").child(UserID).child("tracks").push().getKey();
        if (trackId != null) {
            // Set the track JSONObject as a child of 'tracks' node using the unique key
            mDatabase.child("users").child(UserID).child("tracks").child(trackId).setValue(tracks.toString());
        }
    }
    public void addDates(LocalDate start, LocalDate end) {
        // Similar to the addArtist method, generate a unique key for each new track
        String value = start.toString() + " to " + end.toString();
        String stringId = mDatabase.child("users").child(UserID).child("dates").push().getKey();
        if (stringId != null) {
            // Set the track JSONObject as a child of 'tracks' node using the unique key
            mDatabase.child("users").child(UserID).child("dates").child(stringId).setValue(value);
        }
    }



    /**public static void addData(JSONObject artist, JSONObject track) {
        Date today = new Date();
        WrapperData wrapperData = new WrapperData(artist, track, today);
        mDatabase.child("artist&track").push().setValue(wrapperData);
    }
     */
}
