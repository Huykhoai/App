package com.example.assignment.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.assignment.Adapter.AdapterTainghe;
import com.example.assignment.Modul.Sanpham;
import com.example.assignment.R;
import com.example.assignment.util.checkConnection;
import com.example.assignment.util.server;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaingheActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    FloatingActionButton fab;
    ArrayList<Sanpham> mangtainghe;
    AdapterTainghe adaptertainghe;
    int idloaisanpham = 0;
    int page = 1;
    View footer;
    boolean isLoadMore= false;
    boolean  limitData=false;
    mHander mHander;
    int a;
    int temp=0;
    EditText edName, edPrice,edDesc,edImage;
    TextInputLayout tilName,tilPrice,tilDesc,tilImage;
    Button btnSua,btnXoa;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tainghe);
        if(checkConnection.HaveNetworking(getApplicationContext())){
            Anhxa();
            ActionBar();
            getData(page);
            getIdTainghe();
            oncickItem();
        }

    }

    private void oncickItem() {


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(user.equals("admin")){
                        a=i;
                        openDialog(Gravity.CENTER);
                }else {
                    Intent intent = new Intent(getApplicationContext(), ChitietsanphamActivity.class);
                    intent.putExtra("thongtinchitiet", mangtainghe.get(i));
                    startActivity(intent);
                }

            }
        });

    }

    public class mHander extends Handler{
          @Override
          public void handleMessage(@NonNull Message msg) {
              switch (msg.what){
                  case 0:
                      listView.addFooterView(footer);
                      break;
                  case 1:
                      getData(++page);
                      isLoadMore = false;
                      break;
              }
              super.handleMessage(msg);

          }
      }
      public class ThreadData extends Thread{
          @Override
          public void run() {
              mHander.sendEmptyMessage(0);
              try {
                  Thread.sleep(3000);
              } catch (InterruptedException e) {
                  throw new RuntimeException(e);
              }
              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      Message message = mHander.obtainMessage(1);
                      mHander.sendMessage(message);
                      adaptertainghe.notifyDataSetChanged();
                  }
              });

              super.run();
          }
      }
    private void getIdTainghe() {
        idloaisanpham = getIntent().getIntExtra("idloaitainghe", -1);
        Log.d( "getIdTainghe: ", String.valueOf(idloaisanpham));
    }

    private void getData(int p) {
       final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String path = server.duongdansanpham+String.valueOf(p);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d( "jsonDongho: ", response.toString());
                int id=0;
                String tentainghe= "";
                Integer giatainghe = 0;
                String hinhanhtaighe = "";
                String motatainghe = "";
                int idSanpham = 0;
                if(response != null && response.length() !=2){
                    listView.removeFooterView(footer);

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i = 0; i<jsonArray.length();i++){

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tentainghe = jsonObject.getString("tensanpham");
                            giatainghe = jsonObject.getInt("giasanpham");
                            motatainghe = jsonObject.getString("motasanpham");
                            hinhanhtaighe = jsonObject.getString("hinhanhsanpham");
                            idSanpham = jsonObject.getInt("idsanpham");
                            mangtainghe.add(new Sanpham(id, tentainghe, giatainghe,motatainghe,hinhanhtaighe, idSanpham));
                            adaptertainghe.notifyDataSetChanged();
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    limitData =true;
                    listView.removeFooterView(footer);
                    checkConnection.ShowToastLong(getApplicationContext(), "không còn dữ liệu");
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
                HashMap<String,String> params = new HashMap<>();
                params.put("idsanpham", String.valueOf(idloaisanpham));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
    }
    private void openDialog(int gravity) {
        final Dialog dialog = new Dialog(TaingheActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_themsp);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = gravity;
        if(Gravity.CENTER == gravity ){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }
        TextView tilView = dialog.findViewById(R.id.dialog_register_tile);

        edName = dialog.findViewById(R.id.dialog_register_txtmasach);
        edPrice = dialog.findViewById(R.id.dialog_register_txtname);
        edDesc = dialog.findViewById(R.id.dialog_register_txtgiathue);
        edImage = dialog.findViewById(R.id.dialog_register_txtimage);

        tilName = dialog.findViewById(R.id.dialog_register_til_masach);
        tilPrice = dialog.findViewById(R.id.dialog_register_til_name);
        tilDesc = dialog.findViewById(R.id.dialog_register_til_giathue);
        tilImage = dialog.findViewById(R.id.dialog_register_til_image);

        btnSua = dialog.findViewById(R.id.dialog_register_add);
        btnXoa = dialog.findViewById(R.id.dialog_register_cancel);

        tilName.setHint("Nhập tên");
        tilPrice.setHint("Nhập giá");
        tilDesc.setHint("Nhập mô tả");
        tilImage.setHint("Nhập link ảnh");

        btnSua.setText("Thêm");
        btnXoa.setText("Hủy");

        if(a==-1){
            btnSua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validate();
                    String name = edName.getText().toString();
                    String price = edPrice.getText().toString();
                    String desc = edDesc.getText().toString();
                    String image = edImage.getText().toString();
                    if (temp == 0) {
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.duongdaninsertsp, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("onResponse: ", response);
                                if (!response.equals("0")) {
                                    Toast.makeText(TaingheActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                    mangtainghe.add(new Sanpham(Integer.parseInt(response.trim()),name,Integer.parseInt(price), desc,image,idloaisanpham));
                                    adaptertainghe.notifyDataSetChanged();
                                    dialog.dismiss();

                                } else {
                                    Toast.makeText(TaingheActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(TaingheActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> params = new HashMap<>();
                                params.put("name", name);
                                params.put("price", price);
                                params.put("desc", desc);
                                params.put("image", image);
                                params.put("idsanpham", String.valueOf(idloaisanpham));
                                return params;
                            }
                        };
                        requestQueue.add(stringRequest);

                    }
                }
            });
            btnXoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }else {
            tilView.setText("Sửa/Xóa");
            btnXoa.setText("Xóa");
            btnSua.setText("Sửa");
            Sanpham sanpham = mangtainghe.get(a);
            int id = sanpham.getId();
            edName.setText(sanpham.getTensanpham());
            edPrice.setText(String.valueOf(sanpham.getGiasanpham()));
            edDesc.setText(sanpham.getMotasanpham());
            edImage.setText(sanpham.getHinhanhsanpham());
            btnSua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validate();
                    String name = edName.getText().toString();
                    String price = edPrice.getText().toString();
                    String desc = edDesc.getText().toString();
                    String image = edImage.getText().toString();
                    if(temp==0){
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.duongdanupdatesp, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("errro: ",response);
                                dialog.dismiss();
                                Sanpham sanphamUpdate = mangtainghe.get(a);
                                sanphamUpdate.setTensanpham(name);
                                sanphamUpdate.setGiasanpham(Integer.parseInt(price));
                                sanphamUpdate.setMotasanpham(desc);
                                sanphamUpdate.setHinhanhsanpham(image);
                                adaptertainghe.notifyDataSetChanged();
                                Toast.makeText(TaingheActivity.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(TaingheActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String,String> params = new HashMap<>();
                                params.put("id", String.valueOf(id));
                                params.put("name", name);
                                params.put("price", price);
                                params.put("desc", desc);
                                params.put("image", image);
                                params.put("idloaisp", String.valueOf(idloaisanpham));
                                return params;
                            }
                        };
                        requestQueue.add(stringRequest);
                    }
                }
            });
            btnXoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server.duongdandelete, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dialog.dismiss();
                            mangtainghe.remove(a);
                            adaptertainghe.notifyDataSetChanged();
                            Toast.makeText(TaingheActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(TaingheActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String,String> params = new HashMap<>();
                            params.put("id", String.valueOf(id));
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
            });
        }

        dialog.show();
    }
    private void validate(){
        String name = edName.getText().toString();
        String price = edPrice.getText().toString();
        String desc = edDesc.getText().toString();
        String image = edImage.getText().toString();
        if(name.isEmpty()){
            tilName.setError("Chưa nhập tên");
            temp++;
        }else {
            tilName.setError("");
            temp=0;
        }
        if(price.isEmpty()){
            tilPrice.setError("Chưa nhập tên");
            temp++;
        }else {
            tilPrice.setError("");
            temp=0;
        }
        if(desc.isEmpty()){
            tilDesc.setError("Chưa nhập tên");
            temp++;
        }else {
            tilDesc.setError("");
            temp=0;
        }
        if(image.isEmpty()){
            tilImage.setError("Mật khẩu xác nhận không đúng!");
            temp++;

        }else {
            tilImage.setError("");
            temp=0;
        }
    }
    private void Anhxa() {
        toolbar = findViewById(R.id.toolbarTainghe);
        listView = findViewById(R.id.listviewTainghe);
        fab = findViewById(R.id.fab);
        mangtainghe = new ArrayList<>();
        adaptertainghe = new AdapterTainghe(getApplicationContext(), mangtainghe);
        adaptertainghe.notifyDataSetChanged();
        listView.setAdapter(adaptertainghe);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if(i+i1 == i2 && i2!=0 &&isLoadMore == false && !limitData ){
                    isLoadMore =true;
                    Thread thread = new ThreadData();
                    thread.start();
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               a=-1;
               openDialog(Gravity.CENTER);
            }
        });
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footer = inflater.inflate(R.layout.progressbar, null);
        mHander = new mHander();

        Intent intent = getIntent();
        user= intent.getStringExtra("user");
        if(user.equals("admin")){
            fab.setVisibility(View.VISIBLE);
        }else {
            fab.setVisibility(View.INVISIBLE);
        }
    }
}