package com.example.questans.activity.subject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.questans.R;
import com.example.questans.model.Subject;
import com.example.questans.activity.testform.TestFormActivity;

import java.util.List;

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.SubjectHolder>{
    List<Subject> mSubjectList;
    private Context context;
    public SubjectListAdapter(Context context, List<Subject> subjectList) {
        this.context = context;
        this.mSubjectList = subjectList;
    }
    @NonNull
    @Override
    public SubjectListAdapter.SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View mItemView = mInflater.inflate(R.layout.subject_item, parent, false);
        return new SubjectListAdapter.SubjectHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectListAdapter.SubjectHolder holder, int position) {
        holder.setItemView(mSubjectList.get(position));
    }

    @Override
    public int getItemCount() {
        return mSubjectList.size();
    }

    public class SubjectHolder extends RecyclerView.ViewHolder {
        final SubjectListAdapter mAdapter;
        TextView txtTen;
        Button btnOntap;
        Button btnKiemtra;
        public SubjectHolder(@NonNull View itemView, SubjectListAdapter adapter) {
            super(itemView);
            this.mAdapter = adapter;

            txtTen = itemView.findViewById(R.id.txtTenmon);
            btnOntap = itemView.findViewById(R.id.btnOnTap);
            btnKiemtra = itemView.findViewById(R.id.btnKiemtra);
        }

        public void setItemView(Subject subject) {
            txtTen.setText(subject.getName());

            btnOntap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mAdapter.context,ChapterActivity.class);
                    intent.putExtra("subjId",subject.getSubjectId());
                    intent.putExtra("subjName",subject.getName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mAdapter.context.startActivity(intent);
                }
            });
            btnKiemtra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mAdapter.context, TestFormActivity.class);
                    intent.putExtra("subjId",subject.getSubjectId());
                    intent.putExtra("type",2);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mAdapter.context.startActivity(intent);
                }
            });
        }
    }
}
