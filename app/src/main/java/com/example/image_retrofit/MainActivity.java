package com.example.image_retrofit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    EditText title;
    Button choosebn,uploadbn;
    private static final int IMG_REQUEST=777;
    private Bitmap bitmap;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=(ImageView)findViewById(R.id.imageview);
        title=(EditText)findViewById(R.id.title);
        choosebn=(Button)findViewById(R.id.choosebn);
        uploadbn=(Button)findViewById(R.id.uploadbn);
        choosebn.setOnClickListener(this);
        uploadbn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.choosebn:
                selectImage();
                break;
            case R.id.uploadbn:
                uploadImage();


                break;
        }

    }

    private void uploadImage()
    {
        String Image=imagetoString();
        //Toast.makeText(MainActivity.this,image,Toast.LENGTH_LONG).show();
        final String Title=title.getText().toString();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<ImageClass> call=apiInterface.uploadImage(Image);
        call.enqueue(new Callback<ImageClass>() {
            @Override
            public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {

                ImageClass imageClass=response.body();
                //Toast.makeText(MainActivity.this,"Server Response"+imageClass.getResponse(),Toast.LENGTH_LONG).show();
                imageView.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
                choosebn.setEnabled(true);
                uploadbn.setEnabled(false);
                title.setText("");
            }

            @Override
            public void onFailure(Call<ImageClass> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Server Response Failed\n"+t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void selectImage()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null)
        {
            Uri path=data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                choosebn.setEnabled(false);
                uploadbn.setEnabled(true);



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imagetoString()
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageByte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte,Base64.DEFAULT);
    }
}
