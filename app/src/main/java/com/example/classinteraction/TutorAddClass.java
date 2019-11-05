package com.example.classinteraction;

import android.os.Bundle;

import com.example.classinteraction.utils.ClassCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TutorAddClass extends AppCompatActivity {

    private DatabaseReference ref ;

    @BindView(R.id.etClassCode)
    EditText classcodeET;

    @BindView(R.id.etClassName)
    EditText etClassName;

    @BindView(R.id.toogleActive)
    ToggleButton toogleActive;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_add_class);
        ref = FirebaseDatabase.getInstance().getReference();
        //CoordinatorLayout mCoordinator = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        ButterKnife.bind(this);


    }
    /*
     * write some class object to firebase
     * @see ClassCode.java
     * */
    @OnClick(R.id.addClassButton) void onClickAddClass(){

        //Input
        classcodeET.setText("1206");
        etClassName.setText("COS10009 SDMD");
        toogleActive.setChecked(true);

        //TODO validation before use
        String cCode= classcodeET.getText().toString();
        String cName= etClassName.getText().toString();
        boolean isActive = toogleActive.isChecked();

        ref = FirebaseDatabase.getInstance().getReference("class");
        ClassCode newClass = new ClassCode(cCode, cName, isActive);
        ref.push().setValue(newClass);
        updateStatus();
    }
    /* show status using snackbar*/

    private void updateStatus(){
        Snackbar mySnackbar =Snackbar.make(mCoordinator, R.string.class_added,
                Snackbar.LENGTH_SHORT);
        mySnackbar.setAction(R.string.undo_string, new MyUndoListener());
        mySnackbar.show();
    }

    /*Undo add class*/
    public class MyUndoListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            // Code to undo the user's last action
        }
    }




}
