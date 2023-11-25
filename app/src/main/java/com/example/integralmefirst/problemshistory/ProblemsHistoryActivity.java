package com.example.integralmefirst.problemshistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.integralmefirst.R;
import com.example.integralmefirst.database.DBHelper;
import com.example.integralmefirst.gameshistory.GamesHistoryActivity;

import java.util.ArrayList;

public class ProblemsHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problems_history);

        DBHelper helper = DBHelper.getCurrentDBHelper();
        ArrayList<ProblemData> problemsHistory = helper.getProblemsHistory();
        RecyclerView recyclerView = findViewById(R.id.ProblemsStatsRecyclerView);
        ProblemsRecViewAdapter adapter = new ProblemsRecViewAdapter();
        adapter.setProblemsHistory(problemsHistory);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button switchToGamesButton = findViewById(R.id.SwitchToGames);
        switchToGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProblemsHistoryActivity.this, GamesHistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}