package com.shivam.deliveryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.TreeMap;

public class contributeNeeds extends AppCompatActivity {
    String itemid;
    String itemname;
    String itemdesc;
   TextView itemnametext;
   TextView itemdesctext;
   Button submitreq;
   EditText sumitname;
   EditText submitdesc;
    FirebaseUser firebaseUser;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute_needs);
        Bundle bundle = getIntent().getExtras();
        itemid = bundle.getString("itemid");
        itemdesc=bundle.getString("itemdesc");
        itemname=bundle.getString("itemname");
        itemnametext=findViewById(R.id.textView21);
        itemdesctext=findViewById(R.id.textView22);
        itemdesctext.setText(itemdesc);
        itemnametext.setText(itemname);

        submitdesc=findViewById(R.id.editTextTextPersonName3);
        sumitname=findViewById(R.id.editTextTextPersonName);
        submitreq=findViewById(R.id.button555);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        user=firebaseUser.getUid();
        Log.i("hahahah",itemid);


        submitreq.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(submitdesc.getText().toString().length()<20)
                    Toast.makeText(getApplicationContext(),"Description should be atleast 20 word long",Toast.LENGTH_LONG).show();
                else if(sumitname.getText().toString().length()<1)
                    Toast.makeText(getApplicationContext(),"please enter item title",Toast.LENGTH_LONG).show();
                else {
                    Map<String,String> itemMap=new TreeMap<>();

                    itemMap.put("title",sumitname.getText().toString());
                    itemMap.put("description",submitdesc.getText().toString());
                    itemMap.put("donorid",user);
                   FirebaseDatabase.getInstance().getReference().child("needs").child(itemid).child("donations").child(user).setValue(itemMap);


                    Toast.makeText(getApplicationContext(),"thank you!",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(),needs.class);
                    startActivity(intent);
                    finish();
                }

            }
        });


    }
}