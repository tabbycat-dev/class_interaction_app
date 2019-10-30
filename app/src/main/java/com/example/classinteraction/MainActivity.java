package com.example.classinteraction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classinteraction.mvp.MVPDiscussionActivity;
import com.example.classinteraction.viewmodel.MapsActivity;

public class MainActivity extends AppCompatActivity  {
    private Button btnLogin, btnRegister, btnCheckin, btnDiscussion, btnMVP, btnGGMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }
    private void initUI() {
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (getApplicationContext(), LoginActivity.class);
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
        btnCheckin = findViewById(R.id.btnCheckin);
        btnCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (getApplicationContext(), CheckinActivity.class);
                startActivity(i);
            }
        });
        btnDiscussion = findViewById(R.id.btnDiscussion);
        btnDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (getApplicationContext(), DiscussionActivity.class);
                startActivity(i);
            }
        });
        btnMVP = findViewById(R.id.btnMVP);
        btnMVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (getApplicationContext(), MVPDiscussionActivity.class);
                startActivity(i);
            }
        });
        btnGGMap = findViewById(R.id.BtnGgmap);
        btnGGMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent (getApplicationContext(), MapsActivity.class);
                    startActivity(i);
            }
        });
}

}
