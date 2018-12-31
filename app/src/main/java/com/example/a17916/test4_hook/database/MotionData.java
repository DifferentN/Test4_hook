package com.example.a17916.test4_hook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import com.greendao.gen.DaoSession;
import com.greendao.gen.ActivityDataDao;
import com.greendao.gen.MotionDataDao;

@Entity
public class MotionData {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "ActivityName")
    private String activityName;

    private Long motionSeq;

    private Long activityId;
    @ToOne(joinProperty = "activityId")
    private ActivityData activityData;

    //一类资源的打开操作可能相同,所以采用类别，而不是资源实体名称
    private String resCategory;

    //用来说明某一特殊页面的特征值
    @Property(nameInDb = "ContentKey")
    private String contentKey;

    //用来说明到达某一特殊页面的操作
    @Property(nameInDb ="EventsByte")
    private byte[] bytes;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 2072665083)
    private transient MotionDataDao myDao;

    @Generated(hash = 1222239601)
    private transient Long activityData__resolvedKey;

    public MotionData(Long id,String activityName,String resCategory,Long motionSeq,byte[] bytes){
        this.id = id;
        this.activityName = activityName;
        this.resCategory = resCategory;
        this.motionSeq = motionSeq;
        this.bytes = bytes;
    }

    @Generated(hash = 1955713332)
    public MotionData(Long id, String activityName, Long motionSeq, Long activityId, String resCategory,
            String contentKey, byte[] bytes) {
        this.id = id;
        this.activityName = activityName;
        this.motionSeq = motionSeq;
        this.activityId = activityId;
        this.resCategory = resCategory;
        this.contentKey = contentKey;
        this.bytes = bytes;
    }

    @Generated(hash = 1146652005)
    public MotionData() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityName() {
        return this.activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Long getMotionSeq() {
        return this.motionSeq;
    }

    public void setMotionSeq(Long motionSeq) {
        this.motionSeq = motionSeq;
    }

    public Long getActivityId() {
        return this.activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getResCategory() {
        return this.resCategory;
    }

    public void setResCategory(String resCategory) {
        this.resCategory = resCategory;
    }

    public String getContentKey() {
        return this.contentKey;
    }

    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 706526624)
    public ActivityData getActivityData() {
        Long __key = this.activityId;
        if (activityData__resolvedKey == null || !activityData__resolvedKey.equals(__key)) {
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
    @Generated(hash = 681735640)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMotionDataDao() : null;
    }

    
   



}
