package com.quantumtech.vido.activites;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.quantumtech.vido.MovieItemClickListener;
import com.quantumtech.vido.net.MySingleton;
import com.quantumtech.vido.R;
import com.quantumtech.vido.fragment.MoviesFragment;
import com.quantumtech.vido.fragment.SettingFragment;
import com.quantumtech.vido.helper.Connectivity;
import com.quantumtech.vido.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int DISCOVERY_PORT = 8080;
    public MovieItemClickListener movieItemClickListener;
    boolean doubleBackToExitPressedOnce = false;
    ImageView mProfileImage;
    TextView mProfileName;
    TextView mProfileEmail;
    private FragmentTransaction transaction;
    private CoordinatorLayout coordinatorLayout;
//    private LinearLayout noConnection;
    private Fragment fragment;
    private String HOST;
    private String img;
    private TextView mProfilePhone;
    private RequestQueue requestQueue;
    private LinearLayout noConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!isNightMode()) {
            setTheme(R.style.BaseTheme_NoActionBar);
        } else {
            setTheme(R.style.NightTheme_NoActionBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HOST = "http://" + new Connectivity(this).getHOST() + "/api/v1/";

        coordinatorLayout = findViewById(R.id.coordinatorlayout);
        noConnection = findViewById(R.id.no_connection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float moveFactor = ((drawer.getWidth() / 2f) * slideOffset);
                coordinatorLayout.setTranslationX(moveFactor);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

//        if (!isNetworkAvailable())
//            noConnection.setVisibility(View.VISIBLE);
        mProfileImage = navigationView.getHeaderView(0).findViewById(R.id.profileImageView);
        mProfileName = navigationView.getHeaderView(0).findViewById(R.id.profileName);
        mProfileEmail = navigationView.getHeaderView(0).findViewById(R.id.profileEmail);
        mProfilePhone = navigationView.getHeaderView(0).findViewById(R.id.profilePhone);

//        TextView retry = findViewById(R.id.retry_connection);
//        retry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                HOST = new Connectivity(MainActivity.this).getHOST();
//                if (isNetworkAvailable())
//                    noConnection.setVisibility(View.GONE);
//
//            }
//        });
//        HOST = new Connectivity(this).getHOST();

        requestQueue = MySingleton.getInstance(this).getRequestQueue();
// Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

// Start the queue
        requestQueue.start();
        try {
            setProfileData();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

//        noConnectionAlert();

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new MoviesFragment());
        transaction.commit();


//        try {
//            Toast.makeText(this, (CharSequence) getBroadcastAddress(), Toast.LENGTH_LONG).show();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }

    }


    private void setProfileData() throws IOException {
        StringRequest genreStringRequest = new StringRequest(Request.Method.POST,HOST + "movie", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);

                    JSONObject object = array.getJSONObject(0);
                    mProfileName.setText(object.getString("name"));
                    mProfileEmail.setText(object.getString("email"));
                    mProfilePhone.setText(object.getString("phone"));
                    mProfileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    img = HOST + object.getString("thumbnail");

                    String url = object.getString("thumbnail");
                    Glide.with(MainActivity.this).load(HOST + url)
                            .thumbnail(0.5f)
                            .transition(new DrawableTransitionOptions().crossFade())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(mProfileImage);

                } catch (Exception e) {
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue.add(genreStringRequest);

//        URL url = new URL(img);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setDoInput(true);
//        connection.connect();
//        InputStream inputStream = connection.getInputStream();
//        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//
//        mProfileImage.setImageBitmap(bitmap);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Please click BACK again to exit", Snackbar.LENGTH_LONG);

            snackbar.show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!Utility.isWifiConnected(this))
            noConnection.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(!Utility.isWifiConnected(this))
            noConnection.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem mNightMode = menu.findItem(R.id.action_night_mode);

        if (isNightMode())
            mNightMode.setChecked(true);
        else
            mNightMode.setChecked(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_night_mode) {
            if (isNightMode())
                setNightModeOn(false);
            else
                setNightModeOn(true);
            recreate();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_movies) {
            fragment = new MoviesFragment();
        } else if (id == R.id.nav_setting) {
            fragment = new SettingFragment();
        }
//        noConnection.setVisibility(View.GONE);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void noConnectionAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.no_connection_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        TextView retry = view.findViewById(R.id.retry_connection);
//        retry.setOnClickListener(v -> {
//            dialog.cancel();
//            if (!isNetworkAvailable())
//                noConnectionAlert();
//        });

        dialog.show();
    }

    public boolean isNetworkAvailable() {
        boolean have_WIFI = false;
        boolean have_MobileData = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

        for (NetworkInfo info : networkInfo) {
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    have_WIFI = true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                    have_MobileData = true;
        }
        return have_WIFI || have_MobileData;
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

    private void setNightModeOn(boolean b) {
        if (b)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Night Mode", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("ON", b);
        editor.apply();
    }

    public void retryConnection(View view) {
        if(Utility.isWifiConnected(this))
            noConnection.setVisibility(View.GONE);
    }
}
