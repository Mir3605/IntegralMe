package integral.me.problemshistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import integral.me.R;
import integral.me.database.DBHelper;
import integral.me.gameshistory.GamesHistoryActivity;
import integral.me.settings.Settings;

import java.util.ArrayList;

public class ProblemsHistoryActivity extends AppCompatActivity {
    private boolean movingToMainActivity = true;

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
                movingToMainActivity = false;
                Intent intent = new Intent(ProblemsHistoryActivity.this, GamesHistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        if (!Settings.getAnimationsDisplay())
            return;
        if (movingToMainActivity)
            overridePendingTransition(R.anim.zoom_increasing_enter, R.anim.zoom_increasing_exit);
        else
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}