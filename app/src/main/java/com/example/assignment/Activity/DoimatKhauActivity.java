package com.example.assignment.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.assignment.R;
import com.example.assignment.util.server;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class DoimatKhauActivity extends AppCompatActivity {
    EditText edPassOld,edPassNew,edConfirm;
    TextInputLayout tilPassOld,tilPassNew,tilConfirm;
    Button btnSubmit,btnCancel;
    String pass,username;
    int a=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doimat_khau);
        Anhxa();
        onClickBtn();
    }

    private void onClickBtn() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passOld = edPassOld.getText().toString();
                String passNew = edPassNew.getText().toString();
                String passConfirm = edConfirm.getText().toString();
                validate(passOld,passNew,passConfirm);
                if(a>0){
                    RequestQueue requestQueue = Volley.newRequestQueue(DoimatKhauActivity.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server.duongdandoimatkhau, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DoimatKhauActivity.this);
                            builder.setTitle("Thông báo");
                            builder.setMessage("Đổi mật khẩu thành công");
                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    Intent intent = new Intent(DoimatKhauActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String,String> param = new HashMap<>();
                            param.put("username",username );
                            param.put("passold", passOld);
                            param.put("passnew", passNew);
                            return param;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoimatKhauActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Anhxa() {
        edPassOld = findViewById(R.id.pass_oldpass);
        edPassNew = findViewById(R.id.pass_newpass);
        edConfirm = findViewById(R.id.pass_newpasscheck);

        btnSubmit = findViewById(R.id.pass_btnsave);
        btnCancel = findViewById(R.id.pass_btncancel);

        tilPassOld = findViewById(R.id.pass_tilOldpass);
        tilPassNew = findViewById(R.id.pass_tilnewpass);
        tilConfirm = findViewById(R.id.pass_tilnewpasscheck);

        SharedPreferences spf = getSharedPreferences("THONGTIN",MODE_PRIVATE);
        username = spf.getString("USERNAME", "");
        String username1 ;
        Intent intent = getIntent();
        username1 = intent.getStringExtra("pass");

        if(username== null){
            username = username1;
        }
    }
    private int validate(String passOld,String passNew,String confirm){
        if(passOld.length()==0){
            tilPassOld.setError("Chưa nhập mật khẩu cũ");
            a=-1;
        }else {
            tilPassOld.setError("");
            a++;
        }
        if(passNew.length()==0){
            tilPassNew.setError("Chưa nhập mật khẩu mới");
            a=-1;
        }else {
            tilPassNew.setError("");
            a++;
        }
        if(confirm.length()==0){
            tilConfirm.setError("Chưa xác nhận mật khẩu");
            a=-1;
        }else {
            tilConfirm.setError("");
            a++;
        }
        if(!passNew.equals(confirm)){
            tilConfirm.setError("Mật khẩu xác nhận không đúng");
            a=-1;
        }else {
            tilConfirm.setError("");
            a++;
        }
        return a;
    }
}