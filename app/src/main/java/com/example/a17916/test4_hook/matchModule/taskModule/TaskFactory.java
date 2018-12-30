package com.example.a17916.test4_hook.matchModule.taskModule;

import android.content.Context;

import com.example.a17916.test4_hook.matchModule.taskModule.DouBanApp.DBFilmInfoPageTask;
import com.example.a17916.test4_hook.matchModule.taskModule.TaoPiaoPiao.TaoPiaoPiaoTask;
import com.example.a17916.test4_hook.matchModule.taskModule.YiDaoApp.YiDaoTask;
import com.example.a17916.test4_hook.monitorService.OpenActivityTask;

public class TaskFactory {
    private static OpenActivityTask task;
    public static OpenActivityTask getTaskByActivityName(String activityName, Context context){
        if(activityName.equals("com.douban.frodo.subject.activity.LegacySubjectActivity")){
            task = new DBFilmInfoPageTask(context);
            task.setTaskType("com.douban.frodo.subject.activity.LegacySubjectActivity");
        }else if(activityName.equals("com.taobao.movie.android.app.oscar.ui.cinema.activity.ScheduleListRootActivity")){
            //添加打开淘票票任务
            task = new TaoPiaoPiaoTask(null,context);
            task.setTaskType("com.taobao.movie.android.app.oscar.ui.cinema.activity.ScheduleListRootActivity");
//            myHandler.addTask(new TaoPiaoPiaoTask(myHandler,context));
        }else if(activityName.equals("com.yongche.android.YDBiz.Order.DataSubpage.address.StartEndAddress.OSearchAddressEndActivity")){
            //添加打开易到任务
            task = new YiDaoTask(null,context);
            task.setTaskType("com.yongche.android.YDBiz.Order.DataSubpage.address.StartEndAddress.OSearchAddressEndActivity");
//            myHandler.addTask(new YiDaoTask(myHandler,context));
        }else if(activityName.equals("com.douban.frodo.search.activity.SearchActivity")){
//            myHandler.addTask(new DouBanSearchTask(myHandler,context));
        }
        return task;
    }

}
