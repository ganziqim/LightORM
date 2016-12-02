package com.ganziqim.core;

/**
 * Created by ganzi on 2016/11/30.
 */
public interface ISession {
    boolean addSavepoint(String savepointName);
    void rollback();
    void rollback(String savepointName);
    void commit();
    void dispose();
    IQuery getQuery(Class cls);
}
