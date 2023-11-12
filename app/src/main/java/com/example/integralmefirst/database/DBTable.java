package com.example.integralmefirst.database;

public class DBTable {
    public final String name;
    public final String[] col;
    public DBTable(String name, String[] colNames) {
        this.name = name;
        col = new String[colNames.length];
        System.arraycopy(colNames, 0, col, 0, colNames.length);
    }

    @Override
    public String toString() {
        return name;
    }
}
