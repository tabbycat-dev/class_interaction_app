package com.example.classinteraction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button buttonW, buttonR, btnClassCode;
    private EditText userIdEdt, longiEdt, lattEdt, userName, classCodeTv;
    private TextView statusTv;
    //retrieve an instance of database and reference location to write/read
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("checkin");
    //private final DatabaseReference checkinRef = root.getReference("checkin");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }
    private void initUI(){
        userIdEdt = findViewById(R.id.edtUserId);
        userName = findViewById(R.id.edtName);
        longiEdt = findViewById(R.id.edtLongitude);
        lattEdt = findViewById(R.id.edtLatitude);
        buttonW = findViewById(R.id.buttonWrite);
        buttonR = findViewById(R.id.buttonRead);
        btnClassCode = findViewById(R.id.btnClassCode);
        statusTv = findViewById(R.id.tvStatus);
        classCodeTv = findViewById(R.id.edtClassCode);
        btnClassCode.setOnClickListener(new View.OnClickListener() {//CHECK CLass Code
            @Override
            public void onClick(View view) {
                //TODO VERIFY CLASS GROUP
                //TODO #1 get user class code input and compare with one in db
                String classCode = classCodeTv.getText().toString();

                statusTv.setText(ref.getRef().child("class_code").toString());
            }
        });
        writeToFirebase();
    }
    public void writeToFirebase() {
        // create checkin1 or user enter checkin details on screen to perform self-checkin
        final Checkin checkin1 = new Checkin(new Date(), "10121012","Tabby", 89.00, 130.92);

        buttonW.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                //add checkin 1 into Checkin collection
                //TODO read user input below and construct checkin instance
                ref.push().setValue(checkin1);
                userIdEdt.setText("");
                userName.setText("");
                longiEdt.setText("");
                lattEdt.setText("");
                statusTv.setText("Successfully added checkin1 to Firebase!");
            }
        });

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //Checkin readCheckin = dataSnapshot.getValue(Checkin.class);
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
                //Log.i("TAG", "Database error", databaseError.toException());
            }
        });

    }
}
