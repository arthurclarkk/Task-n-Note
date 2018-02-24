package com.example.clark.newone;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.clark.newone.Login.LoginActivity;
import com.example.clark.newone.Task.TasksActivity;
import com.example.clark.newone.Tools.ToolsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;

    private RecyclerView mNotesList;
    private GridLayoutManager gridLayoutManager;

    private DatabaseReference fNotesDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

              DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mNotesList = findViewById(R.id.main_notes_list);

        gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);

        mNotesList.setHasFixedSize(true);
        mNotesList.setLayoutManager(gridLayoutManager);


        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(mAuth.getCurrentUser().getUid());
        }

        loadData();

    }


    @Override
    public void onStart() {
        super.onStart();


    }

    private void loadData() {
        Query query = fNotesDatabase.orderByChild("timestamp");
        FirebaseRecyclerAdapter<NoteModel, NoteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NoteModel, NoteViewHolder>(

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
                                    Intent intent = new Intent(MainActivity.this, NoteActivity.class);
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

        mNotesList.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void mTaskblanc(View view){
        Intent i = new Intent(MainActivity.this, TasksActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();

    }

    public void mNoteblanc(View view){
        Intent in = new Intent(MainActivity.this, NoteActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Toast.makeText(this, "SORRY! Settings under construction", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_logout:
                logoutUser();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        mAuth.signOut();
        if (mAuth.getCurrentUser() == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_tasks:
                Intent i = new Intent(MainActivity.this, TasksActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case R.id.nav_notes:
                Intent e = new Intent(MainActivity.this, NotesActivity.class);
                startActivity(e);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case R.id.nav_settings:
                Intent t = new Intent(MainActivity.this, ToolsActivity.class);
                startActivity(t);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));

    }


}
