package com.example.classinteraction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classinteraction.utils.ClassCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardActivity extends AppCompatActivity  {
    private Button  btnDiscussion, signOutBtn, btnGGMap, btnLogin, btnRegister;
    private TextView tvName;

    @BindView(R.id.etClassName)
    TextView etClassName ;

    @BindView(R.id.etClassCode)
    TextView etClassCode ;

    @BindView(R.id.etActive)
    TextView etActive ;

    private FirebaseAuth mAuth ;
    private boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        initUI();

    }
    /* Get Intent Parcel from First Activity and call updateUI method
     * @param: parcelName is name of Intent
     */
    private void getParcelFromFirstActivity(String parcelName){
        ArrayList<ClassCode> list = getIntent().getParcelableArrayListExtra(parcelName);
        ClassCode classcode = list.get(0);
        updateUI(classcode);
    }
    private void updateUI(ClassCode classcode){
        etClassCode.setText(classcode.getClass_code());
        etClassName.setText(classcode.getName());
        if (classcode.isActive()){
            etActive.setText("active");
            status = true;
        }else{
            etActive.setText("closed");
        }
    }

    /*checkin activity */
    @OnClick(R.id.btnGGMap) void checkinActivity() {
        if (!status){
            updateStatus("Sorry! Check-in was closed.");
        }else{
            Intent i = new Intent (getApplicationContext(), MapsActivity2.class);
            startActivity(i);
        }
    }

    /*Discussion activity*/
    @OnClick(R.id.btnDiscussion) void discussionActivity() {
        if (!status){
            updateStatus("Sorry! Discussion was closed.");
        }else {
            Intent i = new Intent(getApplicationContext(), DiscussionActivity.class);
            startActivity(i);
        }
    }

        private void initUI() {
        if (getIntent().hasExtra("CC_01")) {
            getParcelFromFirstActivity("CC_01");
        }

        mAuth =FirebaseAuth.getInstance();
        tvName = findViewById(R.id.tvName);
        accessUserInfo();

        signOutBtn = findViewById(R.id.btnSignout);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUserOut();
            }
        });
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (getApplicationContext(), RegisterActivity.class);
                startActivity(i);

            }
        });
    }
    private void accessUserInfo(){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            updateStatus("Hello "+email);
            tvName.setText("Hello "+ email);
        }
    }
    private void signUserOut() {
        // TODO: sign the user out
        mAuth.signOut();
        updateStatus("signUserOut: successful");
        Intent i = new Intent (getApplicationContext(), MainActivity.class);
        startActivity(i);


    }
    private void updateStatus(String text){
        Toast.makeText(this ,text, Toast.LENGTH_SHORT).show();
    }

}
