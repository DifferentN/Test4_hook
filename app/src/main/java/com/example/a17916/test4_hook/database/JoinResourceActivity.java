package com.example.a17916.test4_hook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinResourceActivity {
    @Id(autoincrement = true)
    private Long id;

    private Long resId;

    private Long activityId;

    @Generated(hash = 1985201095)
    public JoinResourceActivity(Long id, Long resId, Long activityId) {
        this.id = id;
        this.resId = resId;
        this.activityId = activityId;
    }

    @Generated(hash = 1650037637)
    public JoinResourceActivity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResId() {
        return this.resId;
    }

    public void setResId(Long resId) {
        this.resId = resId;
    }

    public Long getActivityId() {
        return this.activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
}
