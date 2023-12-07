package com.example.assignment.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.assignment.Activity.LoginActivity;
import com.example.assignment.Activity.MainActivity;
import com.example.assignment.R;
import com.example.assignment.util.server;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {
    EditText edEmail, edPass;
    TextInputLayout tilEmail, tilPass;
    Button btnLogin;
    CheckBox checkBox;
    int temp=0;
    public LoginFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login, container, false);
        Anhxa(view);
        onclickButton();
        return view;
    }
    private void Anhxa(View view) {
        edEmail = view.findViewById(R.id.login_edemail);
        edPass = view.findViewById(R.id.login_edpassword);
        btnLogin = view.findViewById(R.id.login_btnlogin);
        checkBox = view.findViewById(R.id.login_checkBox);
        tilEmail = view.findViewById(R.id.login_tilemail);
        tilPass = view.findViewById(R.id.login_tilpassword);

        SharedPreferences spf = getActivity().getSharedPreferences("THONGTIN", Context.MODE_PRIVATE);
        edEmail.setText(spf.getString("EMAIL", ""));
        edPass.setText(spf.getString("PASSWORD", ""));
        checkBox.setChecked(spf.getBoolean("REMEMBER", false));


    }
    private void onclickButton(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString().trim();
                String pass = edPass.getText().toString().trim();
                if (validateLogin(email, pass) > 0) {
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server.duongdanlogin, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d( "onResponse: ",response);
                            if (Integer.parseInt(response.trim())>0) {
                                Toast.makeText(getActivity(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.putExtra("email", email);
                                intent.putExtra("pass", pass);
                                startActivity(intent);
                                rememberUser(email, pass, checkBox.isChecked());
                            }else if(Integer.parseInt(response.trim())==0){
                                Toast.makeText(getActivity(), "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), " Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                                Log.d( "passLOgin: ", response);
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
                            params.put("email", edEmail.getText().toString());
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
    private int validateLogin(String email,String pass){
        int a=1;
        if (email.length()==0) {
            tilEmail.setError("Chưa nhập Email");
            a=-1;
        } else {
            tilEmail.setError("");
            a++;
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tilEmail.setError("Email không hợp lệ");
                a=-1;
            } else {
                tilPass.setError("");
                a++;
            }
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
    private void rememberUser(String u,String p, boolean status){
        SharedPreferences sharedPreferences =getActivity().getSharedPreferences("THONGTIN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!status){
            editor.clear();
        }else {
            editor.putString("EMAIL", u);
            editor.putString("PASSWORD" , p);
            editor.putBoolean("REMEMBER", status);
        }
        editor.commit();
    }
}