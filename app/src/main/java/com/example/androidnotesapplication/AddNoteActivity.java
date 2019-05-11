package com.example.androidnotesapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class AddNoteActivity extends AppCompatActivity {

    private static final String TAG = "AddNoteActivity";
    private EditText mtitle, mdesc;
    private Button btn;
    private String title, desc;
    private FirebaseAuth mAuth;
    private DatabaseReference NotesRef;

    private ProgressDialog progressDialog;
    private Toolbar mtoolbar;

    String ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Log.d(TAG, "onCreate: Add note has ben created");

        mtoolbar=findViewById(R.id.add_note_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();
        NotesRef = FirebaseDatabase.getInstance().getReference().child("Notes").child(uid);

        mtitle = findViewById(R.id.notes_title);
        mdesc = findViewById(R.id.notes_desc);
        btn = findViewById(R.id.create_noteBtn);
        progressDialog = new ProgressDialog(this);

        getIncomingIntent();



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = mtitle.getText().toString();
                desc = mdesc.getText().toString();

                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc)) {
                    progressDialog.setTitle("Please wait! ");
                    progressDialog.setMessage("we are added you data to database");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    CreateNote(title, desc);

                } else {
                    if (TextUtils.isEmpty(title)) {
                        Toast.makeText(AddNoteActivity.this, "Title should not be empty", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(desc)) {
                        Toast.makeText(AddNoteActivity.this, "Description should not be empty", Toast.LENGTH_SHORT).show();

                    }
                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.delete:
                deleteData();
                break;
        }
        return true;
    }

    private void deleteData() {
        NotesRef.child(ref).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(AddNoteActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    sendToMAin();
                }
                else {
                    Toast.makeText(AddNoteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("ref")) {
            ref=getIntent().getStringExtra("ref");
        }
    }

    private void CreateNote(String title, String desc) {

        if (mAuth.getCurrentUser() != null) {
            DatabaseReference newNoteRef = NotesRef.push();
            Map noteMap = new HashMap();
            noteMap.put("title", title);
            noteMap.put("description", desc);
            noteMap.put("time", ServerValue.TIMESTAMP);

            newNoteRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(AddNoteActivity.this, "New Note has been added to the Firebase Storage", Toast.LENGTH_SHORT).show();
                        sendToMAin();
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(AddNoteActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    private void sendToMAin() {

        Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
