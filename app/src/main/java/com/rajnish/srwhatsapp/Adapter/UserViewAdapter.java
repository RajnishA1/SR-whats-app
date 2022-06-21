package com.rajnish.srwhatsapp.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rajnish.srwhatsapp.ChatPage;
import com.rajnish.srwhatsapp.ModelClass.Users;
import com.rajnish.srwhatsapp.R;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserViewAdapter extends RecyclerView.Adapter<UserViewAdapter.viewHolder> {

    ArrayList <Users> list;
    Context context;

    public UserViewAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.userlistshample,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
          Users users = list.get(position);
           holder.userDisplayname.setText(users.getName());
           holder.lastchart.setText("Wow Great");
           Picasso.get().load(users.getProfile()).placeholder(R.drawable.rj).into(holder.userProfile);
           holder.foronclicklinearlayout.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = new Intent(context, ChatPage.class);
                   intent.putExtra("userId",users.getUserid());
                   intent.putExtra("ProfilePic",users.getProfile());
                   intent.putExtra("UserName",users.getName());
                   intent.putExtra("UserTokenId",users.getUserToken());

                   context.startActivity(intent);

               }
           });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView userDisplayname , lastchart;
        ImageView userProfile;
        LinearLayout foronclicklinearlayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            userDisplayname = itemView.findViewById(R.id.userDisplayname);
            lastchart = itemView.findViewById(R.id.lastchart);
            userProfile = itemView.findViewById(R.id.ProfilePic);
            foronclicklinearlayout = itemView.findViewById(R.id.foronclicklinearlayout);


        }
    }



}
