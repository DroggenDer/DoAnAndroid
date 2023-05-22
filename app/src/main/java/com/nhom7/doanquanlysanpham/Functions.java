package com.nhom7.doanquanlysanpham;

import android.content.Context;

public class Functions {
    public static int getImageID(Context context, String imageName){
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }
}
