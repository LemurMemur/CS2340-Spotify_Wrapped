package com.example.cs2340_spotify_wrapped;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DeleteUserActivity extends AppCompatActivity {
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private TextView textViewAuthenticated;
    private EditText editTextUserPwd;
    RelativeLayout deleteUser;
    private ProgressBar progressBar;
    private String userPwd;
    private ImageButton goBack_btn;

    private Button buttonReAuthenticate, buttonDeleteUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        progressBar = findViewById(R.id.progressBar);
        textViewAuthenticated = findViewById(R.id.loginText);
        editTextUserPwd = findViewById(R.id.pw);
        buttonDeleteUser = findViewById(R.id.delete);
        buttonReAuthenticate = findViewById(R.id.authenticate);
        goBack_btn = findViewById(R.id.goBack_btn);

        buttonDeleteUser.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser.equals("")) {
            Toast.makeText(DeleteUserActivity.this, "Something went wrong!" + " User details are not available at the moment.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DeleteUserActivity.this, UserLogin.class);
            startActivity(intent);
            finish();
        } else {
            reAuthenticateUser(firebaseUser);
        }
        deleteUser = findViewById(R.id.deleteUser);
        AnimationDrawable animationdrawable = (AnimationDrawable) deleteUser.getBackground();
        animationdrawable.setEnterFadeDuration(1000);
        animationdrawable.setExitFadeDuration(3000);
        animationdrawable.start();


        goBack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Settings.class);
                startActivity(intent);
            }
        });

    }

    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        buttonReAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPwd = editTextUserPwd.getText().toString();
                if(TextUtils.isEmpty(userPwd)) {
                    Toast.makeText(DeleteUserActivity.this, "Password is needed.", Toast.LENGTH_SHORT).show();
                    editTextUserPwd.setError("Please enter your current password to authenticate.");
                    editTextUserPwd.requestFocus();

                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), userPwd);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                editTextUserPwd.setEnabled(false);
                                buttonDeleteUser.setEnabled(true);
                                buttonReAuthenticate.setEnabled(false);
                                textViewAuthenticated.setText("You are authenticated. You can delete your account now.");

                                buttonDeleteUser.setBackgroundTintList(ContextCompat.getColorStateList(DeleteUserActivity.this, R.color.yellowGreen));


                                Toast.makeText(DeleteUserActivity.this, "Password has been verified. You can delete user now.", Toast.LENGTH_SHORT).show();
                                buttonDeleteUser.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showAlertDialog();
                                    }
                                });
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(DeleteUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteUserActivity.this);
        builder.setTitle("Delete User and related data");
        builder.setMessage("Do you really want to delete your profile and related data? This action is irreversible.");

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUser(firebaseUser);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(DeleteUserActivity.this, UserLogin.class);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();



        alertDialog.show();
    }

    private void deleteUser(FirebaseUser firebaseUser) {
        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    authProfile.signOut();
                    Toast.makeText(DeleteUserActivity.this, "User has been deleted!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DeleteUserActivity.this, UserLogin.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        Toast.makeText(DeleteUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}