package com.example.classinteraction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;

public class TutorDashboard extends AppCompatActivity {
    private Button btnDiscussion, signOutBtn, btnGGMap, btnLogin, btnRegister;
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
        setContentView(R.layout.activity_tutor_dashboard);
    }
}
