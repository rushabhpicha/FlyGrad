package com.example.application.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.example.application.rest.ApiClient;
import com.example.application.rest.services.UserInterface;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "GOOGLEACTIVITY";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser;

    private SignInButton signInButton;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set the dimensions of the sign-in button.
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        mFirebaseAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Signing you In... Please Wait !");

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public FirebaseUser getFirebaseAuth(){
        return mFirebaseUser;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            progressDialog.show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            Log.d("TAG", "User id already Logged In");
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
    public void signOut(){
        mFirebaseAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                LoginActivity.this.finish();
            }
        });
    }
    public void changeFirebaseUser(){
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseUser = null;
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            final FirebaseUser user = mFirebaseAuth.getCurrentUser();

                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    String userToken = instanceIdResult.getToken();

                                    String uid = user.getUid();
                                    String name = user.getDisplayName();
                                    String email = user.getEmail();
                                    String profileUrl = user.getPhotoUrl().toString();
                                    final String coverUrl = "";
                                    UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
                                    Call<Integer> call = userInterface.signin(new UserInfo(uid, name, email, profileUrl, coverUrl, userToken));
                                    call.enqueue(new Callback<Integer>() {
                                        @Override
                                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                                            if (response.body() == 1) {
                                                progressDialog.dismiss();
                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                                openAlertDialog();
                                                finish();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {
                                            progressDialog.dismiss();
                                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                            FirebaseAuth.getInstance().signOut();
                                        }
                                    });
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }

    private void openAlertDialog() {

        LayoutInflater factory = (LayoutInflater) LayoutInflater.from(LoginActivity.this);
        final View textEntryView = factory.inflate(R.layout.edit_profile, null);
        //text_entry is an Layout XML file containing two text field to display in alert dialog
        final EditText eGre = textEntryView.findViewById(R.id.edt_gre);
        final EditText eToefl = textEntryView.findViewById(R.id.edt_toefl);
        final EditText eWorkex = textEntryView.findViewById(R.id.edt_workex);
        final EditText eUgradcollege = textEntryView.findViewById(R.id.edt_ugradcollege);
        final EditText eUgradpercent = textEntryView.findViewById(R.id.edt_ugradpercent);
        final EditText eTechnicalpapers = textEntryView.findViewById(R.id.edt_technicalPapers);

        try {
            final int gre = Integer.parseInt(eGre.getText().toString());
            final int toefl = Integer.parseInt(eToefl.getText().toString());
            final int workex = Integer.parseInt(eWorkex.getText().toString());
            final int ugradpercent = Integer.parseInt(eUgradpercent.getText().toString());
            final int technicalpapers = Integer.parseInt(eTechnicalpapers.getText().toString());


        final String ugradcollege = String.valueOf(eUgradcollege.getText());

        final AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);

        alert.setIcon(R.drawable.icon_background_after_comment)
                .setTitle("Enter the Text:")
                .setView(textEntryView)
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
                                Call<Integer> call = userInterface.insertlogin(new UserDetails(gre,toefl,workex,ugradcollege,ugradpercent,technicalpapers));

                                call.enqueue(new Callback<Integer>() {
                                    @Override
                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                        if (response.body() == 1) {
                                            progressDialog.dismiss();
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                            FirebaseAuth.getInstance().signOut();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Integer> call, Throwable t) {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                    }
                                });
                            }
                        })
                .setNegativeButton("Skip for now!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                        });
        alert.show();
        }catch(NumberFormatException e){
            e.printStackTrace();
        }
    }

    public class UserInfo {
        String uid, name, email, profileUrl, coverUrl, userToken;

        public UserInfo(String uid, String name, String email, String profileUrl, String coverUrl, String userToken) {
            this.uid = uid;
            this.name = name;
            this.email = email;
            this.profileUrl = profileUrl;
            this.coverUrl = coverUrl;
            this.userToken = userToken;
        }
    }

    public class UserDetails {
        int gre, toefl, workex, ugradpercent, technicalpapers;
        String ugradcollege;

        public UserDetails(int gre, int toefl, int workex, String ugradcollege, int ugradpercent, int technicalpapers) {
            this.gre = gre;
            this.toefl = toefl;
            this.workex = workex;
            this.ugradpercent = ugradpercent;
            this.technicalpapers = technicalpapers;
            this.ugradcollege = ugradcollege;
        }
    }
}

