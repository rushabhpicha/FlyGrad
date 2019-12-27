package com.example.application.rest.services;



import com.example.application.activity.LoginActivity;
import com.example.application.activity.MainActivity;
import com.example.application.activity.ProfileActivity;
import com.example.application.fragment.bottomsheets.CommentBottomSheet;
import com.example.application.model.CommentModel;
import com.example.application.model.NotificationModel;
import com.example.application.model.PostModel;
import com.example.application.model.UniversityModel;
import com.example.application.model.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface UserInterface {
    @POST("login")
    Call<Integer> signin(@Body LoginActivity.UserInfo userInfo);

    @POST("insertlogin")
    Call<Integer> insertlogin(@Body LoginActivity.UserDetails userDetails);

    @GET("loadownprofile")
    Call<User> loadownProfile(@QueryMap Map<String, String> params);

    @GET("loadothersprofile")
    Call<User> loadothersProfile(@QueryMap Map<String, String> params);

    @POST("poststatus")
    Call<Integer> uploadStatus(@Body MultipartBody requestBody);

    @GET("search")
    Call<List<User>> search(@QueryMap Map<String, String> params);

    @GET("loadfriends")
    Call<List<User>> loadfriends(@QueryMap Map<String, String> params);

    @GET("profiletimeline")
    Call<List<PostModel>> getProfilePosts(@QueryMap Map<String, String> params);

    @GET("gettimelinepost")
    Call<List<PostModel>> getTimeline(@QueryMap Map<String, String> params);

    @POST("likeunlike")
    Call<Integer> likeUnlike(@Body MultipartBody requestBody);

    @POST("postcomment")
    Call<CommentModel> postComment(@Body CommentBottomSheet.AddComment addComment);

    @GET("retreivetopcomment")
    Call<CommentModel> retreiveTopComments(@QueryMap Map<String, String> params);

    @GET("retreivelowlevelcomment")
    Call<List<CommentModel.Comment>> retreiveLowLevelComments(@QueryMap Map<String, String> params);

    @GET("getnotification")
    Call<List<NotificationModel>> getNotification(@QueryMap Map<String, String> params);

    @POST("updateProfile")
    Call<Integer> updateProfile(@Body ProfileActivity.UserInfo userInfo);

    @GET("loaduniversities")
    Call<List<UniversityModel>> loadUniversities(@QueryMap Map<String, String> params);

    @GET("details")
    Call<PostModel> getPostDetails(@QueryMap Map<String, String> params);

    @POST("logout")
    Call<Integer> onLogoutCLicked(@Body MainActivity.UserInfo userInfo);

}
