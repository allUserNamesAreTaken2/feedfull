package com.shivam.deliveryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class detailsofuser extends AppCompatActivity {

    FirebaseUser user;
    ImageView ppictue;
    TextView namee;
    TextView numberr;
    TextView emaill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsofuser);
        user = FirebaseAuth.getInstance().getCurrentUser();
        ppictue=findViewById(R.id.ppimageView3);
        namee=findViewById(R.id.nameeditText4);
        emaill=findViewById(R.id.emaileditText7);
        numberr=findViewById(R.id.numbereditText6);
        final Button button=findViewById(R.id.profileupdbutton2);

        final Handler handler=new Handler();
        final int delay=1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(namee.getText().toString().length()>4&&emaill.getText().toString().length()>4&&numberr.getText().toString().length()>10)
                   button.setEnabled(true);
                handler.postDelayed(this,delay);
            }
        },delay);


        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplication(),the_main_screen.class);
                startActivity(intent);
                finish();
            }

        });



        try{
            if(user!=null){
                if(user.getDisplayName()!=null)
                    namee.setText(user.getDisplayName());

                if(user.getEmail()!=null)
                    emaill.setText(user.getEmail());

                if(user.getPhoneNumber()!=null)
                    numberr.setText(user.getPhoneNumber());

                if(user.getPhotoUrl()!=null)
                    Glide.with(this).load(user.getPhotoUrl()).into(ppictue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
