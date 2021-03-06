package com.example.classinteraction;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
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
public class RegisterActivity extends AppCompatActivity implements TextDialog.TextDialogListener {
    @BindView(R.id.emailEt)
    EditText email_et;

    @BindView(R.id.passwordEt)
    EditText password_et;

    @BindView(R.id.et_passwordET02)
    EditText password_et2;

    @BindView(R.id.nameEt)
    EditText name_et;
    @BindView(R.id.registerBtn)
    Button registerBtn;
    private FirebaseAuth mAuth ;
    private String email, password, user_name;
    public static final String DIALOG_TAG = "dialog_tag";
    private final String TAG = "TAG";
    private ProgressDialog progressDialog;

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
        if (!isOnline()){
            updateStatus("No Internet found!");
        }else if (validateFormFields()) {
                email = email_et.getText().toString();
                password = password_et.getText().toString();
                user_name = name_et.getText().toString();

                //progress dialog show and disable button
                registerBtn.setEnabled(false);
                progressDialog = new ProgressDialog(this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Creating account...");
                progressDialog.show();

                MyTaskRegister myTask = new MyTaskRegister();
                myTask.execute(email,password);

        }
    }

    public class MyTaskRegister extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try{ Thread.sleep(1000);
            }catch(InterruptedException e){}
            createUserProcess(params[0], params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //setUpAdapter();
            registerBtn.setEnabled(true);
        }
    };
    private boolean validateFormFields() {

        boolean valid = true;
        if (name_et.getText().toString().isEmpty()) {
            name_et.setError("Your full name is required");
            return false;
        }
        if ((email_et.getText().toString().isEmpty()||!android.util.Patterns.EMAIL_ADDRESS.matcher(email_et.getText().toString()).matches())) {
            email_et.setError("enter a valid email address");
            return false;
        }
        if (password_et.getText().toString().isEmpty()||password_et.getText().toString().length()<6){
            password_et.setError("Password should be at least 6 characters");
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

    private void createUserProcess(final String email, String password){
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
                                                progressDialog.dismiss();
                                                confirmRegister(); }
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.i(TAG, "createUserWithEmail:failure "+email, task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            updateStatus("createUserWithEmail:failure");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            progressDialog.dismiss();
                            updateStatus("This email address is already in use.");
                        } else {
                            progressDialog.dismiss();
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
    @OnClick(R.id.link_login) void linkToLogin(){
        Intent i = new Intent (getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}