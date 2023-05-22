package com.nhom7.doanquanlysanpham;

import java.io.Serializable;

public class SanPham implements Serializable {
    String ma = "", ten = "";
    int dongia = 0;
    byte[] pic = null;
    String picDrawable = "";

    public SanPham(String ma, String ten, int dongia, byte[] pic) {
        this.ma = ma;
        this.ten = ten;
        this.dongia = dongia;
        this.pic = pic;
    }

    public SanPham(String ma, String ten, int dongia, String picDrawable) {
        this.ma = ma;
        this.ten = ten;
        this.dongia = dongia;
        this.pic = null;
        this.picDrawable = picDrawable;
    }


    public SanPham(String ma, String ten, int dongia) {
        this.ma = ma;
        this.ten = ten;
        this.dongia = dongia;
        this.pic = null;
        this.picDrawable = null;
    }

    public String getMa() {
        return ma;
    }

    public String getTen() {
        return ten;
    }

    public int getDongia() {
        return dongia;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setDongia(int dongia) {
        this.dongia = dongia;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public String getPicDrawable() {
        return picDrawable;
    }

    public void setPicDrawable(String picDrawable) {
        this.picDrawable = picDrawable;
    }

    public SanPham() {
    }
}
