package com.shivam.deliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class ngoView extends AppCompatActivity {
    FloatingActionButton addnewneed;
    ImageView backbutton;
    private ArrayList<String> mImages=new ArrayList<>();
    private ArrayList<String> mname=new ArrayList<>();
    private ArrayList<String> mDescription=new ArrayList<>();

    RecyclerView recyclerView;
    ProgressBar progressBar;
    DatabaseReference mDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_view);
        mDatabase = FirebaseDatabase.getInstance().getReference();



          addnewneed=findViewById(R.id.floatingActionButton2);
        recyclerView=findViewById(R.id.recyclerViewngo);
        progressBar=findViewById(R.id.progressBar4);
        backbutton=findViewById(R.id.mainpageback);

      backbutton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),the_main_screen.class);
                startActivity(intent);
                finish();
            }
        });


      addnewneed.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),addneedngo.class);
                startActivity(intent);
            }
        });


         intitngoArrays();

    }



    public void intitngoArrays(){


        Log.i("hbei","hahahahhahaah");



       mDatabase.child("items").addListenerForSingleValueEvent(new ValueEventListener() {
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
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
           mname.add((singleUser.get("title").toString()));
            mDescription.add(singleUser.get("description").toString());


            try {

                mImages.add(singleUser.get("imageUrl").toString());

            } catch (Exception e) {
                e.printStackTrace();
            }



        }
///initiate adapter
        ProfileRecyclerAdapter profileRecyclerAdapter = new ProfileRecyclerAdapter(getApplicationContext(), mImages, mname, mDescription);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
         recyclerView.setAdapter(profileRecyclerAdapter);

        progressBar.setVisibility(View.INVISIBLE);


    }









}