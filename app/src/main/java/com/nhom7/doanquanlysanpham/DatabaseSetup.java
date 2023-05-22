package com.nhom7.doanquanlysanpham;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.database.sqlite.SQLiteDatabase;

public class DatabaseSetup {
    public DatabaseSetup(){
    }

    public void CreateDatabase(SQLiteDatabase database){
//        String databaseName = "QuanLySanPham.db";
//        SQLiteDatabase database;
//        database = openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
        String createTable = "CREATE TABLE SanPham (" +
                "ma TEXT PRIMARY KEY," +
                "ten TEXT," +
                "dongia INTEGER," +
                "hinhanh BLOB)";
        database.execSQL(createTable);
        database.execSQL("INSERT INTO SanPham VALUES('D01', 'Cà phê đen', '18000', 'cf')");
        database.execSQL("INSERT INTO SanPham VALUES('D02', 'Capuchino', '25000', 'capu')");
        database.execSQL("INSERT INTO SanPham VALUES('D03', 'Lipton đá', '16000', 'lipton')");
        database.execSQL("INSERT INTO SanPham VALUES('D04', 'Cam ép', '20000', 'camep')");
        database.execSQL("INSERT INTO SanPham VALUES('D05', 'Cà phê sữa', '22000', 'cfsua')");
        database.execSQL("INSERT INTO SanPham VALUES('D06', 'Bạc xỉu', '20000', 'bacxiu')");
        database.execSQL("INSERT INTO SanPham VALUES('D07', 'Dưa hấu ép', '20000', 'duahauep')");
        database.execSQL("INSERT INTO SanPham VALUES('D08', 'Trà sữa', '25000', 'trasua')");
        database.execSQL("INSERT INTO SanPham VALUES('D09', 'Trà đào', '23000', 'tradao')");
        database.execSQL("INSERT INTO SanPham VALUES('D10', 'Trà chanh', '15000', 'trachanh')");
    }
}
