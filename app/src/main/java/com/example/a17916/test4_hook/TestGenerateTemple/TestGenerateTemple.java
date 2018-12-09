package com.example.a17916.test4_hook.TestGenerateTemple;

import android.content.ComponentName;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a17916.test4_hook.view_data.MyViewNode;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class TestGenerateTemple {
    private static TestGenerateTemple generateTemple;
    private List<TestNode> nodeList ;
    private String activityName,shortActivityName;
    private String pkName;
    private Context context;
    private ComponentName componentName;
    public TestGenerateTemple(String activityName,String pkName){
        this.activityName = activityName;
        this.pkName = pkName;
        nodeList = new ArrayList<>();
    }

    public void createTemplate(MyViewNode viewNode){
        List<MyViewNode> viewNodes = new ArrayList<>();
        viewNodes.add(viewNode);
        MyViewNode node;
        while(!viewNodes.isEmpty()){
            node = viewNodes.remove(0);
            if(node.getView() instanceof TextView){
                addItem(node);
            }else if(node.getView() instanceof ViewGroup){
                List<MyViewNode> nodes = node.getChild();
                for(MyViewNode temp:nodes){
                    if(temp.getView() instanceof TextView||temp.getView()instanceof ViewGroup){
                        viewNodes.add(temp);
                    }
                }
            }
        }
        Log.i("LZH","write file");
        generateXML();
    }
    private void addItem(MyViewNode viewNode){
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
    public void generateXML(){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if(builder == null){
            return ;
        }
        File file = new File(path+"/"+activityName+".xml");
        Document dom = null;
        if(!file.exists()){
            try {
                file.createNewFile();
                dom = builder.newDocument();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            if(file.length()<=0){
                dom = builder.newDocument();
            }else{
                try {
                    dom = builder.parse(file);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
        }


        NodeList nodes = dom.getElementsByTagName(activityName);
        Node root;
        if(nodes.getLength()<=0){
            root = dom.createElement(activityName);
            dom.appendChild(root);
        }else{
            root = nodes.item(0);
        }
        Element child = null;
        for(TestNode node:nodeList){
            child = dom.createElement("child");
            child.setAttribute("nodeType",node.getNodeType());
            child.setAttribute("nodeValue",node.getNodeValue());
            root.appendChild(child);
        }


        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING,"utf-8");
            transformer.setOutputProperty(OutputKeys.VERSION,"1.0");
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            transformer.transform(new DOMSource(dom),new StreamResult(file));
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        Log.i("LZH","file path "+file.getAbsolutePath());

    }
}
