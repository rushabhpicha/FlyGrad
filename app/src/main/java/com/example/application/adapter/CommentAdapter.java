package com.example.application.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.fragment.bottomsheets.SubCommentBottomSheet;
import com.example.application.model.CommentModel;
import com.example.application.model.PostModel;
import com.example.application.util.AgoDateParse;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.ParseException;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;
    List<CommentModel.Result> results;
    PostModel postModel;

    public CommentAdapter(Context context, List<CommentModel.Result> results, PostModel postModel) {
        this.context = context;
        this.results = results;
        this.postModel = postModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final CommentModel.Result result = results.get(position);
        holder.commentPerson.setText(result.getComment().getName());
        holder.commentBody.setText(result.getComment().getComment());

        if (!result.getComment().getProfileUrl().equals("")) {
            Picasso.with(context).load(result.getComment().getProfileUrl()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.img_default_user).into(holder.commentProfile, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(result.getComment().getProfileUrl()).placeholder(R.drawable.img_default_user).into(holder.commentProfile);
                }
            });
        }

        try {
            holder.commentDate.setText(AgoDateParse.getTimeAgo(AgoDateParse.getTimeInMillsecond(result.getComment().getCommentDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(result.getComment().getHasSubComment().equals("1")){
            holder.subCommentSection.setVisibility(View.VISIBLE);
            int commentTotal = Integer.parseInt(result.getSubComments().getTotal());
            if(commentTotal==1){
                holder.moreComments.setVisibility(View.GONE);
            }else{
                holder.moreComments.setVisibility(View.VISIBLE);
                commentTotal--;
                holder.moreComments.setText("View "+commentTotal+" more comments");
            }

//            holder.subCommentBody.setText(result.getSubComments().getLastComment().get(0).getComment());
//            holder.subCommentPerson.setText(result.getSubComments().getLastComment().get(0).getName());

//            if (!result.getSubComments().getLastComment().get(0).getProfileUrl().equals("")) {
//                Picasso.with(context).load(result.getSubComments().getLastComment().get(0).getProfileUrl()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.img_default_user).into(holder.subCommentProfile, new com.squareup.picasso.Callback() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onError() {
//                        Picasso.with(context).load(result.getSubComments().getLastComment().get(0).getProfileUrl()).placeholder(R.drawable.img_default_user).into(holder.subCommentProfile);
//                    }
//                });
//            }

//            try {
//                holder.subCommentDate.setText(AgoDateParse.getTimeAgo(AgoDateParse.getTimeInMillsecond(result.getSubComments().getLastComment().get(0).getCommentDate())));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }


        }else{
            holder.subCommentSection.setVisibility(View.GONE);
        }
        holder.replyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialogFragment bottomSheetDialogFragment = new SubCommentBottomSheet();
                Bundle args = new Bundle();
                args.putParcelable("postModel", Parcels.wrap(postModel));
                args.putParcelable("commentModel", Parcels.wrap(results.get(position).getComment()));
                args.putBoolean("openkeyBoard",true);
                bottomSheetDialogFragment.setArguments(args);
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                bottomSheetDialogFragment.show(fragmentActivity.getSupportFragmentManager(), "commentFragment");
            }
        });

        holder.moreComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialogFragment bottomSheetDialogFragment = new SubCommentBottomSheet();
                Bundle args = new Bundle();
                args.putParcelable("postModel", Parcels.wrap(postModel));
                args.putParcelable("commentModel", Parcels.wrap(results.get(position).getComment()));
                args.putBoolean("openkeyBoard",false);
                bottomSheetDialogFragment.setArguments(args);
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                bottomSheetDialogFragment.show(fragmentActivity.getSupportFragmentManager(), "commentFragment");
            }
        });

    }

    @Override
    public int getItemCount() {
        return results.size();
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

