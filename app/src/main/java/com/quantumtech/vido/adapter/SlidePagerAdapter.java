package com.quantumtech.vido.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.quantumtech.vido.R;
import com.quantumtech.vido.model.Slide;

import java.util.List;

public class SlidePagerAdapter extends PagerAdapter {

    Context mContext;
    List<Slide> mList;

    public SlidePagerAdapter(Context mContext, List<Slide> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_item, null);

        ImageView imageView = view.findViewById(R.id.imageView);
        ImageView imageView2 = view.findViewById(R.id.imageView2);
        TextView title = view.findViewById(R.id.slide_title);
        TextView description = view.findViewById(R.id.slide_description);
        TextView rate = view.findViewById(R.id.rate);
        LinearLayout layout = view.findViewById(R.id.linearLayout);

        Glide.with(mContext).load(mList.get(position).getImage())
                .thumbnail(0.5f)
                .transition(new DrawableTransitionOptions().crossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        Glide.with(mContext).load(mList.get(position).getImage())
                .thumbnail(0.5f)
                .transition(new DrawableTransitionOptions().crossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView2);

        title.setText(mList.get(position).getTitle());
        description.setText(mList.get(position).getDescription());
        rate.setText(String.valueOf(mList.get(position).getRate()));
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            layout.setBackgroundResource(R.drawable.gradiant_bg_dark);
        } else {
            layout.setBackgroundResource(R.drawable.gradiant_bg);
        }
        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
