package com.example.image_retrofit;

import android.widget.ImageView;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("/api/upload")
    Call<ImageClass> uploadImage(@Field("image") String image);}
