package com.example.integralmefirst.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.integralmefirst.R;
import com.example.integralmefirst.gameshistory.GameData;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class DBHelper extends SQLiteOpenHelper {
    private final Resources resources;
    private static final String dbName = "integrals.db";
    public static final DBTable problemsTable = new DBTable("problems", new String[]{"id",
            "value", "difficulty", "user_solves_counter"});
    public static final DBTable answersTable = new DBTable("answers", new String[]{"id",
            "problem_id", "ord_index", "value"});
    public static final DBTable gamesHistoryTable = new DBTable("games_history", new String[]{"game_id",
            "problem_id", "time"});
    public static final DBTable gamesPointsTable = new DBTable("games_points", new String[]{"id",
            "points", "date"});
    private static DBHelper currentDBHelper;

    public DBHelper(@Nullable Context context) {
        super(context, dbName, null, 1);
        assert context != null;
        resources = context.getResources();
        currentDBHelper = this;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTables;
        try {
            createTables = getCreateTablesSQL();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] createTablesSeparated;
        createTablesSeparated = createTables.split("=");
        for (String s : createTablesSeparated)
            db.execSQL(s);
        try {
            insertData(db);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCreateTablesSQL() throws IOException {
        InputStream is = resources.openRawResource(R.raw.create_tables);
        byte[] buffer;
        ByteArrayOutputStream os;
        buffer = new byte[is.available()];
        is.read(buffer);
        os = new ByteArrayOutputStream();
        os.write(buffer);
        os.close();
        is.close();
        return os.toString();
    }

    public static DBHelper getCurrentDBHelper() {
        if (currentDBHelper == null)
            throw new RuntimeException("DBHelper not initialized");
        return currentDBHelper;
    }

    private void insertData(SQLiteDatabase db) throws IOException {
        InputStream is = resources.openRawResource(R.raw.problems);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",", 3);
            int id = Integer.parseInt(data[0].replaceAll(" ", ""));
            String value = data[1];
            int difficulty = Integer.parseInt(data[2].replaceAll(" ", ""));
            ContentValues values = new ContentValues();
            values.put(problemsTable.col[0], id);
            values.put(problemsTable.col[1], value);
            values.put(problemsTable.col[2], difficulty);
            values.put(problemsTable.col[3], 0);
            db.insert(problemsTable.name, null, values);
        }
        is = resources.openRawResource(R.raw.answers);
        br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",", 3);
            int problem_id = Integer.parseInt(data[0].replaceAll(" ", ""));
            int index = Integer.parseInt(data[1].replaceAll(" ", ""));
            String value = data[2];
            ContentValues values = new ContentValues();
            values.put(answersTable.col[1], problem_id);
            values.put(answersTable.col[2], index);
            values.put(answersTable.col[3], value);
            db.insert(answersTable.name, null, values);
        }
    }

    public ArrayList<String> getAnswers(int problemId) {
        ArrayList<String> answers = new ArrayList<>();
        try (SQLiteDatabase db = getReadableDatabase()) {
            String queryString = "SELECT " + answersTable.col[3] + " FROM " + answersTable.name + " WHERE " + answersTable.col[1] +
                    " = " + problemId + " ORDER BY " + answersTable.col[2] + " ASC";
            Cursor cursor = db.rawQuery(queryString, null);
            if (cursor.moveToFirst()) {
                String value = cursor.getString(0);
                answers.add(value);
            }
            while (cursor.moveToNext()) {
                String value = cursor.getString(0);
                answers.add(value);
            }
            cursor.close();
        }
        answers.replaceAll(DBHelper::addDolar);
        return answers;
    }

    public ArrayList<String> getRandomAnswers(int difficulty, int numberOfAnswers) {
        if (numberOfAnswers <= 0)
            throw new RuntimeException("Cannot select 0 or less answers");
        ArrayList<String> answers = new ArrayList<>();
        try (SQLiteDatabase db = getReadableDatabase()) {
            String queryString = "SELECT " + answersTable.name + "." + answersTable.col[3] + " FROM " + answersTable.name +
                    " INNER JOIN " + problemsTable.name + " ON " + answersTable.name + "." +
                    answersTable.col[1] + " = " + problemsTable.name + "." + problemsTable.col[0] +
                    " WHERE " + problemsTable.col[2] + " = " + difficulty;
            Cursor cursor = db.rawQuery(queryString, null);
            if (cursor.moveToFirst()) {
                String value = cursor.getString(0);
                answers.add(value);
            }
            while (cursor.moveToNext()) {
                String value = cursor.getString(0);
                answers.add(value);
            }
            cursor.close();
        }
        Collections.shuffle(answers);
        if (answers.size() > numberOfAnswers)
            answers.subList(numberOfAnswers, answers.size()).clear();
        answers.replaceAll(DBHelper::addDolar);
        return answers;
    }

    private static String addDolar(String s) {
        return "$" + s + "$";
    }

    public ArrayList<Integer> getRandomProblemsIds(int problemsNumber, int difficulty) {
        if (problemsNumber <= 0)
            throw new RuntimeException("Cannot select 0 or less problems");
        ArrayList<Integer> problemsIds = new ArrayList<>();
        try (SQLiteDatabase db = getReadableDatabase()) {
            String queryString = "SELECT " + problemsTable.col[0] + " FROM " + problemsTable.name +
                    " WHERE " + problemsTable.col[2] + " = " + difficulty;
            Cursor cursor = db.rawQuery(queryString, null);
            if (cursor.moveToFirst()) {
                Integer value = cursor.getInt(0);
                problemsIds.add(value);
            }
            while (cursor.moveToNext()) {
                Integer value = cursor.getInt(0);
                problemsIds.add(value);
            }
            cursor.close();
        }
        Collections.shuffle(problemsIds);
        if (problemsIds.size() > problemsNumber) {
            problemsIds.subList(problemsNumber, problemsIds.size()).clear();
        }

        return problemsIds;
    }

    public String getProblemValueById(int problemId) {
        String value = "";
        try (SQLiteDatabase db = getReadableDatabase()) {
            String queryString = "SELECT " + problemsTable.col[1] + " FROM " + problemsTable.name +
                    " WHERE " + problemsTable.col[0] + " = " + problemId;
            Cursor cursor = db.rawQuery(queryString, null);
            if (cursor.moveToFirst()) {
                value = cursor.getString(0);
            }
            cursor.close();
        }
        return addDolar(value);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static void removeDatabase(Context context) {
        context.deleteDatabase(dbName);
    }

    public void addGameToHistory(ArrayList<Integer> problemIds, long[] times, int points) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(gamesPointsTable.col[1], points);
            values.put(gamesPointsTable.col[2], System.currentTimeMillis());
            long id = db.insert(gamesPointsTable.name, null, values);
            for (int i = 0; i < problemIds.size(); i++) {
                values = new ContentValues();
                values.put(gamesHistoryTable.col[0], id);
                values.put(gamesHistoryTable.col[1], problemIds.get(i));
                values.put(gamesHistoryTable.col[2], times[i]);
                db.insert(gamesHistoryTable.name, null, values);
            }
        }
    }

    public ArrayList<GameData> getGamesHistory() {
        ArrayList<GameData> gamesHistoryArray = new ArrayList<>();
        try (SQLiteDatabase db = getReadableDatabase()) {
            ArrayList<Integer> gamesIds = new ArrayList<>();
            String query = "SELECT " + gamesPointsTable.col[0] + " FROM " + gamesPointsTable.name;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                Integer value = cursor.getInt(0);
                gamesIds.add(value);
            }
            while (cursor.moveToNext()) {
                Integer value = cursor.getInt(0);
                gamesIds.add(value);
            }
            cursor.close();
            for (Integer gameId : gamesIds) {
                query = "SELECT " + problemsTable.getColWithTableName(1) + ", " +
                        gamesPointsTable.getColWithTableName(1) + ", " +
                        gamesPointsTable.getColWithTableName(2) + ", " +
                        gamesHistoryTable.getColWithTableName(2) + " FROM " +
                        gamesPointsTable.name + " INNER JOIN " + gamesHistoryTable.name + " ON " +
                        gamesPointsTable.getColWithTableName(0) + " = " +
                        gamesHistoryTable.getColWithTableName(0) + " INNER JOIN " +
                        problemsTable.name + " ON " + problemsTable.getColWithTableName(0) +
                        " = " + gamesHistoryTable.getColWithTableName(1) + " WHERE " +
                        gamesPointsTable.getColWithTableName(0) + " = " + gameId;
                cursor = db.rawQuery(query, null);
                cursor.moveToFirst();
                ArrayList<String> problems = new ArrayList<>();
                ArrayList<Long> times = new ArrayList<>();
                int points = cursor.getInt(1);
                long date = cursor.getLong(2);
                while (!cursor.isAfterLast()) {
                    problems.add(cursor.getString(0));
                    times.add(cursor.getLong(3));
                    cursor.moveToNext();
                }
                cursor.close();
                problems.replaceAll(DBHelper::addDolar);
                gamesHistoryArray.add(new GameData(problems, points, times, date));
            }
        }
        return gamesHistoryArray;
    }
}