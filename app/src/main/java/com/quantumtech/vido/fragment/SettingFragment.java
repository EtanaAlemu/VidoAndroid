package com.quantumtech.vido.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.quantumtech.vido.R;
import com.quantumtech.vido.helper.Connectivity;

public class SettingFragment extends Fragment {

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        final Connectivity connectivity = new Connectivity(getActivity().getBaseContext());
        final EditText server = view.findViewById(R.id.server_address);
        server.setText(connectivity.getHOST());
        Button save = view.findViewById(R.id.save_setting);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                connectivity.setHOST(server.getText().toString());
                Toast.makeText(SettingFragment.this.getContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
