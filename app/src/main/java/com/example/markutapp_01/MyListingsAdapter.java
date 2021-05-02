package com.example.markutapp_01;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyListingsAdapter extends RecyclerAdapter
{
	private static final String Tag = "RecyclerView";
	private Context mContext;
	private ArrayList<Messages> messagesList;

	Map<String, String> adImages = new HashMap<String, String>();

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
		TextView id = (TextView)view.findViewById(R.id.editAdID);

		edit.setOnClickListener((View.OnClickListener) v ->
		{
			Intent intent = new Intent(mContext, PostAd.class);
			intent.putExtra("adID", id.getText().toString());
			intent.putExtra("editAd", true);
			intent.putExtra("imageURL", adImages.get(id.getText().toString()));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
		});

		view.setOnClickListener((View.OnClickListener) v ->
		{
			Intent intent = new Intent(mContext, ViewAdvertisement.class);
			intent.putExtra("adID", id.getText().toString());
			intent.putExtra("imageURL", adImages.get(id.getText().toString()));
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
		adImages.put(messagesList.get(position).getAdID(), messagesList.get(position).getImageUrl());
		String newDate = "";
		try
		{
			newDate = convertDateFormat(messagesList.get(position).getDate());
		} catch(ParseException e)
		{
			e.printStackTrace();
		}
		holder.date.setText(newDate);
	}


	@Override
	public int getItemCount()
	{
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
}