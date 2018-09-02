package com.example.crizma_pclaptop.pencil;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CRIZMA_PC&LAPTOP on 10/06/2018.
 */
public class networkconfig {
    private static Retrofit retrofit;

    private networkconfig() {
    }

    public static Retrofit newInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.2/proj/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}







