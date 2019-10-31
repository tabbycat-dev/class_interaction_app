package com.example.classinteraction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Firebase Authentication Activity
 * app shows REGISTER / SIGN IN / SIGN OUT
 */
public class FirebaseAuthActivity extends AppCompatActivity {
    private EditText emailEt, passwordEt;
    private Button registerBtn, btnSignout, loginBtn, viewDetaisBtn;
    private TextView statusTv;
    private FirebaseAuth mAuth ;
    private String email, password;
    //private FirebaseAuth.AuthStateListener mAuthListener;
    private final String TAG = "TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Initialize Firebase Auth
        mAuth =FirebaseAuth.getInstance();
        initUI();
    }

    private void initUI(){
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        emailEt.setText("tan@gmail.com");
        passwordEt.setText("tan123");
        statusTv = findViewById(R.id.tvStatus);

        // CREATE AN ACCOUNT button
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInput();
                createUser(email, password);
            }
        });
        // SIGN IN button
        loginBtn = findViewById(R.id.btnSignIn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInput();
                signUserIn(email,password);
            }
        });
        // SIGN OUT button
        btnSignout = findViewById(R.id.btnSignout);
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInput();
                signUserOut();
            }
        });
        // VIEW USER DETAILS button
        viewDetaisBtn = findViewById(R.id.btnUserInfo);
        viewDetaisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accessUserInfo();
            }
        });
    }
    /**
     * get user input of email and password from UI
     */
    private void getInput(){
        //TODO get user input
        if (!checkFormFields())
            return;
        email = emailEt.getText().toString();
        password = passwordEt.getText().toString();
        Log.i(TAG, "GET input...");
        //email ="tan@gmail.com";
        //password = "tan123";

    }
    /**
     * access user info
     */
    private void accessUserInfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            statusTv.setText(name +" "+email+" "+uid);
        }
    }
    /**
     * create a new account
     */
    private void createUser(final String email, String password){
        Log.i(TAG, "CREATING...");
        // TODO: Create the user account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(TAG, "createUserWithEmail:success "+email);
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateStatus("createUserWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i(TAG, "createUserWithEmail:failure "+email, task.getException());
                            Toast.makeText(FirebaseAuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateStatus("createUserWithEmail:failure");
                        }
                        // ...
                    }
                });
    }
    /**
     * sign user in
     */
    private void signUserIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user !=null){
                                updateStatus("signInWithEmail:success");
                            }else{
                                updateStatus("signInWithEmail:completed not success");
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText( FirebaseAuthActivity.this,"Authentication failed.",Toast.LENGTH_SHORT).show();
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

    private void signUserOut() {
        // TODO: sign the user out
        mAuth.signOut();
        updateStatus("signUserOut: successful");
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI();
        // TODO: add the AuthListener
        //mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void updateUI(){
        //TODO update UI
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            //statusTv.setText("Signed in: " + user.getEmail());
        }
        else {
            //statusTv.setText("Signed Out");
        }
    }
    private void updateStatus(String stat) {
        statusTv.setText(stat);
    }

    private boolean checkFormFields() {
        //TODO check form fields
        String email, password;

        if (emailEt.toString().isEmpty()) {
            emailEt.setError("Email Required");
            return false;
        }
        if (passwordEt.toString().isEmpty()){
            passwordEt.setError("Password Required");
            return false;
        }

        return true;
    }

}

