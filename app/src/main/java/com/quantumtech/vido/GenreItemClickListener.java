package com.quantumtech.vido;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.quantumtech.vido.model.Genre;
import com.quantumtech.vido.model.Movie;

public interface GenreItemClickListener {

    void onGenreClicked(Genre genre, Button button, TextView title);
}

