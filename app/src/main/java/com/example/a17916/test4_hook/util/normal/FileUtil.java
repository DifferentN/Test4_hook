package com.example.a17916.test4_hook.util.normal;

import android.os.Environment;
import android.util.Log;

import com.example.a17916.test4_hook.util.ACache;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by vector on 16/9/22.
 */
public class FileUtil {
    public static void writeFileToSD(String fileName, String content) {
        String sdStatus = Environment.getExternalStorageState();
        if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.d("TestFile", "SD card is not avaiable/writeable right now.");
            return;
        }
        try {
            String pathName="/sdcard/ias/";
            File path = new File(pathName);
            File file = new File(pathName + fileName);
            if( !path.exists()) {
                Log.d("TestFile", "Create the path:" + pathName);
                path.mkdir();
            }
            if( !file.exists()) {
                Log.d("TestFile", "Create the file:" + fileName);
                file.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(file);
            byte[] buf = content.getBytes();
            stream.write(buf);
            stream.close();

        } catch(Exception e) {
            Log.e("TestFile", "Error on writeFilToSD.");
            e.printStackTrace();
        }
    }

    public static ACache getACache() {
        String path = "/sdcard/ias/";
        File tmp = new File(path);
        if(!tmp.exists()){
            tmp.mkdir();
        }
        ACache aCache = ACache.get(new File(path, "ias"));
        return aCache;
    }

}
