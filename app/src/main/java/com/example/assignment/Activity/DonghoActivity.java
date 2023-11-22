package com.example.assignment.Activity;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.assignment.Adapter.AdapterDongho;
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

public class DonghoActivity extends AppCompatActivity {
      Toolbar toolbar;
      ListView listView;
      ArrayList<Sanpham> mangsanpham;
      AdapterDongho adapterSanpham;
      View footer;
      boolean isLoadMore= false;
      boolean limitData = false;
       mhander mhander;
    public int page = 1;
    public int idloaisanpham=0;
    EditText edName, edPrice,edDesc,edImage;
    TextInputLayout tilName,tilPrice,tilDesc,tilImage;
    Button btnSua,btnXoa;
    FloatingActionButton fab;
    String user;
     int temp=0;
     int a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanpham);
        Anhxa();
        ActionBar();
        GetData(page);
        getIdsanpham();
        clickItem();
    }
    private void openDialog(int gravity) {
        final Dialog dialog = new Dialog(DonghoActivity.this);
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
                                Toast.makeText(DonghoActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                mangsanpham.add(new Sanpham(Integer.parseInt(response.trim()),name,Integer.parseInt(price), desc,image,idloaisanpham));
                                adapterSanpham.notifyDataSetChanged();

                            } else {
                                Toast.makeText(DonghoActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DonghoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
            Sanpham sanpham = mangsanpham.get(a);
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
                                    Sanpham sanphamUpdate = mangsanpham.get(a);
                                    sanphamUpdate.setTensanpham(name);
                                    sanphamUpdate.setGiasanpham(Integer.parseInt(price));
                                    sanphamUpdate.setMotasanpham(desc);
                                    sanphamUpdate.setHinhanhsanpham(image);
                                    adapterSanpham.notifyDataSetChanged();
                                    Toast.makeText(DonghoActivity.this, response, Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(DonghoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                      mangsanpham.remove(a);
                      adapterSanpham.notifyDataSetChanged();
                         Toast.makeText(DonghoActivity.this, response, Toast.LENGTH_SHORT).show();
                     }
                 }, new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         Toast.makeText(DonghoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
    private void clickItem() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(user.equals("admin")){
                       a=i;
                       openDialog(Gravity.CENTER);
                }else {
                    Intent intent = new Intent(getApplicationContext(), ChitietsanphamActivity.class);
                    intent.putExtra("thongtinchitiet", mangsanpham.get(i));
                    startActivity(intent);
                }
            }
        });

    }



    public class mhander extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    listView.addFooterView(footer);
                    break;
                case 1:
                    GetData(++page);
                    isLoadMore =false;
                    break;
            }
            super.handleMessage(msg);

        }
    }
  public class ThreadData extends Thread{
      @Override
      public void run() {
          mhander.sendEmptyMessage(0);
          try {
              Thread.sleep(3000);
          } catch (InterruptedException e) {
              throw new RuntimeException(e);
          }
          runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  Message message =mhander.obtainMessage(1);
                  mhander.handleMessage(message);
                  adapterSanpham.notifyDataSetChanged();
              }
          });

          super.run();

      }
  }
    private void getIdsanpham() {
        idloaisanpham = getIntent().getIntExtra("idloaisanpham", -1);
        Log.d("getIdsanpham: ",String.valueOf(idloaisanpham));
    }


    private void GetData(int p) {
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String path = server.duongdansanpham + String.valueOf(p);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d( "jsonDongho: ", response.toString());
                int id=0;
                String tendongho = "";
                Integer giadongho = 0;
                String hinhanhdongho = "";
                String motadongho = "";
                int idSanpham = 0;
                if(response != null && response.length() !=2){
                    listView.removeFooterView(footer);

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i = 0; i<jsonArray.length();i++){

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tendongho = jsonObject.getString("tensanpham");
                            giadongho = jsonObject.getInt("giasanpham");
                            motadongho = jsonObject.getString("motasanpham");
                            hinhanhdongho = jsonObject.getString("hinhanhsanpham");
                            idSanpham = jsonObject.getInt("idsanpham");
                            mangsanpham.add(new Sanpham(id, tendongho, giadongho, motadongho,hinhanhdongho, idSanpham));
                            adapterSanpham.notifyDataSetChanged();
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
                HashMap<String, String> param = new HashMap<>();
                param.put("idsanpham", String.valueOf(idloaisanpham));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void Anhxa() {
        listView = findViewById(R.id.listviewSanpham);
        toolbar = findViewById(R.id.toolbarSanpham);
        mangsanpham = new ArrayList<>();
        adapterSanpham = new AdapterDongho(getApplicationContext(),mangsanpham);
        listView.setAdapter(adapterSanpham);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a=-1;
                openDialog(Gravity.CENTER);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if(i+i1 == i2 && i2!=0 && isLoadMore ==false && !limitData  ){
                    isLoadMore = true;
                    Thread thread = new ThreadData();
                    thread.start();
                }
            }
        });
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
         footer =layoutInflater.inflate(R.layout.progressbar, null);
         mhander = new mhander();

        Intent intent = getIntent();
        user= intent.getStringExtra("user");
        if(user.equals("admin")){
            fab.setVisibility(View.VISIBLE);
        }else {
            fab.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
         SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterSanpham.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterSanpham.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if(item.getItemId()==R.id.giohang){
           Intent intent = new Intent(getApplicationContext(),GiohangActivity.class);
           startActivity(intent);
           return true;
       }
        return super.onOptionsItemSelected(item);
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
}