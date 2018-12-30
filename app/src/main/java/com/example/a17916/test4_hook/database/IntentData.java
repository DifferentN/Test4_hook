package com.example.a17916.test4_hook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import com.greendao.gen.DaoSession;
import com.greendao.gen.ActivityDataDao;
import com.greendao.gen.IntentDataDao;
import com.greendao.gen.ResourceDataDao;

@Entity
public class IntentData {
    @Id(autoincrement = true)
    private Long id;

    private Long activityId;
    @ToOne(joinProperty = "activityId")
    private ActivityData activityData;

    private Long resId;
    @ToOne(joinProperty = "resId")
    private ResourceData resourceData;

    //用来说明到达某一特殊页面的Intent
    @Property(nameInDb = "IntentByte")
    private byte[] bytes;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 954907208)
    private transient IntentDataDao myDao;

    @Generated(hash = 1554979471)
    public IntentData(Long id, Long activityId, Long resId, byte[] bytes) {
        this.id = id;
        this.activityId = activityId;
        this.resId = resId;
        this.bytes = bytes;
    }

    @Generated(hash = 1382371866)
    public IntentData() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActivityId() {
        return this.activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getResId() {
        return this.resId;
    }

    public void setResId(Long resId) {
        this.resId = resId;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Generated(hash = 1222239601)
    private transient Long activityData__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 706526624)
    public ActivityData getActivityData() {
        Long __key = this.activityId;
        if (activityData__resolvedKey == null
                || !activityData__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ActivityDataDao targetDao = daoSession.getActivityDataDao();
            ActivityData activityDataNew = targetDao.load(__key);
            synchronized (this) {
                activityData = activityDataNew;
                activityData__resolvedKey = __key;
            }
        }
        return activityData;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1656906040)
    public void setActivityData(ActivityData activityData) {
        synchronized (this) {
            this.activityData = activityData;
            activityId = activityData == null ? null : activityData.getActivityId();
            activityData__resolvedKey = activityId;
        }
    }

    @Generated(hash = 582302209)
    private transient Long resourceData__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 493085716)
    public ResourceData getResourceData() {
        Long __key = this.resId;
        if (resourceData__resolvedKey == null
                || !resourceData__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ResourceDataDao targetDao = daoSession.getResourceDataDao();
            ResourceData resourceDataNew = targetDao.load(__key);
            synchronized (this) {
                resourceData = resourceDataNew;
                resourceData__resolvedKey = __key;
            }
        }
        return resourceData;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1496478277)
    public void setResourceData(ResourceData resourceData) {
        synchronized (this) {
            this.resourceData = resourceData;
            resId = resourceData == null ? null : resourceData.getResId();
            resourceData__resolvedKey = resId;
        }
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
    @Generated(hash = 1293825761)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getIntentDataDao() : null;
    }






}
