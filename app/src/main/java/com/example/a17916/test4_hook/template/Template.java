package com.example.a17916.test4_hook.template;

import java.util.ArrayList;
import java.util.List;

public class Template {
    private List<Nodetuple> nodetuples;
    private String activityName;

    public Template(String activityName){
        this.activityName = activityName;
        nodetuples = new ArrayList<>();
    }
    //返回“”,表示无法匹配结点类型对应的实体类型
    public String getEntityType(String nodeType){
        String res="";
        for(Nodetuple tuple:nodetuples){
            if(tuple.nodeType.compareTo(nodeType)==0){
                return tuple.entityType;
            }
        }
        return "";
    }
    public void addTuple(Nodetuple nodetuple){
        nodetuples.add(nodetuple);
    }
    public static class Nodetuple{
        private String nodeType;
        private String entityType;
        public Nodetuple(String nodeType,String entityType){
            this.nodeType = nodeType;
            this.entityType = entityType;
        }
    }
}
