package com.example.crizma_pclaptop.pencil;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class homee extends Fragment {
    ImageView i1, i2, i3, i4, i5;


    public homee() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_homee, container, false);
        i1 = (ImageView) view.findViewById(R.id.profile);
        i2 = (ImageView) view.findViewById(R.id.subject);
        i3 = (ImageView) view.findViewById(R.id.message);
        i4 = (ImageView) view.findViewById(R.id.quize);
        i5 = (ImageView) view.findViewById(R.id.assignment);

        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transection = ((signup2) getActivity()).manager.beginTransaction();
                profile profilefragment = new profile();
                transection.replace(R.id.frame, profilefragment);
                transection.commit();


            }
        });

        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transection = ((signup2) getActivity()).manager.beginTransaction();
                subject subjectfragment = new subject();
                transection.replace(R.id.frame, subjectfragment);
                transection.commit();


            }
        });

        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transection = ((signup2) getActivity()).manager.beginTransaction();
                message messagefragment = new message();
                transection.replace(R.id.frame, messagefragment);
                transection.commit();

            }
        });

        i4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transection = ((signup2) getActivity()).manager.beginTransaction();
                quize quizefragment = new quize();
                transection.replace(R.id.frame, quizefragment);
                transection.commit();

            }
        });


        i5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transection = ((signup2) getActivity()).manager.beginTransaction();
                assignment assignmentfragment = new assignment();
                transection.replace(R.id.frame, assignmentfragment);
                transection.commit();

            }
        });

        return view;
    }


}
