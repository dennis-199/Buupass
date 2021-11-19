package com.example.buupass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import io.alterac.blurkit.BlurLayout;

public class RegisterUser extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    BlurLayout blurLayout;
    private FirebaseAuth mAuth;
    private EditText edittextfullname, edittextemail, edittextage, edittextphonnumer, edittextpassword;
    private RadioButton malebutton, femalebutton, radioButton;
    private RadioGroup radioGroup;
    private ProgressBar progressBar;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        blurLayout = findViewById(R.id.blurLayout);
        mAuth = FirebaseAuth.getInstance();
        edittextfullname = (EditText) findViewById(R.id.full_name);
        edittextemail = (EditText) findViewById(R.id.email_address);
        edittextphonnumer = (EditText) findViewById(R.id.phone_number);
        edittextpassword = (EditText) findViewById(R.id.password);
        malebutton = (RadioButton) findViewById(R.id.radioMale);
        femalebutton = (RadioButton) findViewById(R.id.radioFemale);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        spinner = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void RegisterUser(View view) {

        registerUser();
    }

    private void registerUser() {
        String email = edittextemail.getText().toString().trim();
        String fullname = edittextfullname.getText().toString().trim();
        String phonenumber = edittextphonnumer.getText().toString().trim();
        String password = edittextpassword.getText().toString().trim();
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
        String Gender = radioButton.getText().toString().trim();
        String Role = spinner.getSelectedItem().toString().trim();

        if(fullname.isEmpty()){
            edittextfullname.setError("Full name is required ");
            edittextfullname.requestFocus();
            return;
        }
        if(email.isEmpty()){
            edittextemail.setError("Email is Required");
            edittextemail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edittextemail.setError("Please provide valid email");
            edittextemail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            edittextpassword.setError("Password is required");
            edittextpassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            edittextpassword.setError("password has to be more than 6 charcters");
            edittextpassword.requestFocus();
            return;
        }
        if (phonenumber.isEmpty()) {
            edittextphonnumer.setError("Phone number is required");
            edittextphonnumer.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullname, email,phonenumber, Gender, Role);

                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this, "You have been Registered succesfully ", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        Intent intent =  new Intent(RegisterUser.this, MainActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(RegisterUser.this,"Failed to Register, Retry", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }

                                }
                            });
                        }else{
                            Toast.makeText(RegisterUser.this,"Failed to Register, Retry", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


    }
}