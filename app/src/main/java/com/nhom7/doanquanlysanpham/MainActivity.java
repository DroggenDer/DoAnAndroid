package com.nhom7.doanquanlysanpham;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public final static int requestTTCT = 0701;
    public final static int requestPickImage = 0601;
    public final static int resultXoa = 0702;
    public final static int resultCapNhat = 0703;
    public final static String keySanPham = "0704";
    public final static String keyPosition = "0705";
    public final static String keyBanDau = "0706";
    public final static String keySanPhamMoi = "0704";

    TextView tvTongSP;
    ListView lv_Products;
    Button btnThoat, btnThem;
    DatabaseProvider databaseProvider;
    ArrayList<SanPham> arr;
    SanPhamAdapter sanPhamAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv_Products = (ListView) findViewById(R.id.simpleListView);
        btnThoat = (Button) findViewById(R.id.btn_Thoat);
        btnThem = (Button) findViewById(R.id.btn_ThemSP);
        tvTongSP = (TextView) findViewById(R.id.tvTongSP);

        try {
            databaseProvider = new DatabaseProvider();
            Toast.makeText(this, "Đã load database!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            String databaseName = "QuanLySanPham.db";
            SQLiteDatabase database = openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
            new DatabaseSetup().CreateDatabase(database);
            Toast.makeText(this, "Đã tạo database!", Toast.LENGTH_SHORT).show();
            databaseProvider = new DatabaseProvider();
        }

        arr = databaseProvider.LoadData();
        tvTongSP.setText("Tổng SP: " + arr.size());
        sanPhamAdapter = new SanPhamAdapter(this, 0, arr);
        lv_Products.setAdapter(sanPhamAdapter);

        lv_Products.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ttchitiet.class);
                intent.putExtra(keyPosition, position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(keySanPham, arr.get(position));
                intent.putExtra(keySanPham, bundle);
                startActivityForResult(intent, requestTTCT);
                return false;
            }
        });

        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Bạn muốn thoát thiệt hả?").setTitle("Thông báo")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(MainActivity.this, "Tiếp tục", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.create().show();
            }
        });
        btnThem.setOnClickListener(new ButtonThem());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==requestTTCT){
            if (resultCode==resultXoa){
                try {
                    Bundle bundle = data.getBundleExtra(keySanPham);
                    SanPham sp = (SanPham) bundle.getSerializable(keySanPham);
                    int pos = data.getIntExtra(keyPosition, -1);
                    databaseProvider.DeleteData(sp.getMa());
                    arr.remove(pos);
                    tvTongSP.setText("Tổng SP: " + arr.size());
                    sanPhamAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Đã xóa thành công " + sp.getTen(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex){
                    Toast.makeText(MainActivity.this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode==resultCapNhat) {
                try {
                    Bundle bundle = data.getBundleExtra(keySanPhamMoi);
                    SanPham sp = (SanPham) bundle.getSerializable(keySanPhamMoi);
                    int pos = data.getIntExtra(keyPosition, -1);
                    String maBanDau = data.getStringExtra(keyBanDau);
                    databaseProvider.UpdateData(maBanDau, sp);
                    arr.set(pos, sp);
                    sanPhamAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Đã cập nhật thành công " + sp.getTen(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex){
                    Toast.makeText(MainActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class ButtonThem implements View.OnClickListener{
        @Override
        public void onClick(View view) {

        }
    }
}