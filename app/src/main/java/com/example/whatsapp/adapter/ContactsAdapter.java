package com.example.whatsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsapp.R;
import com.example.whatsapp.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    private List<User> mContacts;
    private Context mContext;

    public ContactsAdapter(List<User> listContacts, Context c) {
        this.mContacts = listContacts;
        this.mContext = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contacts, parent, false);
        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User user = mContacts.get(position);
        holder.mName.setText(user.getName());
        holder.mEmail.setText(user.getEmail());
        if(user.getProfilePic() != null){
            Uri uri = Uri.parse(user.getProfilePic());
            Glide.with(mContext).load(uri).into(holder.mPic);
        }else {
            holder.mPic.setImageResource(R.drawable.padrao);
        }
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView mPic;
        TextView mName, mEmail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mPic = itemView.findViewById(R.id.imageViewPicAdapter);
            mName = itemView.findViewById(R.id.textViewNameAdapter);
            mEmail = itemView.findViewById(R.id.textViewEmailAdapter);
        }
    }
}
