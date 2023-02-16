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
import com.example.reseller.Book_Activity;
import com.example.reseller.Book_User_Activity;
import com.example.reseller.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {
//    static RecyclerView myrc;
//    static UserRecyclerViewAdapter myAdapter;
//    private List<Book> lstBook = new ArrayList<>();
    private Context mContext;
    private List<Book> mData;
    static int counter;


    public BookAdapter(Context mContext, List<Book> mData) {
        this.mContext = mContext;
        this.mData = mData;
        counter++;
    }

    public BookAdapter( List<Book> mData) {
        this.mData = mData;
    }


    @NonNull
    @Override
    public BookAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_book, parent, false);

        return new BookAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookAdapter.MyViewHolder holder, final int position) {
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
        holder.tv_book_cat.setText(uploadCurrent.getCategory());
//        Picasso.with(mContext)
//                .load(uploadCurrent.getImageUrl())
//
//                .placeholder(R.mipmap.ic_launcher)
//                .centerCrop()
//                .fit()
//                .into(holder.img_book_thumbnail);
        Glide.with(mContext).load(uploadCurrent.getImageUrl()).into(holder.img_book_thumbnail);
//                Picasso.with(mContext)
//                .load(uploadCurrent.getImageUrl())
//                .fit()
//                .placeholder(R.mipmap.ic_launcher)
//                .centerCrop()
//                .fit()
//                .into(holder.img_book_thumbnail);
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
                // start the activity
                mContext.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_book_name, tv_book_cat;
        ImageView img_book_thumbnail;
        CardView cardView;
        TextView tv_book_price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_book_price = itemView.findViewById(R.id.book_price_id_book);
            tv_book_name = itemView.findViewById(R.id.book_title_id_book);
            img_book_thumbnail =  itemView.findViewById(R.id.book_img_id_book);
            cardView =  itemView.findViewById(R.id.cardview_id_book);
            tv_book_cat = itemView.findViewById(R.id.book_category_book);
        }
    }

}
