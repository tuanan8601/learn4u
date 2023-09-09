package com.example.questans.activity.testform;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.questans.R;
import com.example.questans.model.FormAnswer;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnswerListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnswerListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AnswerListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnswerListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnswerListFragment newInstance(String param1, String param2) {
        AnswerListFragment fragment = new AnswerListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private AnswerListViewModel viewModel;
    private int curr;
    List<FormAnswer> formAnswers;
    TableLayout tableLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View tview = inflater.inflate(R.layout.fragment_answer_list, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(AnswerListViewModel.class);
        setupAnswerList(inflater,tview);
        return tview;
    }
    public void setupAnswerList(LayoutInflater inflater,View tview){
        formAnswers = viewModel.getFormAnswers();
        curr = viewModel.getCurr();

        if(formAnswers!=null) {
            tableLayout = tview.findViewById(R.id.menutable);
            tableLayout.removeAllViews();

            Button btn;
            TableRow tableRow = new TableRow(inflater.getContext());

            int row = 4;
            for (int i = 0; i < formAnswers.size(); i++) {
                btn = new Button(inflater.getContext());
                if (formAnswers.get(i).getAnswerHead() != null) {
                    btn.setText((i+1) + ". " + formAnswers.get(i).getAnswerHead());
                    btn.setBackground(getResources().getDrawable(R.drawable.button_af));
                } else {
                    btn.setText((i+1) + ". ");
                    btn.setBackground(getResources().getDrawable(R.drawable.button_bf));
                }
                if(i==curr){
                    btn.setBackground(getResources().getDrawable(R.drawable.button_curr));
                }

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String currstr = ((Button) view).getText().toString().split("\\.")[0];
                        curr = Integer.parseInt(currstr)-1;
                        viewModel.setCurr(curr);
                        ((TestFormActivity)getActivity()).setQuestionView();
                        setupAnswerList(inflater,tview);
                    }
                });
                tableRow.addView(btn);
                if ((i > 0 && i % row == (row - 1)) || (i == formAnswers.size() - 1)) {
                    tableLayout.addView(tableRow);
                    tableRow = new TableRow(inflater.getContext());
                }
            }
        }
    }
}