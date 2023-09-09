package com.example.questans.activity.result;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.questans.R;
import com.example.questans.model.result.Result;
import com.example.questans.utils.DownloadImageTask;

import java.util.List;

public class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.ResultHolder> {
    List<Result> mResultList;
    private Context context;
    public ResultListAdapter(Context context, List<Result> resultList) {
        this.context = context;
        this.mResultList = resultList;
    }
    @NonNull
    @Override
    public ResultListAdapter.ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View mItemView = mInflater.inflate(R.layout.result_item, parent, false);
        return new ResultHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultListAdapter.ResultHolder holder, int position) {
        holder.setItemView(mResultList.get(position));
    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }

    public class ResultHolder extends RecyclerView.ViewHolder {
        final ResultListAdapter mAdapter;
        TextView txtQuestion;
        RadioGroup groupAnswers;
        TextView txtFeedback;
        ImageView imgTitle;

        public ResultHolder(@NonNull View itemView, ResultListAdapter adapter) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuest);
            groupAnswers = itemView.findViewById(R.id.grpAnswers);
            txtFeedback = itemView.findViewById(R.id.txtFeedback);
            imgTitle = itemView.findViewById(R.id.imgTitle);
            this.mAdapter = adapter;
        }

        public void setItemView(Result result) {
            if (result.getQuestion() != null) {
                txtQuestion.setText(result.getQuestion().getTitle());

                if (result.getQuestion().getImage() != null) {
                    new DownloadImageTask(imgTitle)
                            .execute(result.getQuestion().getImage());

                } else imgTitle.setVisibility(View.GONE);

                if (result.getFormAnswer().getAnswerHead() == null) {
                    txtQuestion.setTextColor(context.getResources().getColor(R.color.notanswer));
                } else if (result.getFormAnswer().isCheck())
                    txtQuestion.setTextColor(context.getResources().getColor(R.color.correct));
                else
                    txtQuestion.setTextColor(context.getResources().getColor(R.color.incorrect));

                RadioButton button;
                groupAnswers.removeAllViews();
                for (int i = 0; i < result.getQuestion().getAnswers().size(); i++) {
                    button = new RadioButton(mAdapter.context);
                    button.setText(result.getQuestion().getAnswers().get(i).getAnswer());
                    button.setEnabled(false);
                    groupAnswers.addView(button);
                    if ((button.getText().toString().trim().charAt(0) + "").equals(result.getFormAnswer().getAnswerHead())) {
                        button.setChecked(true);
                        if (!result.getFormAnswer().isCheck())
                            button.setTextColor(context.getResources().getColor(R.color.incorrect));
                    }
                    if ((button.getText().toString().trim().charAt(0) + "").equals(result.getQuestion().getSolutionHead() + ""))
                        button.setTextColor(context.getResources().getColor(R.color.correct));
                }

                if (result.getQuestion().getFeedback() != null) {
                    String feedback = result.getQuestion().getFeedback();
                    feedback = feedback.replaceAll("<br>", "\n");
                    txtFeedback.setVisibility(View.VISIBLE);
                    txtFeedback.setText(feedback);
                }
            }
            else {
                txtQuestion.setText("Câu hỏi đã bị xóa!");
                txtFeedback.setVisibility(View.GONE);
                imgTitle.setVisibility(View.GONE);
                groupAnswers.setVisibility(View.GONE);
                if (result.getFormAnswer().getAnswerHead() == null) {
                    txtQuestion.setTextColor(context.getResources().getColor(R.color.notanswer));
                } else if (result.getFormAnswer().isCheck())
                    txtQuestion.setTextColor(context.getResources().getColor(R.color.correct));
                else
                    txtQuestion.setTextColor(context.getResources().getColor(R.color.incorrect));

            }
        }
    }
}
