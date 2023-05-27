package com.nhom7.doanquanlysanpham;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.UiAutomation;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class ThemSanPham extends AppCompatActivity {

    ImageView imgHinhAnh;
    EditText edtMaSP, edtTenSP, edtDonGia;
    Button btnDoiAnh, btnThemSP, btnTroVe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_san_pham);
        this.setTitle("");
        imgHinhAnh = (ImageView) findViewById(R.id.img_themActivity_hinhAnh);
        edtMaSP = (EditText) findViewById(R.id.edt_themActivity_productCode);
        edtTenSP = (EditText) findViewById(R.id.edt_themActivity_productName);
        edtDonGia = (EditText) findViewById(R.id.edt_themActivity_productPrice);
        btnDoiAnh = (Button) findViewById(R.id.btn_themActivity_DoiHinhAnh);
        btnThemSP = (Button) findViewById(R.id.btn_ThemSP);
        btnTroVe = (Button) findViewById(R.id.btn_themSP_TroVe);

        btnDoiAnh.setOnClickListener(new ButtonDoiAnh());
        btnThemSP.setOnClickListener(new ButtonThem());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==MainActivity.requestPickImage){
            if (resultCode==RESULT_OK && data != null){
                Uri img = data.getData();
                imgHinhAnh.setImageURI(img);
            }
        }
    }
    public class ButtonDoiAnh implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), MainActivity.requestPickImage);
        }
    }

    public class ButtonThem implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ThemSanPham.this);
            builder.setMessage("Bạn muốn thêm thiệt " + edtTenSP.getText() + " hả?").setTitle("Thông báo")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = getIntent();
                            ArrayList<SanPham> arr = (ArrayList<SanPham>) intent.getBundleExtra(MainActivity.keyMangSP).
                                    getSerializable(MainActivity.keyMangSP);
                            byte[] img = Functions.ConvertImgToArray(imgHinhAnh);
                            int donGia = 0;
                            try {
                                donGia = Integer.parseInt(edtDonGia.getText()+"");
                            }
                            catch (Exception ex){
                                donGia = 0;
                            }
                            String maSP = edtMaSP.getText()+"";
                            String tenSP = edtTenSP.getText() + "";
                            SanPham sp = new SanPham(maSP.trim(), tenSP.trim(), donGia, img);
                            int checkResult = sp.CheckValidSP(arr);
                            if (checkResult==1){
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(MainActivity.keySanPham, sp);
                                intent.putExtra(MainActivity.keySanPham, bundle);
                                setResult(MainActivity.resultThem, intent);
                                finish();
                            } else if (checkResult==0) {
                                Toast.makeText(ThemSanPham.this, "Mã SP hoặc Tên không được trống", Toast.LENGTH_LONG).show();
                            } else if (checkResult==-1) {
                                Toast.makeText(ThemSanPham.this, "Mã SP hoặc Tên SP đã tồn tại", Toast.LENGTH_LONG).show();
                            }

                        }
                    })
                    .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.create().show();
        }
    }
}