package com.example.androidnotesapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.androidnotesapplication.Adapter.NoteViewHolder;
import com.example.androidnotesapplication.Model.NoteModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// starting the main
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    private Toolbar mtoolbar;
    private RecyclerView recyclerView;
    private DatabaseReference NotesRef;

    FirebaseRecyclerOptions<NoteModel> options;
    FirebaseRecyclerAdapter<NoteModel, NoteViewHolder> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        Log.d(TAG, "onCreate: App has started and it running");

        mAuth = FirebaseAuth.getInstance();

        if (mAuth != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            String uid = user.getUid();
            NotesRef = FirebaseDatabase.getInstance().getReference().child("Notes").child(uid);
        }


        mtoolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Notes App");

        options = new FirebaseRecyclerOptions.Builder<NoteModel>()
                .setQuery(NotesRef, NoteModel.class)
                .build();

        loadRecyclerview();


    }

    private void loadRecyclerview() {

        adapter = new FirebaseRecyclerAdapter<NoteModel, NoteViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull NoteModel model) {

                final String ref = getRef(position).getKey();

                holder.setTitle(model.getTitle());
                Log.d(TAG, "onBindViewHolder: title" + model.getTitle());
                holder.setDesc(model.getDescription());
                Log.d(TAG, "onBindViewHolder: desc" + model.getDescription());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                        intent.putExtra("ref", ref);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_single_layout, viewGroup, false);
                return new NoteViewHolder(view);
            }
        };

        recyclerView = findViewById(R.id.recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        gridLayoutManager.setReverseLayout(true);
        gridLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter.startListening();
        //    recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_note:
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
                break;
            default:
                return false;
        }
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(MainActivity.this, StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        } else {
            if (adapter != null) {
                adapter.startListening();
            }
        }
    }

    @Override
    protected void onStop() {

        if (adapter != null) {
            adapter.stopListening();
        }
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();

        }
    }
}
