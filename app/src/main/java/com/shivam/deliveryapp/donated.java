package com.shivam.deliveryapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class donated extends Fragment {
RecyclerView recyclerView;
    ArrayList<String> mImages=new ArrayList<>();
    ArrayList<String> mname=new ArrayList<>();
    ArrayList<String> mDescription=new ArrayList<>();

    FirebaseAuth mAuth;
    private String uid;
    FirebaseUser user;
    ProgressBar progressBar;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thread.start();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_donated, container, false);
       recyclerView=v.findViewById(R.id.recyclerView555);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        progressBar=v.findViewById(R.id.progressBar4);
        user=FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();

       return v;
    }
    public void intitProfileArrays() {

       DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("donated").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot != null) {
                    Log.i("Fver", String.valueOf(Arrays.asList(dataSnapshot.getValue().toString())));
                    collectusers((Map<String,Object>) dataSnapshot.getValue());
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void collectusers(Map<String,Object> users) {

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()) {

            Map singleUser = (Map) entry.getValue();
            if (singleUser.get("uid").toString().equals(uid)) {
                mname.add((singleUser.get("title").toString()));
                mDescription.add(singleUser.get("description").toString());


                try {

                    mImages.add(singleUser.get("imageUrl").toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
///initiate adapter
            ProfileRecyclerAdapter profileRecyclerAdapter = new ProfileRecyclerAdapter(getContext(), mImages, mname, mDescription);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(profileRecyclerAdapter);
            progressBar.setVisibility(View.INVISIBLE);
        }


    }


    Thread thread=new Thread(){
        @Override
        public void run() {
            super.run();

            intitProfileArrays();
        }
    };
}