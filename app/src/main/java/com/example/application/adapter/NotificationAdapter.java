package com.example.application.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.activity.FullPostActivity;
import com.example.application.activity.ProfileActivity;
import com.example.application.model.NotificationModel;
import com.example.application.util.AgoDateParse;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Context context;
    List<NotificationModel> notificationModels;

    public NotificationAdapter(Context context, List<NotificationModel> notificationModels) {
        this.context = context;
        this.notificationModels = notificationModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        /*
                1. liked your post
                2. commented on your post
                3. replied on your comment
                4. Send you friend Request
                5. Accepted your friend Request

         */

        final NotificationModel notificationModel = notificationModels.get(position);

        if (notificationModel.getType().equals("1")) {
            holder.notificationTitle.setText(notificationModel.getName() + " liked your post");
        } else if (notificationModel.getType().equals("2")) {
            holder.notificationTitle.setText(notificationModel.getName() + " commented your post");
        } else if (notificationModel.getType().equals("3")) {
            holder.notificationTitle.setText(notificationModel.getName() + " replied on your comment");
        } else if (notificationModel.getType().equals("4")) {
            holder.notificationTitle.setText(notificationModel.getName() + " send you friend request");
        } else {
            holder.notificationTitle.setText(notificationModel.getName() + " acceped your friend request");
        }

            holder.notificationBody.setText(notificationModel.getPost()+"");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FullPostActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isLoadFromNetwork", true);
                    bundle.putString("postId", notificationModel.getPostId());
                    intent.putExtra("postBundle", bundle);
                    context.startActivity(intent);
                }
            });

            try {
                if (!notificationModel.getProfileUrl().isEmpty()) {
                    Picasso.with(context).load(notificationModel.getProfileUrl()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.img_default_user).into(holder.notificationSenderProfile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(context).load(notificationModel.getProfileUrl()).placeholder(R.drawable.img_default_user).into(holder.notificationSenderProfile);
                        }
                    });
                }
            }
        catch(Exception e){
            e.printStackTrace();
        }
        try {
            holder.notificationDate.setText(AgoDateParse.getTimeAgo(AgoDateParse.getTimeInMillsecond(notificationModel.getNotificationTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView notificationSenderProfile;
        TextView notificationTitle;
        TextView notificationBody;
        TextView notificationDate;

        ViewHolder(View view) {
            super(view);
            notificationSenderProfile = view.findViewById(R.id.notfication_sender_profile);
            notificationTitle = view.findViewById(R.id.notification_title);
            notificationBody = view.findViewById(R.id.notification_body);
            notificationDate = view.findViewById(R.id.notification_date);
        }
    }
}

