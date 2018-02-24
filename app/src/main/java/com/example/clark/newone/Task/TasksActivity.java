package com.example.clark.newone.Task;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.clark.newone.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TasksActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText eTitle;

    private RecyclerView mTaskList;
    private GridLayoutManager gridLayoutManager;

    private DatabaseReference fTaskDatabase;

    private String taskID;

    private boolean isExist;

    private Button btnSend;
    private Object key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        try {
            taskID = getIntent().getStringExtra("taskId");

            //Toast.makeText(this, noteID, Toast.LENGTH_SHORT).show();
            if (!taskID.trim().equals("")) {
                isExist = true;
            }else {
                isExist = false;
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        eTitle = (EditText) findViewById(R.id.taskEditText);

        btnSend = (Button) findViewById(R.id.buttonTaskSend);
        mTaskList = findViewById(R.id.main_task_list);

        gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL,false);

        mTaskList.setHasFixedSize(true);
        mTaskList.setLayoutManager(gridLayoutManager);




        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            fTaskDatabase = FirebaseDatabase.getInstance().getReference().child("Tasks").child(mAuth.getCurrentUser().getUid());
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = eTitle.getText().toString().trim();


                if(!TextUtils.isEmpty(title)){
                    createTask(title);
                }else
                    Snackbar.make(view, "Fill the task field", Snackbar.LENGTH_SHORT).show();
            }
        });

        putData();

        loadData();


    }

    private void putData() {
        if (isExist) {
            fTaskDatabase.child(taskID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("title")){
                        String title = dataSnapshot.child("title").getValue().toString();

                        eTitle.setText(title);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void createTask(String title) {
        if (mAuth.getCurrentUser() != null) {

                //CREATE A NEW TASK
                final DatabaseReference newTaskRef = fTaskDatabase.push();

                final Map taskMap = new HashMap();
                taskMap.put("title", title);
                taskMap.put("key", key);


                Thread mainThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        newTaskRef.setValue(taskMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(TasksActivity.this, "Task added", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                mainThread.start();

        } else {
            Toast.makeText(this, "USER IS NOT SIGNED IN", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    private void loadData() {
        Query query = fTaskDatabase.orderByChild("key");
        FirebaseRecyclerAdapter<TaskModel, TaskViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TaskModel, TaskViewHolder> (

                TaskModel.class,
                R.layout.simple_task_layout,
                TaskViewHolder.class,
                query


        ) {
            @Override
            protected void populateViewHolder(final TaskViewHolder viewHolder, TaskModel model, int position) {
                final String taskId = getRef(position).getKey();


                fTaskDatabase.child(taskId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("title")) {
                            String title = dataSnapshot.child("title").getValue().toString();

                            viewHolder.setTaskTitle(title);


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };

        mTaskList.setAdapter(firebaseRecyclerAdapter);


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}
