package com.example.classinteraction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import butterknife.OnClick;


public class ViewAttendanceActivity extends AppCompatActivity {
    private ArrayList<Checkin> studentList;
    private DatabaseReference ref ;
    private String class_code;
    private final String CLASS_KEY = "class_code";


    @BindView(R.id.re_studentList)
    RecyclerView recyclerView;

    private RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
        ButterKnife.bind(this);
        initUI();

    }

    private void initUI(){
        studentList = new ArrayList<Checkin>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        setUpAdapter();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {

            class_code = extras.getString(CLASS_KEY);
            Log.d("initUI ", String.valueOf(studentList.size()));
            readAttendance();
        }
    }
    public void setUpAdapter(){
        if (studentList !=null ){
            Log.d("setUpAdapter size", String.valueOf(studentList.size()));
            mAdapter = new RecyclerViewAdapter(studentList);
            recyclerView.setAdapter(mAdapter);
        }
        else {
            recyclerView.setAdapter(null);
            Log.d("setUpAdapter: ", "empty");

        }
    }

    /*read from database / View Attendance List*/
    private void readAttendance(){
        Log.d("readAttendance", "read attendance");
        ref = FirebaseDatabase.getInstance().getReference("class").child(class_code);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Checkin checkin = dataSnapshot.getValue(Checkin.class);

                updateStatus(checkin.getName());
                Log.d("read name",checkin.getName());


                studentList.add(checkin);
                Log.d("read size ", String.valueOf(studentList.size()));
                mAdapter.notifyDataSetChanged();
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
    private void updateStatus(String text){
        Toast.makeText(this ,text, Toast.LENGTH_SHORT).show();
    }



}
