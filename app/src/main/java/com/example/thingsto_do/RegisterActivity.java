package com.example.thingsto_do;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText userName,emailReg,passwordReg;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private DatabaseReference userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.username_reg);
        emailReg = findViewById(R.id.email_reg);
        passwordReg = findViewById(R.id.password_reg);
        Button signUp = findViewById(R.id.signUpButton);

        auth = FirebaseAuth.getInstance();
        updateUI();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName = userName.getText().toString();
                String uEmail = emailReg.getEditableText().toString().trim();
                String uPassword = passwordReg.getEditableText().toString().trim();
                registerUser(uName,uEmail,uPassword);

            }
        });
    }

    private void registerUser(final String uName , String email , String password){

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (passwordReg.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                 Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:"
                         + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                 if (task.isSuccessful()){
                    userDatabase.child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).child("basics")
                            .child("username").setValue(uName)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                          if (task.isSuccessful()){
                                              progressDialog.dismiss();
                                              Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                                              startActivity(mainIntent);
                                              overridePendingTransition(R.anim.slide_right, R.anim.slide_left_out);
                                              finish();

                                              Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                          }else{
                                              progressDialog.dismiss();
                                              Toast.makeText(RegisterActivity.this, "Authentication failed."
                                                              + Objects.requireNonNull(task.getException()).getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                          }

                                }
                            });
                 }else{

                     progressDialog.dismiss();
                     Toast.makeText(RegisterActivity.this, "Authentication failed."
                                     + Objects.requireNonNull(task.getException()).getMessage(),
                             Toast.LENGTH_SHORT).show();
                 }
            }
        });

    }

    private void updateUI(){
        if (auth.getCurrentUser() != null){
            Log.i("LogIn Activity","auth != null");
            Intent uIntent = new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(uIntent);
            finish();
        }else {
            Log.i("Log In Activity","auth == null");
        }
    }
}
