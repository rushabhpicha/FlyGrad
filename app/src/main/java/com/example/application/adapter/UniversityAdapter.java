package com.example.application.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.activity.ProfileActivity;
import com.example.application.model.FriendsModel;
import com.example.application.model.UniversityModel;
import com.example.application.model.User;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UniversityAdapter extends RecyclerView.Adapter<UniversityAdapter.ViewHolder> {
    Context context;
    List<UniversityModel> universityModels;
    public UniversityAdapter(Context context, List<UniversityModel> universityModels){
        this.context = context;
        this.universityModels = universityModels;
    }
    @NonNull
    @Override
    public UniversityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_list, parent, false);
        return new UniversityAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final UniversityModel universityModel = universityModels.get(position);
        holder.userName.setText(universityModel.getName());
        holder.top_rel.removeView(holder.userImage);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                im.hideSoftInputFromWindow(v.getWindowToken(),0);
//                context.startActivity(new Intent(context, ProfileActivity.class).putExtra("uid",user.getUid()));
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return universityModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName;
        RelativeLayout top_rel;
        public ViewHolder(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);
            top_rel = itemView.findViewById(R.id.top_rel);
        }
    }
}

