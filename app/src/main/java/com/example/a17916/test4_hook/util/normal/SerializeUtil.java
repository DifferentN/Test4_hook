package com.example.a17916.test4_hook.util.normal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.net.URISyntaxException;

public class SerializeUtil {
    public static Intent getIntent(String ser, ClassLoader classLoader){
        JSONObject intentInfo = JSONObject.parseObject(ser);
        String target = intentInfo.getString("intentUri");
        JSONArray datas = intentInfo.getJSONArray("datas");
        Intent targetIntent = null;
        try {
            targetIntent = Intent.parseUri(target,0);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //TODO 保存的时候我们不保存基本类型的数据？
        if(datas != null && datas.size() > 0){
            Bundle bundle = new Bundle();
            for(int i = 0; i < datas.size(); i++) {
                JSONObject data = datas.getJSONObject(i);


                Class subjectClass;
                try {
                    subjectClass = classLoader.loadClass(data.getString("class"));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                String key = data.getString("key");
                String value = data.getString("value");
                //这里需要判断对应的Class实现的是哪一个接口，对应的使用bundle的putParcelable和putSerializable
                boolean isFromParcelable = Parcelable.class.isAssignableFrom(subjectClass);
                boolean isFromSerializable = Serializable.class.isAssignableFrom(subjectClass);
                if(isFromSerializable) {
                    bundle.putSerializable(key, (Serializable) JSONObject.parseObject(value, subjectClass));
                    //bundle.putSerializable(key, (Serializable) new Gson().fromJson(value, subjectClass));
                }else if(isFromParcelable) {
                    bundle.putParcelable(key, (Parcelable) JSONObject.parseObject(value, subjectClass));
                    //bundle.putParcelable(key, (Parcelable) new Gson().fromJson(value, subjectClass));
                }
                targetIntent.putExtras(bundle);
            }
        }
        return targetIntent;
    }
    //将Intent序列化成String存储
    public static String serialize(Intent intent){
        if(intent == null){
//            KLog.v("intentliuyi", "intent is null");
        }
        Bundle b = intent.getExtras();
        JSONObject result = new JSONObject();
        //TODO 查看一下toUri的不同参数的作用
//        KLog.v("intentliuyi", "11111111111111");
        result.put("intentUri", intent.toUri(0));
//        KLog.v("intentliuyi", "222222222222222222");
        JSONArray datas = new JSONArray();
        JSONObject ob;
        if(b!= null && b.keySet() != null) {
            for (String key : b.keySet()) {
                Object o = b.get(key);
                if(o == null) {
//                    KLog.v("intentliuyi", "key: " + key + ", but value null");
                    continue;
                }
                String className = o.getClass().getName();

//                KLog.v("intentliuyi", "className: " + className + " , key: " + key);

                //如果不是基本数据类型，就是Parcelable或者Serializable对象，需要序列化成JSON格式的保存
                //TODO ArrayList还得特殊处理
                if (!className.startsWith("java") && !className.startsWith("[")) {
                    JSONObject data = new JSONObject();
                    //判断是Parcelable还是Serializable的对象
                    boolean isFromParcelable = b.get(key) instanceof Parcelable;
                    boolean isFromSerializable = b.get(key) instanceof Serializable;
                    if (isFromParcelable)
                        ob = (JSONObject) JSONObject.toJSON(b.getParcelable(key));
                    else if (isFromSerializable)
                        ob = (JSONObject) JSONObject.toJSON(b.getSerializable(key));
                    else
                        ob = (JSONObject) JSONObject.toJSON(b.get(key));
                    data.put("key", key);
                    data.put("value", ob.toJSONString());
                    data.put("class", className);
                    datas.add(data);
                }
            }
        }
        result.put("datas", datas);
        //TODO sourceClass，暂时没保存，如果需要保存则应该需要用一个全局变量来保存一下上一个Activity
        result.put("sourceClass", "");
        return result.toJSONString();
    }
    public static String getAbbr(String name){
        String[] words = name.split("\\.");
        if(words.length == 0)
            return name;
        String result = "";
        for(int i = 0; i < words.length; i++){
            result += (""+words[i].charAt(0));
        }
        return result;
    }
}
