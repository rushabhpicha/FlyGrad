package com.example.application.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.application.R;
import com.example.application.adapter.SearchAdapter;
import com.example.application.model.User;
import com.example.application.rest.ApiClient;
import com.example.application.rest.services.UserInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView searchRecy;
    SearchAdapter searchAdapter;
    List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = findViewById(R.id.toolbar);
        searchRecy = findViewById(R.id.search_recy);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this,MainActivity.class));
            }
        });
        searchAdapter = new SearchAdapter(SearchActivity.this,users);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
        searchRecy.setLayoutManager(layoutManager);
        searchRecy.setAdapter(searchAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setIconified(false);
        ((EditText) searchView.findViewById(R.id.search_src_text)).setTextColor(getResources().getColor(R.color.hint_color));
        ((EditText) searchView.findViewById(R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.hint_color));
        ((ImageView) searchView.findViewById(R.id.search_close_btn)).setImageResource(R.drawable.icon_clear);
        searchView.setQueryHint("Search People ");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFromDb(query, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() > 1) {
                    searchFromDb(query, false);
                } else {
                    users.clear();
                    searchAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        return true;
    }

    private void searchFromDb(String query, boolean b) {
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("keyword", query);

        Call<List<User>> call = userInterface.search(params);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {

                users.clear();
                users.addAll(response.body());
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {

            }
        });
    }
}
