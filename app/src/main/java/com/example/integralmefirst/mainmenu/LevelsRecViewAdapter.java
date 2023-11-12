package com.example.integralmefirst.mainmenu;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.integralmefirst.level.LevelActivity;
import com.example.integralmefirst.R;
import com.example.integralmefirst.database.DBHelper;

import java.util.ArrayList;

public class LevelsRecViewAdapter extends RecyclerView.Adapter<LevelsRecViewAdapter.ViewHolder> {

    private ArrayList<Lvl> lvls = new ArrayList<>();
    private final MainActivity mainActivity;

    public LevelsRecViewAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_lvl_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lvl lvl = lvls.get(position);
        holder.lvlButton.setText(lvl.toString());
        holder.lvlButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DBHelper helper = new DBHelper(mainActivity);
                        ArrayList<Integer> problemIds = helper.getRandomProblemsIds(
                                MainActivity.difficultyLevelsNumber, lvl.getDifficulty());
                        Intent intent = new Intent(mainActivity, LevelActivity.class);
                        intent.putExtra("difficulty", lvl.getDifficulty());
                        intent.putExtra("chosenProblems", problemIds);
                        mainActivity.startActivity(intent);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return lvls.size();
    }

    public void setLvls(ArrayList<Lvl> lvls) {
        this.lvls = lvls;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Button lvlButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lvlButton = itemView.findViewById(R.id.LvlButton);
        }
    }
}
