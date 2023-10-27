package com.example.assignment.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.assignment.Modul.Photo;
import com.example.assignment.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoFragment extends Fragment {


    public PhotoFragment() {
        // Required empty public constructor
    }


    public static PhotoFragment newInstance(String param1, String param2) {
        PhotoFragment fragment = new PhotoFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_photo, container, false);
           Bundle bundle = getArguments();
        Photo photo = (Photo) bundle.getSerializable("objImage");
        ImageView imageView = view.findViewById(R.id.image_fragment);
        imageView.setImageResource(photo.getIdImage());
        return view;

    }
}