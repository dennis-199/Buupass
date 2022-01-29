package com.example.buupass;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.alterac.blurkit.BlurLayout;


public class RegistrationFragment extends Fragment {
    DatabaseReference saccosDbRef;
    BlurLayout blurLayout;
    EditText saccosName;
    EditText registrationNo;
    EditText KRApin;
    EditText location;
    EditText matatuBusname;
    Button registrationBTN;

    private FirebaseAuth mAuth;
    //Declare an Instance of the database reference where we have user details
    private DatabaseReference mDatabaseUsers;
    //Declare a Instance of currently logged in user
    private FirebaseUser mCurrentUser;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        saccosName = view.findViewById(R.id.saccos_name);
        registrationNo = view.findViewById(R.id.registrationNo);
        KRApin = view.findViewById(R.id.kraID);
        location = view.findViewById(R.id.location);
        matatuBusname = view.findViewById(R.id.matatu_bus);
        registrationBTN = view.findViewById(R.id.registerBtn);

        mAuth = FirebaseAuth.getInstance();
        //Initialize the instance of the firebase user
        mCurrentUser = mAuth.getCurrentUser();

        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        saccosDbRef = FirebaseDatabase.getInstance().getReference().child("SACCOS");

        registrationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertsaccosdata();
            }
        });

        return view;
    }
    private void insertsaccosdata(){
        String SaccoName = saccosName.getText().toString();
        String RegistrationNumber = registrationNo.getText().toString();
        String KRA_pin = KRApin.getText().toString();
        String Location = location.getText().toString();
        String matatu_BusName = matatuBusname.getText().toString();
        String currentUser = mCurrentUser.getUid();

        if(SaccoName.isEmpty()){
            saccosName.setError("SACCOS name is required ");
            return;
        }
        if(RegistrationNumber.isEmpty()){
            registrationNo.setError("SACCOS name is required ");
            registrationNo.requestFocus();
            return;
        }

        if (KRA_pin.isEmpty()) {
            KRApin.setError("KRA PIN is required");
            KRApin.requestFocus();
            return;
        }
        if (Location.isEmpty()) {
            location.setError("Location is required");
            location.requestFocus();
            return;
        }
        if (matatu_BusName.isEmpty()) {
            matatuBusname.setError("Matatu or Bus Name is required");
            matatuBusname.requestFocus();
            return;
        }

        Saccos saccos = new Saccos(SaccoName,RegistrationNumber,KRA_pin,Location,matatu_BusName,currentUser);

        saccosDbRef.push().setValue(saccos);
        Toast.makeText(getActivity(), "Successfully UPDATED SACCO", Toast.LENGTH_SHORT).show();

    }

}