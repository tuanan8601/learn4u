package com.example.questans.activity.schedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import com.example.questans.data.ApiClient;
import com.example.questans.R;
import com.example.questans.model.Subject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleDialogFragment extends android.app.DialogFragment {

    private static final String TAG = "DialogFragment";

    String subject;
    String note;

    public ScheduleDialogFragment() {
    }

    @SuppressLint("ValidFragment")
    public ScheduleDialogFragment(String subject, String note) {
        this.subject = subject;
        this.note = note;
    }

    public interface OnInputListener {
        void sendInput(String subjectText,String noteText);
    }
    public OnInputListener mOnInputListener;

    EditText txtNote;
    EditText txtSubj;
    LinearLayout subjGroup;
    Button btnCancel;
    Button btnOK;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(
                R.layout.fragment_schedule_dialog, container, false);
        txtNote
                = view.findViewById(R.id.txt_scheduleText);
        txtSubj = view.findViewById(R.id.txt_ScheduleSubj);
        subjGroup = view.findViewById(R.id.group_subject);
        btnOK = view.findViewById(R.id.btnOK);
        btnCancel = view.findViewById(R.id.btnCancel);

        if(note!=null) txtNote.setText(note);
        if(txtSubj!=null) txtSubj.setText(subject);

        btnCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(View v)
                    {
                        getDialog().dismiss();
                    }
                });

        btnOK.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(View v)
                    {
                        mOnInputListener.sendInput(txtSubj.getText().toString(),txtNote.getText().toString());
                        getDialog().dismiss();
                    }
                });

        ApiClient.getAPI().getAllSubjects().enqueue(new Callback<List<Subject>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Subject>> call, Response<List<Subject>> response) {
                List<Subject> subjectList = response.body();
                subjectList.forEach(d->{
                    Button button = new Button(view.getContext());
                    button.setText(d.getName());
                    button.setBackground(getResources().getDrawable(R.drawable.table_border));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            txtSubj.setText(button.getText());
                        }
                    });
                    subjGroup.addView(button);
                });
            }

            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {

            }
        });
        return view;
    }

    @Override public void onAttach(Context context)
    {
        super.onAttach(context);
        try {
            mOnInputListener
                    = (OnInputListener)getActivity();
        }
        catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }
}