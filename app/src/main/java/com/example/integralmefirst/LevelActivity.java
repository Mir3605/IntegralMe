package com.example.integralmefirst;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class LevelActivity extends AppCompatActivity {
    public static int lastStageNumber = 3;
    private int currentStageNumber = 1;
    private RecyclerView emptyFieldsRecyclerView;
    private TextView headline;
    private TextView stage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        int difficulty = getIntent().getIntExtra("difficulty", -1);
        headline = findViewById(R.id.levelHeadline);
        String newHeadlineText = getString(R.string.difficulty) + difficulty;
        headline.setText(newHeadlineText);
        stage = findViewById(R.id.stage);
        updateStage();

        emptyFieldsRecyclerView = findViewById(R.id.EmptyFieldsRecyclerView);
        int operationsNumber = 5;  //todo import number of operations
        ArrayList<String> emptyFieldsValues = new ArrayList<>();
        for(int i = 0; i < operationsNumber; i++){
            emptyFieldsValues.add(" ");
        }
        EmptyFieldsRecViewAdapter adapter = new EmptyFieldsRecViewAdapter(this);
        adapter.setEmptyFields(emptyFieldsValues);
        emptyFieldsRecyclerView.setAdapter(adapter);
        emptyFieldsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentStageNumber++;
                if(currentStageNumber>lastStageNumber){
                    finish();
                }
                else {
                    updateStage();
                }
            }
        });
    }

    private void updateStage(){
        String newStageText = currentStageNumber + "/" + lastStageNumber;
        stage.setText(newStageText);
    }
}