package com.quantumtech.vido.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class Connectivity {

    private String HOST = "";
    SharedPreferences preferences ;
    Context context;

    public Connectivity(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("vido_preferences",MODE_PRIVATE);
    }

    public String getHOST() {
        return preferences.getString("server", "");
    }

    public void setHOST(String HOST) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("server", HOST);
        editor.apply();
    }
}
