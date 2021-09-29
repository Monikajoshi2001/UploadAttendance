package com.example.uploadfile.API.M.service;

import com.example.uploadfile.API.M.model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserClient {
    @Multipart
    @POST("attendance/upload-attendance/")
    Call<ResponseBody> uploadExcel(
            @Part("month") RequestBody month,
            @Part("total_lectures") RequestBody total_lectures,
            @Part MultipartBody.Part file
    );
}
