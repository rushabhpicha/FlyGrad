package com.example.application.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.activity.MainActivity;
import com.example.application.adapter.NotificationAdapter;
import com.example.application.model.NotificationModel;
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

public class NotificationFragment extends Fragment {

    Context context;
    RecyclerView notificationRecy;
    TextView defaultTextView;
    NotificationAdapter notificationAdapter;
    List<NotificationModel> notificationModels = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        notificationRecy = view.findViewById(R.id.notification_recy);
        defaultTextView = view.findViewById(R.id.defaultTextView);
        notificationAdapter = new NotificationAdapter(context, notificationModels);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        notificationRecy.setLayoutManager(linearLayoutManager);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getNotificationInFragment();
    }

    private void getNotificationInFragment() {
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        Call<List<NotificationModel>> call = userInterface.getNotification(params);
        call.enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                if(response.body().size()>0){
                    notificationModels.addAll(response.body());
                    notificationRecy.setAdapter(notificationAdapter);
                }else{
                    defaultTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<NotificationModel>> call, Throwable t) {
                Toast.makeText(context,"Something went wrong !",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        notificationModels.clear();
        notificationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context.startActivity(new Intent(context, MainActivity.class));
    }
}
