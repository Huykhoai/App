package com.example.assignment.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.Interface.interfaceRecycle;
import com.example.assignment.Modul.Sanpham;
import com.example.assignment.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterSanphammoinhat extends RecyclerView.Adapter<AdapterSanphammoinhat.ViewHolderRecycle> implements Filterable {
    Context context;
    ArrayList<Sanpham> arrayList;
    ArrayList<Sanpham> arrayListOld;
   private interfaceRecycle onItemClickListener;

    public void setOnItemClickListener(interfaceRecycle listener) {
        this.onItemClickListener = listener;
    }

    public AdapterSanphammoinhat(Context context, ArrayList<Sanpham> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.arrayListOld =arrayList;
    }

    @NonNull
    @Override
    public ViewHolderRecycle onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moinhat, parent,false);
        ViewHolderRecycle viewHolderRecycle = new ViewHolderRecycle(view);
        return viewHolderRecycle;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderRecycle holder, int position) {
        Sanpham sanpham = arrayList.get(position);
        holder.txtName.setText(sanpham.getTensanpham());
        holder.txtName.setMaxLines(2);
        holder.txtName.setEllipsize(TextUtils.TruncateAt.END);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGia.setText("Giá: "+ decimalFormat.format(sanpham.getGiasanpham()));
        Picasso.get().load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.home)
                .error(R.drawable.erro)
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(onItemClickListener != null){
                  onItemClickListener.onItemClick(position);
              }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(arrayList != null)
        return arrayList.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                    String str =charSequence.toString();
                  if(str.isEmpty()){
                      arrayList = arrayListOld;
                  }else {
                    ArrayList<Sanpham> list = new ArrayList<>();
                    for(Sanpham sanpham: arrayListOld){
                    if(sanpham.getTensanpham().toLowerCase().contains(str.toLowerCase())){
                       list.add(sanpham);
                    }
                    }
                    arrayList = list;
                  }
                  FilterResults filterResults =new FilterResults();
                  filterResults.values = arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                arrayList = (ArrayList<Sanpham>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolderRecycle extends RecyclerView.ViewHolder{
        TextView txtName,txtGia;
        ImageView imageView;
         public ViewHolderRecycle(@NonNull View itemView) {
             super(itemView);
             txtName = itemView.findViewById(R.id.textviewtensanpham);
             txtGia = itemView.findViewById(R.id.textviewgiasanpham);
             imageView = itemView.findViewById(R.id.imageviewsanpham);
         }
     }

}
