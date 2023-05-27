package com.nhom7.doanquanlysanpham;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Functions {
    public static int getImageID(Context context, String imageName){
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    public static byte[] ConvertImgToArray(ImageView img){
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap ConvertArrayToBitmap (byte[] bytes) {
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bmp;
    }

    public static String removeUnicode(String input) {
        String normalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalizedString.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public static ArrayList<SanPham> TimSanPham(String tensp, ArrayList<SanPham> arr){
        ArrayList<SanPham> res = new ArrayList<>();
        tensp = tensp.toLowerCase().trim();
        for (SanPham sp: arr) {
            String spGoc = removeUnicode(sp.getTen().toLowerCase().trim());
            if (spGoc.startsWith(tensp))
                res.add(sp);
        }
        return res;
    }

}
