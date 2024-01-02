package com.example.integralmefirst.level;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.integralmefirst.R;
import com.example.integralmefirst.gameshistory.GameCardRecViewAdapter;
import com.example.integralmefirst.gameshistory.GameData;

import java.util.Objects;

public class LevelSummaryDialog extends DialogFragment {
    private final GameData gameData;


    public LevelSummaryDialog(GameData gameData) {
        this.gameData = gameData;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        requireActivity().finish();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View builderView = inflater.inflate(R.layout.level_summary_dialog, null);
        RelativeLayout layout = builderView.findViewById(R.id.LevelSummaryRelativeLayout);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                requireActivity().finish();
                return true;
            }
        });
        builderView.findViewById(R.id.cardInSummary).findViewById(R.id.GameCardRelativeLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                requireActivity().finish();
                return true;
            }
        });
        TextView points = builderView.findViewById(R.id.PointsGameCard);
        String pointsText = points.getText().toString();
        pointsText += " " + gameData.getPoints();
        points.setText(pointsText);
        TextView date = builderView.findViewById(R.id.DateGameCard);
        date.setText(gameData.getDateAsString());
        RecyclerView recyclerView = builderView.findViewById(R.id.cardInSummary).findViewById(R.id.GameCardRecyclerView);
        GameCardRecViewAdapter adapter = new GameCardRecViewAdapter();
        adapter.setTimesAndProblems(gameData.getTimes(), gameData.getProblems());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        builder.setView(builderView);
        return builder.create();
    }
}
