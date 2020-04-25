package com.example.thingsto_do;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.icu.util.ULocale;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private FirebaseAuth auth;
    private DatabaseReference reference,noteReference;
    private FirebaseDatabase database;
    private RecyclerView notesList;
    private GridLayoutManager manager;
    private FirebaseRecyclerOptions<NoteModel> options;
    private FirebaseRecyclerAdapter<NoteModel,NoteViewHolder> adapter;

    private TextView userName;
    private Button plusButton;
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton = findViewById(R.id.user_profile);
        userName = findViewById(R.id.userNameText);
        plusButton = findViewById(R.id.plus_button);
        notesList = findViewById(R.id.recyclerView);
        manager = new GridLayoutManager(this,2, GridLayoutManager.VERTICAL,false);


        notesList.setHasFixedSize(true);
        notesList.setLayoutManager(manager);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        if (auth.getCurrentUser() !=null) {
            noteReference = FirebaseDatabase.getInstance().getReference();
        }
        Query query = noteReference.child("Notes").child(auth.getCurrentUser().getUid());

        reference = database.getReference("Users");

        reference .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = (String) dataSnapshot.child(auth.getCurrentUser().getUid()).child("basics")
                        .child("username").getValue(String.class);
                if (username == null)
                    userName.setText("Hello, Anonymous");
                else
                    userName.setText("Hello, "+username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgIntent = new Intent(MainActivity.this,UserProfileActivity.class);
                startActivity(imgIntent);

            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent plusIntent = new Intent(MainActivity.this,CreateNotes.class);
                startActivity(plusIntent);
                overridePendingTransition(R.anim.slide_right, R.anim.slide_left_out);
            }
        });


     options = new FirebaseRecyclerOptions.Builder<NoteModel>()
             .setQuery( query, new SnapshotParser<NoteModel>() {
                 @NonNull
                 @Override
                 public NoteModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                     return new NoteModel(
                             Objects.requireNonNull(snapshot.child("title").getValue()).toString(),
                             Objects.requireNonNull(snapshot.child("timesTemp").getValue()).toString(),
                             Objects.requireNonNull(snapshot.child("description").getValue()).toString());
                 }
             })
             .build();

     adapter = new FirebaseRecyclerAdapter<NoteModel, NoteViewHolder>(options){
         @Override
         protected void onBindViewHolder(@NonNull NoteViewHolder holder, final int position, @NonNull NoteModel model) {
             holder.setNoteTitle(NoteModel.getNoteTitle());
             //GetTimeAgo getTime = new GetTimeAgo();
             holder.setTime(GetTimeAgo.getTimeAgo(Long.parseLong(NoteModel.getNoteTime()),getApplicationContext()));
             holder.setDescription(NoteModel.getDescription());
             holder.root.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                 }
             });
         }

         @NonNull
         @Override
         public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

             View view = LayoutInflater.from(parent.getContext())
                     .inflate(R.layout.card_note_layout, parent, false);

             return new NoteViewHolder(view);

         }
     };

     notesList.setAdapter(adapter);
    }// end of onCreate

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
