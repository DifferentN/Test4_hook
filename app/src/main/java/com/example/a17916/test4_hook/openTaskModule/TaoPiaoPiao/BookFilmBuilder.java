package com.example.a17916.test4_hook.openTaskModule.TaoPiaoPiao;

import android.content.Context;
import android.content.Intent;

import com.example.a17916.test4_hook.monitorService.MyActivityHandler;
import com.example.a17916.test4_hook.openTaskModule.StepContent;
import com.example.a17916.test4_hook.openTaskModule.TaskBuilder;
import com.example.a17916.test4_hook.openTaskModule.UnionOpenActivityTask;
import com.example.a17916.test4_hook.share.SavePreference;

/**
 * 用来打开淘票票的购买电影页面
 * com.taobao.movie.android.app.oscar.ui.cinema.activity.ScheduleListRootActivity
 */
public class BookFilmBuilder extends TaskBuilder {

    public BookFilmBuilder(Context context) {
        super(context);
    }

    @Override
    public void addStepToTask(UnionOpenActivityTask task) {
        Intent intent = savePreference.getIntent("com.taobao.movie.android.app.oscar.ui.cinema.activity.ScheduleListRootActivity");
        StepContent stepContent = new StepContent(StepContent.INTENT_TYPE,intent,null,null);

    }
}
