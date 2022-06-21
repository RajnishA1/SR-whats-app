package com.rajnish.srwhatsapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rajnish.srwhatsapp.Adapter.ChatAdapter;
import com.rajnish.srwhatsapp.ModelClass.MessageModel;
import com.rajnish.srwhatsapp.databinding.ActivityChatPageBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;


public class ChatPage extends AppCompatActivity {
    ActivityChatPageBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityChatPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
       final String senderId = auth.getUid();
       final String recieverId = getIntent().getStringExtra("userId");
       final String UserTokenId = getIntent().getStringExtra("UserTokenId");
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        binding.UserName.setText(getIntent().getStringExtra("UserName"));
        Picasso.get().load(getIntent().getStringExtra("ProfilePic")).into(binding.UserProfile);

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatPage.this,UserViewList.class);
                startActivity(intent);
            }
        });

        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter( messageModels,this);
        binding.ChatRecycleView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        binding.ChatRecycleView.setLayoutManager(layoutManager);

        final String senderRoom = senderId+recieverId;
        final String recieverRoom = recieverId+senderId;


        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for (DataSnapshot dataSnapshot :snapshot.getChildren()){
                            MessageModel model = dataSnapshot.getValue(MessageModel.class);
                            messageModels.add(model);
                        }
                        binding.ChatRecycleView.scrollToPosition(binding.ChatRecycleView.getAdapter().getItemCount()-1);
                        chatAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





        binding.sendimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

           FcmNotificationsSender notificationsSender = new FcmNotificationsSender(UserTokenId,
                   "Rj",binding.editmessagettext.getText().toString(),getApplicationContext(),ChatPage.this);

           notificationsSender.SendNotifications();

                String message = binding.editmessagettext.getText().toString();

                final  MessageModel model = new MessageModel(senderId,message);

                model.setTimestamp(new Date().getTime());

                binding.editmessagettext.setText("");
                database.getReference().child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats")
                                .child(recieverRoom)
                                .push()
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });








            }
        });


    }
}