package com.example.assignment.Modul;

import java.io.Serializable;

public class Photo implements Serializable {
    int idImage;

    public Photo(int idImage) {
        this.idImage = idImage;
    }

    public int getIdImage() {
        return idImage;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }
}
