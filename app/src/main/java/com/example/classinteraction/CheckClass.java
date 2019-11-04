package com.example.classinteraction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.classinteraction.utils.ClassCode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckClass extends AppCompatActivity {

    @BindView(R.id.classCodeET)
    EditText classcodeET;

    //private DatabaseReference ref ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_class);
        ButterKnife.bind(this);

    }
    /*
     * check class code in firebase */
    @OnClick(R.id.classCodeButton) void checkClassCode(){
        Log.d("CHECKCLASS.java", "0 LOG: Button is click");
        //query class code in database
        // if exists->return true class name and datetime
        String classCode = classcodeET.getText().toString();

        Query query = FirebaseDatabase.getInstance().getReference("class")
                .orderByChild("class_code")
                .equalTo(classCode);

        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("CHECKCLASS.java", " 1 Query run and on datachanged");
                if (dataSnapshot.exists()){
                    Log.d("CHECKCLASS.java", " 2 Query run and exists");
                    for (DataSnapshot classnumber : dataSnapshot.getChildren()){
                        ClassCode newCodeClass = classnumber.getValue(ClassCode.class);
                        updateToast("found class "+newCodeClass.getClass_code());

                        //send bundle of class info to MainBoard?
                        Intent i = new Intent (getApplicationContext(), DashboardActivity.class);
                        startActivity(i);
                    }
                    //updateToast("found class");
                }else{
                    updateToast(classCode+" not exists!");
                    //ref.push();
                    Log.d("CHECKCLASS.java", " 3 Query run and not exists");

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("CHECKCLASS.java", "4 Query run and on canceled");
            }
        });
        //if not exist->false


    }

    private void updateToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}
