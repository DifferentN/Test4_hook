package com.example.a17916.test4_hook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.greendao.gen.DaoSession;
import com.greendao.gen.ActivityDataDao;
import com.greendao.gen.AppDataDao;
import com.greendao.gen.ResourceDataDao;

@Entity
public class ResourceData {
    @Id(autoincrement = true)
    private Long resId;

    private Long appId;
    @ToOne(joinProperty = "appId")
    private AppData app;

    private String resCategory;

    private String resEntityName;

    private Long activityId;
    @ToMany(referencedJoinProperty = "activityId")

    private List<ActivityData> activityData;

    public ResourceData(Long resId,Long appId,String resCategory,String resEntityName){
        this.resId = resId;
        this.appId = appId;
        this.resCategory = resCategory;
        this.resEntityName = resEntityName;
    }

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 579745500)
    private transient ResourceDataDao myDao;
    @Generated(hash = 54816759)
    public ResourceData(Long resId, Long appId, String resCategory,
            String resEntityName, Long activityId) {
        this.resId = resId;
        this.appId = appId;
        this.resCategory = resCategory;
        this.resEntityName = resEntityName;
        this.activityId = activityId;
    }
    @Generated(hash = 317345498)
    public ResourceData() {
    }
    public Long getResId() {
        return this.resId;
    }
    public void setResId(Long resId) {
        this.resId = resId;
    }
    public Long getAppId() {
        return this.appId;
    }
    public void setAppId(Long appId) {
        this.appId = appId;
    }
    public String getResCategory() {
        return this.resCategory;
    }
    public void setResCategory(String resCategory) {
        this.resCategory = resCategory;
    }
    public String getResEntityName() {
        return this.resEntityName;
    }
    public void setResEntityName(String resEntityName) {
        this.resEntityName = resEntityName;
    }
    public Long getActivityId() {
        return this.activityId;
    }
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
    @Generated(hash = 457438214)
    private transient Long app__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1565522752)
    public AppData getApp() {
        Long __key = this.appId;
        if (app__resolvedKey == null || !app__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AppDataDao targetDao = daoSession.getAppDataDao();
            AppData appNew = targetDao.load(__key);
            synchronized (this) {
                app = appNew;
                app__resolvedKey = __key;
            }
        }
        return app;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 579278985)
    public void setApp(AppData app) {
        synchronized (this) {
            this.app = app;
            appId = app == null ? null : app.getAppId();
            app__resolvedKey = appId;
        }
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 943963340)
    public List<ActivityData> getActivityData() {
        if (activityData == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ActivityDataDao targetDao = daoSession.getActivityDataDao();
            List<ActivityData> activityDataNew = targetDao
                    ._queryResourceData_ActivityData(resId);
            synchronized (this) {
                if (activityData == null) {
                    activityData = activityDataNew;
                }
            }
        }
        return activityData;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1209830084)
    public synchronized void resetActivityData() {
        activityData = null;
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
    @Generated(hash = 210154025)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getResourceDataDao() : null;
    }




}
