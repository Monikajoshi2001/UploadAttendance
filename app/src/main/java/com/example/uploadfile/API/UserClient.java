package com.example.uploadfile.API;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserClient {
    @Multipart
    @POST("attendance/upload-attendance/")
    Call<FileModel> uploadExcel(
            @Part("month") RequestBody month,
            @Part("total_lectures") RequestBody total_lectures,
            @Part MultipartBody.Part file
    );
}
