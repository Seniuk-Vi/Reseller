package com.example.reseller.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.reseller.Book_Activity;
import com.example.reseller.R;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    static RecyclerView myrc;
    static RecyclerViewAdapter myAdapter;
    private List<Book> lstBook = new ArrayList<>();
    private Context mContext;
    private List<Book> mData;
    static int counter;

    public RecyclerViewAdapter(Context mContext, List<Book> mData) {
        this.mContext = mContext;
        this.mData = mData;
        counter++;
    }

    public RecyclerViewAdapter(List<Book> mData) {
        this.mData = mData;
        counter++;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_book, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Book uploadCurrent = mData.get(position);

        holder.tv_book_name.setText(uploadCurrent.getName());
        String Price =String.valueOf(uploadCurrent.getPrice());
        if(Price.length()> 4){
            String upToNCharacters = Price.substring(0, Math.min(Price.length(), 4));
            holder.tv_book_price.setText("  "+upToNCharacters + "..." + " $  ");

        }else {
            String upToNCharacters = Price;
            holder.tv_book_price.setText("  " + upToNCharacters  + " $  ");

        }
        if (uploadCurrent.getAdv().equals("yes")){
            holder.tv_book_adv.setVisibility(View.VISIBLE);
        }else holder.tv_book_adv.setVisibility(View.INVISIBLE);
        holder.tv_book_category.setText(uploadCurrent.getCategory());
        Glide.with(mContext)
                .load(uploadCurrent.getImageUrl())
                .into(holder.img_book_thumbnail);
//        holder.tv_book_name.setText(mData.get(position).getName());
//        holder.img_book_thumbnail.setImageResource(Integer.parseInt(mData.get(position).getThumbnail()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, Book_Activity.class);

                // passing data to the book activity
                intent.putExtra("Title", mData.get(position).getName());
                intent.putExtra("Description", mData.get(position).getDescription());
                intent.putExtra("Thumbnail", mData.get(position).getImageUrl());
                intent.putExtra("Price", mData.get(position).getPrice());
                intent.putExtra("UserID", mData.get(position).getUserID());
                intent.putExtra("UserName", mData.get(position).getUserName());
                intent.putExtra("UserPhoto", mData.get(position).getUserPhoto());
                intent.putExtra("itemID", mData.get(position).getThumbnail());
                intent.putExtra("category", mData.get(position).getCategory());

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

        TextView tv_book_name, tv_book_category;
        ImageView img_book_thumbnail;
        CardView cardView;
        TextView tv_book_price;
        TextView tv_book_adv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_book_adv = itemView.findViewById(R.id.book_adv);
            tv_book_price = itemView.findViewById(R.id.book_price_id);
            tv_book_name = (TextView) itemView.findViewById(R.id.book_title_id);
            img_book_thumbnail = (ImageView) itemView.findViewById(R.id.book_img_id);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
            tv_book_category = itemView.findViewById(R.id.book_category);
        }
    }

}
