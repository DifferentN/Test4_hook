package com.example.a17916.test4_hook.TestGenerateTemple;

import android.os.Parcel;
import android.os.Parcelable;

public class PageResult implements Parcelable {
    private String nodeValue,entityType;
    public PageResult(String nodeValue,String entityType){
        this.nodeValue = nodeValue;
        this.entityType = entityType;
    }

    protected PageResult(Parcel in) {
        nodeValue = in.readString();
        entityType = in.readString();
    }

    public static final Creator<PageResult> CREATOR = new Creator<PageResult>() {
        @Override
        public PageResult createFromParcel(Parcel in) {
            PageResult pageResult = new PageResult(in.readString(),in.readString());
            return pageResult;
        }

        @Override
        public PageResult[] newArray(int size) {
            return new PageResult[size];
        }
    };

    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getNodeValue() {

        return nodeValue;
    }

    public String getEntityType() {
        return entityType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nodeValue);
        dest.writeString(entityType);
    }
}
