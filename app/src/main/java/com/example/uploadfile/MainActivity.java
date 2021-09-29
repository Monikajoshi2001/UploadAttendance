package com.example.uploadfile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uploadfile.API.M.service.UserClient;
import com.example.uploadfile.API.ServiceGenerator;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSION_REQUEST=100;
    private int PICK_FILE_FROM_STORAGE_REQUEST=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
        }
        Button upload=(Button)findViewById(R.id.Upload_Button);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String[] mimeTypes ={}
                Intent intent=new Intent();
                //show only excel files
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select File"),PICK_FILE_FROM_STORAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_FILE_FROM_STORAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            Uri uri=data.getData();
            uploadFile(uri);
        }
        else{
            Toast.makeText(MainActivity.this,"Errrorrrrr",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                }
                else{
                    Toast.makeText(MainActivity.this,"Please allow the app to read your external storage",Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void uploadFile(Uri uri) {
        // create upload service client
        UserClient service =
                ServiceGenerator.createService(UserClient.class);

       File originalFile= new File(uri.getPath());
        //File file = FileUtils.getFile(MainActivity.this, uri);
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)),originalFile);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", originalFile.getName(), requestFile);

        // add another part within the multipart request
        EditText Month_number=(EditText) findViewById(R.id.month_edit);
        EditText Lectures=(EditText) findViewById(R.id.lectures_edit);
        RequestBody month = RequestBody.create(okhttp3.MultipartBody.FORM, Month_number.getText().toString());
        RequestBody total_lectures = RequestBody.create(okhttp3.MultipartBody.FORM, Lectures.getText().toString());

        // finally, execute the request
        Call<ResponseBody> call = service.uploadExcel(month,total_lectures,body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_LONG).show();
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Failure",Toast.LENGTH_LONG).show();
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
}