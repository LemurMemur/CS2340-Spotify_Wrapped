package com.example.cs2340_spotify_wrapped;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
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

import java.net.InetSocketAddress;

public class ChangePassword extends AppCompatActivity {
    private FirebaseAuth authProfile;
    private EditText editCurrPassword, editNewPassword, editConfirmNewPassword;
    private Button authenticate, update;
    private TextView textViewAuthenticated;
    RelativeLayout changepw;

    private ImageButton goBack_btn;
    private ProgressBar progressBar;
    private String userPwdCurr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        editCurrPassword = findViewById(R.id.pw);
        editNewPassword = findViewById(R.id.new_pw);
        editConfirmNewPassword = findViewById(R.id.confirm_pw);
        authenticate = findViewById(R.id.authenticate);
        update = findViewById(R.id.update);
        progressBar = findViewById(R.id.progressBar);
        goBack_btn = findViewById(R.id.goBack_btn);
        textViewAuthenticated = findViewById(R.id.loginText);
        editNewPassword.setEnabled(false);
        editConfirmNewPassword.setEnabled(false);
        update.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser.equals("")) {
            Toast.makeText(ChangePassword.this, "Something went wrong. User details not found.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChangePassword.this, UserLogin.class);
            startActivity(intent);
            finish();
        } else {
            reauthenticate(firebaseUser);
        }

        goBack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Settings.class);
                startActivity(intent);
            }
        });

        changepw = findViewById(R.id.changepw);
        AnimationDrawable animationdrawable = (AnimationDrawable) changepw.getBackground();
        animationdrawable.setEnterFadeDuration(1000);
        animationdrawable.setExitFadeDuration(3000);
        animationdrawable.start();
    }




    private void reauthenticate(FirebaseUser firebaseUser) {
        authenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPwdCurr = editCurrPassword.getText().toString();
                if(TextUtils.isEmpty(userPwdCurr)) {
                    Toast.makeText(ChangePassword.this, "Password is needed.", Toast.LENGTH_SHORT).show();
                    editCurrPassword.setError("Please enter your current password to authenticate.");
                    editCurrPassword.requestFocus();

                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), userPwdCurr);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                editCurrPassword.setEnabled(false);
                                editNewPassword.setEnabled(true);
                                editConfirmNewPassword.setEnabled(true);
                                textViewAuthenticated.setText("You are authenticated. You can update your password now.");

                                update.setEnabled(true);
                                authenticate.setEnabled(false);
                                update.setBackgroundTintList(ContextCompat.getColorStateList(ChangePassword.this, R.color.yellowGreen));



                                Toast.makeText(ChangePassword.this, "Password has been verified. Change password now.", Toast.LENGTH_SHORT).show();
                                update.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changePwd(firebaseUser);
                                    }
                                });
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(ChangePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private void changePwd(FirebaseUser firebaseUser) {
        String userPwdNew = editNewPassword.getText().toString();
        String userPwdConfirmNew = editConfirmNewPassword.getText().toString();

        if (TextUtils.isEmpty(userPwdNew)) {
            Toast.makeText(ChangePassword.this, " New Password is needed.", Toast.LENGTH_SHORT).show();
            editNewPassword.setError("Please enter your new password to authenticate.");
            editNewPassword.requestFocus();

        } else if (TextUtils.isEmpty(userPwdConfirmNew)) {
            Toast.makeText(ChangePassword.this, " Confirm New Password is needed.", Toast.LENGTH_SHORT).show();
            editConfirmNewPassword.setError("Please confirm your new password to authenticate.");
            editConfirmNewPassword.requestFocus();
        } else if (!userPwdNew.matches(userPwdConfirmNew)) {
            Toast.makeText(ChangePassword.this, "Password did not match", Toast.LENGTH_SHORT).show();
            editConfirmNewPassword.setError("Please re enter the same password");
            editConfirmNewPassword.requestFocus();
        } else if (userPwdNew.matches(userPwdCurr)) {
            Toast.makeText(ChangePassword.this, "New password cannot be the same as the old password", Toast.LENGTH_SHORT).show();
            editNewPassword.setError("Please enter a new password.");
            editNewPassword.requestFocus();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            firebaseUser.updatePassword(userPwdNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(ChangePassword.this, "Password has been changed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePassword.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            Toast.makeText(ChangePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}