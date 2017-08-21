package com.shuishou.digitalmenu.io;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.shuishou.digitalmenu.InstantValue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/6/8.
 */

public class IOOperator {

    public static void saveServerURL(String url){
        FileWriter writer = null;
        try {
            writer = new FileWriter(InstantValue.FILE_SERVERURL);
            writer.write(url);
            writer.close();
        } catch (IOException e) {
            Log.e("IOException", "error to save ServerURL +\n"+e.getStackTrace());
        } finally {
            try {
                if (writer != null)
                    writer.close();
            }catch (IOException e) {}
        }
    }
    public static String loadServerURL(){
        File file = new File(InstantValue.FILE_SERVERURL);
        if (!file.exists())
            return "";
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String line = in.readLine();
            return line;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("IOException", "error to load ServerURL +\n"+e.getStackTrace());
        } finally {
            try {
                if (in != null)
                    in.close();
            }catch (IOException e) {}
        }
        return null;
    }

    @Nullable
    public static BitmapDrawable getDishImageDrawable(Resources res, String filename){
        File file = new File(filename);
        if (!file.exists())
            return null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[4096];
        try {
            InputStream in = new FileInputStream(file);
            int readLength = in.read(bytes);
            while(readLength != -1){
                outputStream.write(bytes, 0, readLength);
                readLength = in.read(bytes);
            }
            byte[] data = outputStream.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            BitmapDrawable d = new BitmapDrawable(res, bitmap);
            return d;
        } catch (IOException e) {
            Log.e("DishTabBuilder", "Failed to load dish image");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * delete all files under the catalog
     * @param directoryName
     */
    public static void deleteDishPicture(String directoryName){
        File dir = new File(directoryName);
        if (dir.exists() && dir.isDirectory()){
            File[] files = dir.listFiles();
            if (files != null){
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
        }
    }
}
