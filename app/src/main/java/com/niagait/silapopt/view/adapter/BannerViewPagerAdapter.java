package com.niagait.silapopt.view.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.bumptech.glide.Glide;
import com.niagait.silapopt.R;
import com.niagait.silapopt.model.SlideModel;

import java.util.ArrayList;


public class BannerViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<SlideModel> slideModels;

    public BannerViewPagerAdapter(Context context, ArrayList<SlideModel> slideModels) {
        this.context = context;
        this.slideModels = slideModels;
    }

    @Override
    public int getCount() {
        return slideModels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        SlideModel slideModel = slideModels.get(position);

        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_image_banner, null);
        ImageView imageView = view.findViewById(R.id.imgImageBanner);
        TextView lblJudul = view.findViewById(R.id.lblJudul);
        TextView lblDeskripsi = view.findViewById(R.id.lblDeskripsi);

        lblJudul.setText(slideModel.getJudul());
        lblDeskripsi.setText(slideModel.getCatatan());
        Glide.with(context)
                .load(slideModel.getImage())
                .centerCrop()
                .into(imageView);
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
