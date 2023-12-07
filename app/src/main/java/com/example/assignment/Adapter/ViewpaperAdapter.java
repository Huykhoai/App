package com.example.assignment.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.assignment.fragment.LoginFragment;
import com.example.assignment.fragment.RegisterFragment;

public class ViewpaperAdapter extends FragmentStateAdapter {
    public ViewpaperAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0){
          return new LoginFragment();
        }

        return new RegisterFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
