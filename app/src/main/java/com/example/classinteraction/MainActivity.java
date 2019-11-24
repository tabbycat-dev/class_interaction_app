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
import android.widget.Button;
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

    @BindView(R.id.btnLogin)
    Button btnLogin;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }
    //TODO implement Firebase Auth
    /**
     * LOG IN METHOD
     * get user email and password from view
     * sign user in using email and password
     */
    @OnClick(R.id.btnLogin) void login() {
        String email = "kevin.ng@gmail.com";
        String password="kevin123";
        //String email = email_et.getText().toString();
        //String password = password_et.getText().toString();
        if (!isOnline()){
            updateStatus("No Internet found!");
        }else if (validate(email, password)) {
            //progress dialog show and disable button
            btnLogin.setEnabled(false);
            progressDialog = new ProgressDialog(this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            MyTask myTask = new MyTask();
            myTask.execute(email,password);
        }
    }
    public class MyTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
                try{ Thread.sleep(1000);
                }catch(InterruptedException e){}
            loginProcess(params[0], params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //setUpAdapter();
            btnLogin.setEnabled(true);
            progressDialog.dismiss();
        }
    };
        private void loginProcess(String email, String password){
        String role  = spinnerRole.getSelectedItem().toString();
        mAuth =FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // If successful sign in, get user and show message
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user !=null){
                                updateStatus("signIn: successful");
                                if (role.equals("Student")){
                                    Intent i = new Intent (getApplicationContext(), CheckClass.class);
                                    startActivity(i);
                                } else{
                                    Intent i = new Intent (getApplicationContext(), TutorAddClass.class);
                                    startActivity(i);
                                }

                            }else{
                                updateStatus("signIn:completed not success");
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i(TAG, "signIn:failure", task.getException());
                            updateStatus("signIn:failure");
                        }
                    }
                })
                //login error handling
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            updateStatus("signIn: invalid password");
                        } else if (e instanceof FirebaseAuthInvalidUserException) {
                            updateStatus("signIn: No account with this email");
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
    @OnClick(R.id.createAcct) void createAnAccount(){
        Intent i = new Intent (getApplicationContext(), RegisterActivity.class);
        startActivity(i);
    }

    public boolean validate(String email, String password) {
        boolean valid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_et.setError("Enter a valid email address");
            valid = false;
        } else {
            email_et.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 ) {
            password_et.setError("Password should be at least 6 characters");
            valid = false;
        } else {
            password_et.setError(null);
        }

        return valid;
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
