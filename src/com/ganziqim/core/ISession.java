package com.ganziqim.core;

import com.ganziqim.utils.Dao;

/**
 * Created by GanZiQim on 2016/11/30.
 */
public interface ISession {
    boolean addSavepoint(String savepointName);
    void rollback();
    void rollback(String savepointName);
    void commit();
    IQuery getQuery(Class cls);
    Dao getDao();
}
