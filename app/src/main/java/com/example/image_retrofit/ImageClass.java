package com.example.image_retrofit;

import com.google.gson.annotations.SerializedName;

public class ImageClass {





    @SerializedName("image")
    private String image;

    @SerializedName("response")
    private String Response;

    public String getResponse() {
        return Response;
    }
}
