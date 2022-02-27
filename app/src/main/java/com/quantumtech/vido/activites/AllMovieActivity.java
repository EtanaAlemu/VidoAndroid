package com.quantumtech.vido.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.quantumtech.vido.BuildConfig;
import com.quantumtech.vido.MovieItemClickListener;
import com.quantumtech.vido.net.MySingleton;
import com.quantumtech.vido.R;
import com.quantumtech.vido.adapter.AllMoviesAdapter;
import com.quantumtech.vido.helper.Connectivity;
import com.quantumtech.vido.model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllMovieActivity extends AppCompatActivity implements MovieItemClickListener {

    private final String TAG = "All Movie";
    String localIP = BuildConfig.IP_LOCAL_SERVER;
    private final String HOST = localIP+"/v1/vido/";
    private RequestQueue requestQueue;
    private Context mContext;
    private ArrayList<Movie> movies;
    private AllMoviesAdapter moviesAdapter;

    private RecyclerView recyclerView;
    private MenuItem mSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!isNightMode()) {
            setTheme(R.style.BaseTheme);
        } else {
            setTheme(R.style.NightTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_movie);
        mContext = this;
//        HOST = "http://" + new Connectivity(mContext).getHOST() + "/vido/";
        movies = new ArrayList<>();

        final int genreId = getIntent().getIntExtra("genre id",0);
        prepareNetwork();

        recyclerView = findViewById(R.id.all_movies_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        moviesAdapter = new AllMoviesAdapter(this, movies, this);

        Toast.makeText(this,String.valueOf(genreId),Toast.LENGTH_SHORT).show();
//        movies = getMovies(String.valueOf(genreId));
        getMovies(String.valueOf(genreId));

//        moviesAdapter.setItems(movies);
        moviesAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(moviesAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.all_movie, menu);
        mSearch = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView)mSearch.getActionView();

        return search(searchView);
    }

    private boolean search(final SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!searchView.isIconified()){
                    searchView.isIconified();
                }
                mSearch.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String charSequence) {
                moviesAdapter.getFilter().filter(charSequence);
                return false;
            }
        });
        return true;
    }

    private void prepareNetwork() {

        requestQueue = MySingleton.getInstance(this).getRequestQueue();
// Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

// Start the queue
        requestQueue.start();
    }


    private ArrayList<Movie> getMovies(final String genreId) {
        final ArrayList<Movie> movies = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOST + "get_movie.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray moviesArray = new JSONObject(response).getJSONArray("movie");
                    for (int i = 0; i < moviesArray.length(); i++) {
                        JSONObject obj = moviesArray.getJSONObject(i);
                        Movie movie = new Movie();

                        movie.setId(obj.getInt("id"));
                        movie.setRate(Float.parseFloat(String.valueOf(obj.getDouble("rate"))));
                        movie.setTitle(obj.getString("title"));
                        movie.setLocation(obj.getString("location"));
                        movie.setThumbnailUrl(HOST + obj.getString("thumbnail"));

                        movies.add(movie);

                    }
                    moviesAdapter.setItems(movies);
                } catch (Exception e) {
                    if (AllMovieActivity.this.getApplicationContext() != null)
                        Toast.makeText(AllMovieActivity.this.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (getApplicationContext() != null)
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("genre_id", genreId);
                return params;
            }
        };

        requestQueue.add(stringRequest);
        return movies;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    private boolean isNightMode() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Night Mode", Context.MODE_PRIVATE);
        boolean isNightMode = preferences.getBoolean("ON", false);

        if (isNightMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        return isNightMode;
    }

    @Override
    public void onMovieClicked(Movie movie, ImageView imageView, TextView title) {

        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("imageURL",movie.getThumbnailUrl());
        intent.putExtra("title",movie.getTitle());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, imageView, "sharedName");
            startActivity(intent,options.toBundle());
        } else {
//            ActivityTransitionLauncher.with(MainActivity.this).from(image).launch(intent);
            startActivity(intent);
        }
    }
}
