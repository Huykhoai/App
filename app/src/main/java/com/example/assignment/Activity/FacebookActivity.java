package com.example.assignment.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.assignment.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class FacebookActivity extends AppCompatActivity {
   LoginButton loginButton;
   CallbackManager callbackManager;//lắng nghe xem có chương trình nào gọi không
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        callbackManager = CallbackManager.Factory.create();//khởi tạo
        loginButton = findViewById(R.id.btn_fb_login);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //login thành công
                Toast.makeText(FacebookActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                //cancel
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Toast.makeText(FacebookActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
  }
    //lấy về kết quả sau khi chuyển qua activity khác

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //sau khi login xong,truyền dữ liệu qua activity mới
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}