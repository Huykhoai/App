package com.example.assignment.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.example.assignment.R;
import com.example.assignment.util.server;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;


public class RegisterFragment extends Fragment {
    EditText edRegisterName,edRegisterPass,edRegisterEmail;
    TextInputLayout tilRegisterName,tilRegisterPass,tilRegisterEmail;

    Button btnRegister;

    int temp=0;
   public RegisterFragment(){
   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        Anhxa(view);
        onClickButton();
        return view;
    }

    private void onClickButton() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edRegisterEmail.getText().toString();
                String name = edRegisterName.getText().toString();
                String pass = edRegisterPass.getText().toString();

                validate(name, pass,email);
                if(temp==0){
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server.duongdanregister, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("-1")){
                                Toast.makeText(getActivity(), "Đăng kí thất bại!", Toast.LENGTH_SHORT).show();

                            }else if(response.equals("0")){
                                Toast.makeText(getActivity(), "Tên tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
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
                            param.put("email", email);
                            param.put("username", name);
                            param.put("pass", pass);
                            return param;
                        }
                    };
                    requestQueue.add(stringRequest);
                }

            }
        });
    }

    private void Anhxa(View view) {
        tilRegisterEmail= view.findViewById(R.id.register_tilemail);
        tilRegisterName= view.findViewById(R.id.register_tilusername);
        tilRegisterPass= view.findViewById(R.id.register_tilpassword);

        edRegisterEmail = view.findViewById(R.id.register_edemail);
        edRegisterName = view.findViewById(R.id.register_edusername);
        edRegisterPass = view.findViewById(R.id.register_edpassword);

        btnRegister = view.findViewById(R.id.register_btnregister);
    }
    private void validate(String name,String pass,String email){
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
        if(email.isEmpty()){
            tilRegisterEmail.setError("Chưa nhập Email");
            temp++;
        }else {
            tilRegisterEmail.setError("");
            temp=0;
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tilRegisterEmail.setError("Email không hợp lệ");
                temp++;
            } else {
                tilRegisterEmail.setError("");
                temp=0;
            }
        }

    }

}