package com.example.markutapp_01;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
    CardView cardView;
    Boolean reportChecker = false;

    public RecyclerAdapter(Context mContext, ArrayList<Messages> messagesList) {
        this.mContext = mContext;
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public com.example.markutapp_01.RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_list,parent,false);

        report_btn_logo=view.findViewById(R.id.report_btn_logo);

        report_btn_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView adid = (TextView)view.findViewById(R.id.adID);

                updateReportFlagToDB(adid.getText().toString());


            }
        });

        CardView cardView = (CardView) view.findViewById(R.id.view_dashboards_cards);

        cardView.setOnClickListener((View.OnClickListener) v ->
        {
            TextView id = (TextView)view.findViewById(R.id.adID);
            Intent intent = new Intent(mContext, ViewAdvertisement.class);
            intent.putExtra("adID", id.getText().toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });


        return new ViewHolder(view);

    }


    public void updateReportFlagToDB(String adID){
        myRef.orderByChild("ad_id").equalTo(adID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    if (snapshot.child("under_report").getValue().equals(false)) {
                        // System.out.println(snapshot.child("ad_id").getValue().toString()+":"+ snapshot.child("under_report").getValue());
                        snapshot.getRef().child("under_report").setValue(true);

                        report_btn_logo.setImageResource(R.drawable.ic_baseline_flag_24);
                        //snapshot.getRef().child("under_report").setValue("true");
                    } else {
                        snapshot.getRef().child("under_report").setValue(false);
                        report_btn_logo.setImageResource(R.drawable.ic_baseline_outlined_flag_24);
                        //snapshot.getRef().child("under_report").setValue("false");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        final  String postkey = messagesList.get(position).getAdID();

        holder.reportChecker(postkey);


        holder.report_btn_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reportChecker = true;

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (reportChecker.equals(true)){
                            if (snapshot.child(postkey).hasChild("")){

                                /// update

                                reportChecker = false;
                            }else {

                                //myRef.child(postkey).setValue(member);
                                reportChecker = false;

                                Toast.makeText(mContext, "Advertisement has been reported and sent for review", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        holder.textView.setText(messagesList.get(position).getImageTitle());
        holder.price.setText(messagesList.get(position).getPrice());
        Glide.with(mContext).load(messagesList.get(position).getImageUrl()).into(holder.imageView);



        holder.adId.setText(messagesList.get(position).getAdID());

//        holder.adId.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(mContext, RegisterActivity.class);
//                mContext.startActivity();
//            }
//        });


    }




    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView,price,edit;
        ImageButton report_btn_logo;
        TextView adId;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.iv_que_item);
            textView = itemView.findViewById(R.id.ad_title);
            price = itemView.findViewById(R.id.ad_price);
            edit = itemView.findViewById(R.id.editAdID);
            adId = itemView.findViewById(R.id.adID);
        }



        public void  reportChecker(final String postkey) {
            report_btn_logo = itemView.findViewById(R.id.report_btn_logo);
            myRef= FirebaseDatabase.getInstance().getReference("Advertisements");


            //messagesList.get(postkey).getImageTitle();

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.child(postkey).hasChild("under_report")){
                        report_btn_logo.setImageResource(R.drawable.ic_baseline_flag_24);
                    }else {
                        report_btn_logo.setImageResource(R.drawable.ic_baseline_outlined_flag_24);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }



    }








}
