package com.example.application.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.R;
import com.example.application.adapter.PostAdapter;
import com.example.application.fragment.bottomsheets.CommentBottomSheet;
import com.example.application.model.PostModel;
import com.example.application.rest.ApiClient;
import com.example.application.rest.services.UserInterface;
import com.example.application.util.AgoDateParse;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullPostActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView postUserImage;
    TextView postUserName;
    ImageView privacy;
    TextView postDate;
    TextView status;
    ImageView postImage;
    RelativeLayout topRel;
    ImageView likeImg;
    TextView likeTxt;
    LinearLayout likeSection;
    TextView commentTxt;
    LinearLayout commentSection;
    CardView reactionCard;
    EditText comment;
    ImageView commentSend;
    LinearLayout commentBottomPart;
    RelativeLayout topHideShow;

    PostModel postModel;

    boolean isFlagZero = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_post);

        toolbar = findViewById(R.id.toolbar);
        postUserName = findViewById(R.id.post_user_name);
        postUserImage = findViewById(R.id.post_user_image);
        privacy = findViewById(R.id.privacy);
        postDate = findViewById(R.id.post_date);
        status = findViewById(R.id.status);
        topRel = findViewById(R.id.top_rel);
//        likeImg = findViewById(R.id.like_img);
//        likeTxt = findViewById(R.id.like_txt);
//        likeSection = findViewById(R.id.like_section);
        commentTxt = findViewById(R.id.comment_txt);
        commentSection = findViewById(R.id.comment_section);
        reactionCard = findViewById(R.id.reaction_card);
        comment = findViewById(R.id.comment);
        commentSend = findViewById(R.id.comment_send);
        commentBottomPart = findViewById(R.id.comment_bottom_part);
        topHideShow = findViewById(R.id.top_hide_show);
        postImage = findViewById(R.id.post_image);



        boolean isLoadFromNetwrok = getIntent().getBundleExtra("postBundle").getBoolean("isLoadFromNetwork",false);
        String postId = getIntent().getBundleExtra("postBundle").getString("postId","0");
        if(isLoadFromNetwrok){
            getPostDataDetails(postId);
        }else{
            postModel = Parcels.unwrap(getIntent().getBundleExtra("postBundle").getParcelable("postModel"));
            setData(postModel);
        }


        // Setting the tool with back button
        setSupportActionBar(toolbar);
        setTitle("");;
        toolbar.setNavigationIcon(R.drawable.arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }

    private void getPostDataDetails(String postId) {
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Map<String,String> params = new HashMap<>();
        params.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
        params.put("postId",postId);
        Call<PostModel> call = userInterface.getPostDetails(params);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                setData(response.body());
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                Toast.makeText(FullPostActivity.this,"Something went wrong !",Toast.LENGTH_SHORT).show();
                FullPostActivity.super.onBackPressed();
            }
        });
    }

//    private void operationLike( PostModel postModel) {
//        likeImg.setImageResource(R.drawable.icon_like_selected);
//        int count = Integer.parseInt(postModel.getLikeCount());
//        count++;
//        if (count == 0 || count == 1) {
//            likeTxt.setText(count + " Like");
//        } else {
//            likeTxt.setText(count + " Likes");
//        }
//
//        postModel.setLikeCount(count + "");
//        postModel.setLiked(true);
//    }
//
//    private void operationUnlike( PostModel postModel) {
//
//
//        likeImg.setImageResource(R.drawable.icon_like);
//        int count = Integer.parseInt(postModel.getLikeCount());
//        count--;
//
//        if (count == 0 || count == 1) {
//            likeTxt.setText(count + " Like");
//        } else {
//            likeTxt.setText(count + " Likes");
//        }
//        postModel.setLikeCount(count + "");
//        postModel.setLiked(false);
//
//
//    }
    private void setData(final PostModel postModel) {


//        if(postModel.getCommentCount().equals("0") || postModel.getCommentCount().equals("1")){
//            commentTxt.setText(postModel.getCommentCount()+ "Comment");
//        }else{
//            commentTxt.setText(postModel.getCommentCount()+ "Comments");
//        }

//        if (postModel.isLiked()) {
//            likeImg.setImageResource(R.drawable.icon_like_selected);
//        } else {
//            likeImg.setImageResource(R.drawable.icon_like);
//        }

//        if(postModel.getLikeCount().equals("0") || postModel.getLikeCount().equals("1")){
//            likeTxt.setText(postModel.getLikeCount()+" Like");
//        }else{
//            likeTxt.setText(postModel.getLikeCount()+" Likes");
//        }
        commentSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialogFragment bottomSheetDialogFragment = new CommentBottomSheet();
                Bundle bundle = new Bundle();
                bundle.putParcelable("postModel",Parcels.wrap(postModel));
                bottomSheetDialogFragment.setArguments(bundle);
                FragmentActivity fragmentActivity = (FragmentActivity) FullPostActivity.this;
                bottomSheetDialogFragment.show(fragmentActivity.getSupportFragmentManager(),"commentFragment");
            }
        });
