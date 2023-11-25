package com.example.integralmefirst.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.integralmefirst.R;
import com.example.integralmefirst.database.DBHelper;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button resetProblemsButton = findViewById(R.id.ResetProblemsButton);
        resetProblemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper helper = DBHelper.getCurrentDBHelper();
                helper.reloadAnswersAndProblems();
            }
        });

        Button removeUserDataButton = findViewById(R.id.RemoveDataButton);
        removeUserDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAreYouSureDialog();
            }
        });

    }

    private void showAreYouSureDialog() {
        AreYouSureDialog dialog = new AreYouSureDialog();
        dialog.show(getSupportFragmentManager(), "AreYouSureDialog");
    }
}