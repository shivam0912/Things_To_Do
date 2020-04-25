package com.example.thingsto_do;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    private EditText email,password;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        Button logInButton = findViewById(R.id.logInButton);
        Button regButton = findViewById(R.id.reg_button);

       email = findViewById(R.id.email_log);
       password = findViewById(R.id.password_log);

       auth = FirebaseAuth.getInstance();
       updateUI();

       regButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent regIntent = new Intent(LogInActivity.this,RegisterActivity.class);
               startActivity(regIntent);
               overridePendingTransition(R.anim.slide_right, R.anim.slide_left_out);
           }
       });

       logInButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String uEmail = email.getEditableText().toString().trim();
               final String uPassword = password.getText().toString();

               logIn(uEmail,uPassword);
           }
       });
    }

    private void logIn(String uEmail, final String uPassword){

        if (TextUtils.isEmpty(uEmail)) {
            Toast.makeText(getApplicationContext(), "Enter email address !", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(uPassword)) {
            Toast.makeText(getApplicationContext(), "Enter password !", Toast.LENGTH_SHORT).show();
            return;
        }


        auth.signInWithEmailAndPassword(uEmail,uPassword)
                .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    // there was an error
                    if (uPassword.length() < 6) {
                        password.setError(getString(R.string.minimum_password));
                    } else {
                        Toast.makeText(LogInActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void updateUI(){
        if (auth.getCurrentUser() != null){
            Log.i("LogIn Activity","auth != null");
            Intent uIntent = new Intent(LogInActivity.this,MainActivity.class);
            startActivity(uIntent);
            finish();
        }else {
            Log.i("Log In Activity","auth == null");
        }
    }
}
