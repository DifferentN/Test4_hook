package com.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.a17916.test4_hook.database.JoinResourceActivity;

import com.greendao.gen.JoinResourceActivityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig joinResourceActivityDaoConfig;

    private final JoinResourceActivityDao joinResourceActivityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        joinResourceActivityDaoConfig = daoConfigMap.get(JoinResourceActivityDao.class).clone();
        joinResourceActivityDaoConfig.initIdentityScope(type);

        joinResourceActivityDao = new JoinResourceActivityDao(joinResourceActivityDaoConfig, this);

        registerDao(JoinResourceActivity.class, joinResourceActivityDao);
    }
    
    public void clear() {
        joinResourceActivityDaoConfig.clearIdentityScope();
    }

    public JoinResourceActivityDao getJoinResourceActivityDao() {
        return joinResourceActivityDao;
    }

}
