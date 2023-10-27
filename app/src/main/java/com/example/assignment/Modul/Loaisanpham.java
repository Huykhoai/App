package com.example.assignment.Modul;

public class Loaisanpham {
    private int id;
    private String tenloaisp;
    private String hinhanhsp;

    public Loaisanpham(int id, String tenloaisp, String hinhanhsp) {
        this.id = id;
        this.tenloaisp = tenloaisp;
        this.hinhanhsp = hinhanhsp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenloaisp() {
        return tenloaisp;
    }

    public void setTenloaisp(String tenloaisp) {
        this.tenloaisp = tenloaisp;
    }

    public String getHinhanhsp() {
        return hinhanhsp;
    }

    public void setHinhanhsp(String hinhanhsp) {
        this.hinhanhsp = hinhanhsp;
    }
}
