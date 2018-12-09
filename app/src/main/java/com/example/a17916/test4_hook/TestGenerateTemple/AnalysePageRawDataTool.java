package com.example.a17916.test4_hook.TestGenerateTemple;

import android.os.Environment;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a17916.test4_hook.template.Template;
import com.example.a17916.test4_hook.view_data.MyViewNode;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AnalysePageRawDataTool {
    private Template template;
    private String activityName;
    private static AnalysePageRawDataTool analysePageRawDataTool;
    private MyViewNode rootViewNode;
    public AnalysePageRawDataTool(){

    }
    public static AnalysePageRawDataTool newInstance(){
        if(analysePageRawDataTool==null){
            analysePageRawDataTool = new AnalysePageRawDataTool();

        }
        return analysePageRawDataTool;
    }
    /**
     * 获得页面内的<实体类型，结点值>
     * @param activityName 要获取的目标页面
     * @return
     */
    public ArrayList<PageResult> getResultByActivityName(String activityName,MyViewNode viewNode){
        Template template = getTemplateByActivityName(activityName);
        ArrayList<PageResult> pageResults = new ArrayList<>();
        List<TestNode> testNodes = generateNodeTypeAndValue(viewNode);
        PageResult pageResult;
        String entityType;
        for(TestNode node:testNodes){
            entityType = template.getEntityType(node.getNodeType());
            if(entityType.length()>0){
                pageResult = new PageResult(node.getNodeValue(),entityType);
                pageResults.add(pageResult);
            }
        }
        return pageResults;
    }

    /**
     * 对一个页面生成结点类型和结点数据的映射
     * @param viewNode 页面的根节点
     * @return 全部结点类型与数据映射 的链表
     */
    private List<TestNode> generateNodeTypeAndValue(MyViewNode viewNode){
        List<TestNode> nodeList = new ArrayList<>();
        List<MyViewNode> viewNodes = new ArrayList<>();
        viewNodes.add(viewNode);
        MyViewNode node;
        while(!viewNodes.isEmpty()){
            node = viewNodes.remove(0);
            if(node.getView() instanceof TextView){
                addItem(nodeList,node);
            }else if(node.getView() instanceof ViewGroup){
                List<MyViewNode> nodes = node.getChild();
                for(MyViewNode temp:nodes){
                    if(temp.getView() instanceof TextView||temp.getView()instanceof ViewGroup){
                        viewNodes.add(temp);
                    }
                }
            }
        }
        return nodeList;
    }

    /**
     *  将符合要求的结点添加到链表中，并设置结点的类型
     * @param nodeList
     * @param viewNode
     */
    private void addItem(List<TestNode> nodeList,MyViewNode viewNode){
        String text = viewNode.getViewText();
        if(text.length()>50){
            return;
        }
        String nodeType="";
        MyViewNode parent = viewNode;
        boolean isListItem = false;
        while(parent!=null){
//            Log.i("LZH","node: "+parent.getViewTransitionTag()+"isItem: "+parent.isListItem);
            if(parent.isListItem()){
                isListItem = true;
            }
            if(!isListItem){
                nodeType+=parent.getSequence()+".";
            }
            nodeType+=parent.getViewTransitionTag()+"/";
            parent = parent.getParent();
        }
        TestNode testNode = new TestNode(nodeType,text);
        nodeList.add(testNode);
    }
    /**
     * 根据生成的xml文件生成一个模板
     * @param activityName 要获取的目标页面，也是xml文件的名，假设保存在
     * @return
     */
    private Template getTemplateByActivityName(String activityName){

        String tarPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/template/"+activityName+".xml";
        Log.i("LZH","tarPath: "+tarPath);
        File file = new File(tarPath);
        if(!file.exists()){
            Log.e("LZH","file not exist");
            return null;
        }
        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if(builder==null){
            Log.e("LZH","builder not create");
            return null;
        }
        Document dom = null;
        try {
            dom = builder.parse(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        NodeList nodeList = dom.getElementsByTagName("child");
        Node node;
        Template.Nodetuple nodetuple;
        Template template = new Template(activityName);
        //2位数组，第一位为结点类型 第二位为实体类型
        String src[];
        for(int i=0;i<nodeList.getLength();i++){
            node = nodeList.item(i);
            src = getNodeTypeAndEntityType(node);
            nodetuple = new Template.Nodetuple(src[0],src[1]);
            template.addTuple(nodetuple);
        }
        return template;
    }

    /**
     *
     * @param node 需要获取结点和实体类型的结点
     * @return 以数组的形式返回结点类型和实体类型
     */
    private String[] getNodeTypeAndEntityType(Node node){
        String res[] = new String[2];
        NamedNodeMap nodeMap = node.getAttributes();
        Node attrNode;
        for(int i=0;i<nodeMap.getLength();i++){
            attrNode = nodeMap.item(i);
            if(attrNode.getNodeName().compareTo("nodeType")==0){
                res[0] = attrNode.getNodeValue();
            }else if(attrNode.getNodeName().compareTo("entityType")==0){
                res[1] = attrNode.getNodeValue();
            }
        }
        return res;
    }


}
