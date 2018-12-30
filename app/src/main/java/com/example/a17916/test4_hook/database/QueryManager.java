package com.example.a17916.test4_hook.database;

import android.content.Intent;
import android.util.Log;

import com.example.a17916.test4_hook.application.MyApplication;
import com.example.a17916.test4_hook.util.normal.ParcelableUtil;
import com.greendao.gen.ActivityDataDao;
import com.greendao.gen.DaoSession;
import com.greendao.gen.IntentDataDao;
import com.greendao.gen.MotionDataDao;
import com.greendao.gen.ResourceDataDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class QueryManager {
    private static QueryManager queryData;
    private DaoSession mDaoSession;
    public static QueryManager getInstance(){
        if(queryData == null){
            queryData = new QueryManager();
        }
        return queryData;
    }
    public QueryManager(){
        MyApplication myApplication = MyApplication.getInstance();
        mDaoSession = myApplication.getDaoSession();

    }

    public List<Intent> queryIntents(String resEntityName){
        List<Long> resIds = queryResByName(resEntityName);
        List<Long[]> resActIds = new ArrayList<>();
        List<Long> activityIds = null;
        //搜索含有该资源的页面
        for(Long resId:resIds){
            activityIds = queryActivityIdByResId(resId);
            for(Long activityId:activityIds){
                resActIds.add(new Long[]{activityId,resId});
            }
        }


        List<Intent> queryResult = new ArrayList<>();
        //根据以上信息得到可以显示该资源的Intent
        for(Long [] result:resActIds){
            queryResult.add(queryIntent(result[0],result[1]));
        }
        return queryResult;
    }

    /**
     * 通过资源实体名称来查询其资源ID
     * @param resEntityName 资源实体名称
     * @return 资源的Id
     */
    private List<Long> queryResByName(String resEntityName){
        ResourceDataDao resourceDataDao = mDaoSession.getResourceDataDao();
        QueryBuilder queryBuilder = resourceDataDao.queryBuilder();
        //根据资源实体名称chaxun
        queryBuilder.where(ResourceDataDao.Properties.ResEntityName.eq(resEntityName));

        ArrayList<Long> queryResult = new ArrayList<>();
        List<ResourceData> resourceDatas = queryBuilder.list();
        for(ResourceData resourceData:resourceDatas){
            queryResult.add(resourceData.getResId());
        }
        return queryResult;
    }

    /**
     * 通过资源Id 来查询可以显示它的activity/页面
     * @param resId
     * @return 查找到的可以显示该资源的一系列页面的Id
     */
    private List<Long> queryActivityIdByResId(Long resId){
        ArrayList<Long> queryResult = new ArrayList<>();

        ActivityDataDao activityDataDao = mDaoSession.getActivityDataDao();

        List<ActivityData> list = activityDataDao.loadAll();
        List<ResourceData> resDatas = null;
        for(ActivityData activityData:list){
            resDatas = activityData.getResourceDatas();
            for(ResourceData resourceData:resDatas){
                if(resourceData.getResId().equals(resId)){
                    queryResult.add(activityData.getActivityId());
                    break;
                }
            }
        }

        return queryResult;
    }

    /**
     * 通过页面Id 查询它显示资源的Intent
     * @param activityId
     * @param resId
     * @return
     */
    public Intent queryIntent(Long activityId,Long resId){
        IntentData intentData;
        Intent intent = null;

        IntentDataDao intentDataDao = mDaoSession.getIntentDataDao();
        QueryBuilder queryBuilder = intentDataDao.queryBuilder();
        //通过ActivityId查询Intent
        queryBuilder.where(IntentDataDao.Properties.ActivityId.eq(activityId),
                IntentDataDao.Properties.ResId.eq(resId));

        List<IntentData> resIntents = queryBuilder.list();

        if(resIntents.size()>=1){
            int size = resIntents.size();
            //移除重复的Intent
            for(int i=0;i<size-1;i++){
                resIntents.remove(1);
            }

            //无法转化为Intent 且不能保存数据
            if(resIntents.get(0).getBytes() ==null){
                Log.i("LZH","byte is null");
            }

            intent = ParcelableUtil.byte2Intent(resIntents.get(0).getBytes());
            if(intent==null){
                Log.i("LZH","can't 2 intent");
            }
        }else if(resIntents.size()<1){
            Log.i("LZH","无法查询到Intent");
        }
        return intent;
    }

    public Intent queryIntent(String activityName,Long resId){
        ActivityDataDao activityDataDao = mDaoSession.getActivityDataDao();
        QueryBuilder queryBuilder = activityDataDao.queryBuilder();
        queryBuilder.where(ActivityDataDao.Properties.ActivityName.eq(activityName),
                ActivityDataDao.Properties.ResId.eq(resId));
        List<ActivityData> activityDatas = queryBuilder.list();
        ActivityData activityData = activityDatas.get(0);

        return queryIntent(activityData.getActivityId(),resId);
    }
    public byte[] queryMotionEvent(String activityName,String resType,int motionSeq){
        MotionData motionData = null;
        byte[] eventBytes = null;

        MotionDataDao motionDataDao = mDaoSession.getMotionDataDao();
        QueryBuilder queryBuilder = motionDataDao.queryBuilder();
        queryBuilder.where(MotionDataDao.Properties.ActivityName.eq(activityName),
                MotionDataDao.Properties.ResCategory.eq(resType),
                MotionDataDao.Properties.MotionSeq.eq(motionSeq));

        List<MotionData> motionDatas = queryBuilder.list();
        int size = motionDatas.size();
        if(size<1){
            Log.i("LZH","未查询到对应的点击事件");
        }else if(size>1){
            Log.i("LZH","查询到的点击事件重复");
        }else{
            motionData = motionDatas.get(0);
            eventBytes = motionData.getBytes();
        }
        return eventBytes;
    }

    public String queryActivityNameById(Long activityId){
        ActivityDataDao activityDataDao = mDaoSession.getActivityDataDao();
        QueryBuilder queryBuilder = activityDataDao.queryBuilder();
        queryBuilder.where(ActivityDataDao.Properties.ActivityId.eq(activityId));
        List<ActivityData> activityDatas = queryBuilder.list();

        return activityDatas.get(0).getActivityName();
    }

}
