package com.example.integralmefirst.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.integralmefirst.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class DBHelper extends SQLiteOpenHelper {
    private final Resources resources;
    public static final DBTable problemsTable = new DBTable("problems", new String[]{"id",
            "value", "difficulty", "user_solves_counter"});
    public static final DBTable answersTable = new DBTable("answers", new String[]{"id",
            "problem_id", "ord_index", "value"});
    public static final DBTable gamesHistoryTable = new DBTable("games_history", new String[]{"id",
            "problem_id", "time", "date"});
    private static DBHelper currentDBHelper;

    public DBHelper(@Nullable Context context) {
        super(context, "integrals.db", null, 1);
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
        for (int i = 0; i < 3; i++)
            db.execSQL(createTablesSeparated[i]);
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
}
