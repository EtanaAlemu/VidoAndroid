package com.quantumtech.vido.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.quantumtech.vido.MovieItemClickListener;
import com.quantumtech.vido.R;
import com.quantumtech.vido.helper.Connectivity;
import com.quantumtech.vido.helper.DBHelper;
import com.quantumtech.vido.model.Movie;
import com.quantumtech.vido.net.MySingleton;

import java.util.ArrayList;
import java.util.HashMap;

public class AllMoviesAdapter extends RecyclerView.Adapter<AllMoviesAdapter.ViewHolder> implements Filterable {

    private static String HOST;
    private final RequestOptions options;
    private final Context mContext;
    private ArrayList<Movie> movies;
    private final MovieItemClickListener listener;

    public AllMoviesAdapter(Context mContext, ArrayList<Movie> mList, MovieItemClickListener listener) {
        this.mContext = mContext;
        this.movies = mList;
        this.listener = listener;

        options = new RequestOptions().centerCrop().placeholder(R.drawable.gradiant_bg)
                .error(R.drawable.gradiant_bg_dark);
        
        HOST = "http://" + new Connectivity(mContext).getHOST() + "/vido/";
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.title.setText(movie.getTitle());

        Glide.with(mContext).load(movie.getThumbnailUrl())
                .apply(options)
                .thumbnail(0.5f)
                .transition(new DrawableTransitionOptions().crossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);
        holder.rate.setRating(movie.getRate() / 2);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setItems(ArrayList<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                ArrayList<Movie> itemListFiltered;

                if (charString.isEmpty()) {
                    itemListFiltered = movies;
                } else {
                    ArrayList<Movie> filteredList = new ArrayList<>();
                    for (Movie row : movies) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    itemListFiltered = filteredList;
                }

                FilterResults results = new FilterResults();
                results.values = itemListFiltered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                movies = (ArrayList<Movie>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView title;
        public RatingBar rate;
        public TextView menu;

        ViewHolder(@NonNull View view) {
            super(view);

            thumbnail = view.findViewById(R.id.thumbnail);
            title = view.findViewById(R.id.movie_title);
            rate = view.findViewById(R.id.rate);
            menu = view.findViewById(R.id.menu);
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu menu = new PopupMenu(mContext, view);
                    menu.inflate(R.menu.all_movie_item);
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @SuppressLint("NonConstantResourceId")
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.download:
                                    addToDownload(movies.get(getAdapterPosition()));
                                    return true;
                                case R.id.favorite:
                                    addToFavorite(movies.get(getAdapterPosition()));
                                    return true;
                            }
                            return false;
                        }
                    });

                    menu.show();
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMovieClicked(movies.get(getAdapterPosition()), thumbnail, title);
                }
            });
        }
    }

    private void addToFavorite(Movie movie) {
        new DBHelper(mContext).insert(movie.getId());
    }

    private void addToDownload(final Movie movie) {

        RequestQueue requestQueue = MySingleton.getInstance(mContext).getRequestQueue();
        requestQueue.start();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOST + "batch/run_batch.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("source", movie.getLocation());
                params.put("target", "E:\\");

//                Toast.makeText(mContext, params.get("target"), Toast.LENGTH_LONG).show();
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
