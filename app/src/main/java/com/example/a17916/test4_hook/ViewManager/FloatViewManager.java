package com.example.a17916.test4_hook.ViewManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.a17916.test4_hook.R;
import com.example.a17916.test4_hook.receive.LocalActivityReceiver;
import com.example.a17916.test4_hook.view.FloatView;

public class FloatViewManager {
    private FloatView floatView;
    private static FloatViewManager floatViewManager;
    private WindowManager.LayoutParams layoutParams;
    private Context context;
    private Activity activity;
    private WindowManager windowManager;
    public FloatViewManager(Context context){
        this.context = context;
        this.activity = (Activity) context;
    }
    public static FloatViewManager getInstance(Context context){
        if(floatViewManager == null){
            floatViewManager = new FloatViewManager(context);
        }
        return floatViewManager;
    }
    public void showFloatButton(){
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        floatView = new FloatView(context);
        if(layoutParams == null){
            layoutParams = new WindowManager.LayoutParams();
            layoutParams.width = floatView.width;
            layoutParams.height = floatView.height;
            layoutParams.gravity = Gravity.TOP|Gravity.LEFT;
            layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            layoutParams.format = PixelFormat.RGBA_8888;

            layoutParams.x = 0;
            layoutParams.y = 0;
        }

        //can't work
//        ((ViewGroup)activity.getWindow().getDecorView()).addView(floatView);
        floatView.setLayoutParams(layoutParams);
        windowManager.addView(floatView,layoutParams);

        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("LZH","click floatBt");
                Intent intent = new Intent();
                intent.setAction(LocalActivityReceiver.viewTree);
                context.sendBroadcast(intent);
            }
        });

        floatView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent();
                intent.setAction(LocalActivityReceiver.viewTree);
                context.sendBroadcast(intent);
                return false;
            }
        });
    }
}