package com.example.crizma_pclaptop.pencil;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
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

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_LONG;


public class assignment extends Fragment {
    Button assignmentButton;
    TextView back;
    ArrayList<String> dataFiles;
    ArrayAdapter<String> adapter;
    ListView listView;
    DownloadManager downloadManager;
    public assignment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assignment, container, false);
        assignmentButton = view.findViewById(R.id.upload);
        back=(TextView) view.findViewById(R.id.back);
        listView=view.findViewById(R.id.lv2);
        listView.setLongClickable(true);
        dataFiles=new ArrayList<>();
        adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,dataFiles);
        listView.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transection = ((signup2) getActivity()).manager.beginTransaction();
                homee homeefragment = new homee();
                transection.replace(R.id.frame, homeefragment);
                transection.commit();
            }
        });

        downloadManager= (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        assignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withFragment(assignment.this)

                        .withRequestCode(10)
                        .start();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                downloadCourse(i);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

        getCourses();
        return view;

    }

    private void deleteCourse(String s) {

        networkconfig.newInstance().create(networkcalls.class).deleteAssignment(s)
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

    private void downloadCourse(int i) {
        String link="http://192.168.1.2/proj/"+courses_url.get(i);
        Uri uri=Uri.parse(link);
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Pencil/"  + courses_url.get(i));

        downloadManager.enqueue(request);
    }

    ArrayList<String>courses_url;
    private void getCourses() {
        dataFiles.clear();
        courses_url=new ArrayList<>();
        networkcalls networkcalls = networkconfig.newInstance().create(networkcalls.class);
        networkcalls.getAssignments(dep_id).enqueue(new Callback<ResponseBody>() {
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
                                courses_url.add(c_name.getString("department_url"));
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




    SharedPreferences details = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE);

    String type = details.getString("type", "not specify");
    final String id=details.getString("id","");
    final String dep_id=details.getString("department_id","");
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {


        System.out.println("result:"+resultCode);
        if(requestCode == 10 && resultCode == RESULT_OK){

            final ProgressDialog progress = new ProgressDialog(getActivity());
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


                            .addFormDataPart("department_id", String.valueOf(dep_id))
                            .build();

                    Log.e("path","file_path:"+file_path.substring(file_path.lastIndexOf("/")+1));
                    Log.e("p2",file_path);

                    System.out.println("second file :"+file_path);

                    Request request = new Request.Builder()
                            .url("http://192.168.1.2/proj/uploadAssignment.php")
                            .post(request_body)
                            .build();

                    try {
                        okhttp3.Response response = client.newCall(request).execute();

                        if(!response.isSuccessful()){
                            throw new IOException("Error : "+response);
                        }else{
                            String resp=response.body().string();
                            JSONObject jsonObject= new JSONObject(resp.substring(resp.indexOf("{"), resp.lastIndexOf("}") + 1));

                            ///dataFiles.add(jsonObject.getString("name"));
                            ///courses_url.add(jsonObject.getString("path"));

                        }

                        progress.dismiss();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
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

}