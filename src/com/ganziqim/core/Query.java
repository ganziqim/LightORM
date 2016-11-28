package com.ganziqim.core;

class Query {
    Class cls = null;
    Session ses = null;

    public Query(Class cls, Session ses) {
        this.cls = cls;
        this.ses = ses;
    }

    public void where(String exp) {

    }

    // TODO
    public void commit() {

    }

    public void all() {

    }

    public void first() {

    }

    public void last() {

    }
}
