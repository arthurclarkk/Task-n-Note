package com.example.clark.newone;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class NotesActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private RecyclerView mNotesList;
    private GridLayoutManager gridLayoutManager;

    private DatabaseReference fNotesDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        mNotesList = findViewById(R.id.main_notes_list);

        gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);

        mNotesList.setHasFixedSize(true);
        mNotesList.setLayoutManager(gridLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(mAuth.getCurrentUser().getUid());
        }

        initSwipe();
        loadData();
    }


    @Override
    public void onStart() {
        super.onStart();


    }

    private void initSwipe() {


         final DatabaseReference root = FirebaseDatabase.getInstance().getReference();

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

               fNotesDatabase.removeValue();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mNotesList);
    }

            private void loadData() {
        Query query = fNotesDatabase.orderByChild("timestamp");
        final FirebaseRecyclerAdapter<NoteModel, NoteViewHolder> mAdapter = new FirebaseRecyclerAdapter<NoteModel, NoteViewHolder>(

                NoteModel.class,
                R.layout.single_note_layout,
                NoteViewHolder.class,
                query


        ) {
            @Override
            protected void populateViewHolder(final NoteViewHolder viewHolder, NoteModel model, int position) {
                final String noteId = getRef(position).getKey();

                fNotesDatabase.child(noteId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("title") && dataSnapshot.hasChild("timestamp") && dataSnapshot.hasChild("content")) {
                            String title = dataSnapshot.child("title").getValue().toString();
                            String timestamp = dataSnapshot.child("timestamp").getValue().toString();
                            String content = dataSnapshot.child("content").getValue().toString();

                            viewHolder.setNoteTitle(title);
                            //viewHolder.setNoteTime(timestamp);
                            viewHolder.setNoteContent(content);

                            GetTime getTime = new GetTime();
                            viewHolder.setNoteTime(getTime.getTime(Long.parseLong(timestamp), getApplicationContext()));

                            viewHolder.noteCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(NotesActivity.this, NoteActivity.class);
                                    intent.putExtra("noteId", noteId);
                                    startActivity(intent);
                                }
                            });


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        mNotesList.setAdapter(mAdapter);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

}
