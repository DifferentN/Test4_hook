package com.example.a17916.test4_hook.util.normal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class IntentUtil {
    private static String[] basicType = new String[]{"java.lang.String","java.lang.Integer","java.lang.Float",
            "java.lang.Double","java.lang.Byte","java.lang.Character","java.lang.Long",
            "java.lang.Boolean"};

    public static String getValue(Bundle bundle,String targetKey){
        Set<String> keySet = bundle.keySet();
        Object o = null;
        String clazzName = null;
        for(String key:keySet){
            o = bundle.get(key);
            clazzName = o.getClass().getName();
            if(key.compareTo(targetKey)==0){
                if(isBasicType(clazzName)){
                    return String.valueOf(o);
                }else if(isArray(clazzName)){

                    Log.i("LZH","类型为数组: 未实现"+clazzName);
                }else if(isList(o)){

                    Log.i("LZH","类型为链表: 未实现"+clazzName);
                }else if(isSerializable(o)){
                    return serializableToString((Serializable) o);
                }else if(isParcelable(o)){
                    return parcelableToString((Parcelable) o);
                }else {
                    Log.i("LZH","无法识别对应的类型: "+clazzName);
                }
            }
            if(clazzName.compareTo(Bundle.class.getName())==0){
                return getValue((Bundle) o,targetKey);
            }
        }
        return null;
    }
    private static boolean isBasicType(String clazzName){
        for(String type:basicType){
            if(type.compareTo(clazzName)==0){
                return true;
            }
        }
        return false;
    }
    private static boolean isArray(String clazzName){
        if(clazzName.startsWith("[")){
            return  true;
        }
        return false;
    }
    private static boolean isList(Object o){
        if(o instanceof List){
            return true;
        }
        return false;
    }
    private static boolean isSerializable(Object o){
        if(o instanceof Serializable){
            return true;
        }
        return false;
    }
    private static boolean isParcelable(Object o){
        if(o instanceof Parcelable){
            return true;
        }
        return false;
    }
    private static String serializableToString(Serializable serializable){
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(serializable);
        return jsonObject.toJSONString();

    }
    private static String parcelableToString(Parcelable parcelable){
        byte[] bytes = parcelableToByte(parcelable);
        String value = byteToString(bytes);
        return value;
    }
    private static byte[] parcelableToByte(Parcelable parcelable){
        Parcel parcel = Parcel.obtain();
        parcel.setDataPosition(0);
        parcel.writeParcelable(parcelable,0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }
    private static String byteToString(byte[] bytes){
        String value = Base64.encodeToString(bytes,0);
        return value;
    }

}
