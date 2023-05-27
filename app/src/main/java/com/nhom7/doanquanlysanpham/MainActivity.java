package com.nhom7.doanquanlysanpham;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public final static int requestTTCT = 0701;
    public final static int requestThem = 0702;
    public final static int requestPickImage = 0703;
    public final static int resultXoa = 0601;
    public final static int resultCapNhat = 0602;
    public final static int resultThem = 0602;
    public final static String keySanPham = "0501";
    public final static String keyPosition = "0502";
    public final static String keyBanDau = "0503";
    public final static String keySanPhamMoi = "0504";
    public final static String keyMangSP = "0505";

    EditText edtTimKiem;
    TextView tvTongSP;
    Spinner spSort;
    ListView lv_Products;
    Button btnThoat, btnThem;
    DatabaseProvider databaseProvider;
    ArrayList<SanPham> arr;
    SanPhamAdapter sanPhamAdapter;
    ArrayList<String> arrSort = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv_Products = (ListView) findViewById(R.id.simpleListView);
        btnThoat = (Button) findViewById(R.id.btn_Thoat);
        btnThem = (Button) findViewById(R.id.btn_ThemSP);
        tvTongSP = (TextView) findViewById(R.id.tvTongSP);
        spSort = (Spinner) findViewById(R.id.spSort);
        edtTimKiem = (EditText) findViewById(R.id.edtTimSP);

        try {
            databaseProvider = new DatabaseProvider();
        } catch (Exception ex) {
            String databaseName = "QuanLySanPham.db";
            SQLiteDatabase database = openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
            new DatabaseSetup().CreateDatabase(database);
            databaseProvider = new DatabaseProvider();
        }

        arr = databaseProvider.LoadData(0);
        tvTongSP.setText("Tổng SP: " + arr.size());
        sanPhamAdapter = new SanPhamAdapter(this, 0, arr);
        lv_Products.setAdapter(sanPhamAdapter);

        lv_Products.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

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
        arrSort.add("Thời gian");
        arrSort.add("Tên");
        arrSort.add("Giá");
        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, arrSort);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spSort.setAdapter(arrayAdapter);
        spSort.setOnItemSelectedListener(new SpinnerSort());
        registerForContextMenu(lv_Products);
        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String tenSP = edtTimKiem.getText()+"";
                ArrayList<SanPham> temp = Functions.TimSanPham(tenSP, arr);

                if (tenSP.trim().isEmpty()){
                    tvTongSP.setText("Tổng SP: " + arr.size());
                    sanPhamAdapter = new SanPhamAdapter(MainActivity.this, 0, arr);
                    lv_Products.setAdapter(sanPhamAdapter);
                }
                else {
                    if (temp.size()==0){
                        Toast.makeText(MainActivity.this, "Không tìm thấy sản phẩm '" + tenSP + "'!", Toast.LENGTH_SHORT).show();
                    }
                    sanPhamAdapter = new SanPhamAdapter(MainActivity.this, 0, temp);
                    lv_Products.setAdapter(sanPhamAdapter);
                    tvTongSP.setText("Tổng SP: " + temp.size());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = info.position;

        switch (item.getItemId()) {
            case R.id.itemCapNhat:
                Intent intent = new Intent(MainActivity.this, ttchitiet.class);
                intent.putExtra(keyPosition, pos);
                Bundle bundle = new Bundle();
                bundle.putSerializable(keySanPham, arr.get(pos));
                intent.putExtra(keySanPham, bundle);
                startActivityForResult(intent, requestTTCT);
                break;
            case R.id.itemXoa:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Bạn muốn xóa thiệt hả?").setTitle("Thông báo")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    databaseProvider.DeleteData(arr.get(pos).getMa());
                                    String ten = arr.get(pos).getTen();
                                    arr.remove(pos);
                                    sanPhamAdapter.notifyDataSetChanged();
                                    tvTongSP.setText("Tổng SP: " + arr.size());
                                    Toast.makeText(MainActivity.this, "Đã xóa thành công " + ten, Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception ex){
                                    Toast.makeText(MainActivity.this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.create().show();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return false;
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
                    arr = databaseProvider.LoadData(spSort.getSelectedItemPosition());
                    sanPhamAdapter = new SanPhamAdapter(MainActivity.this, 0, arr);
                    lv_Products.setAdapter(sanPhamAdapter);
                    Toast.makeText(MainActivity.this, "Đã cập nhật thành công " + sp.getTen(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex){
                    Toast.makeText(MainActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode==requestThem) {
            if (resultCode==resultThem) {
                Bundle bundle = data.getBundleExtra(keySanPham);
                SanPham sp = (SanPham) bundle.getSerializable(keySanPham);
                databaseProvider.InsertData(sp);
                arr.add(sp);
                Toast.makeText(MainActivity.this, "Đã thêm thành công " + sp.getTen(), Toast.LENGTH_SHORT).show();
            }
        }
        tvTongSP.setText("Tổng SP: " + arr.size());
        sanPhamAdapter.notifyDataSetChanged();
    }

    public class ButtonThem implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(keyMangSP, arr);
            Intent intent = new Intent(MainActivity.this, ThemSanPham.class);
            intent.putExtra(keyMangSP, bundle);
            startActivityForResult(intent, requestThem);
        }
    }

    public class SpinnerSort implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            arr = databaseProvider.LoadData(i);
            sanPhamAdapter = new SanPhamAdapter(MainActivity.this, 0, arr);
            lv_Products.setAdapter(sanPhamAdapter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}