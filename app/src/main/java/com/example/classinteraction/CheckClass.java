package com.example.classinteraction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.classinteraction.utils.ClassCode;
import com.example.classinteraction.viewpager.StudentActivity;
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

public class CheckClass extends AppCompatActivity  {

    @BindView(R.id.classCodeET)
    EditText classcodeET;

    private String parcelName = "CC_01";
    private int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_class);
        ButterKnife.bind(this);

    }
    /*
     * check class code in firebase */
    @OnClick(R.id.classCodeButton) void checkClassCode(){

        //query class code in database
        // if exists->return true class name and datetime

        //String classCode = classcodeET.getText().toString();
        //TODO DELETE THIS
        String classCode = "5501";

        Query query = FirebaseDatabase.getInstance().getReference("class")
                .orderByChild("class_code")
                .equalTo(classCode);

        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    Log.d("CHECKCLASS.java", " 2 Query run and exists");
                    for (DataSnapshot classnumber : dataSnapshot.getChildren()){
                        ClassCode newCodeClass = classnumber.getValue(ClassCode.class);
                        updateToast("found class "+newCodeClass.getClass_code());

                        //send bundle of class info to MainBoard?
                        setUpIntent(newCodeClass, parcelName,requestCode);

                    }
                    //updateToast("found class");
                }else{
                    updateToast(classCode+" not exists!");


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //if not exist->false


    }
    private void setUpIntent(ClassCode classCodeObject, String parcelName, int requestedCode) {
        //TODO #1a set up intent to get a result, receiver: main nav activity
        Intent i = new Intent (getApplicationContext(), StudentActivity.class);
        //TODO #1b set up food parcel with name, date and image
        ArrayList<ClassCode> list = new ArrayList<ClassCode>();
        list.add(classCodeObject);
        i.putParcelableArrayListExtra(parcelName,list);
        startActivityForResult(i, requestedCode);
    }

    private void updateToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}
