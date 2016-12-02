package com.ganziqim.core;

import java.util.ArrayList;

/**
 * Created by ganzi on 2016/11/22.
 */
public interface IDatabase {
    boolean init();
    void dispose();
    Session getSession();
    void createTable(Class obj);
    void createTable(ArrayList<Class> objs);
    void recovery(ISession ses);
}
