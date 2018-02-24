package com.example.clark.newone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NoteActivity extends AppCompatActivity {

    private Button btnCreate, btnPlus;
    private EditText eTitle, eContent;
    private FirebaseAuth mAuth;
    private DatabaseReference fNoteDatabase;
    private Toolbar mToolbar;

    private Menu mainMenu;

    private String noteID;

    private boolean isExist;

    private LinearLayout bottomsheetlayout;
    private BottomSheetBehavior bottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        try {
            noteID = getIntent().getStringExtra("noteId");

            //Toast.makeText(this, noteID, Toast.LENGTH_SHORT).show();
            if (!noteID.trim().equals("")) {
                isExist = true;
            } else {
                isExist = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        btnCreate = (Button) findViewById(R.id.item_send_btn);
        btnPlus = (Button) findViewById(R.id.btn_plus);
        eTitle = (EditText) findViewById(R.id.new_note_title);
        eContent = (EditText) findViewById(R.id.new_note_content);

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
                bottomSheetDialog.show(getSupportFragmentManager(), bottomSheetDialog.getTag());
            }
        });


        mAuth = FirebaseAuth.getInstance();
        fNoteDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(mAuth.getCurrentUser().getUid());


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = eTitle.getText().toString().trim();
                String content = eContent.getText().toString().trim();

                if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)){
                    createNote(title, content);
                }else
                    Snackbar.make(view, "Fill empty fields", Snackbar.LENGTH_SHORT).show();
            }
        });
        putData();
    }

    private void putData() {
        if (isExist) {

            fNoteDatabase.child(noteID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("title") && dataSnapshot.hasChild("content")) {
                        String title = dataSnapshot.child("title").getValue().toString();
                        String content = dataSnapshot.child("content").getValue().toString();

                        eTitle.setText(title);
                        eContent.setText(content);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void createNote(String title, String content){

        if(mAuth.getCurrentUser() != null){

            if (isExist) {
                // UPDATE A NOTE
                Map updateMap = new HashMap();
                updateMap.put("title", eTitle.getText().toString().trim());
                updateMap.put("content", eContent.getText().toString().trim());
                updateMap.put("timestamp", ServerValue.TIMESTAMP);

                fNoteDatabase.child(noteID).updateChildren(updateMap);

                Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
            }else {
                // CREATE A NEW NOTE
                final DatabaseReference newNoteRef = fNoteDatabase.push();

                final Map noteMap = new HashMap();
                noteMap.put("title", title);
                noteMap.put("content", content);
                noteMap.put("timestamp", ServerValue.TIMESTAMP);

                Thread mainThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        newNoteRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                if (task.isSuccessful()) {
                                    Toast.makeText(NoteActivity.this, "Note added to Database", Toast.LENGTH_SHORT).show();
                                    finish();

                                } else
                                    Toast.makeText(NoteActivity.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
                mainThread.start();
            }



        }else{
            Toast.makeText(this, "USER IS NOT SIGNED IN", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.new_note_menu, menu);

        mainMenu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.new_note_delete_btn:
                if (isExist) {
                    deleteNote();
                }else {
                    Toast.makeText(this, "Nothing to Delete", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return true;
    }

    @Override
    public void finish() {
        Intent in = new Intent(this, MainActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        super.finish();
    }

    private void deleteNote() {

        fNoteDatabase.child(noteID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(NoteActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                    noteID = "no";
                    finish();
                }else {
                    Log.e("NoteActivity", task.getException().toString());
                    Toast.makeText(NoteActivity.this, "ERROR " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
