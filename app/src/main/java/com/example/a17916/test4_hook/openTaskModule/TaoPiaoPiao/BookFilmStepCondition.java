package com.example.a17916.test4_hook.openTaskModule.TaoPiaoPiao;

import com.example.a17916.test4_hook.openTaskModule.StepCondition;

public class BookFilmStepCondition extends StepCondition {
    private int time = 0;
    @Override
    public boolean canExecute(String curActivityName) {
        if(curActivityName.equals("com.taobao.movie.android.app.home.activity.MainActivity")){
            time++;
        }
        if(time<2){
            return false;
        }
        return true;
    }
}
