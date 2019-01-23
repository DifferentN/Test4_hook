package com.example.a17916.test4_hook.util.normal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.a17916.test4_hook.monitorService.SaveJSONAndIntentByIt;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AddJsonParameterUtil {
    private final String API_NAME = "apiName";
    private final String VALUE = "value";
    private final String JSON_CHILD = "json";

    private Activity activity;


    public AddJsonParameterUtil(Activity activity){
        this.activity = activity;
    }

    public void addParameter(String activityName,String specialKey,Intent intent){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/douban.xml";
        List<JSONNode> lists = getJSONFromXML(activityName,path);

//        String tempJsonArrayStr = "[{\"basic_name\":\"page_uri\",\"basic_type\":\"java.lang.String\",\"basic_value\":\"parameter1\"},{\"basic_name\":\"subject_uri\",\"basic_type\":\"java.lang.String\",\"basic_value\":\"parameter2\"},[{\"object_name\":\"com.douban.frodo.SUBJECT\",\"object_type\":\"com.douban.frodo.subject.model.subject.Subject\"},{\"attribute_name\":\"title\",\"attribute_type\":\"java.lang.String\",\"attribute_value\":\"parameter3\"}]]";
//        JSONArray tempJsonArray = JSONArray.parseArray(tempJsonArrayStr);
//        JSONNode tempNode = new JSONNode("<com.douban.frodo.subject.activity.LegacySubjectActivity>1",tempJsonArray);
//        lists.add(tempNode);

        JSON json = null;
        int size = 0;

        int first = 0;
        Intent reCreateIntent = null;
        JSONArray jsonArray = null;
        for(JSONNode node:lists){
            jsonArray = node.jsonArray;
            size = jsonArray.size();
            for(int i=0;i<size;i++){
                json = (JSON) jsonArray.get(i);
                if(json instanceof JSONArray){
                    addObjectParameter((JSONArray) json,intent);
                }else if(json instanceof JSONObject){
                    addBasicParameter((JSONObject) json,intent);
                }
            }

            addIntentUriToJSONArray(jsonArray,intent);

            sendAndSaveJson(node.apiName,specialKey,jsonArray);
            if(first==0){
                reCreateIntent = recreateIntent(jsonArray);
                first++;
            }
            sendAndSaveIntent(node.apiName,specialKey,reCreateIntent);
        }


    }
    //在JSON中添加一些必要信息，如IntentUri 应用的名称
    private void addIntentUriToJSONArray(JSONArray jsonArray,Intent intent){
        String intentUri = intent.toUri(0);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("intentUri",intentUri);

        String appName = AppUtil.getAppName(activity);
        jsonObject.put("appName",appName);
        jsonArray.add(0,jsonObject);
    }
    //测试
    private void sendAndSaveJson(String apiName,String specialKey,JSONArray jsonArray){
        Intent broadIntent = new Intent();
        broadIntent.setAction(SaveJSONAndIntentByIt.SAVE_JSON);
        String jsonData = jsonArray.toJSONString();
        String key = apiName+"/"+specialKey+"/"+"JSON";
        broadIntent.putExtra(SaveJSONAndIntentByIt.KEY,key);
        broadIntent.putExtra(SaveJSONAndIntentByIt.JSON_DATA,jsonData);
        activity.sendBroadcast(broadIntent);
    }
    //测试
    private Intent recreateIntent(JSONArray jsonArray){
        Intent intent = null;
        JSON json = null;
        int size = jsonArray.size();


        JSONObject headJson = jsonArray.getJSONObject(0);
        String intentUri = headJson.getString("intentUri");
        try {
            intent = Intent.parseUri(intentUri,0);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if(intent==null){
            Log.i("LZH","初始化Intent失败");
            return null;
        }

        for(int i=1;i<size;i++){
            json = (JSON) jsonArray.get(i);
            if(json instanceof  JSONObject){
                addBasicToIntent((JSONObject) json,intent);
            }else if(json instanceof JSONArray){
                addObjectToIntent((JSONArray) json,intent);
            }
        }
        return intent;
    }
    //测试
    private void sendAndSaveIntent(String apiName,String specialKey,Intent intent){
        Intent broadIntent = new Intent();
        broadIntent.setAction(SaveJSONAndIntentByIt.SAVE_INTENT_CREATEBY_JSON);

        String key = apiName+"/"+specialKey+"/"+"Intent";
        broadIntent.putExtra(SaveJSONAndIntentByIt.KEY,key);
        broadIntent.putExtra(SaveJSONAndIntentByIt.INTENT_DATA,intent);
        if(intent == null){
            Log.i("LZH","不能创建Intent");
        }
        activity.sendBroadcast(broadIntent);
    }
    //测试
    private void addBasicToIntent(JSONObject jsonObject,Intent intent){
        String key = jsonObject.getString("basic_name");
        String type = jsonObject.getString("basic_type");
        String value = jsonObject.getString("basic_value");
        if(value!=null){
            if(type.equals("java.lang.String")){
                intent.putExtra(key,value);
            }else if(type.equals("java.lang.Float")){
                intent.putExtra(key,Float.valueOf(value));
            }else if(type.equals("java.lang.Double")){
                intent.putExtra(key,Double.valueOf(value));
            }else if(type.equals("java.lang.Byte")){
                intent.putExtra(key,Byte.valueOf(value));
            }else if(type.equals("java.lang.Character")){
                intent.putExtra(key,value.charAt(0));
            }else if(type.equals("java.lang.Long")){
                intent.putExtra(key,Long.valueOf(value));
            }else if(type.equals("java.lang.Boolean")){
                intent.putExtra(key,Boolean.valueOf(value));
            }
        }

    }
    //测试
    private void addObjectToIntent(JSONArray jsonArray,Intent intent){
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);

        String className = jsonObject.getString("object_type");
        String key = jsonObject.getString("object_name");
        String fatherType = jsonObject.getString("objectFather_type");

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
//        ClassLoader classLoader = null;

        Class clazz = null;
        int size = jsonArray.size();
        JSONObject attrJson = null;
        Object parameter = null;
        try {
            clazz = classLoader.loadClass(className);
            if(clazz==null){
                Log.i("LZH","重新创建Intent时，无法创建对象参数");
            }
            parameter = clazz.newInstance();
            Field field = null;
            String attrName = null,value = null,type = null;
            for(int i=1;i<size;i++){
                attrJson = (JSONObject) jsonArray.get(i);
                attrName = attrJson.getString("attribute_name");
                value = attrJson.getString("attribute_value");
                type = attrJson.getString("attribute_type");
                field = clazz.getField(attrName);
                field.setAccessible(true);
                fillAttributeToObject(field,parameter,value,type);
            }
            if(fatherType.compareTo(Serializable.class.getName())==0){
                intent.putExtra(key,(Serializable)parameter);
            }else if(fatherType.compareTo(Parcelable.class.getName())==0){
                intent.putExtra(key,(Parcelable)parameter);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }
    //测试
    private Object fillAttributeToObject(Field field,Object o,String value,String type){
        if(value==null||value.length()<=0){
            return null;
        }
        if(type.equals("java.lang.String")){
            return value;
        }else if(type.equals("java.lang.Float")){
            return Float.valueOf(value);
        }else if(type.equals("java.lang.Double")){
            return Double.valueOf(value);
        }else if(type.equals("java.lang.Byte")){
            return Byte.valueOf(value);
        }else if(type.equals("java.lang.Character")){
            return value.charAt(0);
        }else if(type.equals("java.lang.Long")){
            return Long.valueOf(value);
        }else if(type.equals("java.lang.Boolean")){
            return  Boolean.valueOf(value);
        }
        Log.i("LZH","重构Intent时，无法识别基础类型："+type);
        return null;
    }

    /**
     * 从文件中读取json，并进行复制，然后返回复制的结果
     * @param path
     * @return
     */
    private List<JSONNode> getJSONFromXML(String activityName,String path){
        ArrayList<JSONNode> list = new ArrayList<>();
        File file = new File(path);
        if(!file.exists()){
            Log.i("LZH","找不到要读取的XML文件："+path);
            return list;
        }
        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document dom = null;
        try {
            dom = builder.parse(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        if(dom==null){
            Log.i("LZH","无法解析xml文件");
            return list;
        }
        NodeList nodeList = dom.getElementsByTagName(API_NAME);
        int size = nodeList.getLength();
        Node node = null;
        JSONNode jsonNode = null;
        for(int i=0;i<size;i++){
            node = nodeList.item(i);
            jsonNode = createJSONNodeByXMLNode(node);
            if(jsonNode!=null&&jsonNode.apiName.startsWith(activityName)){
                list.add(jsonNode);
            }
        }

        return list;
    }

    private JSONObject addBasicParameter(JSONObject targetJsonObject, Intent intent){
        String key = targetJsonObject.getString("basic_name");
        String value = IntentUtil.getValue(intent.getExtras(),key);
        targetJsonObject.put("basic_value",value);
        return targetJsonObject;
    }

    /**
     * 假设对象中的属性为基础属性
     * @param targetJsonArray 需要填充属性的对象的JSON
     * @param intent
     * @return
     */
    private JSONArray addObjectParameter(JSONArray targetJsonArray, Intent intent){
        JSONObject objectJSON = (JSONObject)targetJsonArray.get(0);
        int size = targetJsonArray.size();
        String key = objectJSON.getString("object_name");
        String value = IntentUtil.getValue(intent.getExtras(),key);
        if(value==null){
            Log.i("LZH","未找到："+key);
            return targetJsonArray;
        }
        String type = IntentUtil.getType(intent.getExtras(),key);
        objectJSON.put("objectFather_type",type);

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
//        ClassLoader classLoader = null;

        Class clazz = null;
        JSONObject attrJson = null;
        if(type.compareTo(Serializable.class.getName())==0){
            Serializable serializable = IntentUtil.getSerializable(key,intent.getExtras());
            try {
                clazz = classLoader.loadClass(type);
                Field field = null;
                String attrName = null;
                for(int i=1;i<size;i++){
                    attrJson = targetJsonArray.getJSONObject(i);
                    attrName = attrJson.getString("attribute_name");
                    field = clazz.getField(attrName);
                    field.setAccessible(true);
                    Object o = field.get(serializable);
                    attrJson.put("attribute_value",String.valueOf(o));
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }else if(type.compareTo(Parcelable.class.getName())==0){
            Parcelable parcelable = IntentUtil.getParcelable(key,intent.getExtras());
            try {
                clazz = classLoader.loadClass(type);
                Field field = null;
                String attrName = null;
                for(int i=1;i<size;i++){
                    attrJson = targetJsonArray.getJSONObject(i);
                    attrName = attrJson.getString("attribute_name");
                    field = clazz.getField(attrName);
                    field.setAccessible(true);
                    Object o = field.get(parcelable);
                    attrJson.put("attribute_value",String.valueOf(o));
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return targetJsonArray;
    }

    private JSONNode createJSONNodeByXMLNode(Node node){
        JSONNode jsonNode = null;
        Element element = (Element) node;
        String apiName = element.getAttribute(VALUE);
        NodeList lists = element.getElementsByTagName(JSON_CHILD);
        Element jsonChild = (Element) lists.item(0);
        String jsonArrayStr = jsonChild.getTextContent();
        if(jsonArrayStr.compareTo("[]")==0){
            jsonNode = new JSONNode(apiName,new JSONArray());
        }else{
//            Log.i("LZH","jsonArrayStr: "+jsonArrayStr);
            JSONArray jsonArray = JSONArray.parseArray(jsonArrayStr);
            if(jsonArray==null){
                Log.i("LZH",apiName+" : JSONArray解析失败");
                return null;
            }
            jsonNode = new JSONNode(apiName,jsonArray);
        }
        return jsonNode;
    }

    static class JSONNode{
        public String apiName;
        public JSONArray jsonArray;
        public JSONNode(){

        }
        public JSONNode(String apiName,JSONArray jsonArray){
            this.apiName = apiName;
            this.jsonArray = jsonArray;
        }
    }

}
