package com.example.a17916.test4_hook.accessibilityService;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class InputTextService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        AccessibilityNodeInfo rootNode=null;
        rootNode = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list;
        switch (eventType){
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
//                list = rootNode.findAccessibilityNodeInfosByViewId("com.yongche.android:id/keyWordInputCET");
//                Log.i("LZH","检测到获得焦点");
//                if(list!=null&&!list.isEmpty()){
//                    Log.i("LZH","获得text");
//                }
//                list = rootNode.findAccessibilityNodeInfosByViewId("button2");
//                if(list!=null&&!list.isEmpty()){
//                    Log.i("LZH","获得bt--1");
//                }
//                list = rootNode.findAccessibilityNodeInfosByViewId("com.example.a17916.test3_1:id/button2");
//                if(list!=null&&!list.isEmpty()){
//                    Log.i("LZH","获得bt--2");
//                }
//                list = rootNode.findAccessibilityNodeInfosByViewId("R.id.button2");
//                if(list!=null&&!list.isEmpty()){
//                    Log.i("LZH","获得bt--3");
//                }
//                for(AccessibilityNodeInfo node:list){
//                    node.setText("北京房山");
//                    Log.i("LZH","输入数据");
//                }
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }
}
