package com.quantumtech.vido.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.quantumtech.vido.R;
import com.quantumtech.vido.model.IntroScreen;

import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {

    Context mContext;
    List<IntroScreen> screenList;

    public IntroViewPagerAdapter(Context mContext, List<IntroScreen> screenList) {
        this.mContext = mContext;
        this.screenList = screenList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.intro_layout_screen, null);

        ImageView introImageView = layoutScreen.findViewById(R.id.intro_img);
        TextView introTitle = layoutScreen.findViewById(R.id.intro_title);
        TextView introDescription = layoutScreen.findViewById(R.id.intro_description);


        Glide.with(mContext).load(screenList.get(position).getScreenImg())
                .thumbnail(0.5f)
                .transition(new DrawableTransitionOptions().crossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(introImageView);
        introTitle.setText(screenList.get(position).getTitle());
        introDescription.setText(screenList.get(position).getDescription());

        container.addView(layoutScreen);
        return layoutScreen;
    }

    @Override
    public int getCount() {
        return screenList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
