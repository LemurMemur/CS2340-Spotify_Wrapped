package com.example.cs2340_spotify_wrapped;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cs2340_spotify_wrapped.databinding.ActivityUserEditBinding;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

public class UserEdit extends AppCompatActivity {

    EditText userId, password,newEmailId;
    Button valid, update;
    RelativeLayout edit;
    ImageButton goBack_btn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentuid;
    DocumentReference documentReference ;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ProgressBar progressBar;
    String oldEmail, newEmail, userPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        currentuid = user.getUid();
//        documentReference = db.collection("user").document(currentuid);

        userId = findViewById(R.id.userID);
        newEmailId = findViewById(R.id.confirm_pw);
        password = findViewById(R.id.pw);
        valid = findViewById(R.id.authenticate);
        update = findViewById(R.id.update);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        progressBar = findViewById(R.id.progressBar);
        goBack_btn = findViewById(R.id.goBack_btn);
        update.setEnabled(false);
        newEmailId.setEnabled(false);
        oldEmail = firebaseUser.getEmail();
        if(firebaseUser.equals("")) {
            Toast.makeText(UserEdit.this, "Something went wrong!", Toast.LENGTH_LONG).show();
        } else {
            reAuthenticate(firebaseUser);
        }

        goBack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });



//        showProfile(firebaseUser);


//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateProfile();
//            }
//        });

        edit = findViewById(R.id.edit);
        AnimationDrawable animationDrawable = (AnimationDrawable) edit.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

    }

    private void reAuthenticate(FirebaseUser firebaseUser) {
        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPwd = password.getText().toString();
                if(TextUtils.isEmpty(userPwd)) {
                    Toast.makeText(UserEdit.this, "Need password", Toast.LENGTH_SHORT).show();
                    password.setError("Enter Password to continue");
                    password.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    AuthCredential credential = EmailAuthProvider.getCredential(oldEmail, userPwd);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(UserEdit.this, "Password has been verified", Toast.LENGTH_LONG).show();

                                update.setEnabled(true);
                                newEmailId.setEnabled(true);
                                password.setEnabled(false);
                                valid.setEnabled(false);

                                update.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        newEmail = newEmailId.getText().toString();
                                        if(TextUtils.isEmpty(newEmail)) {
                                            Toast.makeText(UserEdit.this, "Need  new email", Toast.LENGTH_SHORT).show();
                                            newEmailId.setError("Enter new Email to continue");
                                            newEmailId.requestFocus();
                                        } else if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                                            Toast.makeText(UserEdit.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                                            newEmailId.setError("Enter valid Email to continue");
                                            newEmailId.requestFocus();
                                        } else if (newEmail.matches(oldEmail)) {
                                            Toast.makeText(UserEdit.this, "Please enter a new email", Toast.LENGTH_SHORT).show();
                                            newEmailId.setError("Enter new Email to continue");
                                            newEmailId.requestFocus();
                                        } else {
                                            progressBar.setVisibility(View.VISIBLE);
                                            updateEmail(firebaseUser);
                                        }
                                    }
                                });
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(UserEdit.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateEmail(FirebaseUser firebaseUser) {
        firebaseUser.verifyBeforeUpdateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()) {
                    firebaseUser.sendEmailVerification();
                    Toast.makeText(UserEdit.this, "email has been updated. PLease verify", Toast.LENGTH_SHORT).show();
                    Intent intent =  new Intent(UserEdit.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        Toast.makeText(UserEdit.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

//    private void showProfile(FirebaseUser firebaseUser) {
//        String userUid = firebaseUser.getUid();
//        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
//        progressBar.setVisibility(View.VISIBLE);
//        referenceProfile.child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
//                if(readUserDetails != null) {
//                    gmail = readUserDetails.email;
//                    passer = readUserDetails.password;
//                    userId.setText(gmail);
//                    password.setText(passer);
//                } else {
//                    Toast.makeText(UserEdit.this, "Something went wrong!", Toast.LENGTH_LONG).show();
//
//                }
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(UserEdit.this, "Something went wrong!", Toast.LENGTH_LONG).show();
//                progressBar.setVisibility(View.GONE);
//            }
//        });
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        documentReference.get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if(task.isSuccessful() && task.getResult().exists()) {
//                            String emailResult = task.getResult().getString("name");
//                            String passwordResult = task.getResult().getString("pass");
//                            userId.setText(emailResult);
//                            password.setText(passwordResult);
//                        } else {
//                            Toast.makeText(UserEdit.this, "No profile", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

//    private void updateProfile() {
//        String name = userId.getText().toString();
//        String pass = password.getText().toString();
//
//        // Use transaction to update Firestore document
//        db.runTransaction(new Transaction.Function<Void>() {
//            @Override
//            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//                transaction.update(documentReference, "name", name);
//                transaction.update(documentReference, "pass", pass);
//                return null;
//            }
//        }).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(UserEdit.this, "Updated", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(UserEdit.this, "Failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}