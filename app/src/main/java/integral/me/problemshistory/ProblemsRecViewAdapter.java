package integral.me.problemshistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import integral.me.R;

import java.util.ArrayList;

import katex.hourglass.in.mathlib.MathView;

public class ProblemsRecViewAdapter extends RecyclerView.Adapter<ProblemsRecViewAdapter.ViewHolder> {
    ArrayList<ProblemData> problemsHistory;

    public void setProblemsHistory(ArrayList<ProblemData> problemsHistory) {
        this.problemsHistory = problemsHistory;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.problem_tries_time, parent, false);
        return new ProblemsRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProblemData data = problemsHistory.get(position);
        holder.problem.setDisplayText(data.getProblem());
        holder.tries.setText(String.valueOf(data.getTriesCounter()));
        holder.avgTime.setText(data.getAverageTimeFormatted());
    }

    @Override
    public int getItemCount() {
        return problemsHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MathView problem;
        private TextView avgTime;
        private TextView tries;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            problem = itemView.findViewById(R.id.ProblemInProblems);
            avgTime = itemView.findViewById(R.id.TimeInProblems);
            tries = itemView.findViewById(R.id.TriesInProblems);
        }
    }
}
