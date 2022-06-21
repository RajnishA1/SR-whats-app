package com.rajnish.srwhatsapp;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rajnish.srwhatsapp.ModelClass.Users;
import com.rajnish.srwhatsapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


   ActivityMainBinding binding;

    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String UserTokenId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());





       GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id1))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
       binding.loginwithgoogle.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               SignIn();

               FirebaseMessaging.getInstance().getToken()
                       .addOnSuccessListener(new OnSuccessListener<String>() {
                           @Override
                           public void onSuccess(String s) {
                               UserTokenId = s;


                           }
                       });

           }
       });



    }
    void SignIn(){
   Intent sinInIntent = mGoogleSignInClient.getSignInIntent();
   startActivityForResult(sinInIntent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
               GoogleSignInAccount account = task.getResult(ApiException.class);
                navigateTosecondActivity(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        }



    }
    private void navigateTosecondActivity(String tokenId){
        AuthCredential credential = GoogleAuthProvider.getCredential(tokenId,null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          FirebaseUser user = auth.getCurrentUser();


                           Users muser = new Users();
                           muser.setUserid(user.getUid());
                           muser.setName(user.getDisplayName());
                           muser.setProfile(user.getPhotoUrl().toString());
                           muser.setUserToken(UserTokenId);


                          database.getReference().child("Users").child(user.getUid()).setValue(muser);

                          Intent intent = new Intent(MainActivity.this,UserViewList.class);
                          startActivity(intent);

                    }
                      else {
                          Toast.makeText(MainActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                      }
                }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(MainActivity.this,UserViewList.class);
            startActivity(intent);
            finish();
        }
    }
}