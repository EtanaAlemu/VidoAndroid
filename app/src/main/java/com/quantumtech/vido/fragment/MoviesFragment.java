package com.quantumtech.vido.fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.material.tabs.TabLayout;
import com.quantumtech.vido.GenreItemClickListener;
import com.quantumtech.vido.MovieItemClickListener;
import com.quantumtech.vido.net.MySingleton;
import com.quantumtech.vido.R;
import com.quantumtech.vido.activites.MovieDetailActivity;
import com.quantumtech.vido.adapter.GenreAdapter;
import com.quantumtech.vido.adapter.MovieAdapter;
import com.quantumtech.vido.adapter.SlidePagerAdapter;
import com.quantumtech.vido.helper.Connectivity;
import com.quantumtech.vido.model.Genre;
import com.quantumtech.vido.model.Movie;
import com.quantumtech.vido.model.Slide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.quantumtech.vido.helper.VolleyErrorHelper.getMessage;

public class MoviesFragment extends Fragment implements GenreItemClickListener, MovieItemClickListener {

    private static final String TAG = "MyTag";
    private String HOST ;
    private List<Slide> list;
    private SlidePagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout indicator;
    private RecyclerView mGenreRecyclerView;
    private GenreAdapter genreAdapter;
    private MovieAdapter movieAdapter;
    private ArrayList<Genre> genres;
    private ArrayList<Movie> movies;
    private ArrayList<Movie> BoxOfficeMovies;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private Context mContext;
    private Genre boxOffice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = MySingleton.getInstance(getContext()).getRequestQueue();
// Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

// Start the queue
        requestQueue.start();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        viewPager = view.findViewById(R.id.slide_pager);
        indicator = view.findViewById(R.id.indicator);
        mGenreRecyclerView = view.findViewById(R.id.genres_recycler);
        HOST = "http://" + new Connectivity(mContext).getHOST() + "/v1/movie/";

//        mDataSource.retrieveData(new DataCallback(){
//           public void dataRetrieved(ArrayList<Genre> genres){
//               mGenreRecyclerView.setAdapter(new GenreAdapter(mContext,genres,null));
//           }
//        });

        ////////// Slider //////////
        list = new ArrayList<>();
        list.add(new Slide(R.drawable.terminator, "Terminator ", "Dark Fate", 6.5f));
        list.add(new Slide(R.drawable.countdown, "Countdown", "Death? there's an app for that", 5.3f));
        list.add(new Slide(R.drawable.last_christmas, "Last Christmas", "sometime you gotta have faith", 6.6f));
        list.add(new Slide(R.drawable.joker, "Joker", "have nice life God blase you", 8.8f));

        adapter = new SlidePagerAdapter(mContext, list);
        viewPager.setAdapter(adapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SlideTimer(), 4000, 6000);

        indicator.setupWithViewPager(viewPager, true);


