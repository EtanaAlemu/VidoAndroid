package com.quantumtech.vido;

import android.widget.ImageView;
import android.widget.TextView;

import com.quantumtech.vido.model.Movie;

public interface MovieItemClickListener {
    void onMovieClicked(Movie movie, ImageView imageView, TextView title);
}
