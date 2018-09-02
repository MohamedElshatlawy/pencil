package com.example.crizma_pclaptop.pencil;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class profile extends Fragment {
    TextView et1, et2, et3 , back;
    Button button, signoutButton , querybutton;
    ImageView im;
    String email,password;
    public profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        et1 = (TextView) view.findViewById(R.id.name);
        et2 = (TextView) view.findViewById(R.id.member);
        et3 = (TextView) view.findViewById(R.id.email);
        im=view.findViewById(R.id.gallery);
        back= (TextView) view.findViewById(R.id.back);
        signoutButton = (Button) view.findViewById(R.id.out);
        querybutton=(Button) view.findViewById(R.id.query);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transection = ((signup2) getActivity()).manager.beginTransaction();
                homee homeefragment = new homee();
                transection.replace(R.id.frame, homeefragment);
                transection.commit();
            }
        });


         SharedPreferences details = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE);

        if (details !=null){
            //String name = details.getString("name", "No name defined");
             email = details.getString("email", "No email defined");
             String type = details.getString("type", "this member is not specified");
             password=details.getString("password","No password found");

            et2.setText(type);
            et3.setText(email);
            if (type.equals("student")){
                getStudentInfo();
            }else{
                getInstructorInfo();
            }


        }

        querybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transection = ((signup2) getActivity()).manager.beginTransaction();
                query queryfragment = new query();

                transection.replace(R.id.frame, queryfragment);
                transection.commit();
            }
        });




        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), login1.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();

            }
        });


        return view;

    }

    private void getInstructorInfo() {
        networkcalls networkcalls = networkconfig.newInstance().create(networkcalls.class);
        networkcalls.loginInstructor(email,password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    String resp= null;
                    try {
                        resp = response.body().string().toString();
                        System.out.println(resp);
                        JSONObject jsonObject= new JSONObject(resp.substring(resp.indexOf("{"), resp.lastIndexOf("}") + 1));
                        String url=jsonObject.getString("img_url");
                        String userName=jsonObject.getString("fname")+jsonObject.getString("lname");
                        et1.setText(userName);
                        ImageDownloader downloader=new ImageDownloader();
                        downloader.execute(url);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void getStudentInfo() {
        networkcalls networkcalls = networkconfig.newInstance().create(networkcalls.class);
        networkcalls.loginStudent(email,password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    String resp= null;
                    try {
                        resp = response.body().string().toString();
                        System.out.println(resp);
                        JSONObject jsonObject= new JSONObject(resp.substring(resp.indexOf("{"), resp.lastIndexOf("}") + 1));
                        String url=jsonObject.getString("img_url");
                        String userName=jsonObject.getString("fname")+jsonObject.getString("lname");
                        et1.setText(userName);
                        ImageDownloader downloader=new ImageDownloader();
                        downloader.execute(url);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                im.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static final String UPLOAD_URL = "http://192.168.1.2/proj/uploadStudentImg.php";
    public static final String UPLOAD_KEY = "image";

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();

                data.put(UPLOAD_KEY, uploadImage);
                data.put("email",et3.getText().toString());
                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }
    public class RequestHandler {

        public String sendPostRequest(String requestURL,
                                      HashMap<String, String> postDataParams) {

            URL url;

            StringBuilder sb = new StringBuilder();
            try {
                url = new URL(requestURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    sb = new StringBuilder();
                    String response;
                    while ((response = br.readLine()) != null){
                        sb.append(response);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }
    }

    public class ImageDownloader extends AsyncTask<String ,Void,Bitmap>{
        ProgressDialog progressDialog;
        Bitmap bitmap;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            super.onPreExecute();
            progressDialog= ProgressDialog.show(getActivity(), "Loading...", null, true, true);

        }

        @Override
        protected Bitmap doInBackground(String... strings) {
          String link=strings[0];
            try {
                URL url=new URL(link);
                HttpURLConnection conn= (HttpURLConnection)url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();

                bitmap = BitmapFactory.decodeStream(is);
                is.close();
                conn.disconnect();

                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bm) {
            super.onPostExecute(bm);
            progressDialog.dismiss();
            if (bm!=null){
                im.setImageBitmap(bm);
            }
        }
    }
    public class ImageDownloader1 {

        public  Bitmap downloadImage(String url) throws MalformedURLException {
            return downloadImage(new URL(url));
        }


        public  Bitmap downloadImage(URL url){
            Bitmap bmImg = null;
            try {
                HttpURLConnection conn= (HttpURLConnection)url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();

                bmImg = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }


            return bmImg;
        }
    }
}