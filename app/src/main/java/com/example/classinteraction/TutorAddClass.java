package com.example.classinteraction;

import android.content.Intent;
import android.os.Bundle;

import com.example.classinteraction.utils.ChatMessage;
import com.example.classinteraction.utils.Checkin;
import com.example.classinteraction.utils.ClassCode;
import com.example.classinteraction.utils.EditTextDialog;
import com.example.classinteraction.utils.TextDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    @BindView(R.id.etClassName)
    TextView etClassName ;

    @BindView(R.id.etClassCode)
    TextView classcodeET;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.toogleActive)
    ToggleButton toogleActive;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinator;

    private FirebaseAuth mAuth ;



    public static final String DIALOG_TAG = "dialog_tag";
    private String class_code= "";
    private final String ID_KEY = "user_id";
    private final String NAME_KEY = "user_name";
    private final String CLASS_KEY = "class_code";
    private String user_id, user_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_add_class);
        ref = FirebaseDatabase.getInstance().getReference();
        CoordinatorLayout mCoordinator = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        ButterKnife.bind(this);
        accessUserInfo();
    }

    /*display username and email when activity start*/
    private void accessUserInfo(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            user_id = user.getEmail();
            user_name = user.getDisplayName();
            tvName.setText("Hello "+ user_name+"("+ user_id+")");

        }
    }

    /* ADD CLASS button */
    @OnClick(R.id.addClassButton) void onClickAddClass() {
        //new instance of dialog fragment appear
        EditTextDialog dialog = EditTextDialog.newInstance("Enter new class detail");
        dialog.show(getSupportFragmentManager(), DIALOG_TAG);
    }

    /* show status using SNACK BAR */
    private void updateSnackbar(){
        Snackbar mySnackbar =Snackbar.make(mCoordinator, R.string.class_added,Snackbar.LENGTH_LONG);
        mySnackbar.setAction(R.string.undo_string, new MyUndoListener());
        mySnackbar.show();
    }

    /* show status using TOAST */
    private void updateToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    /*  CALL BACK fragment to get CLASS info input by tutor
    *   Perform add class to FIREBASE */
    @Override
    public void onEditTextDialogOK(String classcode, String classname, String tag) {
        ClassCode newClass = new ClassCode(classcode, classname, true);
        writeToFirebase(newClass);
        updateUI(newClass);
    }

    /* Open new class and add to firebase*/
    private void writeToFirebase(ClassCode object) {
        ref = FirebaseDatabase.getInstance().getReference("class");
        ref.push().setValue(object);
        updateSnackbar();
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

    /*UNDO add class*/
    public class MyUndoListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            removeItemFromFirebse();
        }
    }

    /* END CLASS button */
    @OnClick(R.id.endClassButton) void deleteClass() {
        //use dialog to confirm
        TextDialog dialog = TextDialog.newInstance("Confirm", "Are you sure to end class? ");
        dialog.show(getSupportFragmentManager(), DIALOG_TAG);
    }

    /*CALL BACK function to get USER CONFIRM to END CLASS*/
    @Override
    public void onTextDialogOK(boolean agree) {
        if (agree){
            removeItemFromFirebse();
        }
    }

    /* END CLASS method */
    private void removeItemFromFirebse(){
    // Code to undo the user's last action
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    Query applesQuery = ref.child("class").orderByChild("class_code").equalTo(classcodeET.getText().toString());
    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot classsnapshot: dataSnapshot.getChildren()) {
                classsnapshot.getRef().removeValue();
                updateToast("Successfully Deleted");
                updateUI(null);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d("TutorAddClass.java", "onCancelled", databaseError.toException());
            updateToast("Fail to Delete");
        }
    });
    }

    /*  VIEW ATTENDANCE button */
    @OnClick(R.id.viewAttendButton) void startViewAttendanceActivity(){
        if (!class_code.isEmpty()){
            Intent i = new Intent (getApplicationContext(), ViewAttendanceActivity.class);
            i.putExtra(CLASS_KEY, class_code);
            startActivity(i);
        }else updateToast("No class found");

    }

    /*  DISCUSSION button */
    @OnClick(R.id.discussionButton) void DiscussionActivity(){
        if (!class_code.isEmpty()){
            Intent i = new Intent (getApplicationContext(), DiscussionActivity.class);
            i.putExtra(ID_KEY, user_id);
            i.putExtra(NAME_KEY, user_name);
            i.putExtra(CLASS_KEY, class_code);
            startActivity(i);
        }else{
            updateToast("No class found");

        }

    }
    /*SIGN OUT button */
    @OnClick(R.id.btnSignout) void signUserOut() {
        // TODO: sign the user out
        mAuth.signOut();
        updateToast("signUserOut: successful");
        Intent i = new Intent (getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

}
