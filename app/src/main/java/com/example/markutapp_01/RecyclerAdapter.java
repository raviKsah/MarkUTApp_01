package com.example.markutapp_01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<com.example.markutapp_01.RecyclerAdapter.ViewHolder> {

    private static final String Tag="RecyclerView";
    private Context mContext;
    private ArrayList<Messages> messagesList;
    private DatabaseReference myRef;
    ImageButton report_btn_logo;
    ViewHolder holder;
    Boolean reportChecker = false;

    public RecyclerAdapter(Context mContext, ArrayList<Messages> messagesList) {
        this.mContext = mContext;
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public com.example.markutapp_01.RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        final  String postkey = messagesList.get(position).getAdID();

//        holder.reportChecker(postkey);
//        holder.report_btn_logo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                reportChecker = true;
//
//                myRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                        if (reportChecker.equals(true)){
//                            if (snapshot.child(postkey).hasChild("")){
//                                myRef.child(postkey).child("").removeValue();
//
//                                reportChecker = false;
//                            }else {
//
//                                //myRef.child(postkey).setValue(member);
//                                reportChecker = false;
//
//                                Toast.makeText(mContext, "Advertisement has been reported and sent for review", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//            }
//        });

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
        ImageButton report_btn_logo;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.iv_que_item);
            textView = itemView.findViewById(R.id.ad_title);
            price = itemView.findViewById(R.id.ad_price);
            edit = itemView.findViewById(R.id.editAdID);
        }



//        public void  reportChecker(final String postkey) {
//            report_btn_logo = itemView.findViewById(R.id.report_btn_logo);
//            myRef= FirebaseDatabase.getInstance().getReference("Advertisements");
//
//
//            //messagesList.get(postkey).getImageTitle();
//
//            myRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                    if (snapshot.child(postkey).hasChild("under_report")){
//                        report_btn_logo.setImageResource(R.drawable.ic_baseline_flag_24);
//                    }else {
//                        report_btn_logo.setImageResource(R.drawable.ic_baseline_outlined_flag_24);
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//        }



    }








}
