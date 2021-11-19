package com.example.buupass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.alterac.blurkit.BlurLayout;

public class MainActivity extends AppCompatActivity {
    BlurLayout blurLayout;
    private TextView register, forgotPassword;
    private EditText editTextEmail, editTextPassword;
    private Spinner spinner;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blurLayout = findViewById(R.id.blurLayout);
        editTextEmail = (EditText) findViewById(R.id.email_address);
        editTextPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        spinner = (Spinner) findViewById(R.id.spinner1);
        mAuth = FirebaseAuth.getInstance();

    }
    @Override
    protected void onStart() {
        super.onStart();
        blurLayout.startBlur();
    }

    @Override
    protected void onStop() {
        blurLayout.pauseBlur();
        super.onStop();
    }

    public void Registerpage(View view) {
        Intent intent= new Intent(this, RegisterUser.class);
        startActivity(intent);
    }

    public void SignIn(View view) {

        userLogin();
    }

    private void userLogin() {

        String email = editTextEmail.getText().toString().trim();
        String passsword = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is Required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Enter a Valid Email");
            editTextEmail.requestFocus();
            return;
        }

        if(passsword.isEmpty()){
            editTextPassword.setError("Password is Required");
            editTextPassword.requestFocus();
            return;
        }
        if(passsword.length()<6){
            editTextPassword.setError("Password should be more than 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, passsword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        // redirect to user profile
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));

                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check Your Email to verify your Account!", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(MainActivity.this, "Failed to LOGIN please check your credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void Forgetpassword(View view) {
        Intent intent = new Intent(MainActivity.this, ForgottenPassword.class);
        startActivity(intent);

    }
}