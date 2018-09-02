package com.example.crizma_pclaptop.pencil;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class query extends Fragment {

    Button queryButton;
    TextView back;

    public query() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_query, container, false);
        queryButton = (Button) view.findViewById(R.id.querygo);
        back=(TextView) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transection = ((signup2) getActivity()).manager.beginTransaction();
                profile profilefragment = new profile();
                transection.replace(R.id.frame, profilefragment);
                transection.commit();
            }
        });
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return view;
    }


    private void findName(String name) {
        networkcalls findStudent = networkconfig.newInstance().create(networkcalls.class);
        Call<ArrayList<findmodel>> find = findStudent.find(name);
        find.enqueue(new Callback<ArrayList<findmodel>>() {
            @Override
            public void onResponse(Call<ArrayList<findmodel>> call, Response<ArrayList<findmodel>> response) {
                //your stuff
            }

            @Override
            public void onFailure(Call<ArrayList<findmodel>> call, Throwable t) {
                Log.d(query.class.getSimpleName(), "Error " + t.getMessage());
            }
        });
    }
}





