package com.example.crizma_pclaptop.pencil;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class signup2 extends AppCompatActivity {
    Button b1,b2;
    TextView back;
    public FragmentTransaction ft ;
    public FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        b1=(Button) findViewById(R.id.instructur);
        b2=(Button) findViewById(R.id.student);
        back=(TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(signup2.this, login1.class);
                startActivity(myIntent);
            }
        });
        manager = getFragmentManager();
        if (getIntent()!=null){
            if (getIntent().getStringExtra("flag")!=null){
                login si = new login();
                ft = manager.beginTransaction();
                ft.replace(R.id.frame, si, "fragmentright").commit();

            }
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("details", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("type", "instructor");
                editor.apply();

                instsignup is = new instsignup();
                ft = manager.beginTransaction();
                ft.replace(R.id.frame, is, "fragmentright").commit();


            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("details", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("type", "student");
                editor.apply();
                signup1 ss = new signup1();
                ft = manager.beginTransaction();
                ft.replace(R.id.frame, ss, "fragmentright").commit();

            }
        });
    }
}
