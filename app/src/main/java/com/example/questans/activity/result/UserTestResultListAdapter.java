package com.example.questans.activity.result;

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
import com.example.questans.model.TestResult;

import java.text.SimpleDateFormat;
import java.util.List;

public class UserTestResultListAdapter extends RecyclerView.Adapter<UserTestResultListAdapter.TestResultHolder>{
    List<TestResult> mTestResultList;
    private Context context;
    public UserTestResultListAdapter(Context context, List<TestResult> testResultList) {
        this.context = context;
        this.mTestResultList = testResultList;
    }
    @NonNull
    @Override
    public UserTestResultListAdapter.TestResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View mItemView = mInflater.inflate(R.layout.test_result_item, parent, false);
        return new UserTestResultListAdapter.TestResultHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull UserTestResultListAdapter.TestResultHolder holder, int position) {
        holder.setItemView(mTestResultList.get(position));
    }

    @Override
    public int getItemCount() {
        return mTestResultList.size();
    }

    public class TestResultHolder extends RecyclerView.ViewHolder {
        final UserTestResultListAdapter mAdapter;
        TextView txtTenKT;
        TextView txtDiem;
        TextView txtTG;
        TextView txtDate;
        Button btnChitiet;

        public TestResultHolder(@NonNull View itemView, UserTestResultListAdapter adapter) {
            super(itemView);
            this.mAdapter = adapter;

            txtTenKT = itemView.findViewById(R.id.txtTenKT);
            txtDiem = itemView.findViewById(R.id.txtDiemso);
            txtTG = itemView.findViewById(R.id.txtThoigian);
            txtDate = itemView.findViewById(R.id.txtDate);
            btnChitiet = itemView.findViewById(R.id.btnChitiet);
        }

        public void setItemView(TestResult testResult) {
            txtTenKT.setText("Tên: "+testResult.getTestName());
            txtDiem.setText("Điểm: "+testResult.getScore()+"/"+testResult.getTotalScore());
            if(testResult.getTime()>0){
                txtTG.setVisibility(View.VISIBLE);
                txtTG.setText("Thời gian: "+testResult.getDotime()+"/"+testResult.getTime()+" phút");
            }

            String strDate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(testResult.getCreatedAt());
            System.out.println(strDate);
            txtDate.setText(strDate);

            btnChitiet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mAdapter.context,TestResultActivity.class);
                    intent.putExtra("resultID",testResult.getFormId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mAdapter.context.startActivity(intent);
                }
            });
        }
    }
}
