package com.rayzhang.android.materialdesign;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FacebookActivity extends AppCompatActivity {
    private static final String TAG = FacebookActivity.class.getSimpleName();
    private ImageView mImgPhoto;
    private TextView mTextDescription;

    // FB
    private LoginManager loginManager;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        // init facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        // init LoginManager & CallbackManager
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();

        findViewById(R.id.mImgFBBut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Facebook Login
                loginFB();
            }
        });
        findViewById(R.id.mImgLogoutBut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Facebook Logout
                loginManager.logOut();
                Glide.with(FacebookActivity.this)
                        .load(R.drawable.ic_user_60dp)
                        .crossFade()
                        .into(mImgPhoto);
                mTextDescription.setText("");
            }
        });
        mImgPhoto = (ImageView) findViewById(R.id.mImgPhoto);
        mTextDescription = (TextView) findViewById(R.id.mTextDescription);

        // method_1.判斷用戶是否登入過
        if (Profile.getCurrentProfile() != null) {
            Profile profile = Profile.getCurrentProfile();
            // 取得用戶大頭照
            Uri userPhoto = profile.getProfilePictureUri(300, 300);
            String id = profile.getId();
            String name = profile.getName();
            Log.d(TAG, "Facebook userPhoto: " + userPhoto);
            Log.d(TAG, "Facebook id: " + id);
            Log.d(TAG, "Facebook name: " + name);
        }

        // method_2.判斷用戶是否登入過
        /*if (AccessToken.getCurrentAccessToken() != null) {
            Log.d(TAG, "Facebook getApplicationId: " + AccessToken.getCurrentAccessToken().getApplicationId());
            Log.d(TAG, "Facebook getUserId: " + AccessToken.getCurrentAccessToken().getUserId());
            Log.d(TAG, "Facebook getExpires: " + AccessToken.getCurrentAccessToken().getExpires());
            Log.d(TAG, "Facebook getLastRefresh: " + AccessToken.getCurrentAccessToken().getLastRefresh());
            Log.d(TAG, "Facebook getToken: " + AccessToken.getCurrentAccessToken().getToken());
            Log.d(TAG, "Facebook getSource: " + AccessToken.getCurrentAccessToken().getSource());
        }*/
    }

    private void loginFB() {
        // 設定FB login的顯示方式 ; 預設是：NATIVE_WITH_FALLBACK
        /**
         * 1. NATIVE_WITH_FALLBACK
         * 2. NATIVE_ONLY
         * 3. KATANA_ONLY
         * 4. WEB_ONLY
         * 5. WEB_VIEW_ONLY
         * 6. DEVICE_AUTH
         */
        loginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        // 設定要跟用戶取得的權限，以下3個是基本可以取得，不需要經過FB的審核
        List<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_friends");
        // 設定要讀取的權限
        loginManager.logInWithReadPermissions(this, permissions);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                /**
                 Facebook getApplicationId: 1263340450401125
                 Facebook getUserId: 228579214326556
                 Facebook getExpires: Sat Aug 12 11:55:25 GMT+08:00 2017
                 Facebook getLastRefresh: Tue Jun 13 11:57:15 GMT+08:00 2017
                 Facebook getToken: EAAR9AF7Gy2UBAEHvxir6gNDeozTFjL8JoQzoo9ZA2uxqDyi1lpexqVLFWNB6RpfLzh1SHveZBxzUGVY4ZAJR9TLGiCgwDW6ZAebq4NqgnckBc3ZCbE7ZA456CmTCeALp57wvTjPrr5O4KcUgRMrPalfVmwfoNlOFyIQtWZAZChOdhPT6XqNuCERRqyfmWSerKcWyeZCIXvi6U0zpm8H8pdOliqYR9qznesafLoIiLylLYqAZDZD
                 Facebook getSource: FACEBOOK_APPLICATION_WEB
                 Facebook getRecentlyGrantedPermissions: [public_profile, user_friends, email]
                 Facebook getRecentlyDeniedPermissions: []
                 */
                /*Log.d(TAG, "Facebook getApplicationId: " + loginResult.getAccessToken().getApplicationId());
                Log.d(TAG, "Facebook getUserId: " + loginResult.getAccessToken().getUserId());
                Log.d(TAG, "Facebook getExpires: " + loginResult.getAccessToken().getExpires());
                Log.d(TAG, "Facebook getLastRefresh: " + loginResult.getAccessToken().getLastRefresh());
                Log.d(TAG, "Facebook getToken: " + loginResult.getAccessToken().getToken());
                Log.d(TAG, "Facebook getSource: " + loginResult.getAccessToken().getSource());
                Log.d(TAG, "Facebook getRecentlyGrantedPermissions: " + loginResult.getRecentlyGrantedPermissions());
                Log.d(TAG, "Facebook getRecentlyDeniedPermissions: " + loginResult.getRecentlyDeniedPermissions());*/

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            if (response.getConnection().getResponseCode() == 200) {
                                Log.d(TAG, "Facebook JSONObject:" + object);
                                long id = object.getLong("id");
                                String name = object.getString("name");
                                String email = object.getString("email");
                                Log.d(TAG, "Facebook id:" + id);
                                Log.d(TAG, "Facebook name:" + name);
                                Log.d(TAG, "Facebook email:" + email);
                                // 取得用戶大頭照
                                Profile profile = Profile.getCurrentProfile();
                                // 設定大頭照大小
                                Uri userPhoto = profile.getProfilePictureUri(300, 300);
                                Log.d(TAG, "Facebook userPhoto: " + userPhoto);
                                Glide.with(FacebookActivity.this)
                                        .load(userPhoto.toString())
                                        .crossFade()
                                        .into(mImgPhoto);
                                mTextDescription.setText(String.format(Locale.TAIWAN, "Name:%s\nE-mail:%s", name, email));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                // https://developers.facebook.com/docs/android/graph?locale=zh_TW
                // 如果要取得email，需透過添加參數的方式來獲取(如下)
                // 不添加只能取得id & name
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Facebook onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "Facebook onError:" + error.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
