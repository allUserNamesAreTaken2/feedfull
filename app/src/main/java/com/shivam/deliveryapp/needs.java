package com.shivam.deliveryapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class needs extends Fragment {




    ArrayList<String> mngoname=new ArrayList<>();
    ArrayList<String> mitemneed=new ArrayList<>();
    ArrayList<String>mitemneedesc=new ArrayList<>();
    ArrayList<String>mitemId=new ArrayList<>();
    ProgressBar progressBar;

    RecyclerView needrecyclerview;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thread.start();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_needs, container, false);
        needrecyclerview=v.findViewById(R.id.needsrecycler);

        progressBar=v.findViewById(R.id.progressBar2);

        return v;
    }



    public void initneedarray(){

        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();


    mDatabase.child("needs").addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            mitemneed.add(dataSnapshot.child("itemname").getValue().toString());
            mitemneedesc.add(dataSnapshot.child("itemdesc").getValue().toString());
            mngoname.add(dataSnapshot.child("ngoname").getValue().toString());
            mitemId.add(dataSnapshot.getKey());


            NeedRecyclerAdapter needRecyclerAdapter=new NeedRecyclerAdapter(getContext(),mngoname,mitemneed,mitemneedesc,mitemId);
            needrecyclerview.setAdapter(needRecyclerAdapter);
            needrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
            progressBar.setVisibility(View.INVISIBLE);

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }


    });


    }


  Thread thread=new Thread(){
      @Override
      public void run() {
          super.run();
          initneedarray();
      }
  };



}
