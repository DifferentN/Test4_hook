package com.example.a17916.test4_hook.view_data;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.a17916.test4_hook.TestGenerateTemple.TestGenerateTemple;
import com.example.a17916.test4_hook.util.normal.ViewUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyViewTree {
    private MyViewNode root;
    private String pkName;
    private String activityName;
    private Context context;

    public static Class<? extends View>[] filtsBys = new Class[]{AbsListView.class, GridView.class};

    public MyViewTree(){}
    public MyViewTree(View decorView, String pkName){
        this.pkName = pkName;
        root = construct(decorView,0);
    }
    public MyViewTree(View deccorView,String pkName,String activityName){
        this(deccorView,pkName);
        this.activityName = activityName;
    }
    private MyViewNode construct(View rootView, int depth){
        MyViewNode now = null;
        if(rootView!=null){
            if(rootView.getVisibility()==View.GONE){
                return null;
            }
            int pos[] =new int[2];
            rootView.getLocationOnScreen(pos);
            now = new MyViewNode();
            now.setX(pos[0]);
            now.setY(pos[1]);
            now.setDepth(depth);
            now.setWidth(rootView.getWidth());
            now.setHeight(rootView.getHeight());
            now.setView(rootView);
            now.setViewTag(rootView.getClass().getName());
            now.setSequence(0);
            now.setViewTransitionTag(rootView.getClass().getSimpleName());
            if(rootView instanceof TextView){
                now.setViewText(((TextView) rootView).getText().toString());
            }else{
                now.setViewText("");
            }
            if (rootView instanceof WebView){
                now.setViewText("It is a WebView");
            }
            if(rootView instanceof ViewGroup){
                int childNum = ((ViewGroup) rootView).getChildCount();
                ArrayList<MyViewNode> child_list = new ArrayList<MyViewNode>();
                String hash_string = now.calString();
                String relate_hash_string = now.calStringWithoutPosition();
                for(int i=0,seq=0;i<childNum;i++){
                    View childView = ((ViewGroup)rootView).getChildAt(i);
                    MyViewNode childNode = construct(childView,depth+1);

                    if(childNode==null){
                        continue;
                    }
                    childNode.setParent(now);
                    child_list.add(childNode);
                    childNode.setSequence(seq++);

                }


                Collections.sort(child_list);
                boolean isList = isList(rootView);
                int child_Num = child_list.size();
//                if(!isList){
//                    if(child_Num <= 1){
//                        isList = false;
//                    }else{
//                        ArrayList<Integer> cnt = new ArrayList<>();
//                        cnt.add(child_list.get(0).getNodeRelateHash());
//                        int ccnt = 0;
//                        for (int i = 1; i < child_Num; i++) {
//                            int id = child_list.get(i).getNodeRelateHash();
//                            if (cnt.contains(id)) {
//                                ++ccnt;
//                            }
//                            else
//                                cnt.add(id);
//                        }
//                        if (ccnt >= child_list.size() * 2 / 3)
//                            isList = true;
//                    }
//                }
                for(int i=0;i<child_Num;i++){
                    MyViewNode node = child_list.get(i);
                    hash_string+=node.getNodeHash();
                    if(!isList){
                        relate_hash_string+=node.getNodeRelateHash();
                    }
                }
                now.isList = isList;
                now.setChild(child_list);
                if(isList(now)){
                    for(MyViewNode node:child_list){
                        node.isListItem = true;
                    }
                }
                now.setNodeHash(hash_string.hashCode());
                now.setNodeRelateHash(relate_hash_string.hashCode());
            }
        }else{
            now.setNodeHash(now.calString().hashCode());
            now.setNodeRelateHash(now.calStringWithoutPosition().hashCode());
        }
        return now;
    }
    public MyViewNode getViewNode(){
        return root;
    }

    private static boolean isList(MyViewNode viewNode){
        List<MyViewNode> childs = viewNode.getChild();
//        Log.i("LZH","node: "+viewNode.getViewTransitionTag());
        if(childs==null||childs.size()<3){
            return false;
        }
//        Log.i("LZH","size: "+childs.size());
        String viewName = childs.get(0).getViewTransitionTag();
        for(MyViewNode node:childs){
//            Log.i("LZH",viewName+":"+node.getViewTransitionTag());
            if(viewName.compareTo(node.getViewTransitionTag())!=0){

                return false;
            }
        }
        return true;
    }

    public static boolean isList(View root){

        boolean beFiltered = false;

        Class viewClass = root.getClass();
        for(int i = 0; i < filtsBys.length; i++) {
            beFiltered = (beFiltered || filtsBys[i].isAssignableFrom(viewClass));
            if (beFiltered) {
                return true;
            }
        }
        //RecyclerView特殊考虑，因为可能有些应用没有引入这个包，会报找不到这个类的错误
        return ViewUtil.fromRecyclerView(viewClass);
    }


}
