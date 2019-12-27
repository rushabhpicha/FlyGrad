package com.example.application.fragment.bottomsheets;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.adapter.SubCommentAdapter;
import com.example.application.model.CommentModel;
import com.example.application.model.PostModel;
import com.example.application.rest.ApiClient;
import com.example.application.rest.services.UserInterface;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCommentBottomSheet extends BottomSheetDialogFragment {
        Context context;
        TextView commentsTxt;
        LinearLayout topSection;
        RecyclerView commentRecy;
        EditText commentEdittext;
        ImageView commentSend;
        RelativeLayout commentSendWrapper;
        LinearLayout commentTopWrapper;

        boolean isFlagZero = true;
        PostModel postModel;
        SubCommentAdapter subCommentAdapter;
        List<CommentModel.Comment> results = new ArrayList<>();
        CommentModel.Comment commentModel;
        boolean isKeypadOpened = false;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            this.context = context;
        }

        @SuppressLint("RestrictedApi")
        @Override
        public void setupDialog(final Dialog dialog, int style) {
            super.setupDialog(dialog, style);
            View view = View.inflate(context, R.layout.bottom_sheet_layout, null);

            postModel = Parcels.unwrap(getFragmentManager().findFragmentByTag("commentFragment").getArguments().getParcelable("postModel"));
            commentModel = Parcels.unwrap(getFragmentManager().findFragmentByTag("commentFragment").getArguments().getParcelable("commentModel"));
            isKeypadOpened = getFragmentManager().findFragmentByTag("commentFragment").getArguments().getBoolean("openkeyBoard", false);

            commentsTxt = view.findViewById(R.id.comments_txt);
            topSection = view.findViewById(R.id.top_section);
            commentRecy =view.findViewById(R.id.comment_recy);
            commentEdittext = view.findViewById(R.id.comment_edittext);
            commentSend = view.findViewById(R.id.comment_send);
            commentSendWrapper = view.findViewById(R.id.comment_send_wrapper);
            commentTopWrapper = view.findViewById(R.id.comment_top_wrapper);

            dialog.setContentView(view);
            View view1 = (View) view.getParent();
            view1.setBackgroundColor(Color.TRANSPARENT);
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    BottomSheetDialog dialog1 = (BottomSheetDialog) dialog;
                    FrameLayout bottomsheet = dialog1.findViewById(R.id.design_bottom_sheet);
                    BottomSheetBehavior.from(bottomsheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            });
            subCommentAdapter = new SubCommentAdapter(context, results);

            if (postModel.getCommentCount().equals("0") || postModel.getCommentCount().equals("1")) {
                commentsTxt.setText(postModel.getCommentCount() + "Comment");
            } else {
                commentsTxt.setText(postModel.getCommentCount() + "Comments");
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            commentRecy.setLayoutManager(linearLayoutManager);
            commentsTxt.setText("Replies");
            retriveComments();
            commentEdittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Drawable img1 = getResources().getDrawable(R.drawable.icon_before_comment_send);
                    Drawable img2 = getResources().getDrawable(R.drawable.icon_after_comment_send);

                    if (charSequence.toString().trim().length() == 0) {
                        isFlagZero = true;
                        commentSendWrapper.setBackgroundResource(R.drawable.icon_background_before_comment);
                        loadImageWithAnimation(context, img1);
                    } else if (charSequence.toString().trim().length() != 0 && isFlagZero) {
                        isFlagZero = false;
                        commentSendWrapper.setBackgroundResource(R.drawable.icon_background_after_comment);
                        loadImageWithAnimation(context, img2);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            commentSendWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFlagZero) {
                        return;
                    }
                    final String comment = commentEdittext.getText().toString().trim();
                    commentEdittext.setText("");
                    ((InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
                    CommentBottomSheet.AddComment addComment = new CommentBottomSheet.AddComment(comment, FirebaseAuth.getInstance().getCurrentUser().getUid(), postModel.getPostId(), commentModel.getCid(), "1", "1", postModel.getPostUserId(), commentModel.getCommentBy());
                    Call<CommentModel> call = userInterface.postComment(addComment);
                    call.enqueue(new Callback<CommentModel>() {
                        @Override
                        public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                            if (response.body().getResult().size() > 0) {
                                Toast.makeText(context, "Comment Successful", Toast.LENGTH_SHORT).show();
                                int commentCount = Integer.parseInt(postModel.getCommentCount());
                                commentsTxt.setText(commentCount + " Comments");

                                results.add(response.body().getResult().get(0).getComment());
                                int position = results.indexOf(response.body().getResult().get(0).getComment());
                                subCommentAdapter.notifyItemInserted(position);
                                commentRecy.scrollToPosition(position);
                            } else {
                                Toast.makeText(context, "Something went Wrong !", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<CommentModel> call, Throwable t) {
                            Toast.makeText(context, "Something went Wrong !", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

        }

        private void retriveComments() {

            UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put("postId", postModel.getPostId());
            params.put("commentId", commentModel.getCid());
            Call<List<CommentModel.Comment>> call = userInterface.retreiveLowLevelComments(params);
            call.enqueue(new Callback<List<CommentModel.Comment>>() {
                @Override
                public void onResponse(Call<List<CommentModel.Comment>> call, Response<List<CommentModel.Comment>> response) {
                    if (response.body().size() > 0) {
                        results.addAll(response.body());
                        commentRecy.setAdapter(subCommentAdapter);
                    } else {
                        Toast.makeText(context, "No Comments Found !", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<CommentModel.Comment>> call, Throwable t) {
                    Toast.makeText(context, "Something went Wrong !", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void loadImageWithAnimation(Context c, final Drawable img1) {

            final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.zoom_out);
            final Animation anim_in = AnimationUtils.loadAnimation(c, R.anim.zoom_in);

            anim_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    commentSend.setImageDrawable(img1);
                    anim_in.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }
                    });

                    commentSend.startAnimation(anim_in);
                }
            });
            commentSend.startAnimation(anim_out);
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
//            unbinder.unbind();
        }
}
