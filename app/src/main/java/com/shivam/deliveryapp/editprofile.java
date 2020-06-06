package com.shivam.deliveryapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

public class editprofile extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;
    Button signout;
    Button delete;
    ImageView profilepic;
    EditText personName;
    EditText personemail;
    EditText PersonAdd;
    EditText personPhno;
    Button profileupdate;
    private Bitmap selectedImage;


    public void getPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getApplication().getContentResolver().openInputStream(imageUri);
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
        signout=findViewById(R.id.signoutbutton);
        delete=findViewById(R.id.button2);
        profilepic=findViewById(R.id.profilepicc2);
        personName=findViewById(R.id.editTextTextPersonName2);
        profileupdate=findViewById(R.id.button);
        personemail=findViewById(R.id.editTextTextEmailAddress);
        personPhno=findViewById(R.id.editTextPhone);
        PersonAdd=findViewById(R.id.editTextTextPostalAddress);
        checkIfInfoPresent();



        profilepic.setOnClickListener(new ImageView.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    getPhoto();
                }
            }
        });

        signout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(getApplicationContext())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "signed out", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
        delete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // [START auth_fui_delete]
                    AuthUI.getInstance()
                            .delete(getApplicationContext())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "deleted", Toast.LENGTH_LONG).show();}
                            });
                    // [END auth_fui_delete]
            }
        });

        profileupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> itemMap=new TreeMap<>();
                itemMap.put("email",personemail.getText().toString());
                itemMap.put("address",PersonAdd.getText().toString());
                itemMap.put("phno",personPhno.getText().toString());

                FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("userinfoo").push().setValue(itemMap);

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(personName.getText().toString())
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("message", "User profile updated.");
                                }
                            }
                        });

            }
        });



    }
public void checkIfInfoPresent(){
    if(user!=null){
        //user logged in the app
        uid=user.getUid();

        if(user.getDisplayName() !=null){
            personName.setText(user.getDisplayName());
        }

        if(user.getPhotoUrl() != null){
            try{
                Glide.with(this).load(user.getPhotoUrl()).into(profilepic);

            }catch (Exception e){

            }
        }
    }


  }





}

