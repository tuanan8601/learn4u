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
import com.example.questans.model.Chapter;
import com.example.questans.activity.testform.TestFormActivity;

import java.util.List;

public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.ChapterHolder>{
    List<Chapter> mChapterList;
    private Context context;
    public ChapterListAdapter(Context context, List<Chapter> chapterList) {
        this.context = context;
        this.mChapterList = chapterList;
    }
    @NonNull
    @Override
    public ChapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View mItemView = mInflater.inflate(R.layout.chapter_item, parent, false);
        return new ChapterHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterHolder holder, int position) {
        holder.setItemView(mChapterList.get(position));
    }

    @Override
    public int getItemCount() {
        return mChapterList.size();
    }

    public class ChapterHolder extends RecyclerView.ViewHolder {
        final ChapterListAdapter mAdapter;
        TextView txtTen;
        Button btnOntap;
        Button btnKiemtra;
        public ChapterHolder(@NonNull View itemView, ChapterListAdapter adapter) {
            super(itemView);
            this.mAdapter = adapter;

            txtTen = itemView.findViewById(R.id.txtTenchuong);
            btnOntap = itemView.findViewById(R.id.btnOnTap);
            btnKiemtra = itemView.findViewById(R.id.btnKiemtra);
        }

        public void setItemView(Chapter chapter) {
            txtTen.setText(chapter.getTestName());
            btnOntap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mAdapter.context,TestFormActivity.class);
                    intent.putExtra("chapId",chapter.getchapterId());
                    intent.putExtra("type",0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mAdapter.context.startActivity(intent);
                }
            });
            btnKiemtra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mAdapter.context, TestFormActivity.class);
                    intent.putExtra("chapId",chapter.getchapterId());
                    intent.putExtra("type",1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mAdapter.context.startActivity(intent);
                }
            });
        }
    }
}
