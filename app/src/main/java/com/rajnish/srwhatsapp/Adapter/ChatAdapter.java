package com.rajnish.srwhatsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.rajnish.srwhatsapp.ModelClass.MessageModel;
import com.rajnish.srwhatsapp.R;
import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter{

    ArrayList<MessageModel> messageModels;
    Context context;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.samplesender ,parent,false);
            return new senderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.samplereciever ,parent,false);
            return new reciverViewHolder(view);

        }

    }

    @Override
    public int getItemViewType(int position) {

         if(messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){

             return SENDER_VIEW_TYPE;



         }
         else {
            return  RECEIVER_VIEW_TYPE;
         }



    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = messageModels.get(position);
        if(holder.getClass() == senderViewHolder.class){
            ((senderViewHolder)holder).senderMsg.setText(messageModel.getMessage());
        }
        else {
            ((reciverViewHolder)holder).recieverMsg.setText(messageModel.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class reciverViewHolder extends RecyclerView.ViewHolder{

        TextView recieverMsg, recievedTime;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);

            recieverMsg = itemView.findViewById(R.id.recieverText);
            recievedTime = itemView.findViewById(R.id.recieverTime);

        }
    }

    public class senderViewHolder extends RecyclerView.ViewHolder{

        TextView senderMsg, sendTime;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMsg = itemView.findViewById(R.id.SenderMsg);
            sendTime = itemView.findViewById(R.id.senderTime);

        }
    }

}
