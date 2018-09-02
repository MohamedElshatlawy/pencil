package com.example.crizma_pclaptop.pencil;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.nbsp.materialfilepicker.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_LONG;


public class subject extends Fragment {
    Button subjectButton, downloadButton;
    TextView back;
    Handler handler;
    ListView lv;
    ArrayAdapter<String> adapter;
    ArrayList<String>dataFiles;
    ProgressDialog progressDialog;

    DownloadManager downloadManager;
    String id,dep_id;
    String type;

    public subject() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subject, container, false);
        SharedPreferences details = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE);

        subjectButton = view.findViewById(R.id.upload);
        downloadManager= (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
         type=details.getString("type","not specify");
         id=details.getString("id","");
         dep_id=details.getString("department_id","");

        dataFiles=new ArrayList<>();
        adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,dataFiles);
        lv=view.findViewById(R.id.lv);
        lv.setAdapter(adapter);


        if(type.equals("student")){
            subjectButton.setVisibility(View.GONE);
        }
        subjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withFragment(subject.this)

                        .withRequestCode(10)
                        .start();

            }
        });


        handler=new Handler();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Downloading...");
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        getCourses(type);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                downloadCourse(i);
            }
        });

        if(type.equals("instructor")){
            lv.setLongClickable(true);
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("Do you want to delete this course..!");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    deleteCourse(courses_url.get(i));

                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                    return true;
                }
            });

        }

        return view;

    }

    private void deleteCourse(String url) {
        networkconfig.newInstance().create(networkcalls.class).deleteCourse(url)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                Toast.makeText(getActivity(),response.body().string(),LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }

    private void downloadCourse(int pos) {
        String link="http://192.168.1.2/proj/"+courses_url.get(pos);
        Uri uri=Uri.parse(link);
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Pencil/"  + courses_url.get(pos));

        downloadManager.enqueue(request);

    }

    ArrayList<String>courses_url;
    private void getCourses(String type) {
        dataFiles.clear();
        courses_url=new ArrayList<>();
        if(type.equals("student")){

            networkcalls networkcalls = networkconfig.newInstance().create(networkcalls.class);
            networkcalls.getStudentCourses(dep_id).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        String resp= null;
                        try {
                            resp = response.body().string().toString();
                            System.out.println(resp);
                            JSONArray array= new JSONArray(resp.substring(resp.indexOf("["), resp.lastIndexOf("]") + 1));
                            for(int i=0;i< array.length();i++){
                                JSONObject c_name= (JSONObject) array.get(i);
                                dataFiles.add(c_name.getString("name"));
                                courses_url.add(c_name.getString("course_url"));
                            }
                            adapter.notifyDataSetChanged();
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

        }else{

            networkcalls networkcalls = networkconfig.newInstance().create(networkcalls.class);
            networkcalls.getInstructorCourses(id).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        String resp= null;
                        try {
                            resp = response.body().string().toString();

                            System.out.println(resp);

                                JSONObject jsonObject = new JSONObject(resp.substring(resp.indexOf("{"), resp.lastIndexOf("}") + 1));
                                JSONArray arr=jsonObject.getJSONArray("result");

                                if(arr.length()>0) {
                                    for (int i = 0; i < arr.length(); i++) {
                                        JSONObject c_name = (JSONObject) arr.get(i);
                                        Toast.makeText(getActivity(), c_name.getString("name"), LENGTH_LONG).show();
                                        dataFiles.add(c_name.getString("name"));
                                        courses_url.add(c_name.getString("course_url"));
                                    }

                                }

                            } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        adapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(getActivity(),"fail response",LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getActivity(),"fail enqueue",LENGTH_LONG).show();
                }
            });

        }



    }






    ProgressDialog progress;

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        System.out.println("result:"+resultCode);
        if(requestCode == 10 && resultCode == RESULT_OK){

            progress = new ProgressDialog(getActivity());
            progress.setTitle("Uploading");
            progress.setMessage("Please wait...");
            progress.show();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String content_type  = getMimeType(f.getPath());

                    String file_path = f.getAbsolutePath();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type",content_type)
                            .addFormDataPart("uploaded_file",file_path.substring(file_path.lastIndexOf("/")+1), file_body)

                            .addFormDataPart("instructor_id", String.valueOf(id))
                            .addFormDataPart("department_id", String.valueOf(dep_id))
                            .build();

                    Log.e("path","file_path:"+file_path.substring(file_path.lastIndexOf("/")+1));
                    Log.e("p2",file_path);

                    System.out.println("second file :"+file_path);

                    Request request = new Request.Builder()
                            .url("http://192.168.1.2/proj/uploadCourse.php")
                            .post(request_body)
                            .build();

                    try {
                        okhttp3.Response response = client.newCall(request).execute();

                        if(!response.isSuccessful()){
                            throw new IOException("Error : "+response);
                        }/*else{
                            String resp=response.body().string();
                            JSONObject jsonObject= new JSONObject(resp.substring(resp.indexOf("{"), resp.lastIndexOf("}") + 1));
                            dataFiles.add(jsonObject.getString("name"));
                            courses_url.add(jsonObject.getString("path"));

                      {  */

                        progress.dismiss();
                        getCourses(type);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            t.start();

        }
    }

    private String getMimeType(String path) {

        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }




    private void upload() {
        /*new MaterialFilePicker()
                .withActivity(getActivity())
                .withRequestCode(1)
                .start();*/
    }
    private static final int FILE_SELECT_CODE = 0;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void downloadfile(){

        networkcalls networkcalls = networkconfig.newInstance(). create(networkcalls.class);
        Call<ResponseBody> call= networkcalls.downloadFile();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                boolean success = writeResponseBodyToDisk(response.body());
                Toast.makeText(getActivity(), "download was successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "no :(", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File futureStudioIconFile = new File(getActivity().getExternalFilesDir(null) + File.separator + "Future Studio Icon.png");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(subject.class.getSimpleName(), "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
     private void uploadFile(Uri fileUri) {
        String filePath = getRealPathFromURIPath(fileUri, subject.this);
        File file = new File(filePath);
        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        networkcalls uploadImage = networkconfig.newInstance().create(networkcalls.class);
        Call<ResponseBody> fileUpload = uploadImage.upload(filename, fileToUpload);
        fileUpload.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //your stuff
                Toast.makeText(getActivity(), "Response " + response.raw() + new message(), LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(quize.class.getSimpleName(), "Error " + t.getMessage());
            }
        });
    }
  private String getRealPathFromURIPath(Uri contentURI, subject activity) {
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
}
