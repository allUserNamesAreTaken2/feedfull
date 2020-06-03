package com.shivam.deliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;


public class signInFG extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_f_g);


        List<AuthUI.IdpConfig> providers = Arrays.asList(

                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()

                );

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                      .setTheme(R.style.AppTheme)
                        .setLogo(R.drawable.ahahahah)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);



    }



    // [START auth_fui_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference reference=firebaseDatabase.getReference();
                try {

                    reference.child("users").child(user.getUid()).child("userinfo").child("useras").setValue("user");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if(user.getMetadata().getLastSignInTimestamp()==user.getMetadata().getCreationTimestamp())
                {
                    Intent intent=new Intent(this,the_main_screen.class);
                    startActivity(intent);

                }else{
                    Intent intent=new Intent(this,detailsofuser.class);
                    startActivity(intent);

                }

                Toast.makeText(getApplicationContext(),"Signed In",Toast.LENGTH_LONG).show();
                finish();

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(getApplicationContext(),"Sign In Failed",Toast.LENGTH_LONG).show();
            }
        }
    }
    // [END auth_fui_result]



    public void delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_delete]
    }





}


