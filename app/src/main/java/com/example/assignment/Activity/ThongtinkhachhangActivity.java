package com.example.assignment.Activity;

import static java.lang.Integer.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ThongtinkhachhangActivity extends AppCompatActivity {
         public EditText edName,edSdt, edEmail,edDiachi;
         public Button btnThanhtoan, btnQuayve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtinkhachhang);
        Anhxa();
        EventButton();
    }

    private void EventButton() {
        btnQuayve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        btnThanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = edName.getText().toString();
                final String sdt = edSdt.getText().toString();
                final String email = edEmail.getText().toString();
                final String diachi = edDiachi.getText().toString();
                if(name.length()>0 || sdt.length()>0|| email.length()>0|| diachi.length()>0){
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server.thongtinkhachhang, new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String madonhang) {
                            Log.d( "madonhang: ", madonhang);
                        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, server.chitietdonhang, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d( "machitiet ",response);
                                if(response.equals("1")){
                                    MainActivity.manggiohang.clear();
                                    Toast.makeText(ThongtinkhachhangActivity.this, "Bạn đã thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(ThongtinkhachhangActivity.this, "Mời ạn tiếp tục mua hàng", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(ThongtinkhachhangActivity.this, "Giỏ hàng của bạn bị lỗi", Toast.LENGTH_SHORT).show();
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
                                JSONArray jsonArray = new JSONArray();
                                for(int i=0;i<MainActivity.manggiohang.size();i++){
                                    JSONObject jsonObject = new JSONObject();
                                    try {

                                        jsonObject.put("madonhang",madonhang);
                                        jsonObject.put("masanpham",MainActivity.manggiohang.get(i).getId());
                                        jsonObject.put("giasanpham", MainActivity.manggiohang.get(i).getGiasp());
                                        jsonObject.put("soluongsanpham", MainActivity.manggiohang.get(i).getSoluong());

                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    jsonArray.put(jsonObject);
                                }
                                param.put("json", jsonArray.toString());
                                return param;
                            }
                        };
                        requestQueue1.add(stringRequest1);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ThongtinkhachhangActivity.this, "Lỗi: "+error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String > params = new HashMap<>();
                            params.put("tenkhachhang", name);
                            params.put("sodienthoai",sdt);
                            params.put("email", email);
                            params.put("diachi",diachi);
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }else {
                    Toast.makeText(ThongtinkhachhangActivity.this, "Bạn chưa điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                }

        });
    }

    private void Anhxa() {
        edName = findViewById(R.id.textinputname);
        edSdt = findViewById(R.id.textinputsdt);
        edEmail = findViewById(R.id.textinputemail);
        edDiachi = findViewById(R.id.textinputaddress);
        btnThanhtoan = findViewById(R.id.btnxacnhan);
        btnQuayve = findViewById(R.id.btnquaylai);

    }
}