package com.example.thingsto_do;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Button logOutButton = findViewById(R.id.log_out_button);

        auth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UserProfile").child(Objects.requireNonNull(auth.getCurrentUser()).getUid());

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               auth.signOut();
                Toast.makeText(UserProfileActivity.this,"SignOut",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserProfileActivity.this,LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
