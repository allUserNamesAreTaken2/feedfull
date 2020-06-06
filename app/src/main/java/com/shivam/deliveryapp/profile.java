package com.shivam.deliveryapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


public class profile extends Fragment {


    ArrayList<String> mImages=new ArrayList<>();
     ArrayList<String> mname=new ArrayList<>();
     ArrayList<String> mDescription=new ArrayList<>();

    FirebaseAuth mAuth;
    ImageView profilepic;
    private Bitmap selectedImage;
    private String uid;
    FirebaseUser user;
    TextView personNamee;
    RecyclerView profilerecyclerView;
    ProgressBar progressBar;

    TextView editprofile;




    public void getPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);

    }




    ////////


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                profilepic.setImageBitmap(selectedImage);

                handleUpload(selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void handleUpload(Bitmap bitmap){

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

        String profilePicName= uid+".jpeg";

        final StorageReference storageReference=FirebaseStorage.getInstance().getReference().child("profileImage").child(profilePicName);
        storageReference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                try{
                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest.Builder().setPhotoUri(task.getResult()).build();
                            try{
                                user.updateProfile(profileChangeRequest);
                                Log.i("bhubgu","vuvyivbinlbuvu");}
                            catch (Exception e){ Log.i("FAILEDD","BCBC");

                            }

                        }
                    });


                }
                catch (Exception e){
                    Log.i("FAILEDD","NIDS");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    ////////




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thread.start();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_profile, container, false);

       profilepic=v.findViewById(R.id.profilepicc);
       personNamee=v.findViewById(R.id.personName);
       editprofile=v.findViewById(R.id.textView8);


        profilerecyclerView=v.findViewById(R.id.profileRecycler);


        mAuth = FirebaseAuth.getInstance();
         user = FirebaseAuth.getInstance().getCurrentUser();
        progressBar=v.findViewById(R.id.profileprogresss);






        if(user!=null){
            //user logged in the app
            uid=user.getUid();

            if(user.getDisplayName() !=null){
                personNamee.setText(user.getDisplayName());
           }

            if(user.getPhotoUrl() != null){
                try{
                    Glide.with(this).load(user.getPhotoUrl()).into(profilepic);

                }catch (Exception e){

                }
            }
        }
        else{

            Log.i("message" ,"user logged iff");
             Intent intent=new Intent(getActivity(),signInFG.class);
            startActivity(intent);
        }





        profilepic.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    getPhoto();
                }
            }
        });

        editprofile.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),editprofile.class);
                startActivity(intent);
            }
        });



        return v;
    }





    public void intitProfileArrays() {



            DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference();
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
        for (Map.Entry<String, Object> entry : users.entrySet()) {

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list


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

            profilerecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            profilerecyclerView.setAdapter(profileRecyclerAdapter);

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

