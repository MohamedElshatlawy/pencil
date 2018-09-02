package com.example.crizma_pclaptop.pencil;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class code extends Fragment {
    Button b1, b2;
    TextView textView , back;

    public code() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_code, container, false);
        b1 = (Button) view.findViewById(R.id.codee);
        b2 = (Button) view.findViewById(R.id.go);
        textView = (TextView) view.findViewById(R.id.code);
        back=(TextView) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transection = ((signup2) getActivity()).manager.beginTransaction();
                instsignup instsignupfragment = new instsignup();
                transection.replace(R.id.frame, instsignupfragment);
                transection.commit();

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Random r = new Random();
//                int i1 = r.nextInt();
                int randomnum = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    randomnum = ThreadLocalRandom.current().nextInt(1, 100000);
                } else {
                    Random r = new Random();
                    randomnum = r.nextInt();
                }

                textView.setText(randomnum + "");
            }


        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transection = ((signup2) getActivity()).manager.beginTransaction();
                login loginfragment = new login();
                transection.replace(R.id.frame, loginfragment);
                transection.commit();

            }
        });


        return view;

    }
}