package com.example.a17916.test4_hook.openTaskModule;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.a17916.test4_hook.database.ActivityData;
import com.example.a17916.test4_hook.database.AppData;
import com.example.a17916.test4_hook.database.QueryManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TaskGenerator {

    //根节点名称
    public static final String TaskModule = "TaskModule";
    //任务模板文件中根节点的页面名称
    public static final String Attribute_ActivityName = "ActivityName";
    //任务模板文件中根节点的资源名称
    public static final String Attribute_ResName = "ResourceType";

    public static final String Step = "Step";
    public static final String Attr_Type = "Type";
    public static final String Attr_TargetActivityName = "TargetActivityName";
    public static final String Attr_MotionEventSeq = "MotionEventSeq";
    public static final String Type_Intent = "Intent";
    public static final String Type_Text = "Text";
    public static final String Type_MotionEvent = "MotionEvent";



    private Context context;
    private UnionTaskBuilder taskBuilder;
    private QueryManager queryManager;
    public TaskGenerator(Context context){
        this.context = context;
        taskBuilder = new UnionTaskBuilder(context);
        queryManager = QueryManager.getInstance();
    }

    public UnionOpenActivityTask generatorTask(int activityId,String resType,String resEntityName,
                                               int resId){
        String activityName = QueryManager.getInstance().queryActivityNameByActivityId(activityId);

        return generatorTask(activityName,resType,resEntityName,resId);
    }
    public UnionOpenActivityTask generatorTask(String activityName,String resType,String resEntityName,
                                               int resId){
        File file = findModuleTaskFile(activityName,resType);
//        Log.i("LZH","file aN: "+activityName+"resType: "+resType);
        if(file == null){
            Log.i("LZH","未能找到任务模板文件");
            return  null;
        }

        UnionOpenActivityTask task = createTask(file,resId,resType,resEntityName,activityName);

        return task;
    }

    public void GenerateTaskByJson(JSONObject jsonObject){

    }

    /**
     * 根据资源类型和显示它的页面找到一个任务模板
     * @param activityName
     * @param resType
     * @return
     */
    private File findModuleTaskFile(String activityName,String resType){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        //文件放在taskModule文件夹下
        path+="/taskModule";
        File dir = new File(path);
        if(!dir.isDirectory()){
            Log.i("LZH","文件夹不存在： "+path);
            return null;
        }

        for (File file:dir.listFiles()){
            if(checkFile(activityName,resType,file)){
                return file;
            }
        }

        return null;
    }

    /**
     * 查看是否是指定的目标模板
     * @param activityName
     * @param resType
     * @param file
     * @return
     */
    private boolean checkFile(String activityName,String resType,File file){
        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if(builder == null){
            Log.i("LZH","无法创建xml builder");
            return false;
        }
        Document dom = null;
        try {
            dom = builder.parse(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        if(dom ==null){
            Log.i("LZH","无法加载file: "+file.getName());
            return false;
        }
        Element root = dom.getDocumentElement();
        if(root.getAttribute(Attribute_ActivityName).equals(activityName)&&root.getAttribute(Attribute_ResName).equals(resType)){
            return true;
        }
        return false;
    }

    /**
     * 利用任务模板创建一个任务，此任务是在打开对应App后要执行的步骤
     * @param file 任务模板文件
     * @param resId 资源的Id
     * @param resType 资源的类型
     * @param text 资源的名称 可以作为搜索词使用
     * @return 要执行的任务任务
     */
    private UnionOpenActivityTask createTask(File file,int resId,String resType,String text,String activityName){

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
        NodeList nodeList = dom.getElementsByTagName(Step);
        int size = nodeList.getLength();
        Node node;
        Element element;
        String type,targetActivityName;
        int seq;
        for(int i=0;i<size;i++){
            node = nodeList.item(i);
            if(node instanceof Element){
                element = (Element) node;
                type = element.getAttribute(TaskGenerator.Attr_Type);
                //根据type 来确定下一步是什么操作
                if(type.equals(Type_Intent)){
                    Log.i("LZH","type: "+resType+" resname: "+text+" intent step ");
                    //Intent操作，用资源ID和activity名称确定
                    targetActivityName = element.getAttribute(Attr_TargetActivityName);
                    createIntentStep(resId,activityName,targetActivityName);
                }else if(type.equals(Type_Text)){
                    //输入文本操作，只需要 搜索词
                    Log.i("LZH","type: "+resType+" resname: "+text+" text step ");
                    targetActivityName = element.getAttribute(Attr_TargetActivityName);
                    createTextStep(text,targetActivityName);
                }else if(type.equals(Type_MotionEvent)){
                    //点击事件 用Activity名称，资源类型，事件顺序确定
                    Log.i("LZH","type: "+resType+" resname: "+text+" motion step ");
                    targetActivityName = element.getAttribute(Attr_TargetActivityName);
                    seq = Integer.valueOf(element.getAttribute(Attr_MotionEventSeq));
                    createMotionEventSteo(targetActivityName,resType,seq);
                }
            }else{
                Log.i("LZH","node无法转化为element");
            }
        }

        UnionOpenActivityTask task = taskBuilder.generateTask();
        return task;

    }

    /**
     *
     * @param resId
     * @param activityName 显示资源的页面
     * @param fromActivityName 指定step在哪个页面发生，由任务模板指定
     */
    private void createIntentStep(int resId,String activityName,String fromActivityName){
        Intent intent = queryManager.queryIntent(activityName,resId);
        String appName = queryManager.queryAppDataByActivityName(activityName).getAppName();
        taskBuilder.addIntentStep(intent,fromActivityName,appName);
    }
    //
    private void createTextStep(String text,String activityName){
        String appName = queryManager.queryAppDataByActivityName(activityName).getAppName();
        taskBuilder.addTextStep(text,activityName,appName);
    }

    /**
     * 回放点击事件中 显示的页面和执行step的页面必须是同一个,所以可以用一个activityName
     * @param activityName
     * @param resType
     * @param seq
     */
    private void createMotionEventSteo(String activityName,String resType,int seq){
        byte [] bytes = queryManager.queryMotionEvent(activityName,resType,seq);
        String appName = queryManager.queryAppDataByActivityName(activityName).getAppName();
        taskBuilder.addMotionEventStep(bytes,activityName,appName);
    }
}
