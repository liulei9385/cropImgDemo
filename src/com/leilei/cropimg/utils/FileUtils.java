package com.leilei.cropimg.utils;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * USER: liulei
 * DATA: 2015/1/16
 * TIME: 16:59
 */
public class FileUtils {

    /**
     * @param bitmap       源文件
     * @param destFilePath 保存的路径
     * @return 是否保存成功
     */
    public static boolean saveBitmapToFile(Bitmap bitmap, String destFilePath) {
        try {
            File file = new File(destFilePath);
            if (!file.exists())
                file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return false;
    }
}
