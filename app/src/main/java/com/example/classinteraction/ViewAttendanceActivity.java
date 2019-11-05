package com.example.classinteraction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import com.example.classinteraction.utils.Checkin;
import com.example.classinteraction.utils.RecyclerViewAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ViewAttendanceActivity extends AppCompatActivity {
    private ArrayList<Checkin> studentList;
    private ArrayAdapter<Checkin> adapter;
    private DatabaseReference ref ;

    @BindView(R.id.re_studentList)
    RecyclerView recyclerView;

    private RecyclerViewAdapter mAdapter;
    private LayoutInflater mInflater;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
        ButterKnife.bind(this);
        initUI();
        ref = FirebaseDatabase.getInstance().getReference("checkin");
        readAttendance();
    }

    private void initUI(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        progressBar.setVisibility(View.VISIBLE);
    }
    public void setUpAdapter(){
        if (studentList !=null ){
            mAdapter = new RecyclerViewAdapter(studentList);
            recyclerView.setAdapter(mAdapter);
            progressBar.setVisibility(View.INVISIBLE);

        }
        else recyclerView.setAdapter(null);
    }

    /*read from database*/
    private void readAttendance(){
        /*View Attendance List*/
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Checkin chat = dataSnapshot.getValue(Checkin.class);
                studentList.add(chat);
                adapter.notifyDataSetChanged();
                        //statusTv.setText(readCheckin.toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }


}
