package com.example.integralmefirst.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.integralmefirst.R;
import com.example.integralmefirst.database.DBHelper;

import java.util.function.Consumer;

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
    private void showAreYouSureDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.remove_user_settings_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_style);
        Button yesButton = findViewById(R.id.DialogUSureYes);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        dialog.show();

    }
}