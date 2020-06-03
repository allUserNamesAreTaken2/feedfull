package com.shivam.deliveryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ngoregn extends AppCompatActivity {
    Button ngoSubmit;
    EditText phoneno;
    EditText ngoname;
    EditText ngoaddress;
    EditText ngoregnid;
    DatabaseReference reference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngoregn);
        ngoSubmit=findViewById(R.id.NGOSUBMIT);

            phoneno=findViewById(R.id.ngophone);
            ngoname=findViewById(R.id.ngoname);
            ngoaddress=findViewById(R.id.ngoadress);
            ngoregnid=findViewById(R.id.ngoregnid);
                user= FirebaseAuth.getInstance().getCurrentUser();
                reference= FirebaseDatabase.getInstance().getReference();



        ngoSubmit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(ngoaddress.getText().toString()!=""&&ngoname.getText().toString()!=""&&ngoregnid.getText().toString()!=""&&phoneno.getText().toString().length()>9) {


                    reference.child("users").child(user.getUid()).child("userinfo").child("useras").setValue("ngo");

                    reference.child("users").child(user.getUid()).child("userinfo").child("ngoname").setValue(ngoname.getText().toString());

                    reference.child("users").child(user.getUid()).child("userinfo").child("ngoadress").setValue(ngoaddress.getText().toString());

                    reference.child("users").child(user.getUid()).child("userinfo").child("ngophone").setValue(phoneno.getText().toString());

                    reference.child("users").child(user.getUid()).child("userinfo").child("ngoregnid").setValue(ngoregnid.getText().toString());

                    Toast.makeText(getApplicationContext(),"logged in as ngo",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), ngoView.class);
                    startActivity(intent);
                    finish();

                }
                else {
                    Toast.makeText(getApplicationContext(),"PLEASE ENTER ALL FIELDS CORRECTLY",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}