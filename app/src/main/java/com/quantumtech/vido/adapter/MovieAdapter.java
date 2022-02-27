package com.quantumtech.vido.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.quantumtech.vido.MovieItemClickListener;
import com.quantumtech.vido.R;
import com.quantumtech.vido.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Movie> movieList;
    private MovieItemClickListener listener;
    private RequestOptions options;

    public MovieAdapter(Context context, ArrayList<Movie> movieList, MovieItemClickListener listener) {

        this.mContext = context;
        this.movieList = movieList;
        this.listener = listener;

        options = new RequestOptions().centerCrop().placeholder(R.drawable.gradiant_bg)
                .error(R.drawable.gradiant_bg_dark);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Movie movie = movieList.get(position);

        Glide.with(mContext).load(movie.getThumbnailUrl())
                .apply(options)
                .thumbnail(0.5f)
                .transition(new DrawableTransitionOptions().crossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        holder.title.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView title;
        private TextView rank;
        private TextView rate;

        ViewHolder(View view) {
            super(view);

            rank = view.findViewById(R.id.rank);
            rate = view.findViewById(R.id.rate);
            imageView = view.findViewById(R.id.imageGalleryView);
            title = view.findViewById(R.id.image_title);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMovieClicked(movieList.get(getAdapterPosition()),imageView,title);
                }
            });
        }

    }
}
