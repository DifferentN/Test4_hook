package com.example.a17916.test4_hook.database;

import android.util.Log;

import com.example.a17916.test4_hook.application.MyApplication;
import com.greendao.gen.DaoSession;
import com.greendao.gen.MotionDataDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class SaveManager {
    private static SaveManager saveManager;
    private DaoSession mDaoSession;
    public static SaveManager getInstance(){
        if(saveManager==null){
            saveManager = new SaveManager();
        }
        return saveManager;
    }
    public SaveManager(){
        MyApplication myApplication = MyApplication.getInstance();
        mDaoSession = myApplication.getDaoSession();
    }

    public void saveMotionEvent(String activityName,String resType,Long seq,byte[] bytes){
        MotionData motionData = null;
        MotionDataDao motionDataDao = mDaoSession.getMotionDataDao();
        QueryBuilder queryBuilder = motionDataDao.queryBuilder();
        queryBuilder.where(MotionDataDao.Properties.ActivityName.eq(activityName),
                MotionDataDao.Properties.ResCategory.eq(resType),
                MotionDataDao.Properties.MotionSeq.eq(seq));
        List<MotionData> motionDatas = queryBuilder.list();
        if(motionDatas.size()>0){
            //更新

            motionData = motionDatas.get(0);
            motionData.setBytes(bytes);
            motionDataDao.update(motionData);
            Log.i("LZH","更新点击事件： "+motionData.getId());
        }else{
            //插入
            Long id = motionDataDao.count()+1;
            motionData = new MotionData(id,activityName,resType,seq,bytes);
            motionDataDao.insert(motionData);
            Log.i("LZH","插入点击事件： "+motionData.getId());
        }
    }
}
