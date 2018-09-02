package com.example.crizma_pclaptop.pencil;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
public interface networkcalls {
    @Multipart
    @POST("domain/serv/files/setFile")
    Call<ResponseBody> upload(
            @Part("description") RequestBody description,
            @Part("") MultipartBody.Part file
    );

    @GET("domain/serv/files/setFile")
    Call<ResponseBody> downloadFile();


    @GET("domain/serv/filter/find")
    Call<ArrayList<findmodel>> find(@Query("name") String name);

    @POST("domain/serv/users/sign")
    Call<findmodel> signUp(@Body studentinfo name);

/////////////////////////////////////////////////////////////////////////////////////

    @GET("insertStudent.php")
    Call<ResponseBody> insertStudent(@Query("fname") String fname,@Query("lname") String lname,@Query("email") String email,@Query("password") String password,@Query("department_id") int department_id, @Query("code") int code);
    @GET("insertInstructor.php")
    Call<ResponseBody> insertInstructor(@Query("fname") String fname, @Query("lname") String lname, @Query("email") String email,@Query("password") String password,@Query("department_id") int department_id);


    @POST("studentLogin.php")
    @FormUrlEncoded
    Call<ResponseBody>loginStudent(@Field("email") String email,@Field("password") String password);


    @POST("instructorLogin.php")
    @FormUrlEncoded
    Call<ResponseBody>loginInstructor(@Field("email")String email,@Field("password")String password);


    @GET("get_student_courses.php")
    Call<ResponseBody>getStudentCourses(@Query("department_id")String dep_id );

    @GET("get_instructor_courses.php")
    Call<ResponseBody>getInstructorCourses(@Query("instructor_id")String ins_id );

    @POST("delete_course.php")
    @FormUrlEncoded
    Call<ResponseBody>deleteCourse(@Field("url")String url);

    @GET("getAssignments.php")
    Call<ResponseBody>getAssignments(@Query("department_id")String dep_id);

    @POST("delete_assignment.php")
    @FormUrlEncoded
    Call<ResponseBody>deleteAssignment(@Field("url")String url);


}
