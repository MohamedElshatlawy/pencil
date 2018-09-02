package com.example.crizma_pclaptop.pencil;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class login extends Fragment {

    Button button1;
    LoginButton loginButton;
    EditText name, email, password;
    CallbackManager callbackManager;

    RadioGroup rd;

    public login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_login, container, false);


        boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
        button1 = (Button) view.findViewById(R.id.login1);
        loginButton = (LoginButton) view.findViewById(R.id.login_button);

        rd=view.findViewById(R.id.rg1);

        callbackManager = CallbackManager.Factory.create();



        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("Onc" , "on success");
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(login.this, Arrays.asList("public_profile"));

            }
        });


        name = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);




        button1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (name.getText().toString().isEmpty()) {
                    name.setError("Please enter name");
                    return;
                }

                if (email.getText().toString().isEmpty()) {
                    email.setError("Please enter email");
                    return;
                }

                if (password.getText().toString().isEmpty()) {
                    password.setError("Please enter password");
                    return;
                }
                SharedPreferences preferences = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = preferences.edit();


                networkcalls networkcalls = networkconfig.newInstance().create(networkcalls.class);
                switch (rd.getCheckedRadioButtonId()){
                    case R.id.rd_student:

                        networkcalls.loginStudent(email.getText().toString(),password.getText().toString()).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                               if(response.isSuccessful()){
                                   try {
                                       String resp=response.body().string().toString();
                                       System.out.println(resp);
                                       JSONObject jsonObject= new JSONObject(resp.substring(resp.indexOf("{"), resp.lastIndexOf("}") + 1));
                                       String res=jsonObject.getString("result");
                                       if(res.equals("WRONG")){
                                           Toast.makeText(getActivity(),"Invalid email or password",Toast.LENGTH_LONG).show();
                                       }else{
                                           Toast.makeText(getActivity(),"done login",Toast.LENGTH_LONG).show();

                                           String id=jsonObject.getString("id");
                                           String dep_id=jsonObject.getString("department_id");

                                           editor.putString("id",id);
                                           editor.putString("department_id",dep_id);

                                           editor.putString("email", email.getText().toString());
                                           editor.putString("password", password.getText().toString());

                                           editor.putString("type","student");
                                           editor.apply();

                                   FragmentTransaction transection = ((signup2) getActivity()).manager.beginTransaction();
                                   homee home = new homee();
                                   transection.replace(R.id.frame, home);
                                   transection.commit();

                                       }
                                   } catch (IOException e) {
                                       e.printStackTrace();
                                   } catch (JSONException e) {
                                       e.printStackTrace();
                                   }


                               }else{
                                   Toast.makeText(getActivity(),response.errorBody().toString(),Toast.LENGTH_LONG).show();
                                   System.out.println("No response");

                                   System.out.println("error:"+response.errorBody());
                               }
                                }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                System.out.println("failed:"+t.getMessage());
                            }
                        });
                        break;
                    case R.id.rd_instructor:
                        networkcalls.loginInstructor(email.getText().toString(),password.getText().toString()).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                if(response.isSuccessful()){
                                    try {

                                        String resp=response.body().string().toString();
                                        System.out.println(resp);
                                        JSONObject jsonObject= new JSONObject(resp.substring(resp.indexOf("{"), resp.lastIndexOf("}") + 1));
                                        //=new JSONObject(new String(resp));
                                        String res=jsonObject.getString("result");

                                        if(res.equals("WRONG")){
                                            Toast.makeText(getActivity(),"inside instructor Invalid email or password",Toast.LENGTH_LONG).show();

                                        }
                                        else{
                                            Toast.makeText(getActivity(),"done login",Toast.LENGTH_LONG).show();
                                            String id=jsonObject.getString("id");
                                            String dep_id=jsonObject.getString("department_id");

                                            editor.putString("id",id);
                                            editor.putString("department_id",dep_id);
                                            editor.putString("email", email.getText().toString());
                                            editor.putString("password", password.getText().toString());
                                            editor.putString("type","instructor");
                                            editor.apply();

                                            FragmentTransaction transection = ((signup2) getActivity()).manager.beginTransaction();
                                            homee home = new homee();
                                            transection.replace(R.id.frame, home);
                                            transection.commit();

                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                /*
                                Toast.makeText(getActivity(),"done login",Toast.LENGTH_LONG).show();
                                editor.putString("email", email.getText().toString());
                                editor.apply();

                                FragmentTransaction transection = ((signup2) getActivity()).manager.beginTransaction();
                                homee home = new homee();
                                transection.replace(R.id.frame, home);
                                transection.commit();
                            */}

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                System.out.println("failed:"+t.getMessage());
                            }
                        });
                }


            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}