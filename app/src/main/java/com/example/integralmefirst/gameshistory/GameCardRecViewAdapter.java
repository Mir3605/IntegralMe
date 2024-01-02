package com.example.integralmefirst.gameshistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.integralmefirst.R;

import java.util.ArrayList;

import katex.hourglass.in.mathlib.MathView;

public class GameCardRecViewAdapter extends RecyclerView.Adapter<GameCardRecViewAdapter.ViewHolder>{
    private ArrayList<Long> times;
    private ArrayList<String> problems;
    @NonNull
    @Override
    public GameCardRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.problem_time, parent, false);
        return new GameCardRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameCardRecViewAdapter.ViewHolder holder, int position) {
        holder.problem.setDisplayText(problems.get(position));
        String timeText = times.get(position)/1000 + "." + (times.get(position)/100)%10 + "s";
        holder.time.setText(timeText);
    }

    @Override
    public int getItemCount() {
        return Math.min(times.size(), problems.size());
    }

    public void setTimesAndProblems(ArrayList<Long> times, ArrayList<String> problems) {
        this.times = times;
        this.problems = problems;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView time;
        private MathView problem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.Time);
            problem = itemView.findViewById(R.id.Problem);
        }
    }
}
