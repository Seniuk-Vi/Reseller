package com.example.reseller.Adapters;

import android.content.Context;
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
import com.example.reseller.Dialogs.DialogPopUpViewPick;
import com.example.reseller.R;

import java.util.ArrayList;
import java.util.List;

public class PickerRecyclerVewAdapter extends RecyclerView.Adapter<PickerRecyclerVewAdapter.MyViewHolder> {

    static RecyclerView myrc;
    static RecyclerViewAdapter myAdapter;
    private List<Book> lstBook = new ArrayList<>();
    private Context mContext;
    private List<Book> mData;
    static int counter;
    private DialogPopUpViewPick.Click click;

    public PickerRecyclerVewAdapter(Context mContext, List<Book> mData, DialogPopUpViewPick.Click click) {
        this.mContext = mContext;
        this.mData = mData;
        counter++;
        this.click = click;
    }


    @NonNull
    @Override
    public PickerRecyclerVewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_picker, parent, false);

        return new PickerRecyclerVewAdapter.MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(PickerRecyclerVewAdapter.MyViewHolder holder, final int position) {
        Book uploadCurrent = mData.get(position);

        holder.tv_book_name_picker.setText(uploadCurrent.getName());
//        holder.tv_book_price.setText(uploadCurrent.getPrice());
        Glide.with(mContext)
                .load(uploadCurrent.getImageUrl())
                .into(holder.img_book_thumbnail_picker);

                // Передача в DialogPick
        holder.tv_book_category_picker.setText(uploadCurrent.getCategory());
                holder.cardView_picker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogPopUpViewPick.photo2 = mData.get(position).getImageUrl();
                        DialogPopUpViewPick.name2 = mData.get(position).getName();
                        DialogPopUpViewPick.category2 = mData.get(position).getCategory();
                        DialogPopUpViewPick.description2 = mData.get(position).getDescription();
                        DialogPopUpViewPick.price2 = mData.get(position).getPrice();
                        DialogPopUpViewPick.userID2 = mData.get(position).getUserID();
                        DialogPopUpViewPick.itemID2 = mData.get(position).getThumbnail();
                        click.onClick();
                    }
                });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_book_name_picker;
        ImageView img_book_thumbnail_picker;
        CardView cardView_picker;
        TextView tv_book_category_picker;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_book_category_picker = itemView.findViewById(R.id.book_category_id_picker);
            tv_book_name_picker = (TextView) itemView.findViewById(R.id.book_title_id_picker);
            img_book_thumbnail_picker = (ImageView) itemView.findViewById(R.id.book_img_id_picker);
            cardView_picker = (CardView) itemView.findViewById(R.id.cardview_id_picker);

        }
    }

}