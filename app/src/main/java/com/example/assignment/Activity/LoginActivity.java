package com.example.assignment.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {
    EditText edName, edPass,edRegisterName,edRegisterPass,edRegisterCF;
    TextInputLayout tilName, tilPass,tilRegisterName,tilRegisterPass,tilRegisterCF;
    TextView register;
    Button btnLogin,btnRegister,btnCancel;
    CheckBox checkBox;
    int temp=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Anhxa();
        onclickButton();
    }
    private void openDialog(){
        final Dialog dialog = new Dialog(this);
        // Set the content view of the dialog to your XML layout
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_themsach);

        Window window = dialog.getWindow();
        if(window==null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        tilRegisterName= dialog.findViewById(R.id.dialog_register_til_masach);
        tilRegisterPass= dialog.findViewById(R.id.dialog_register_til_name);
        tilRegisterCF= dialog.findViewById(R.id.dialog_register_til_giathue);

        edRegisterName = dialog.findViewById(R.id.dialog_register_txtmasach);
        edRegisterPass = dialog.findViewById(R.id.dialog_register_txtname);
        edRegisterCF = dialog.findViewById(R.id.dialog_register_txtgiathue);

        btnRegister = dialog.findViewById(R.id.dialog_register_add);
        btnCancel = dialog.findViewById(R.id.dialog_register_cancel);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = edRegisterName.getText().toString();
                String pass = edRegisterPass.getText().toString();
                String cofirm = edRegisterCF.getText().toString();
                validate(name, pass,cofirm);
                if(temp==0){
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server.duongdanregister, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                           if(!response.equals("-1")){
                               Toast.makeText(LoginActivity.this, "Đăng kí thành công!", Toast.LENGTH_SHORT).show();
                               dialog.dismiss();
                           }else if(response.equals("0")){
                               Toast.makeText(LoginActivity.this, "Tên tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                           }else {
                               Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                           }
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
                            param.put("username", name);
                            param.put("pass", pass);
                            param.put("confirm", cofirm);
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
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void Anhxa() {
        edName = findViewById(R.id.login_edusername);
        edPass = findViewById(R.id.login_edpassword);
        register = findViewById(R.id.textDangki);
        btnLogin = findViewById(R.id.login_btnlogin);
        checkBox = findViewById(R.id.login_checkBox);
        tilName = findViewById(R.id.login_tilusername);
        tilPass = findViewById(R.id.login_tilpassword);

        SharedPreferences spf = getSharedPreferences("THONGTIN", MODE_PRIVATE);
        edName.setText(spf.getString("USERNAME", ""));
        edPass.setText(spf.getString("PASSWORD", ""));
        checkBox.setChecked(spf.getBoolean("REMEMBER", false));

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openDialog();
            }
        });
    }
    private void onclickButton(){
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = edName.getText().toString().trim();
                    String pass = edPass.getText().toString().trim();
                    if (validateLogin(name, pass) > 0) {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server.duongdanlogin, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            if (response.equals("1")) {
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("user", name);
                                startActivity(intent);
                                rememberUser(name, pass, checkBox.isChecked());
                            } else {
                                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("username", edName.getText().toString());
                            params.put("password", edPass.getText().toString());
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
                }
            });

            tilPass.setEndIconOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(edPass!= null){
                        if(edPass.getTransformationMethod()== null){
                            edPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }else {
                            edPass.setTransformationMethod(null);
                        }
                        edPass.setSelection(edPass.getText().length());
                    }
                }
            });

    }
    private void rememberUser(String u,String p, boolean status){
        SharedPreferences sharedPreferences =getSharedPreferences("THONGTIN", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!status){
            editor.clear();
        }else {
          editor.putString("USERNAME", u);
          editor.putString("PASSWORD" , p);
          editor.putBoolean("REMEMBER", status);
        }
        editor.commit();
    }

    private void validate(String name,String pass,String confirm){

        if(name.isEmpty()){
            tilRegisterName.setError("Chưa nhập tên");
            temp++;
        }else {
            tilRegisterName.setError("");
            temp=0;
        }
        if(pass.isEmpty()){
            tilRegisterPass.setError("Chưa nhập tên");
            temp++;
        }else {
            tilRegisterPass.setError("");
            temp=0;
        }
        if(confirm.isEmpty()){
            tilRegisterCF.setError("Chưa nhập tên");
            temp++;
        }else {
            tilRegisterCF.setError("");
            temp=0;
        }
        if(pass.equals(confirm)){
            tilRegisterCF.setError("");
            temp=0;
        }else {
            tilRegisterCF.setError("Mật khẩu xác nhận không đúng!");
            temp++;
        }
    }
    private int validateLogin(String name,String pass){
        int a=1;
        if (name.length()==0) {
            tilName.setError("Chưa nhập username");
            a=-1;
        } else {
            tilName.setError("");
            a++;
        }
        if(pass.length()==0){
            tilPass.setError("Chưa nhập pass");
            a=-1;
        }else {
            tilPass.setError("");
            a++;
        }
        return  a;
    }
}
