package com.quantumtech.vido.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quantumtech.vido.GenreItemClickListener;
import com.quantumtech.vido.MovieItemClickListener;
import com.quantumtech.vido.R;
import com.quantumtech.vido.activites.AllMovieActivity;
import com.quantumtech.vido.model.Genre;
import com.quantumtech.vido.model.Movie;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    private RecyclerView.RecycledViewPool viewPool;
    private Context mContext;
    private ArrayList<Genre> genreList;
    private MovieItemClickListener movieItemClickListener;
    private Genre genre;

    public GenreAdapter(Context context, ArrayList<Genre> genreList, MovieItemClickListener listener) {
        this.mContext = context;
        this.genreList = genreList;
        this.movieItemClickListener = listener;
        viewPool = new RecyclerView.RecycledViewPool();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genre_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        genre = genreList.get(position);
        ArrayList<Movie> movies = genre.getList();
        MovieAdapter movieAdapter = new MovieAdapter(mContext, movies, movieItemClickListener);
        movieAdapter.setHasStableIds(true);

        holder.genreTitle.setText(genre.getName());
        holder.moviesRecyclerView.setHasFixedSize(true);
        holder.moviesRecyclerView.setItemViewCacheSize(20);
        holder.moviesRecyclerView.setRecycledViewPool(viewPool);
        holder.moviesRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,
                RecyclerView.HORIZONTAL, false));

        holder.moviesRecyclerView.setAdapter(movieAdapter);

        holder.moviesRecyclerView.setNestedScrollingEnabled(false);

//        holder.btnMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int genreId = genre.getId();
//                Intent intent =  new Intent(mContext,  AllMovieActivity.class);
//                intent.putExtra("genre id", genreId);
//                mContext.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public void setItems(ArrayList<Genre> genres) {
        genreList = genres;
//        movieAdapter.setItems(movies);
        notifyDataSetChanged();
    }

     class ViewHolder extends RecyclerView.ViewHolder {
        private TextView btnMore;
        private TextView genreTitle;
        private RecyclerView moviesRecyclerView;

        ViewHolder(View view) {
            super(view);

            btnMore = view.findViewById(R.id.view_all);
            genreTitle = view.findViewById(R.id.genre);
            moviesRecyclerView = view.findViewById(R.id.movies_recycler);
            btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Genre genre = genreList.get(getAdapterPosition());
                    int genreId = genre.getId();
                    Intent intent =  new Intent(mContext,  AllMovieActivity.class);
                    intent.putExtra("genre id", genreId);
                    mContext.startActivity(intent);
                }
            });
        }

    }
}
