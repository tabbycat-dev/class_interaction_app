package com.example.classinteraction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.classinteraction.utils.Checkin;
import com.example.classinteraction.utils.ClassCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardActivity extends AppCompatActivity  {
    private Button  btnDiscussion, signOutBtn, btnGGMap, btnLogin, btnRegister;
    @BindView(R.id.tvName) TextView tvName;
    private DatabaseReference ref ;

    @BindView(R.id.etClassName)
    TextView etClassName ;

    @BindView(R.id.etClassCode)
    TextView etClassCode ;

    @BindView(R.id.etActive)
    TextView etActive ;

    private FirebaseAuth mAuth ;
    private boolean status = false;
    private String user_name, user_id, class_code;
    private final String ID_KEY = "user_id";
    private final String NAME_KEY = "user_name";
    private final String CLASS_KEY = "class_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        initUI();

    }
    private void readLiveClass(){
        Query query = FirebaseDatabase.getInstance().getReference("class")
                .orderByChild("class_code")
                .equalTo(class_code);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("FIREBASE","- child is added");

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("FIREBASE","- child is changed");

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d("FIREBASE","- child is remvoed");
                updateClassClose();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("FIREBASE","- child is moved");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("FIREBASE","- child is cancel");

            }

    });


    }

    private void updateClassClose(){
        status = false;
        etActive.setText("closed");
    }

        private void updateUI(ClassCode classcode){
        etClassCode.setText(classcode.getClass_code());
        etClassName.setText(classcode.getName());
        class_code = classcode.getClass_code();
        status = true;
        etActive.setText("active");
    }

    /*
    checkin activity
    */
    @OnClick(R.id.btnGGMap) void checkinActivity() {
        if (!status){
            updateStatus("Sorry! Check-in was closed.");
        }else{
            Intent i = new Intent (getApplicationContext(), CheckinActivity.class);
            i.putExtra(ID_KEY, user_id);
            i.putExtra(NAME_KEY, user_name);
            i.putExtra(CLASS_KEY, class_code);
            startActivity(i);
        }
    }

    /*Discussion activity*/
    @OnClick(R.id.btnDiscussion) void discussionActivity() {
        if (!status){
            updateStatus("Sorry! Discussion was closed.");
        }else {
            Intent i = new Intent (getApplicationContext(), DiscussionActivity.class);
            i.putExtra(ID_KEY, user_id);
            i.putExtra(NAME_KEY, user_name);
            i.putExtra(CLASS_KEY, class_code);
            startActivity(i);
        }
    }
    /*display username and email when activity start*/
    private void accessUserInfo(){
        mAuth =FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            user_id = user.getEmail();
            user_name = user.getDisplayName();
            updateStatus("Hello "+user_name);
            tvName.setText("Hello "+ user_name+"("+ user_id+")");

        }
    }

    @OnClick(R.id.btnSignout) void signUserOut() {
        // TODO: sign the user out
        mAuth.signOut();
        updateStatus("signUserOut: successful");
        Intent i = new Intent (getApplicationContext(), MainActivity.class);
        startActivity(i);

    }
    private void initUI() {
        //get parcel include class_code from CheckClass.java
        if (getIntent().hasExtra("CC_01")) {
            ArrayList<ClassCode> list = getIntent().getParcelableArrayListExtra("CC_01");
            ClassCode classcode = list.get(0);
            updateUI(classcode);
        }
        readLiveClass();        // display details of class user is in
        accessUserInfo();        //display username and email when activity start
        //updateStatus(userid + username + class_code); //Test
    }

    private void updateStatus(String text){
        Toast.makeText(this ,text, Toast.LENGTH_SHORT).show();
    }

}
