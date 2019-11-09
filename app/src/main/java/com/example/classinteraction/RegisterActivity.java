package com.example.classinteraction;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classinteraction.utils.TextDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * REGISTER ACTIVITY
 * User Authentication using Firebase Auth
 * app shows REGISTER / SIGN IN / SIGN OUT
 */
public class RegisterActivity extends AppCompatActivity implements TextDialog.TextDialogListener
    {
    @BindView(R.id.emailEt)
    EditText email_et;

    @BindView(R.id.passwordEt)
    EditText password_et;

    @BindView(R.id.et_passwordET02)
    EditText password_et2;

    @BindView(R.id.nameEt)
    EditText name_et;

    private FirebaseAuth mAuth ;
    private String email, password, user_name;
    public static final String DIALOG_TAG = "dialog_tag";
    private final String TAG = "TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        // Initialize Firebase Auth
        mAuth =FirebaseAuth.getInstance();
        initUI();
    }
    private void initUI() {
    }
    /*REGISTER button*/
    @OnClick(R.id.registerBtn) void registerNewAccount(){
        //  1 - validate form
        if (validateFormFields()){
            email = email_et.getText().toString();
            password = password_et.getText().toString();
            user_name = name_et.getText().toString();
            Log.i(TAG, "GET input...");

            //  2 - create new account using Firebase Auth
            createUser(email, password);
        }
    }
    private boolean validateFormFields() {
        if (name_et.getText().toString().isEmpty()) {
            name_et.setError("Your full name is required");
            return false;
        }
        if (email_et.getText().toString().isEmpty()) {
            email_et.setError("Your email Required");
            return false;
        }
        if (password_et.getText().toString().isEmpty()){
            password_et.setError("Password Required");
            return false;
        }
        if (password_et2.getText().toString().isEmpty()){
            password_et2.setError("Re-type Password Required");
            return false;
        }
        if (!password_et.getText().toString().equals(password_et2.getText().toString())){
            password_et2.setError("Password and retype must be matched");
            return false;
        }
        return true;
    }


    private void confirmRegister(){
        //use dialog to confirm
        TextDialog dialog = TextDialog.newInstance("Successfully registered", "Hi "+user_name+"! Now you can log in.");
        dialog.show(getSupportFragmentManager(), DIALOG_TAG);
    }

    private void createUser(final String email, String password){
        Log.i(TAG, "CREATING...");
        // TODO: Create the user account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.i(TAG, "createUserWithEmail:success "+email);
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(user_name)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                //Log.d(TAG, "User profile updated.");
                                                confirmRegister(); }
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.i(TAG, "createUserWithEmail:failure "+email, task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateStatus("createUserWithEmail:failure");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            updateStatus("This email address is already in use.");
                        } else {
                        updateStatus(e.getLocalizedMessage());
                    }
                    }
                });
    }
    private void updateStatus(String stat) {
        Toast.makeText(getApplicationContext(), stat, Toast.LENGTH_SHORT).show();
    }
        @Override
        public void onTextDialogOK(boolean agree) {
            Intent i = new Intent (getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    }
