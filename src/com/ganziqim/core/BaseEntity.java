package com.ganziqim.core;

public class BaseEntity {
    private int id;
    private Database db;

    public void createTable() {
        db.createTable(this.getClass());
    }

    public void setDb(Database db) {
        this.db = db;
    }
}
