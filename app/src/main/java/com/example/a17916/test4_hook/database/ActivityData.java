package com.example.a17916.test4_hook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.greendao.gen.DaoSession;
import com.greendao.gen.AppDataDao;
import com.greendao.gen.ActivityDataDao;
import com.greendao.gen.ResourceDataDao;

@Entity
public class ActivityData {
    @Id(autoincrement = true)
    private Long activityId;

    private String activityName;

    private Long appId;
    @ToOne(joinProperty = "appId")
    private AppData appData;

    private Long resId;
    @ToMany(referencedJoinProperty = "resId")
    private List<ResourceData> resourceDatas;



    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1551556676)
    private transient ActivityDataDao myDao;

    @Generated(hash = 1661756033)
    private transient Long appData__resolvedKey;

    public ActivityData(Long activityId,Long appId,String activityName){
        this.activityId = activityId;
        this.appId = appId;
        this.activityName = activityName;
    }

    @Generated(hash = 631048842)
    public ActivityData(Long activityId, String activityName, Long appId, Long resId) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.appId = appId;
        this.resId = resId;
    }

    @Generated(hash = 500776674)
    public ActivityData() {
    }

    public Long getActivityId() {
        return this.activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return this.activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Long getAppId() {
        return this.appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getResId() {
        return this.resId;
    }

    public void setResId(Long resId) {
        this.resId = resId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1213675402)
    public AppData getAppData() {
        Long __key = this.appId;
        if (appData__resolvedKey == null || !appData__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AppDataDao targetDao = daoSession.getAppDataDao();
            AppData appDataNew = targetDao.load(__key);
            synchronized (this) {
                appData = appDataNew;
                appData__resolvedKey = __key;
            }
        }
        return appData;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1224033528)
    public void setAppData(AppData appData) {
        synchronized (this) {
            this.appData = appData;
            appId = appData == null ? null : appData.getAppId();
            appData__resolvedKey = appId;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 968223199)
    public List<ResourceData> getResourceDatas() {
        if (resourceDatas == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ResourceDataDao targetDao = daoSession.getResourceDataDao();
            List<ResourceData> resourceDatasNew = targetDao
                    ._queryActivityData_ResourceDatas(activityId);
            synchronized (this) {
                if (resourceDatas == null) {
                    resourceDatas = resourceDatasNew;
                }
            }
        }
        return resourceDatas;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 231864259)
    public synchronized void resetResourceDatas() {
        resourceDatas = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1696373401)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getActivityDataDao() : null;
    }




}
