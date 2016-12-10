package com.ganziqim.core;

import java.util.List;

/**
 * Created by GanZiQim on 2016/11/22.
 */
public interface IDatabase {
    boolean init();
    void dispose();
    Session getSession();
    void createTable(Class cls);
    void createTable(List<Class> clazz);
    void updateTable(Class cls);
    void recovery(ISession ses);

    int getInitConnectionNumber();
    void setInitConnectionNumber(int initConnectionNumber);
    int getMaxConnectionNumber();
    void setMaxConnectionNumber(int maxConnectionNumber);
    int getIncrementalConnections();
    void setIncrementalConnections(int incrementalConnections);
}
