package com.example.application.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.application.R;
import com.example.application.rest.ApiClient;
import com.example.application.rest.services.UserInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadActivity extends AppCompatActivity {

    Spinner privacySpinner;
    TextView postBtnTxt;
    Toolbar toolbar;
    CircleImageView dialogAvatar;
    EditText statusEdit;
    ImageView addImage;
    Button addButton;
    ProgressDialog progressDialog;
    File compressedImageFile = null;
    boolean isImageSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        privacySpinner = findViewById(R.id.privacy_spinner);
        postBtnTxt = findViewById(R.id.postBtnTxt);
        toolbar = findViewById(R.id.toolbar);
        dialogAvatar = findViewById(R.id.dialogAvatar);
        statusEdit = findViewById(R.id.status_edit);
        addImage = findViewById(R.id.image);
        addButton = findViewById(R.id.add_image);

        setSupportActionBar(toolbar);
        toolbar.setTitle("Ask a query");
        toolbar.setNavigationIcon(R.drawable.arrow_back_white);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Ask a Query");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading...");


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.create(UploadActivity.this)
                        .folderMode(true)
                        .single().start();
            }
        });
        postBtnTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
            }
        });
    }

    private void uploadPost() {
        String status = statusEdit.getText().toString();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (status.trim().length() > 0 || isImageSelected) {
            progressDialog.show();

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);

            builder.addFormDataPart("post", status);
            builder.addFormDataPart("postUserId", userId);
            builder.addFormDataPart("privacy", "1");

            if (isImageSelected) {
                builder.addFormDataPart("isImageSelected", "1");
                builder.addFormDataPart("file", compressedImageFile.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile));
            } else {
                builder.addFormDataPart("isImageSelected", "0");
            }

            MultipartBody multipartBody = builder.build();

            UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
            Call<Integer> call = userInterface.uploadStatus(multipartBody);
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    progressDialog.dismiss();
                    if (response.body() != null && response.body() == 1) {
                        Toast.makeText(UploadActivity.this, "Post is Successfull", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UploadActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(UploadActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Toast.makeText(UploadActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

        } else {
            Toast.makeText(UploadActivity.this, "Please write your post first", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UploadActivity.this, MainActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image selectedImage = ImagePicker.getFirstImageOrNull(data);

            try {
                compressedImageFile = new Compressor(this)
                        .setQuality(75)
                        .compressToFile(new File(selectedImage.getPath()));

                isImageSelected = true;

                Picasso.with(UploadActivity.this).load(new File(selectedImage.getPath())).placeholder(R.drawable.default_image_placeholder).into(addImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

