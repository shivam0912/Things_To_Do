package com.example.thingsto_do;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateNotes extends AppCompatActivity {

    private EditText titleText,descriptionText;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private ConstraintLayout constraintLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);

        Button createButton = findViewById(R.id.create_button);
        titleText = findViewById(R.id.title_text);
        descriptionText = findViewById(R.id.description_text);
        constraintLayout= findViewById(R.id.constraintLayout);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Notes").child(Objects.requireNonNull(auth.getCurrentUser()).getUid());

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleText.getText().toString().trim();
                String description = descriptionText.getText().toString().trim();

                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)){

                    createNote(title,description);
                }
                else {
                    //Toast.makeText(this,"Fill the title and description",Toast.LENGTH_LONG).show();
                    Snackbar snackbar2 = Snackbar.make(constraintLayout,"Fill empty fields",Snackbar.LENGTH_LONG);
                    snackbar2.show();
                    snackbar2.setBackgroundTint(Color.BLACK);
                    View sbView = snackbar2.getView();
                    TextView textView2 = sbView.findViewById(R.id.snackbar_text);
                    textView2.setTextColor(Color.RED);
                    textView2.setTextSize(16);
                    textView2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
            }
        });

    }

    @SuppressLint("NewApi")

   public void createNote(String title,String description){

        if (auth.getCurrentUser()!=null)
        {
            final DatabaseReference newNoteRef = reference.push();
            final Map<String, Object> noteMap = new HashMap<>();

            noteMap.put("title",title);
            noteMap.put("description",description);
            noteMap.put("timesTemp", ServerValue.TIMESTAMP);

            Thread newThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    newNoteRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Snackbar snackbar = Snackbar.make(constraintLayout,"Note Created",Snackbar.LENGTH_LONG)
                                        .setAction("Show", new View.OnClickListener(){
                                            @Override
                                            public void onClick(View v){
                                                Intent i = new Intent(CreateNotes.this,MainActivity.class);
                                                startActivity(i);
                                                finish();
                                                overridePendingTransition(R.anim.slide_left,R.anim.slide_right_out );
                                            }
                                        });
                                snackbar.show();
                                snackbar.setActionTextColor(Color.WHITE);
                                snackbar.setBackgroundTint(Color.BLACK);
                                snackbar.setDuration(2000);

                                // Changing action button text color
                                View sbView = snackbar.getView();
                                TextView textView = sbView.findViewById(R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);
                                textView.setTextSize(16);

                            }else{
                                Toast.makeText(CreateNotes.this,"Something is wrong! Try again",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
            newThread.start();
        }
   }

}
