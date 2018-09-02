package com.example.crizma_pclaptop.pencil;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class signup1 extends Fragment {


    Button button;
    LoginButton loginbutton;
    EditText et1, et2, et3, et4, et5;
    TextView back;
    CallbackManager callbackManager;
    RadioGroup rd;

    public signup1() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_signup1, container, false);

        boolean loggedIn = AccessToken.getCurrentAccessToken() == null;

        et1 = (EditText) view.findViewById(R.id.fname);
        et2 = (EditText) view.findViewById(R.id.lname);
        et3 = (EditText) view.findViewById(R.id.email);
        et4 = (EditText) view.findViewById(R.id.pass);
        et5 = (EditText) view.findViewById(R.id.code);
        button = (Button) view.findViewById(R.id.signup1);
        back=(TextView)view.findViewById(R.id.back);
        rd=view.findViewById(R.id.radioGrp);
        loginbutton = (LoginButton) view.findViewById(R.id.login_button);
        SharedPreferences details = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE);
        final SharedPreferences.Editor ed=details.edit();
        final String flag = details.getString("type", "this member is not specified");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), signup2.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

    if(flag.equals("instructor"))
    {
        et5.setVisibility(View.GONE);
    }


        callbackManager = CallbackManager.Factory.create();


        loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Toast.makeText(getActivity(),"done..",Toast.LENGTH_LONG).show();
                Log.e("Onc", "on success");
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getActivity(),"cancel..",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getActivity(),"error..",Toast.LENGTH_LONG).show();
            }
        });
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(signup1.this, Arrays.asList("public_profile"));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*nackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                if (et1.getText().toString().isEmpty()) {
                    et1.setError("Please enter fname");
                    return;
                }

                if (et2.getText().toString().isEmpty()) {
                    et2.setError("Please enter lname");
                    return;
                }

                if (et3.getText().toString().isEmpty()) {
                    et3.setError("Please enter E-mail");
                    return;
                }

                if (et4.getText().toString().isEmpty()) {
                    et4.setError("Please enter password");
                    return;
                }

                if (et5.getText().toString().isEmpty()) {
                    et5.setError("Please enter code");
                    return;
                }
                int depId=0;
                int radioChecked=rd.getCheckedRadioButtonId();
                switch (radioChecked){
                    case R.id.rd1:
                        depId=1; break;
                    case R.id.rd2:
                        depId=2; break;
                    case R.id.rd3:
                        depId=3; break;

                }
                final int finalDepId = depId;
                networkcalls networkcalls = networkconfig.newInstance().create(networkcalls.class);



                    networkcalls.insertStudent(et1.getText().toString(),et2.getText().toString(),et3.getText().toString(),
                            et4.getText().toString(),depId,Integer.parseInt(et5.getText().toString())).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Toast.makeText(getActivity(),"Done Inserting",Toast.LENGTH_LONG).show();
                            System.out.println("Resposne:"+response.body().toString());
                            FragmentTransaction transection = ((signup2) getActivity()).manager.beginTransaction();
                            homee  homeefragment = new homee();
                            ed.putString("name",et1.getText().toString()+""+et2.getText().toString());
                            ed.putString("email",et3.getText().toString());
                            ed.putString("type","student");
                            ed.putString("department_id",Integer.toString(finalDepId));
                            ed.commit();
                            transection.replace(R.id.frame, homeefragment);
                            transection.commit();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_LONG).show();
                            Log.e("error",t.getMessage());
                        }
                    });




            }



        });
        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void signUp(studentinfo studentinfo) {
        networkcalls signUpStudent = networkconfig.newInstance().create(networkcalls.class);
        Call<findmodel> signUp = signUpStudent.signUp(studentinfo);
        signUp.enqueue(new Callback<findmodel>() {
            @Override
            public void onResponse(Call<findmodel> call, Response<findmodel> response) {
                //your stuff
            }

            @Override
            public void onFailure(Call<findmodel> call, Throwable t) {
                Log.d(signup1.class.getSimpleName(), "Error " + t.getMessage());
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(getActivity());
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}




