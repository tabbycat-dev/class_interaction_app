package com.example.classinteraction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Log in Activity
 * contains Firebase authentication using email and password
 * using ButterKnife to BindView
 * @author: Tan Phuc Nguyen
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.et_email)
    EditText email_et;

    @BindView(R.id.et_password)
    EditText password_et;

    FirebaseAuth mAuth ;
    private final String TAG = "LOGIN";

    @BindView(R.id.spinnerRole)
    Spinner spinnerRole;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }
    //TODO implement Firebase Auth
    /**
     * LOG IN METHOD
     * get user email and password from view
     * sign user in using email and password
     */
    @OnClick(R.id.btnLogin) void login(){
        //String email = email_et.getText().toString();
        //String password=password_et.getText().toString();

        String role  = spinnerRole.getSelectedItem().toString();

        String email = "student0@gmail.com";
        String password="student123";

        mAuth =FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // If successful sign in, get user and show message
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user !=null){
                                updateStatus("signInWithEmail:success");
                                if (role.equals("Student")){
                                    Intent i = new Intent (getApplicationContext(), CheckClass.class);
                                    startActivity(i);
                                } else{
                                    Intent i = new Intent (getApplicationContext(), TutorAddClass.class);
                                    startActivity(i);
                                }

                            }else{
                                updateStatus("signInWithEmail:completed not success");
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i(TAG, "signInWithEmail:failure", task.getException());
                            updateStatus("signInWithEmail:failure");
                        }
                    }
                })
                //login error handling
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            updateStatus("signInWithEmail:invalid password");
                        } else if (e instanceof FirebaseAuthInvalidUserException) {
                            updateStatus("signInWithEmail:No account with this email");
                        } else {
                            updateStatus(e.getLocalizedMessage());
                        }
                    }
                })
        ;
    }

    private void updateStatus(String stat) {
        Toast.makeText(getApplicationContext(), stat, Toast.LENGTH_SHORT).show();
    }
    @OnClick (R.id.guestMode) void gotoGuestMode(){
        Intent i = new Intent (getApplicationContext(), DashboardActivity.class);
        startActivity(i);
    }
    @OnClick(R.id.createAcct) void createAnAccount(){
        Intent i = new Intent (getApplicationContext(), RegisterActivity.class);
        startActivity(i);
    }


}
