package com.rajnish.srwhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.os.Bundle;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rajnish.srwhatsapp.Adapter.UserViewAdapter;
import com.rajnish.srwhatsapp.ModelClass.Users;

import com.rajnish.srwhatsapp.databinding.ActivityUserViewListBinding;

import java.util.ArrayList;


public class UserViewList extends AppCompatActivity {

ActivityUserViewListBinding binding;

DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserViewListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.UserListRecycleView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.UserListRecycleView.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance().getReference();

        ArrayList<Users> list = new ArrayList<>();



        UserViewAdapter userViewAdapter = new UserViewAdapter(list,this);
        binding.UserListRecycleView.setAdapter(userViewAdapter);

        database.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for ( DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserid(dataSnapshot.getKey());
                    if(!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser())) {
                        list.add(users);


                    }

                }
                userViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








    }



}