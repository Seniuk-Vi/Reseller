package com.example.reseller.Adapters;

import android.app.Activity;
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
import com.example.reseller.BookMyStorage;
import com.example.reseller.Book_Activity;
import com.example.reseller.R;

import java.util.ArrayList;
import java.util.List;

public class MyStorageRecyclerViewAdapter extends RecyclerView.Adapter<MyStorageRecyclerViewAdapter.MyViewHolder> {

    static RecyclerView myrc;
    static MyStorageRecyclerViewAdapter myAdapter;
    private List<Book> lstBook = new ArrayList<>();
    private Context mContext;
    private List<Book> mData;
    static int counter;

    public MyStorageRecyclerViewAdapter(Context mContext, List<Book> mData) {
        this.mContext = mContext;
        this.mData = mData;
        counter++;
    }

    public MyStorageRecyclerViewAdapter(List<Book> mData) {
        this.mData = mData;
        counter++;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_storage, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Book uploadCurrent = mData.get(position);

        holder.tv_book_name.setText(uploadCurrent.getName());
        holder.tv_book_price.setText(String.valueOf("Price: "+uploadCurrent.getPrice()+" $"));
        holder.tv_book_orders.setText("Orders: " + uploadCurrent.getItemOrders());
        holder.tv_book_views.setText("Views: " + uploadCurrent.getItemViews());
        holder.tv_book_category.setText(uploadCurrent.getCategory());
        Glide.with(mContext)
                .load(uploadCurrent.getImageUrl()).fitCenter().centerCrop()
                .into(holder.img_book_thumbnail);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, BookMyStorage.class);

                // passing data to the book activity
                intent.putExtra("Title", mData.get(position).getName());
                intent.putExtra("Description", mData.get(position).getDescription());
                intent.putExtra("Image", mData.get(position).getImageUrl());
                intent.putExtra("Price", mData.get(position).getPrice());
                intent.putExtra("Category" , mData.get(position).getCategory());
                intent.putExtra("ItemID", mData.get(position).getThumbnail());
                intent.putExtra("Privacy", mData.get(position).isPrivacy());

                // start the activity
                Activity activity = (Activity) mContext;
                mContext.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_right_animation, R.anim.slide_out_left);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_book_name, tv_book_category, tv_book_orders, tv_book_views;
        ImageView img_book_thumbnail;
        CardView cardView;
        TextView tv_book_price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_book_price = itemView.findViewById(R.id.card_price);
            tv_book_name = (TextView) itemView.findViewById(R.id.card_heading);
            img_book_thumbnail = (ImageView) itemView.findViewById(R.id.card_photo);
            cardView = (CardView) itemView.findViewById(R.id.cardview_storage);
            tv_book_category = itemView.findViewById(R.id.card_category);
            tv_book_orders = itemView.findViewById(R.id.card_orders);
            tv_book_views = itemView.findViewById(R.id.catd_views);

        }
    }

}
