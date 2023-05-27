package com.nhom7.doanquanlysanpham;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ttchitiet extends AppCompatActivity {

    ImageView imgIcon;
    EditText edtMa, edtTen, edtDonGia;
    Button btnDoiAnh, btnXoa, btnCapNhat;
    Bundle bundleFromMain;
    SanPham sp;
    String maSPBanDau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttchitiet);
        this.setTitle("");

        bundleFromMain = getIntent().getBundleExtra(MainActivity.keySanPham);
        sp = (SanPham) bundleFromMain.getSerializable(MainActivity.keySanPham);
        maSPBanDau = sp.getMa();

        imgIcon = findViewById(R.id.img_detail);
        imgIcon.setTag("NoUpdate");
        edtMa = findViewById(R.id.et_productcode);
        edtTen = findViewById(R.id.et_productname);
        edtDonGia = findViewById(R.id.et_productcost);
        btnDoiAnh = findViewById(R.id.btn_DoiHinhAnh);
        btnXoa = findViewById(R.id.btn_XoaSP);
        btnCapNhat = findViewById(R.id.btn_CapNhatSP);

        edtMa.setText(sp.getMa());
        edtTen.setText(sp.getTen());
        edtDonGia.setText(sp.getDongia()+"");
        if (sp.getPic()==null)
            imgIcon.setImageResource(Functions.getImageID(getApplicationContext(), sp.getPicDrawable()));
        else
            imgIcon.setImageBitmap(Functions.ConvertArrayToBitmap(sp.getPic()));

        btnDoiAnh.setOnClickListener(new ButtonDoiAnh());
        btnXoa.setOnClickListener(new ButtonXoaSP());
        btnCapNhat.setOnClickListener(new ButtonCapNhat());
    }

    public class ButtonDoiAnh implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), MainActivity.requestPickImage);
        }
    }

    public class ButtonXoaSP implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(ttchitiet.this);
            builder.setMessage("Bạn muốn xóa thiệt hả?").setTitle("Thông báo")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = getIntent();
                            setResult(MainActivity.resultXoa, intent);
                            finish();
                        }
                    })
                    .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.create().show();
        }
    }

    public class ButtonCapNhat implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ttchitiet.this);
            builder.setMessage("Bạn muốn cập nhật thiệt hả?").setTitle("Thông báo")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String dongia = edtDonGia.getText()+"";
                            byte[] pic = Functions.ConvertImgToArray(imgIcon);
                            SanPham spMoi = new SanPham(edtMa.getText()+"", edtTen.getText()+"",
                                    Integer.parseInt(dongia),
                                    sp.getPicDrawable());
                            spMoi.setPic(pic);
                            Intent intent = getIntent();
                            intent.putExtra(MainActivity.keyBanDau, maSPBanDau);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(MainActivity.keySanPhamMoi, spMoi);
                            intent.putExtra(MainActivity.keySanPhamMoi, bundle);
                            setResult(MainActivity.resultCapNhat, intent);
                            finish();
                        }
                    })
                    .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.create().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.requestPickImage && resultCode == RESULT_OK && null != data) {
            try {
                final Uri uri = data.getData();
                final InputStream imgStream;
                imgStream = getContentResolver().openInputStream(uri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imgStream);
                imgIcon.setImageBitmap(selectedImage);
            } catch (Exception e) {
                Toast.makeText(ttchitiet.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}