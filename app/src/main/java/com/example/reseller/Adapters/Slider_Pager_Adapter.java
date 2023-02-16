package com.example.reseller.Adapters;

import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.example.reseller.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Slider_Pager_Adapter extends PagerAdapter {
    Context context;
    private String[] imageUrls;
    private LayoutInflater layoutInflater;

    public Slider_Pager_Adapter(Context context, String[] imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View view = layoutInflater.inflate(R.layout.slider_home_layout, container, false);
//
//        ImageView im_slider = view.findViewById(R.id.im_slider);
//        Picasso.with(context).load()
//                .placeholder(R.drawable.image_uploading)
//                .error(R.drawable.image_not_found).into(imageView);
//
//        container.addView(view);

        ImageView imageView = new ImageView(context);
        Picasso.with(context).load(imageUrls[position])
                .fit()
                .centerCrop()

                .into(imageView);
        container.addView(imageView);

        return imageView;
    }

    @Override
    public int getCount() {
        return imageUrls.length;
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        View view = (View) object;
//        container.removeView(view);
        container.removeView((View) object);
    }

}