        //////// Genre List //////////
        mGenreRecyclerView.setHasFixedSize(true);
        mGenreRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false));
        genres = new ArrayList<>();
        movies = new ArrayList<>();
        BoxOfficeMovies = new ArrayList<>();

        boxOffice = new Genre();
        boxOffice.setName("Box Office");
        boxOffice.setBoxOffice(true);
        setBoxOffice();

        setGenresData();

    }

    private File getCacheDir() {
        return Environment.getDataDirectory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        genreAdapter = new GenreAdapter(getContext(), genres, this);
        mGenreRecyclerView.setAdapter(genreAdapter);

        movieAdapter = new MovieAdapter(getContext(), movies, this);

    }

    private void setBoxOffice() {

        StringRequest genreStringRequest = new StringRequest(Request.Method.POST, HOST + "box_office.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray boxOfficeArray = object.getJSONArray("movie");

                    for (int i = 0; i < boxOfficeArray.length(); i++) {
                        Movie movie = new Movie();
                        JSONObject obj = boxOfficeArray.getJSONObject(i);
                        movie.setBoxOfficeItem(true);
                        movie.setRate(Float.parseFloat(String.valueOf(obj.getDouble("rate"))));
                        movie.setId(obj.getInt("movie_id"));
                        movie.setTitle(obj.getString("title"));
                        movie.setThumbnailUrl(HOST + obj.getString("thumbnail"));
                        movie.setRank(obj.getInt("rank"));
                        BoxOfficeMovies.add(movie);
                    }

                    boxOffice.setList(BoxOfficeMovies);
                    genres.add(boxOffice);
//                    genreAdapter.setItems(genres);
                } catch (Exception e) {
                    if (MoviesFragment.this.getContext() != null)
//                        Toast.makeText(MoviesFragment.this.getContext(), getMessage(e,mContext), Toast.LENGTH_LONG).show();
                        Toast.makeText(MoviesFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (MoviesFragment.this.getContext() != null)
//                    Toast.makeText(MoviesFragment.this.getContext(), getMessage(error,mContext), Toast.LENGTH_LONG).show();
                    Toast.makeText(MoviesFragment.this.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        genreStringRequest.setTag(TAG);

        requestQueue.add(genreStringRequest);
    }

    private void setGenresData() {
        StringRequest genreStringRequest = new StringRequest(Request.Method.POST, HOST + "join.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                try {
//                    JSONArray genresArray = new JSONArray(response);
//                    for (int j = 0; j < genresArray.length(); ++j) {
//                        Genre genre = new Genre();
//
//                        JSONObject object = genresArray.getJSONObject(j);
//                        genre.setId(object.getInt("id"));
//                        genre.setName(object.getString("genre"));
//                        JSONArray moviesArray = object.getJSONArray("movies");
//
//                        movies = new ArrayList<>();
//
//                        for (int i = 0; i < moviesArray.length(); ++i) {
//                            Movie movie = new Movie();
//                            JSONObject obj = moviesArray.getJSONObject(i);
//                            movie.setRate(Float.parseFloat(String.valueOf(obj.getDouble("rate"))));
//                            movie.setId(obj.getInt("id"));
//                            movie.setTitle(obj.getString("title"));
//                            movie.setThumbnailUrl(HOST + obj.getString("thumbnail"));
//                            movies.add(movie);
//                        }
//                        genre.setList(movies);
//                        genres.add(genre);
//                        genreAdapter.setItems(genres);
//                    }
//
//                } catch (Exception e) {
                try {
                    JSONArray genresArray = new JSONArray(response);
                    for (int j = 0; j < genresArray.length(); ++j) {
                        Genre genre = new Genre();

                        JSONObject object = (JSONObject) genresArray.get(j);
                        genre.setId(object.getInt("id"));
                        genre.setName(object.getString("genre"));
                        JSONArray moviesArray = object.getJSONArray("movies");

                        movies = new ArrayList<>();

                        for (int i = 0; i < moviesArray.length(); ++i) {
                            Movie movie = new Movie();
                            JSONObject obj = moviesArray.getJSONObject(i);
                            movie.setRate(Float.parseFloat(String.valueOf(obj.getDouble("rate"))));
                            movie.setId(obj.getInt("id"));
                            movie.setTitle(obj.getString("title"));
                            movie.setThumbnailUrl(HOST + obj.getString("thumbnail"));
                            movies.add(movie);
                        }
                        genre.setList(movies);
                        genres.add(genre);
                        genreAdapter.setItems(genres);
                    }

                } catch (Exception e) {
                    if (MoviesFragment.this.getContext() != null)
//                        Toast.makeText(MoviesFragment.this.getContext(), getMessage(e,mContext), Toast.LENGTH_LONG).show();
                        Toast.makeText(MoviesFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (MoviesFragment.this.getContext() != null)
//                    Toast.makeText(MoviesFragment.this.getContext(), getMessage(error,mContext), Toast.LENGTH_LONG).show();
                    Toast.makeText(MoviesFragment.this.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        genreStringRequest.setTag(TAG);

        requestQueue.add(genreStringRequest);
    }

    private ArrayList<Movie> getMovies(final String genreId) {
        final ArrayList<Movie> movies = new ArrayList<>();
//        final Movie movie = new Movie();
        stringRequest = new StringRequest(Request.Method.POST, HOST + "get_movie.php", new Response.Listener<String>() {
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
                        movie.setThumbnailUrl(HOST + obj.getString("thumbnail"));

                        movies.add(movie);

                    }
                } catch (Exception e) {
                    if (MoviesFragment.this.getContext() != null)
                        Toast.makeText(MoviesFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (MoviesFragment.this.getContext() != null)
                    Toast.makeText(MoviesFragment.this.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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
    public void onGenreClicked(Genre genre, Button button, TextView title) {

    }

    @Override
    public void onMovieClicked(Movie movie, ImageView imageView, TextView title) {

        Intent intent = new Intent(mContext, MovieDetailActivity.class);
        intent.putExtra("imageURL", movie.getThumbnailUrl());
        intent.putExtra("title", movie.getTitle());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), imageView, "sharedName");
            startActivity(intent, options.toBundle());
        } else {
//            ActivityTransitionLauncher.with(MainActivity.this).from(image).launch(intent);
            startActivity(intent);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mGenreRecyclerView = null;
    }

    class SlideTimer extends TimerTask {

        @Override
        public void run() {
            if (getActivity() != null)
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() < list.size() - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
        }
    }
}
