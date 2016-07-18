package com.hoangtv.authenticationdemo1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Result extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.txt_info)
    TextView tvInfo;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.btn_update_profile)
    Button btnUpdateProfile;
    @BindView(R.id.btn_get_profile)
    Button btnGetProfile;
    @BindView(R.id.btn_update_email)
    Button btnUpdateEmail;
    @BindView(R.id.btn_update_password)
    Button btnUpdatePassword;
    @BindView(R.id.btn_reset_password)
    Button btnResetPassword;
    @BindView(R.id.btn_get_provide)
    Button btnGetProvide;
    @BindView(R.id.btn_delete_user)
    Button btnDeleteUser;
    @BindView(R.id.btn_re_authenticate)
    Button btnReAuthenticate;
    FirebaseUser user;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        btnLogout.setOnClickListener(this);
        btnGetProfile.setOnClickListener(this);
        btnDeleteUser.setOnClickListener(this);
        btnGetProvide.setOnClickListener(this);
        btnUpdateProfile.setOnClickListener(this);
        btnUpdatePassword.setOnClickListener(this);
        btnUpdateEmail.setOnClickListener(this);
        btnResetPassword.setOnClickListener(this);
        btnReAuthenticate.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_logout:
                auth.signOut();
                this.finish();
                break;

            case R.id.btn_get_profile:
                showProgressDialog();
                getProfileUser();
                break;

            case R.id.btn_update_profile:
                showProgressDialog();
                updateProfile();
                break;

            case R.id.btn_get_provide:
                showProgressDialog();
                getProvide();
                hideProgressDialog();
                break;

            case R.id.btn_update_email:
                showProgressDialog();
                updateEmail();
                break;

            case R.id.btn_update_password:
                showProgressDialog();
                updatePassword();
                break;

            case R.id.btn_reset_password:
                showProgressDialog();
                resetPassword();
                break;

            case R.id.btn_re_authenticate:
                showProgressDialog();
                reAuthenticate();
                break;


            case R.id.btn_delete_user:
                showProgressDialog();
                deleteUser();
                break;
        }
    }

    private void reAuthenticate() {
        AuthCredential credential = EmailAuthProvider.getCredential("Hoangtv27@gmail.com", "12345678");
        user.reauthenticate(credential).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("HoangTV", "User re-authenticated.");
                hideProgressDialog();
            }
        });
    }

    private void deleteUser() {
        user.delete().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("HoangTV", "Delele User");
                    hideProgressDialog();
                }
            }
        });
    }

    private void resetPassword() {
        final String emailAddress = "hoangtv27@gmail.com";
        auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("HoangTV", "Reset password:" + emailAddress);
                }
                hideProgressDialog();
            }
        });
    }

    private void updatePassword() {
        String newPassword = "12345678";
        user.updatePassword(newPassword).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("HoangTV", "Update Password Success");
                }
                hideProgressDialog();
            }
        });
    }

    private void updateEmail() {
        user.updateEmail("hoangtv27@gmail.com").addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("HoangTV", "Email updated");
                }
                hideProgressDialog();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void getProfileUser() {
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String uid = user.getUid();
            tvInfo.setText("Name: " + name + "\n"
                    + "Email: " + email + "\n"
                    + "U ID: " + uid + "\n");
        }
        hideProgressDialog();
    }

    @SuppressLint("SetTextI18n")
    private void getProvide() {
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {

                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();

                tvInfo.setText("Name: " + name + "\n"
                        + "Email: " + email + "\n"
                        + "provide ID: " + providerId + "\n");
            }
        }
    }

    private void updateProfile() {
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName("HoangTV")
                .setPhotoUri(Uri.parse("https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcRoV-jiAmilnoV88PuoTBjakR8DfYoeaWdfZCG_5F_VBuLLTBmvEw"))
                .build();
        user.updateProfile(profileChangeRequest).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("HoangTV", "User profile updated");
                }
                hideProgressDialog();
            }
        });

    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
}
