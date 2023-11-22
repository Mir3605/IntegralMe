package com.example.integralmefirst.gameshistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.integralmefirst.R;
import com.example.integralmefirst.database.DBHelper;

import java.util.ArrayList;

public class GamesHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_history);
        DBHelper helper = DBHelper.getCurrentDBHelper();
        ArrayList<GameData> gamesHistory = helper.getGamesHistory();
        RecyclerView recyclerView = findViewById(R.id.GamesHistoryRecyclerView);
        GamesRecViewAdapter adapter = new GamesRecViewAdapter(this);
        adapter.setGamesHistory(gamesHistory);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}