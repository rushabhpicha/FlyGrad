package com.example.application.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.activity.SearchActivity;
import com.example.application.adapter.FriendAdapter;
import com.example.application.model.FriendsModel;
import com.example.application.model.User;
import com.example.application.rest.ApiClient;
import com.example.application.rest.services.UserInterface;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsFragment extends Fragment {
    Toolbar toolbar;
    RecyclerView searchRecy;
    FriendAdapter friendAdapter;
    List<User> users = new ArrayList<>();
    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        searchRecy = view.findViewById(R.id.friends_rcy);

        friendAdapter = new FriendAdapter(context,users);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        searchRecy.setLayoutManager(layoutManager);
        searchRecy.setAdapter(friendAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getListData();
    }

    private void getListData() {
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Map<String, String> params = new HashMap<String, String>();

        Call<List<User>> call = userInterface.search(params);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {

                users.clear();
                users.addAll(response.body());
                friendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onPause() {
        super.onPause();

        users.clear();
        friendAdapter.notifyDataSetChanged();
    }
}

