package com.nhom7.doanquanlysanpham;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DatabaseProvider {
    SQLiteDatabase database;
    public DatabaseProvider() {
        database = LoadDatabase();
    }

    public SQLiteDatabase LoadDatabase(){
        String databasePath = "/data/data/com.nhom7.doanquanlysanpham/databases/QuanLySanPham.db";
        database = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
        return database;
    }

    public ArrayList<SanPham> LoadData(){
        ArrayList<SanPham> arr = new ArrayList<SanPham>();
        SanPham sp;
        Cursor c = database.query("sanpham", null, null, null, null, null, null);
        c.moveToFirst();
        do {
            sp = new SanPham();
            sp.setMa(c.getString(0));
            sp.setTen(c.getString(1));
            sp.setDongia(c.getInt(2));
            sp.setPicDrawable(c.getString(3));
            arr.add(sp);
        }
        while (c.moveToNext());
        return arr;
    }

    public void DeleteData(String maSP){
        String sql = String.format("DELETE FROM sanpham WHERE ma='%s'", maSP);
        database.execSQL(sql);
    }

    public void UpdateData(String maBanDau, SanPham sp){
        String sql = "UPDATE sanpham " +
                "SET ma='%s', ten='%s', dongia='%d'" +
                "WHERE ma='%s'";
        sql = String.format(sql, sp.getMa(), sp.getTen(), sp.getDongia(), maBanDau);
        database.execSQL(sql);
    }
}