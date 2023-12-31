package com.example.integralmefirst.gameshistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.integralmefirst.R;
import com.example.integralmefirst.database.DBHelper;
import com.example.integralmefirst.problemshistory.ProblemsHistoryActivity;
import com.example.integralmefirst.settings.SettingsActivity;

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
        Button switchToProblemsButton = findViewById(R.id.SwitchToProblems);
        switchToProblemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GamesHistoryActivity.this, ProblemsHistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}