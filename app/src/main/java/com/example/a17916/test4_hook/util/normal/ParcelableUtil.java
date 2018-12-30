package com.example.a17916.test4_hook.util.normal;

import android.content.Intent;
import android.os.Parcel;

public class ParcelableUtil {

    /**
     *  将Intent转化为byte
     * @param intent
     * @return
     */
    public static byte[] intent2Bytes(Intent intent){
        Parcel parcel = Parcel.obtain();
        parcel.setDataPosition(0);
        parcel.writeParcelable(intent,0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    /**
     *  将byte数组转化为Intent
     * @param bytes
     * @return
     */
    public static Intent byte2Intent(byte[] bytes){
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes,0,bytes.length);
        parcel.setDataPosition(0);
        Intent intent = parcel.readParcelable(Intent.class.getClassLoader());
        return intent;
    }
}
