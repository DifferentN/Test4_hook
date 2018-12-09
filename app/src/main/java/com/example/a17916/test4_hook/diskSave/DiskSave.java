package com.example.a17916.test4_hook.diskSave;

import android.content.Intent;
import android.os.Environment;
import android.os.Parcel;
import android.util.Log;
import android.view.MotionEvent;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DiskSave {
    private File file;
    private DiskLruCache diskLruCache;
    private static final int MAX_SIZE = 1000 * 1000 * 50; // 50 mb
    private static DiskSave diskSave;
    public static DiskSave getDiskSave(File file){
        if(diskSave == null){
            diskSave = new DiskSave(file);
        }
        return diskSave;
    }
    public DiskSave(File file){
        this.file = file;
        try {
            diskLruCache = DiskLruCache.open(file,1,1,MAX_SIZE);
        } catch (IOException e) {
            Log.i("LZH",""+e.toString());
            e.printStackTrace();
        }
    }
    public static File getShareFile(){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/disk";
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.i("LZH","can't create file");
                e.printStackTrace();
            }
        }
        return file;
    }
    public void writeIntent(String key,Intent intent){
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            OutputStream out = editor.newOutputStream(0);
            BufferedOutputStream bufout = new BufferedOutputStream(out);
            Parcel parcel = Parcel.obtain();
            parcel.setDataPosition(0);
            parcel.writeParcelable(intent,0);
            bufout.write(parcel.marshall());
            bufout.flush();
            bufout.close();
            out.flush();
            out.close();
            editor.commit();
            diskLruCache.flush();
            parcel.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Intent readIntent(String key){
        Intent intent = null;
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            InputStream in = snapshot.getInputStream(0);
            byte[] bytes = new byte[in.available()];
            Parcel parcel = Parcel.obtain();
            parcel.unmarshall(bytes,0,bytes.length);
            parcel.setDataPosition(0);
            intent = parcel.readParcelable(Intent.class.getClassLoader());
            parcel.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return intent;
    }
    public void writeMotionEvents(String key, List<MotionEvent> list){
        if(list.size()<=0){
            Log.i("LZH","list size is 0");
            return ;
        }
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            OutputStream outputStream = editor.newOutputStream(0);
            BufferedOutputStream bufOut = new BufferedOutputStream(outputStream);
            Parcel parcel = Parcel.obtain();
            parcel.writeInt(list.size());
            parcel.writeList(list);
            bufOut.write(parcel.marshall());
            bufOut.flush();
            bufOut.close();
            outputStream.flush();
            outputStream.close();
            editor.commit();
            diskLruCache.flush();
            parcel.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public List<MotionEvent> readMotionEvents(String key){
        ArrayList<MotionEvent> list;
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            InputStream in = snapshot.getInputStream(0);
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            Parcel parcel = Parcel.obtain();
            parcel.unmarshall(bytes,0,bytes.length);
            parcel.setDataPosition(0);
            int size = parcel.readInt();
            MotionEvent[] motionEvents = new MotionEvent[size];
            parcel.readTypedArray(motionEvents,MotionEvent.CREATOR);
            in.close();
            snapshot.close();
            list = new ArrayList<>();
            for(int i=0;i<size;i++){
                list.add(motionEvents[i]);
            }
            parcel.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String trimKey(String key){
        String [] src = key.split("\\.");
        String res = "";
        for(int i=0;i<src.length;i++){
            res+=src[i];
        }
        return res;
    }
}
