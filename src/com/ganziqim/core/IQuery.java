package com.ganziqim.core;

/**
 * Created by ganzi on 2016/11/30.
 */
public interface IQuery {
    void add(Object obj);
    IQuery delete();
    IQuery where(String exp);
    void execute();
}
