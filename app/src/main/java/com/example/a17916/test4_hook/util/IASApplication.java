package com.example.a17916.test4_hook.util;


import android.app.Application;

//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.socks.library.KLog;

/**
 * Created by vector on 16/4/17.
 */
public class IASApplication extends Application {


    public IASApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Fresco.initialize(this);
//        KLog.init(BuildConfig.LOG_DEBUG);

    }
}
