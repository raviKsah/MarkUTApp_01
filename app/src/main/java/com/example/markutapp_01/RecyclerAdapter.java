package com.example.markutapp_01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<com.example.markutapp_01.RecyclerAdapter.ViewHolder> {

    private static final String Tag="RecyclerView";
    private Context mContext;
    private ArrayList<Messages> messagesList;

    public RecyclerAdapter(Context mContext, ArrayList<Messages> messagesList) {
        this.mContext = mContext;
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public com.example.markutapp_01.RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_list,parent,false);

        Globals global = Globals.getInstance();
        User_Details user = global.getUser();

        TextView report = (TextView)view.findViewById(R.id.report_btn);
        ImageView reportLogo = (ImageView)view.findViewById(R.id.report_btn_logo);

        if(user.type.toLowerCase().equals("admin"))
        {
            report.setVisibility(View.GONE);
            reportLogo.setVisibility(View.GONE);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(messagesList.get(position).getImageTitle());
        holder.price.setText(messagesList.get(position).getPrice());
        Glide.with(mContext).load(messagesList.get(position).getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView,price,edit;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.iv_que_item);
            textView = itemView.findViewById(R.id.ad_title);
            price = itemView.findViewById(R.id.ad_price);
            edit = itemView.findViewById(R.id.editAdID);
        }
    }

}
