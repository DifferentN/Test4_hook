package com.example.a17916.test4_hook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.greendao.gen.DaoSession;
import com.greendao.gen.AppDataDao;
import com.greendao.gen.ResourceDataDao;

@Entity
public class AppData {
    @Id(autoincrement = true)
    private Long appId;

    private String appName;

    private String version ;

    private Long resId;
    @ToMany(referencedJoinProperty = "resId")
    private List<ResourceData> resourceDatas;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 679872930)
    private transient AppDataDao myDao;


    public AppData(Long appId,String appName,String version){
        this.appId = appId;
        this.appName = appName;
        this.version = version;
    }


    @Generated(hash = 1504355784)
    public AppData(Long appId, String appName, String version, Long resId) {
        this.appId = appId;
        this.appName = appName;
        this.version = version;
        this.resId = resId;
    }


    @Generated(hash = 1112619805)
    public AppData() {
    }


    public Long getAppId() {
        return this.appId;
    }


    public void setAppId(Long appId) {
        this.appId = appId;
    }


    public String getAppName() {
        return this.appName;
    }


    public void setAppName(String appName) {
        this.appName = appName;
    }


    public String getVersion() {
        return this.version;
    }


    public void setVersion(String version) {
        this.version = version;
    }


    public Long getResId() {
        return this.resId;
    }


    public void setResId(Long resId) {
        this.resId = resId;
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2049503295)
    public List<ResourceData> getResourceDatas() {
        if (resourceDatas == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ResourceDataDao targetDao = daoSession.getResourceDataDao();
            List<ResourceData> resourceDatasNew = targetDao
                    ._queryAppData_ResourceDatas(appId);
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
    @Generated(hash = 211212037)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAppDataDao() : null;
    }


}
