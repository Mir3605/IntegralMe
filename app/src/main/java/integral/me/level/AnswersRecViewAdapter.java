package integral.me.level;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import integral.me.R;

import java.util.ArrayList;
import java.util.Objects;

import katex.hourglass.in.mathlib.MathView;

public class AnswersRecViewAdapter extends RecyclerView.Adapter<AnswersRecViewAdapter.ViewHolder>{
    private ArrayList<String> fields = new ArrayList<>();
    private final LevelActivity levelActivity;

    public AnswersRecViewAdapter(LevelActivity levelActivity) {
        this.levelActivity = levelActivity;
    }

    @NonNull
    @Override
    public AnswersRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_button, parent, false);
        return new AnswersRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersRecViewAdapter.ViewHolder holder, int position) {
        String field = fields.get(position);
        holder.answerButton.setDisplayText(field);
        holder.answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String receivedData = levelActivity.setSelectedEmptyField(field);
                if (Objects.equals(receivedData, LevelActivity.emptyMathField)){
                    fields.remove(field);
                    notifyItemRangeRemoved(holder.getAdapterPosition(), 1);
                }
                else{
                    fields.set(holder.getAdapterPosition(), receivedData);
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private MathView answerButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            answerButton = itemView.findViewById(R.id.answerButton);
        }
    }
    public void setAnswers(ArrayList<String> answerList) {
        this.fields = answerList;
        notifyDataSetChanged();
    }

    public void addAnswer(String answer) {
        if (LevelActivity.emptyMathField.equals(answer))
            return;
        fields.add(answer);
        notifyItemInserted(fields.size()-1);
    }
}
