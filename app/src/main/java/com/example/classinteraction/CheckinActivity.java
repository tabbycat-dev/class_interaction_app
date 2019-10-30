package com.example.classinteraction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classinteraction.utils.Checkin;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckinActivity extends AppCompatActivity {

    //private Button buttonW, buttonR, btnClassCode;

    private EditText userIdEdt, longiEdt, lattEdt, userName;
    private String id, name;
    private Double lat, longi;
    private Checkin checkin;
    @BindView(R.id.tvStatus)
    TextView statusTv;

    @BindView(R.id.edtClassCode)
    EditText classCodeTv;
    //retrieve an instance of database and reference location to write/read
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    //private final DatabaseReference checkinRef = root.getReference("checkin");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        initUI();
        ButterKnife.bind(this);

    }

    private void initUI(){
        userIdEdt = findViewById(R.id.edtUserId);
        //userName = findViewById(R.id.edtName);
        longiEdt = findViewById(R.id.edtLongitude);
        lattEdt = findViewById(R.id.edtLatitude);
        //buttonW = findViewById(R.id.buttonWrite);
        //buttonR = findViewById(R.id.buttonRead);
        //btnClassCode = findViewById(R.id.btnClassCode);
        //statusTv = findViewById(R.id.tvStatus);
        //classCodeTv = findViewById(R.id.edtClassCode);

    }
    @OnClick(R.id.buttonRead) void buttonReadClicked() {

    }
    @OnClick(R.id.btnClassCode) void verifyClassCode(Button button){
        //TODO VERIFY CLASS GROUP
        //TODO #1 get user class code input and compare with one in db
        //final String classCode = classCodeTv.getText().toString();
        final String stuEmail ="tan@gmail.com";
        final String classCode = "1206";
        final String longtitudeVal = "130.92";
        Query query = ref.child("class").child(classCode);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    updateStatus(classCode+" Child Class exists!Ready to checkin");
                    for (DataSnapshot classnumber : dataSnapshot.getChildren()){
                        String value = classnumber.getValue().toString();
                        //statusTv.append("0 " + value);
                        if(stuEmail.equals(value)) {
                            statusTv.append("1 " + value);
                            //TODO allow checkin
                        }
                        if(longtitudeVal.equals(value)){
                            statusTv.append("2 " + value);

                        }
                    }

                    //findAddress(classCode, "longitude",longtitudeVal );
                }
                else{
                    updateStatus(classCode+"Child Class NOT exists!");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                updateStatus("not exist?");

            }
        });

    }
    private void findAddress (String child,String orderbyChild, String value){
        Query query = ref.child("class").child(child);

        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot classnumber : dataSnapshot.getChildren()){
                        if(classnumber.equals(value)){
                            statusTv.append("2 "+value);
                        }
                    }
                }else{
                    //ref.push();
                    statusTv.append("You already checkin");
                    statusTv.append(dataSnapshot.getChildren().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void findAddress2(String child,String orderbyChild, String value ) {
        //TODO CHECK EMAIL EXIST?

        ref.child(child)
                .orderByChild(orderbyChild)
                .equalTo(value)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            statusTv.append("2"+dataSnapshot.toString());
                        }
                        else{
                            statusTv.append("2" +"not found");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        statusTv.append("Not exist");

                    }
                });
    }

    @OnClick(R.id.buttonWrite) void writeToFirebase() {
        //TODO user registration using email and password
        // create checkin1 or user enter checkin details on screen to perform self-checkin
        //add checkin 1 into Checkin collection
        //TODO read user input below and construct checkin instance
        ref.push().setValue(checkin);
        userIdEdt.setText("");
        userName.setText("");
        longiEdt.setText("");
        lattEdt.setText("");
        updateStatus("Successfully added checkin1 to Firebase!");


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
    private void updateStatus(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

    }
    private void getInput(){
        //TODO get input
        //TODO VALIDATE INPUT
        if (validation()) {
            id = userIdEdt.getText().toString();
            name = userName.getText().toString();
            lat = Double.valueOf(lattEdt.getText().toString());
            longi = Double.valueOf(longiEdt.getText().toString());
            if (id.isEmpty() || lat.isInfinite() || longi.isInfinite()) {
                updateStatus("All fields are required!");
            }
            //checkin = new Checkin(new Date(), id,name, lat, longi);
            checkin = new Checkin(new Date(), "10121012", "Tabby", 89.00, 130.92);
        }
    }
    private boolean validation(){
        if (id.isEmpty()||lat.isInfinite()||longi.isInfinite()){
            updateStatus("All fields are required!");
            return false;
        }else return true;

    }
}
