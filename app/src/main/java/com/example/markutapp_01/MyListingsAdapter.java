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

		TextView edit = (TextView)view.findViewById(R.id.editAd);

		edit.setOnClickListener((View.OnClickListener) v ->
		{
			TextView id = (TextView)view.findViewById(R.id.editAdID);
			Intent intent = new Intent(mContext, EditAdvertisement.class);
			intent.putExtra("adID", id.getText().toString());
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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