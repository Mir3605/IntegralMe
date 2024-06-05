package integral.me.database;

import androidx.annotation.NonNull;

// a class that holds table name and column names for a table
public class DBTable {
    public final String name;
    public final String[] col;
    public DBTable(String name, String[] colNames) {
        this.name = name;
        col = new String[colNames.length];
        System.arraycopy(colNames, 0, col, 0, colNames.length);
    }
    public String getColWithTableName(int i){
        return name + "." + col[i];
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
