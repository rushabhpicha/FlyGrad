package com.example.application.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.R;
import com.example.application.adapter.ProfileViewPagerAdapter;
import com.example.application.model.User;
import com.example.application.rest.ApiClient;
import com.example.application.rest.services.UserInterface;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileCover;
    CircleImageView profileImage;
    Button profileOptionButton;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbar;
    ViewPager ViewPageProfile;
    ProfileViewPagerAdapter profileViewPageAdapter;
    String uid = "0";
    int current_state = 0;
    String profileUrl = "", coverUrl = "";
    ProgressDialog progressDialog;
    int imageUploadType = 0;
    File compressedImageFile;
    Button profileOptionBtn;
    FrameLayout frameLayout;
    TextView greTxt,toeflTxt,workexTxt,ugradcollegeTxt,ugradpercentTxt,technicalpapersTxt,unione,unitwo;
    TextView toolbarTitle;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //For hiding status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        uid = getIntent().getStringExtra("uid");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


//        profileCover = findViewById(R.id.profile_cover);
        profileImage = findViewById(R.id.profile_image);
        profileOptionBtn = findViewById(R.id.profile_option_btn);
        toolbar = findViewById(R.id.toolbar);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        ViewPageProfile = findViewById(R.id.ViewPager_profile);
        frameLayout = findViewById(R.id.framelayout);
        greTxt = findViewById(R.id.gre);
        toeflTxt = findViewById(R.id.toefl);
        workexTxt = findViewById(R.id.workex);
        ugradcollegeTxt = findViewById(R.id.ugradcollege);
        ugradpercentTxt = findViewById(R.id.ugradpercent);
        technicalpapersTxt = findViewById(R.id.technicalpapers);
        toolbarTitle = findViewById(R.id.toolbar_title);
        unione = findViewById(R.id.unione);


        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equalsIgnoreCase(uid)) {
            // UID is matched , we are going to load our own profile
            current_state = 5;
            profileOptionBtn.setText("Edit Profile");
            loadProfile();
        } else {
            // load others profile here
            loadothersprofile();
        }

        profileOptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
                Map<String, String> params = new HashMap<>();
                params.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                Call<User> call = userInterface.loadownProfile(params);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        edtProfile(response.body());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Something went wrong ... Please try later", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }

    private void edtProfile(final User body) {
        LayoutInflater factory = LayoutInflater.from(ProfileActivity.this);
        final View textEntryView = factory.inflate(R.layout.edit_profile, null);
        //text_entry is an Layout XML file containing two text field to display in alert dialog
        final EditText eGre = textEntryView.findViewById(R.id.edt_gre);
        final EditText eToefl = textEntryView.findViewById(R.id.edt_toefl);
        final EditText eWorkex = textEntryView.findViewById(R.id.edt_workex);
        final EditText eUgradcollege = textEntryView.findViewById(R.id.edt_ugradcollege);
        final EditText eUgradpercent = textEntryView.findViewById(R.id.edt_ugradpercent);
        final EditText eTechnicalpapers = textEntryView.findViewById(R.id.edt_technicalPapers);
        final EditText eUniOne = textEntryView.findViewById(R.id.edt_unione);
        final EditText eUniTwo = textEntryView.findViewById(R.id.edt_unitwo);
        final EditText eUniThree = textEntryView.findViewById(R.id.edt_unithree);

        final AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
        alert.setIcon(R.drawable.icon_background_after_comment)
                .setTitle("Enter the Text:")
                .setView(textEntryView)
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
                                Call<Integer> call = userInterface.updateProfile(new ProfileActivity.UserInfo(uid, Integer.parseInt(eGre.getText().toString()),
                                        Integer.parseInt(eToefl.getText().toString()),Integer.parseInt(eWorkex.getText().toString()),eUgradcollege.getText().toString(),
                                        Integer.parseInt(eUgradpercent.getText().toString()), Integer.parseInt(eTechnicalpapers.getText().toString()), eUniOne.getText().toString()
                                        ,eUniTwo.getText().toString(),eUniThree.getText().toString()));
                                call.enqueue(new Callback<Integer>() {
                                    @Override
                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                        progressDialog.dismiss();
                                        if (response.body() != null && response.body() == 1) {
                                            loadProfile();
                                            Toast.makeText(ProfileActivity.this, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
//                                            startActivity(intent);
//                                            finish();
                                        } else {
                                            Toast.makeText(ProfileActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Integer> call, Throwable t) {
                                        Toast.makeText(ProfileActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        });
        alert.show();
        eGre.setText(body.getGre());
        eToefl.setText(body.getToefl());
        eWorkex.setText(body.getWorkex());
        eUgradcollege.setText(body.getUgradcollege());
        eUgradpercent.setText(body.getUgradpercent());
        eTechnicalpapers.setText(body.getTechnicalpapers());
    }

    private void insertProfileData() {

    }


    private void loadothersprofile() {
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        params.put("profileId",uid);

        Call<User> call = userInterface.loadothersProfile(params);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                    showUserData(response.body());
                ViewGroup layout = (ViewGroup) profileOptionBtn.getParent();
                if(null!=layout) //for safety only  as you are doing onClick
                    layout.removeView(profileOptionBtn);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadProfile() {
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        Call<User> call = userInterface.loadownProfile(params);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showUserData(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Something went wrong ... Please try later", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showUserData(User body) {
        ProfileViewPagerAdapter profileViewPagerAdapter = new ProfileViewPagerAdapter(getSupportFragmentManager(), 1,body.getUid(),body.getState());
        ViewPageProfile.setAdapter(profileViewPagerAdapter);
        Typeface lato=Typeface.createFromAsset(ProfileActivity.this.getAssets(), "font/latobold.ttf");

        progressDialog.dismiss();
        if (body != null) {
            greTxt.setTypeface(lato);
            toeflTxt.setTypeface(lato);
            workexTxt.setTypeface(lato);
            ugradcollegeTxt.setTypeface(lato);
            ugradpercentTxt.setTypeface(lato);
            technicalpapersTxt.setTypeface(lato);

            profileUrl = body.getProfileUrl();
            coverUrl = body.getCoverUrl();
            collapsingToolbar.setTitle(body.getName());
            greTxt.setText("GRE Score "+body.getGre());
            toeflTxt.setText("TOEFL/IELTS Score:- "+body.getToefl());
            workexTxt.setText("Work Experience:- "+body.getWorkex());
            ugradcollegeTxt.setText("College:- "+body.getUgradcollege());
//            ugradpercentTxt.setText("Undergrad GPA:- "+body.getUgradpercent());
            technicalpapersTxt.setText(("Technical Papers:- "+body.getTechnicalpapers()));
            unione.setText("Admit one:- "+body.getUnione());

            Toast.makeText(this, ""+body.getUnione(), Toast.LENGTH_SHORT).show();

            if (!profileUrl.isEmpty()) {
                Picasso.with(ProfileActivity.this).load(profileUrl).into(profileImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ProfileActivity.this).load(profileUrl).into(profileImage);
                    }
                });

//                if (!coverUrl.isEmpty()) {
//                    Picasso.with(ProfileActivity.this).load(coverUrl).into(profileCover, new com.squareup.picasso.Callback() {
//                        @Override
//                        public void onSuccess() {
//
//                        }
//
//                        @Override
//                        public void onError() {
//                            Picasso.with(ProfileActivity.this).load(coverUrl).into(profileCover);
//                        }
//                    });
//                }
//                        addImageCoverClick();
            }
        } else {
            Toast.makeText(ProfileActivity.this, "Something went wrong ... Please try later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
    }

    public class UserInfo {
        String uid, ugradcollege, unione, unitwo, unithree;
        int gre, toefl, workex, ugradpercent, technicalpapers;

        public UserInfo(String uid, int gre, int toefl, int workex,String ugradcollege, int ugradpercent, int technicalpapers, String unione,
                        String unitwo, String unithree) {
            this.uid = uid;
            this.ugradcollege = ugradcollege;
            this.gre = gre;
            this.toefl = toefl;
            this.workex = workex;
            this.ugradpercent = ugradpercent;
            this.technicalpapers = technicalpapers;
            this.unione = unione;
            this.unitwo = unitwo;
            this.unithree = unithree;
        }
    }
}
