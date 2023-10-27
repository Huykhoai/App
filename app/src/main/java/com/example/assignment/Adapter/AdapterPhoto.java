package com.example.assignment.Adapter;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.assignment.Modul.Photo;
import com.example.assignment.fragment.PhotoFragment;

import java.util.List;

public class AdapterPhoto extends FragmentStateAdapter {
    public List<Photo> arraylist;

    public AdapterPhoto(@NonNull FragmentActivity fragmentActivity, List<Photo> arraylist) {
        super(fragmentActivity);
        this.arraylist = arraylist;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Photo photo = arraylist.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("objImage", photo);
        PhotoFragment photoFragment = new PhotoFragment();
        photoFragment.setArguments(bundle);
        return photoFragment;
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }
}