//        likeSection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                likeSection.setEnabled(false);
//                if (!postModel.isLiked()) {
//                    //like operation in here
//                    operationLike( postModel);
//
//                    UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
//                    Call<Integer> call = userInterface.likeUlike(new PostAdapter.AddLike(FirebaseAuth.getInstance().getCurrentUser().getUid(), postModel.getPostId(), postModel.getPostUserId(), 1));
//                    call.enqueue(new Callback<Integer>() {
//                        @Override
//                        public void onResponse(Call<Integer> call, Response<Integer> response) {
//                            likeSection.setEnabled(true);
//                            if(response.body().equals("0")){
//                                operationUnlike(postModel);
//                                Toast.makeText(FullPostActivity.this,"Something went wrong ! ",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Integer> call, Throwable t) {
//                            likeSection.setEnabled(true);
//                            operationUnlike(postModel);
//                            Toast.makeText(FullPostActivity.this,"Something went wrong ! ",Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else {
//                    operationUnlike( postModel);
//                    //unlike operation in here
//
//
//                    UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
//                    Call<Integer> call = userInterface.likeUlike(new PostAdapter.AddLike(FirebaseAuth.getInstance().getCurrentUser().getUid(), postModel.getPostId(), postModel.getPostUserId(), 0));
//                    call.enqueue(new Callback<Integer>() {
//                        @Override
//                        public void onResponse(Call<Integer> call, Response<Integer> response) {
//                            likeSection.setEnabled(true);
//                            if(response.body()==null){
//                                operationLike(postModel);
//                                Toast.makeText(FullPostActivity.this,"Something went wrong ! ",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Integer> call, Throwable t) {
//                            likeSection.setEnabled(true);
//                            operationLike(postModel);
//                            Toast.makeText(FullPostActivity.this,"Something went wrong ! ",Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        });


        postUserName.setText(postModel.getName());
        status.setText(postModel.getPost());
        if (!postModel.getProfileUrl().isEmpty()) {
            Picasso.with(FullPostActivity.this).load(postModel.getProfileUrl()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_image_placeholder).into(postUserImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(FullPostActivity.this).load(postModel.getProfileUrl()).placeholder(R.drawable.default_image_placeholder).error(R.drawable.default_image_placeholder).into(postUserImage);
                }
            });
        }
        if(!postModel.getStatusImage().isEmpty()){
            Picasso.with(FullPostActivity.this).load(ApiClient.BASE_URL_1+postModel.getStatusImage()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_image_placeholder).into(postImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(FullPostActivity.this).load(ApiClient.BASE_URL_1+postModel.getStatusImage()).placeholder(R.drawable.default_image_placeholder).error(R.drawable.default_image_placeholder).into(postImage);
                }
            });
        }else{
            postImage.setVisibility(View.GONE);
        }

        try {
            postDate.setText(AgoDateParse.getTimeAgo(AgoDateParse.getTimeInMillsecond(postModel.getStatusTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(postModel.getPrivacy().equalsIgnoreCase("0")){
            privacy.setImageResource(R.drawable.icon_friends);
        }else if(postModel.getPrivacy().equalsIgnoreCase("1")){
            privacy.setImageResource(R.drawable.icon_onlyme);
        }else{
            privacy.setImageResource(R.drawable.icon_public);
        }
    }
}