package com.example.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.model.CommentModel;
import com.example.application.util.AgoDateParse;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

public class SubCommentAdapter extends RecyclerView.Adapter<SubCommentAdapter.ViewHolder> {
    Context context;
    List<CommentModel.Comment> comments;


    public SubCommentAdapter(Context context, List<CommentModel.Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CommentModel.Comment result = comments.get(position);
        holder.commentPerson.setText(result.getName());
        holder.commentBody.setText(result.getComment());

        if (!result.getProfileUrl().equals("")) {
            Picasso.with(context).load(result.getProfileUrl()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.img_default_user).into(holder.commentProfile, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(result.getProfileUrl()).placeholder(R.drawable.img_default_user).into(holder.commentProfile);
                }
            });
        }

        try {
            holder.commentDate.setText(AgoDateParse.getTimeAgo(AgoDateParse.getTimeInMillsecond(result.getCommentDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView commentProfile;
        TextView commentPerson;
        ImageView optionId;
        TextView commentBody;
        TextView commentDate;
        TextView replyTxt;
        TextView moreComments;
        ImageView subCommentProfile;
        TextView subCommentPerson;
        TextView subCommentBody;
        TextView subCommentDate;
        LinearLayout subCommentSection;

        ViewHolder(View view) {
            super(view);
            subCommentSection = view.findViewById(R.id.sub_comment_section);
//            subCommentDate = view.findViewById(R.id.sub_comment_date);
//            subCommentBody = view.findViewById(R.id.sub_comment_body);
//            subCommentPerson = view.findViewById(R.id.sub_comment_person);
//            subCommentProfile = view.findViewById(R.id.sub_comment_profile);
            moreComments = view.findViewById(R.id.more_comments);
            replyTxt = view.findViewById(R.id.reply_txt);
            commentDate = view.findViewById(R.id.comment_date);
            commentBody = view.findViewById(R.id.comment_body);
            optionId = view.findViewById(R.id.option_id);
            commentPerson = view.findViewById(R.id.comment_person);
            commentProfile = view.findViewById(R.id.comment_profile);
        }
    }
}

