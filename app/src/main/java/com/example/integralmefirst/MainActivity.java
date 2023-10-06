package com.example.integralmefirst;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static int difficultyLevelsNumber = 3;
    private RecyclerView chooseLvlRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chooseLvlRecView = findViewById(R.id.ChooseLvlRecView);

        ArrayList<Lvl> lvlsArray = new ArrayList<>();
        for(int i = 0; i < difficultyLevelsNumber; i++){
            lvlsArray.add(new Lvl(i));
        }
        LevelsRecViewAdapter adapter = new LevelsRecViewAdapter(this);
        adapter.setLvls(lvlsArray);

        chooseLvlRecView.setAdapter(adapter);
        chooseLvlRecView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.BackMenu:
                Toast.makeText(this, "Back clicked", Toast.LENGTH_SHORT).show();
                //todo back button activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}