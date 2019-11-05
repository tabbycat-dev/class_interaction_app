package com.example.classinteraction;

import android.content.Intent;
import android.os.Bundle;

import com.example.classinteraction.utils.ChatMessage;
import com.example.classinteraction.utils.Checkin;
import com.example.classinteraction.utils.ClassCode;
import com.example.classinteraction.utils.EditTextDialog;
import com.example.classinteraction.utils.TextDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TutorAddClass extends AppCompatActivity
        implements EditTextDialog.EditTextDialogListener,
        TextDialog.TextDialogListener {

    private DatabaseReference ref ;


    @BindView(R.id.etClassCode)
    TextView classcodeET;

    @BindView(R.id.etClassName)
    TextView etClassName;

    @BindView(R.id.toogleActive)
    ToggleButton toogleActive;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinator;


    public static final String DIALOG_TAG = "dialog_tag";
    private final String CLASS_KEY = "class_code";
    private String class_code= "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_add_class);
        ref = FirebaseDatabase.getInstance().getReference();
        CoordinatorLayout mCoordinator = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        ButterKnife.bind(this);

    }
    /*
     * write some class object to firebase
     * @see ClassCode.java
     * */
    @OnClick(R.id.addClassButton) void onClickAddClass() {
        //new instance of dialog fragment appear
        EditTextDialog dialog = EditTextDialog.newInstance("Enter new class detail");
        dialog.show(getSupportFragmentManager(), DIALOG_TAG);
    }

    private void writeToFirebase(ClassCode object)
    {
        //TODO validation before use
        //String cCode= classcodeET.getText().toString();
        //String cName= etClassName.getText().toString();
        //boolean isActive = toogleActive.isChecked();

        ref = FirebaseDatabase.getInstance().getReference("class");
        ref.push().setValue(object);
        updateStatus();
    }
    /* show status using snackbar*/

    private void updateStatus(){
        Snackbar mySnackbar =Snackbar.make(mCoordinator, R.string.class_added,Snackbar.LENGTH_LONG);
        mySnackbar.setAction(R.string.undo_string, new MyUndoListener());
        mySnackbar.show();
    }
    private void statusToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    //Call back fragment
    @Override
    public void onEditTextDialogOK(String classcode, String classname, String tag) {
        ClassCode newClass = new ClassCode(classcode, classname, true);
        writeToFirebase(newClass);
        updateUI(newClass);
    }

    public void updateUI(ClassCode object){
        if (object ==null){
            String text ="N/A";
            classcodeET.setText(text);
            etClassName.setText(text);
            toogleActive.setChecked(false);
        }else{
            classcodeET.setText(object.getClass_code());
            class_code = object.getClass_code();
            etClassName.setText(object.getName());
            toogleActive.setChecked(object.isActive());
        }

    }

    /*Undo add class*/
    public class MyUndoListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            removeItemFromFirebse();
        }
    }

    /* stop class */
    @OnClick(R.id.endClassButton) void deleteClass() {

        //use dialog to confirm
        TextDialog dialog = TextDialog.newInstance("Confirm", "Are you sure to end class? ");
        dialog.show(getSupportFragmentManager(), DIALOG_TAG);
    }
    //call back function to get user confirm to end class

    @Override
    public void onTextDialogOK(boolean agree) {
        if (agree){
            removeItemFromFirebse();
        }
    }

    private void removeItemFromFirebse(){
    // Code to undo the user's last action
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    Query applesQuery = ref.child("class").orderByChild("class_code").equalTo(classcodeET.getText().toString());

    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot classsnapshot: dataSnapshot.getChildren()) {
                classsnapshot.getRef().removeValue();
                statusToast("Successfully Deleted");
                updateUI(null);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d("TutorAddClass.java", "onCancelled", databaseError.toException());
            statusToast("Fail to Delete");

        }
    });
}

    @OnClick(R.id.viewAttendButton) void startViewAttendanceActivity(){
        if (!class_code.isEmpty()){
            Intent i = new Intent (getApplicationContext(), ViewAttendanceActivity.class);
            i.putExtra(CLASS_KEY, class_code);
            startActivity(i);
        }else statusToast("No class found");

    }



}
