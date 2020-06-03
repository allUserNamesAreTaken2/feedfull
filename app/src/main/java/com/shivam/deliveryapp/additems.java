package com.shivam.deliveryapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.navigation.Navigation;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class additems extends Fragment {

    Spinner spin;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ImageView itemImage;
    Button submitItem;
    TextView titleOfItem;
    TextView descriptionOfItem;
    Bitmap selectedImage=null;
   String imageUriInString;
   int completed=0;
   ProgressBar progressBar;

    private EditText OtherCategoty;





    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public additems() {
        // Required empty public constructor

    }

    public static additems newInstance(String param1, String param2) {
        additems fragment = new additems();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                 selectedImage = BitmapFactory.decodeStream(imageStream);
                itemImage.setImageBitmap(selectedImage);




                ///add image to storage

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                storageRef = storage.getReference();
                final StorageReference imagesRef = storageRef.child("images");

                String imageName = UUID.randomUUID().toString() + ".jpg";
                itemImage.setDrawingCacheEnabled(true);
                itemImage.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) itemImage.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imagedata = baos.toByteArray();
                progressBar.setVisibility(View.VISIBLE);
                final UploadTask uploadTask = imagesRef.child(imageName).putBytes(imagedata);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                final Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override

                                    public void onSuccess(Uri uri) {

                                        imageUriInString=result.getResult().toString();
                                        Log.i("url",imageUriInString);
                                       completed=1;
                                       progressBar.setVisibility(View.INVISIBLE);

                                        //createNewPost(imageUrl);
                                    }
                                });
                            }
                        }
                    }});



            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_additems, container, false);
        spin=(Spinner)v.findViewById(R.id.categorySpinner);
        itemImage=v.findViewById(R.id.itemAddedImage);
        descriptionOfItem=v.findViewById(R.id.DescriptionOfItemeditText);
        titleOfItem=v.findViewById(R.id.titleOfItemEditText);
        submitItem=v.findViewById(R.id.submitItembutton);
        progressBar=v.findViewById(R.id.progressBar);


        final Handler handler=new Handler();
        final int delay=1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(completed==1&&!titleOfItem.equals(null)&&!descriptionOfItem.equals(null))
                   submitItem.setEnabled(true);
                handler.postDelayed(this,delay);
            }
        },delay);



        OtherCategoty=v.findViewById(R.id.OtherCategoryeditText);

       spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                        OtherCategoty.setVisibility(View.INVISIBLE);
                        break;
                    case  3:
                        OtherCategoty.setVisibility(View.VISIBLE);
                        break;
                }
            }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }

       });




        itemImage.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    getPhoto();
                }
            }
        });





        submitItem.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                 Map<String,String> itemMap=new TreeMap<>();


                //get desc and title
               String categoryOfItem= spin.getSelectedItem().toString();

               if(categoryOfItem=="Others")
               {

                  categoryOfItem= OtherCategoty.getText().toString();
               }

//add image url and other info to database
                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                String user=firebaseUser.getUid();

                itemMap.put("title",titleOfItem.getText().toString());
                itemMap.put("description",descriptionOfItem.getText().toString());
                itemMap.put("category",categoryOfItem);
                itemMap.put("imageUrl",imageUriInString);
                itemMap.put("uid",user);

              // Log.i("IMAGE", "aaa"+itemMap.get(imageUri));






                FirebaseDatabase.getInstance().getReference().child("items").push().setValue(itemMap);


                Toast.makeText(getContext(),"ADDED",Toast.LENGTH_LONG).show();

               Navigation.findNavController(v).navigate(R.id.action_additems_to_home2);





            }


        });

        ArrayList<String> categoryList=new ArrayList<>();

        categoryList.add("Food");
        categoryList.add("Clothing");
        categoryList.add("Shelter");
        categoryList.add("Others");

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,categoryList);

            arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

            spin.setAdapter(arrayAdapter);
        return v;
    }
}
