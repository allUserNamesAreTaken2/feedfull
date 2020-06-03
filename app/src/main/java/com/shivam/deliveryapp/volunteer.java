package com.shivam.deliveryapp;

import android.content.Intent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class volunteer extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAnalytics mFirebaseAnalytics;
    Button gotongo;
    FirebaseUser user;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_volunteer, container, false);
        user=FirebaseAuth.getInstance().getCurrentUser();




       gotongo=v.findViewById(R.id.ngoregnbutton);

       gotongo.setOnClickListener(new Button.OnClickListener() {
           @Override
           public void onClick(View v) {

               DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
               reference.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                  final String useras=  dataSnapshot.child("users").child(user.getUid()).child("userinfo").child("useras").getValue().toString();

                       Log.i("useras",useras);
                       if(useras.equals("ngo")){
                           Intent intent=new Intent(getContext(),ngoView.class);
                           startActivity(intent);
                       }
                       else if(useras.equals("user")){
                           Intent intent = new Intent(getContext(), ngoregn.class);
                           startActivity(intent);
                       }
                   }


                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });




           }
       });


        return v;
    }


}
