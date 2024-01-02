package com.example.integralmefirst.gameshistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.integralmefirst.R;

import java.util.ArrayList;

public class GamesRecViewAdapter extends RecyclerView.Adapter<GamesRecViewAdapter.ViewHolder> {
    private final GamesHistoryActivity gamesHistoryActivity;
    private ArrayList<GameData> gamesHistory;

    public GamesRecViewAdapter(GamesHistoryActivity activity) {
        gamesHistoryActivity = activity;
    }

    @NonNull
    @Override
    public GamesRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_history_card, parent, false);
        return new GamesRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GamesRecViewAdapter.ViewHolder holder, int position) {
        holder.date.setText(gamesHistory.get(position).getDateAsString());
        System.out.println(gamesHistory.get(position).getPoints());
        String pointsText = gamesHistoryActivity.getString(R.string.score) + " " + gamesHistory.get(position).getPoints();
        holder.points.setText(pointsText);
        GameCardRecViewAdapter adapter = new GameCardRecViewAdapter();
        adapter.setTimesAndProblems(gamesHistory.get(position).getTimes(), gamesHistory.get(position).getProblems());
        holder.problemTimeRecyclerView.setAdapter(adapter);
        holder.problemTimeRecyclerView.setLayoutManager(new LinearLayoutManager(gamesHistoryActivity));
    }

    @Override
    public int getItemCount() {
        return gamesHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView points;
        private TextView date;
        private RecyclerView problemTimeRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            points = itemView.findViewById(R.id.PointsGameCard);
            date = itemView.findViewById(R.id.DateGameCard);
            problemTimeRecyclerView = itemView.findViewById(R.id.GameCardRecyclerView);
        }
    }

    public void setGamesHistory(ArrayList<GameData> gamesHistory) {
        this.gamesHistory = gamesHistory;
    }
}
