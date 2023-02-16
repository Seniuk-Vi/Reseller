package com.example.reseller.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reseller.Book;
import com.example.reseller.Book_User_Activity;
import com.example.reseller.Book_User_Activity_Process;
import com.example.reseller.R;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.MyViewHolder> {

    static RecyclerView myrc;
    static UserRecyclerViewAdapter myAdapter;
    private List<Book> lstBook = new ArrayList<>();
    private Context mContext;
    private List<Book> mData;
    static int counter;


    public UserRecyclerViewAdapter(Context mContext, List<Book> mData) {
        this.mContext = mContext;
        this.mData = mData;
        counter++;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_last_operation, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Book uploadCurrent = mData.get(position);

        holder.tv_book_name.setText(uploadCurrent.getName());
//        holder.tv_book_price.setText(uploadCurrent.getPrice());
        Glide.with(mContext)
                .load(uploadCurrent.getImageUrl()).centerCrop()
                .into(holder.img_book_thumbnail);
        holder.tv_book_phone.setText("Phone: " + uploadCurrent.getCustomerPhone());
        holder.tv_book_address.setText("Address: " + uploadCurrent.getCustomerLocation());
        holder.tv_book_customer.setText("Name: " + uploadCurrent.getCustomerName());
        if (uploadCurrent.isNew()) {
            holder.tv_book_new.setVisibility(View.VISIBLE);
        } else holder.tv_book_new.setVisibility(View.INVISIBLE);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadCurrent.getStatus().matches("TODO")) {
                    Intent intent = new Intent(mContext, Book_User_Activity.class);

                    // passing data to the book activity
                    intent.putExtra("Price", mData.get(position).getPrice());
                    intent.putExtra("Title", mData.get(position).getName());
                    intent.putExtra("Description", mData.get(position).getDescription());
                    intent.putExtra("Image", mData.get(position).getImageUrl());
                    intent.putExtra("Name", mData.get(position).getCustomerName());
                    intent.putExtra("Phone", mData.get(position).getCustomerPhone());
                    intent.putExtra("Location", mData.get(position).getCustomerLocation());
                    intent.putExtra("Category", mData.get(position).getCategory());
                    intent.putExtra("ItemID", mData.get(position).getThumbnail());
                    // start the activity
                    mContext.startActivity(intent);
                }
                if (uploadCurrent.getStatus().matches("PROCESSING")) {
                    Intent intent = new Intent(mContext, Book_User_Activity_Process.class);

                    // passing data to the book activity
                    intent.putExtra("Price", mData.get(position).getPrice());
                    intent.putExtra("Title", mData.get(position).getName());
                    intent.putExtra("Description", mData.get(position).getDescription());
                    intent.putExtra("Image", mData.get(position).getImageUrl());
                    intent.putExtra("Name", mData.get(position).getCustomerName());
                    intent.putExtra("Phone", mData.get(position).getCustomerPhone());
                    intent.putExtra("Location", mData.get(position).getCustomerLocation());
                    intent.putExtra("Category", mData.get(position).getCategory());
                    intent.putExtra("ItemID", mData.get(position).getThumbnail());
                    // start the activity
                    mContext.startActivity(intent);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_book_name;
        ImageView img_book_thumbnail;
        CardView cardView;
        TextView tv_book_price, tv_book_customer, tv_book_address, tv_book_phone, tv_book_new;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            tv_book_price = itemView.findViewById(R.id.card_price);
            tv_book_name = itemView.findViewById(R.id.card_heading);
            img_book_thumbnail = itemView.findViewById(R.id.card_photo);
            cardView = itemView.findViewById(R.id.cardview_storage);
            tv_book_customer = itemView.findViewById(R.id.customer);
            tv_book_address = itemView.findViewById(R.id.address);
            tv_book_phone = itemView.findViewById(R.id.telephone);
            tv_book_new = itemView.findViewById(R.id.book_new);
        }
    }

}
