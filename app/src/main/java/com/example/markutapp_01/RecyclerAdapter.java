package com.example.markutapp_01;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RecyclerAdapter extends RecyclerView.Adapter<com.example.markutapp_01.RecyclerAdapter.ViewHolder> {

    private static final String Tag="RecyclerView";
    private Context mContext;
    private ArrayList<Messages> messagesList;
    private DatabaseReference myRef;
    ImageButton report_btn_logo;
    ViewHolder holder;
    CardView cardView;
    Boolean reportChecker = false;

    Globals global = null;

    Map<String, String> adImages = new HashMap<String, String>();

    public RecyclerAdapter(Context mContext, ArrayList<Messages> messagesList) {
        super();
        this.mContext = mContext;
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public com.example.markutapp_01.RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_list,parent,false);

        global = Globals.getInstance();

        Button deactivate = (Button)view.findViewById(R.id.admin_deactivate_btn);

        if(!global.getUser().type.toLowerCase().equals("admin"))
        {
            deactivate.setVisibility(View.GONE);
        }

        deactivate.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                TextView adid = (TextView)view.findViewById(R.id.adID);

                myRef.orderByChild("ad_id").equalTo(adid.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot)
                    {
                        for(DataSnapshot snapshot : datasnapshot.getChildren())
                        {
                            snapshot.child("is_complete").getRef().setValue(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
            }
        });

        TextView report = (TextView)view.findViewById(R.id.report_btn);
        ImageView reportLogo = (ImageView)view.findViewById(R.id.report_btn_logo);
       // ImageView reportLogo = (ImageView)view.findViewById(R.id.report_btn_logo);
        report_btn_logo= view.findViewById(R.id.report_btn_logo);

        report_btn_logo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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
            intent.putExtra("imageURL", adImages.get(id.getText().toString()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });


        return new ViewHolder(view);

    }


    public void updateReportFlagToDB(String adID){
        myRef.orderByChild("ad_id").equalTo(adID).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot)
            {
                for (DataSnapshot snapshot : datasnapshot.getChildren())
                {
                    if (!Boolean.parseBoolean(snapshot.child("under_report").getValue().toString()))
                    {
                        System.out.println(snapshot.child("ad_id").getValue().toString()+":"+ snapshot.child("under_report").getValue());
                        snapshot.getRef().child("under_report").setValue(true);

                        report_btn_logo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_flag_24));
                        //snapshot.getRef().child("under_report").setValue("true");
                    }

                    else
                    {
                        if(global.getUser().type.toLowerCase().equals("admin"))
                        {
                            snapshot.getRef().child("under_report").setValue(false);
                            report_btn_logo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_outlined_flag_24));
                            //snapshot.getRef().child("under_report").setValue("false");
                        }

                        else
                        {
                            Toast.makeText(mContext, "This advertisement has already been flagged and is currently being reviewed by the administrators. Once it has been reviewed it will either be unflagged or removed.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        final  String postkey = messagesList.get(position).getAdID();
        System.out.println(postkey);

        holder.reportChecker(postkey);


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
//
//                                /// update
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

 //           }
  //      });

        holder.textView.setText(messagesList.get(position).getImageTitle());
        holder.price.setText(messagesList.get(position).getPrice());
        holder.adId.setText(messagesList.get(position).getAdID());
        String newDate = null;
        try
        {
            newDate = convertDateFormat(messagesList.get(position).getDate());
        } catch(ParseException e)
        {
            e.printStackTrace();
        }
        holder.date.setText(newDate);
        Glide.with(mContext).load(messagesList.get(position).getImageUrl()).into(holder.imageView);

        adImages.put(messagesList.get(position).getAdID(), messagesList.get(position).getImageUrl());



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

    public String convertDateFormat(String date) throws ParseException
    {
        String newDate = "";

        DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

        Date inputDate = inputFormat.parse(date);

        newDate = outputFormat.format(inputDate);

        return newDate;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView,price,edit,date,report;
        ImageButton report_btn_logo;
        TextView adId;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.iv_que_item);
            textView = itemView.findViewById(R.id.ad_title);
            price = itemView.findViewById(R.id.ad_price);
            edit = itemView.findViewById(R.id.editAdID);
            adId = itemView.findViewById(R.id.adID);
            report_btn_logo = itemView.findViewById(R.id.report_btn_logo);
            report = itemView.findViewById(R.id.report_btn);
            date = itemView.findViewById(R.id.view_dashboard_date);

        }



        public void  reportChecker(final String postkey) {
            //report_btn_logo = imageView.findViewById(R.id.report_btn_logo);
            myRef= FirebaseDatabase.getInstance().getReference("Advertisements");


            //messagesList.get(postkey).getImageTitle();

            myRef.orderByChild("ad_id").equalTo(postkey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                   for (DataSnapshot snapshot : datasnapshot.getChildren())
                   {
                       if(snapshot.child("advertiser").getValue().toString().equals(global.getUser().email_id))
                       {
                           report_btn_logo.setVisibility(View.GONE);
                           report.setVisibility(View.GONE);
                       }
                       else
                       {
                           if(Boolean.parseBoolean(snapshot.child("under_report").getValue().toString()))
                           {
                               System.out.println("sdfghgfdsdfghgfdsdfghjhgfdsdfghjhgfds");
                               report_btn_logo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_flag_24));
                           }
                           else
                           {
                               report_btn_logo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_outlined_flag_24));
                           }
                       }
                    }
               }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }



    }








}
