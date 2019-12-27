package com.example.application.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.application.R;
import com.example.application.fragment.NewsFeedFragment;
import com.example.application.fragment.NotificationFragment;
import com.example.application.fragment.FriendsFragment;
import com.example.application.fragment.UniversityFragment;
import com.example.application.model.PostModel;
import com.example.application.rest.ApiClient;
import com.example.application.rest.services.UserInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_title;
    FrameLayout frameLayout;
    FloatingActionButton fab;
    BottomNavigationView bottomNavigation;
    ImageView icon_search;

    NewsFeedFragment newsFeedFragment;
    NotificationFragment notificationFragment;
    FriendsFragment friendsFragment;
    UniversityFragment universityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
        }

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        frameLayout = findViewById(R.id.framelayout);
        fab = findViewById(R.id.fab);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        icon_search = findViewById(R.id.search);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //Bottom Navihation Bar code
        bottomNavigation.inflateMenu(R.menu.bottom_navigation_main);
        bottomNavigation.setItemBackgroundResource(R.color.colorPrimary);
        bottomNavigation.setItemTextColor(ContextCompat.getColorStateList(bottomNavigation.getContext(), R.color.nav_item_colors));
        bottomNavigation.setItemIconTintList(ContextCompat.getColorStateList(bottomNavigation.getContext(), R.color.nav_item_colors));

        // Declaring the Fragments
        newsFeedFragment = new NewsFeedFragment();
        notificationFragment = new NotificationFragment();
        friendsFragment = new FriendsFragment();
        universityFragment = new UniversityFragment();


        //Setting the newsfeed fragment as the deafult one on the home page
        setFragment(newsFeedFragment);

        Bundle bundle = getIntent().getExtras();
        String isFromNotification = "false";
        if (bundle != null) {
            isFromNotification = getIntent().getExtras().getString("isFromNotification", "false");
            if (isFromNotification.equals("true")) {
                bottomNavigation.getMenu().findItem(R.id.profile_notification).setChecked(true);
                setFragment(notificationFragment);
            } else {
                setFragment(newsFeedFragment);
            }
        } else {
            setFragment(newsFeedFragment);
        }

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.newsfeed_fragment:
                        setFragment(newsFeedFragment);
                        return true;

                    case R.id.profile_notification:
                        setFragment(notificationFragment);
                        return true;

                    case R.id.profile_friends:
                        setFragment(friendsFragment);
                        return true;

                    case R.id.university:
                        setFragment(universityFragment);
                        return true;

                    case R.id.profile_fragment:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class).putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()));
                        return true;
                }
                return false;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UploadActivity.class));
            }
        });
        icon_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }

//    @Override
//    public void onBackPressed() {
//        if (bottomNavigation.getSelectedItemId() == R.id.newsfeed_fragment) {
//            super.onBackPressed();
//            finish();
//        } else {
//            bottomNavigation.setSelectedItemId(R.id.newsfeed_fragment);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.our_services:
                startActivity(new Intent(MainActivity.this, OurServices.class));
                break;

            case R.id.contact_us:
                startActivity(new Intent(MainActivity.this, OurServices.class));
                break;

            case R.id.logout: {
                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                alert.setIcon(R.drawable.icon_background_after_comment)
                        .setTitle("Are you sure you want to sign out:")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        FirebaseAuth.getInstance().signOut();
                                        MainActivity.this.finish();
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {

                                    }
                                });
                alert.show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public <sharedPreferences> void clear(){
        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("SHAREDPREFERENCENAME",this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }
    private void onLogoutClicked() {

        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        Call<Integer> call = userInterface.onLogoutCLicked(new MainActivity.UserInfo(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() != null && response.body() == 1) {
                    MainActivity.this.finish();
                } else {
                    Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    public class UserInfo {
        String uid;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public UserInfo(String uid){

        }

    }
}
