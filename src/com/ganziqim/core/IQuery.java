package com.ganziqim.core;

import java.util.List;

/**
 * Created by ganzi on 2016/11/30.
 */
public interface IQuery {
    void add(Object obj);
    void addAll(List objs);
    IQuery delete();
    IQuery update(String exp);
    IQuery select();
    IQuery where(String exp);
    IQuery limit(int offset, int rows);
    IQuery limit(int rows);
    List execute();
}
