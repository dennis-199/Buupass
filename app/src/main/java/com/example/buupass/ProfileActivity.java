package com.example.buupass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView greetingTextView = (TextView) findViewById(R.id.greeting);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile = snapshot.getValue(User.class);

                Calendar c = Calendar.getInstance();
                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
                String fullname = userprofile.fullname;

                if(timeOfDay >= 0 && timeOfDay < 12){
                    greetingTextView.setText("Good Morning " + fullname + "!");
                }else if(timeOfDay >= 12 && timeOfDay < 16){
                    greetingTextView.setText("Good Afternoon " + fullname + "!");
                }else if(timeOfDay >= 16 && timeOfDay < 21){
                    greetingTextView.setText("Good Evening " + fullname + "!");
                }else if(timeOfDay >= 21 && timeOfDay < 24){
                    greetingTextView.setText("Good Night " + fullname + "!");
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new fragment1(), "PAY");
        vpAdapter.addFragment(new fragment2(), "BOOK NOW");
        viewPager.setAdapter(vpAdapter);
    }

    public void messegesview(View view) {

        Intent intent = new Intent(ProfileActivity.this, MessageActivity.class);
        startActivity(intent);

    }
}