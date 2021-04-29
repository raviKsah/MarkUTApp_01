package com.example.markutapp_01;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyListingsAdapter extends RecyclerAdapter
{
	private static final String Tag = "RecyclerView";
	private Context mContext;
	private ArrayList<Messages> messagesList;

	public MyListingsAdapter(Context mContext, ArrayList<Messages> messagesList)
	{
		super(mContext, messagesList);
		this.mContext = mContext;
		this.messagesList = messagesList;
	}

	@NonNull
	@Override
	public com.example.markutapp_01.RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_listings, parent, false);

		view.setOnClickListener((View.OnClickListener) v ->
		{
			TextView a = (TextView)v.findViewById(R.id.editAdID);
			System.out.println(a.getText().toString());
			Intent intent = new Intent(mContext, EditAdvertisement.class);
			intent.putExtra("adID", a.getText().toString());
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			mContext.startActivity(intent);
		});

		return new RecyclerAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position)
	{
		holder.textView.setText(messagesList.get(position).getImageTitle());
		holder.price.setText(messagesList.get(position).getPrice());
		holder.edit.setText(messagesList.get(position).getAdID());
		Glide.with(mContext).load(messagesList.get(position).getImageUrl()).into(holder.imageView);
	}

	@Override
	public int getItemCount()
	{
		return messagesList.size();
	}
}