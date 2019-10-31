package com.example.classinteraction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
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
        String email = email_et.getText().toString();
        String password=password_et.getText().toString();

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
                                Intent i = new Intent (getApplicationContext(), DashboardActivity.class);
                                startActivity(i);
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
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            updateStatus("This email address is already in use.");
                        }
                        else {
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
