package com.example.a17916.test4_hook.view_data;

import android.view.View;

import com.example.a17916.test4_hook.util.normal.SerializeUtil;

import java.io.Serializable;
import java.util.List;

public class MyViewNode implements Serializable,Comparable<MyViewNode> {
    private String viewText;
    private String viewTag,viewTransitionTag;
    private int total_View;
    private int depth;
    private List<MyViewNode> child;
    public boolean isList;
    private int x,y;
    private int width,height;
    private View view;
    private MyViewNode parent;
    private int nodeHash;
    private int sequence;
    private int nodeRelateHash;

    private int viewNodeIDRelative;

    public boolean isListItem = false;
    public void setView(View view) {
        this.view = view;
    }

    public View getView() {

        return view;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getSequence() {

        return sequence;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {

        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getViewText() {
        return viewText;
    }

    public String getViewTag() {
        return viewTag;
    }

    public void setViewTransitionTag(String viewTransitionTag) {
        this.viewTransitionTag = viewTransitionTag;
    }

    public String getViewTransitionTag() {

        return viewTransitionTag;
    }

    public int getTotal_View() {
        return total_View;
    }

    public int getDepth() {
        return depth;
    }

    public MyViewNode getParent() {
        return parent;
    }

    public void setParent(MyViewNode parent) {
        this.parent = parent;
    }

    public List<MyViewNode> getChild() {
        return child;
    }

    public boolean isList() {
        return isList;
    }

    public boolean isListItem(){
        return isListItem;
    }

    public void setViewText(String viewText) {
        this.viewText = viewText;
    }

    public void setViewTag(String viewTag) {
        this.viewTag = viewTag;
    }

    public void setTotal_View(int total_View) {
        this.total_View = total_View;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setChild(List<MyViewNode> child) {
        this.child = child;
    }

    public void setList(boolean list) {
        isList = list;
    }

    public void setNodeRelateHash(int nodeRelateHash) {
        this.nodeRelateHash = nodeRelateHash;
    }

    public void setViewNodeIDRelative(int viewNodeIDRelative) {
        this.viewNodeIDRelative = viewNodeIDRelative;
    }

    public int getNodeRelateHash() {

        return nodeRelateHash;
    }

    public int getViewNodeIDRelative() {
        return viewNodeIDRelative;
    }

    public void setNodeHash(int nodeHash) {
        this.nodeHash = nodeHash;
    }

    public int getNodeHash() {

        return nodeHash;
    }

    public String calStringWithoutPosition(){
        return  SerializeUtil.getAbbr(this.viewTag) + "-" + this.depth;
    }

    //返回的是class+深度+位置
    public String calString(){
        String res = SerializeUtil.getAbbr(this.viewTag) + "-" + depth + "-" + viewText;
        return res + "-" + (x + width/2) + "-" + (y + height/2);
    }
    public String toString(){
        //输出类名
        String res = viewTag;
        if(child!=null){
            res += " "+child.size();
        }
        if(child!=null&&child.size()>0){
            for(int i=0;i<child.size();i++){
                res+="/"+child.get(i).toString();
            }
        }
        //输出内容
//        res = viewText;
//        if(child!=null&&child.size()>0){
//            for(int i=0;i<child.size();i++){
//                res+="/"+child.get(i).toString();
//            }
//        }
        return res;
    }


    @Override
    public int compareTo(MyViewNode o) {
        int res = getY()-o.getY();
        if(res!=0){
            return res;
        }
        res = getNodeRelateHash() - o.getNodeRelateHash();
        if(res!=0){
            return res;
        }
        return getX()-o.getX();
    }
}
