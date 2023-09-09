package com.example.questans.activity.testform;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.questans.data.ApiClient;
import com.example.questans.R;
import com.example.questans.activity.result.TestResultActivity;
import com.example.questans.model.FormAnswer;
import com.example.questans.model.Chapter;
import com.example.questans.model.Question;
import com.example.questans.model.TestResult;
import com.example.questans.model.objTest.ObjectiveTest;
import com.example.questans.model.objTest.TestQuest;
import com.example.questans.user.AccountManager;
import com.example.questans.utils.DownloadImageTask;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestFormActivity extends AppCompatActivity {
    private AnswerListViewModel viewModel;
    boolean isList;
    float initialX;
    int type;
    String subjId;
    String chapId;
    Handler mHandler;
    Thread thread;
    int dotime;

    TextView objTestTitle;
    ImageView imgTitle;
    TextView questNum;
    TextView txtTime;
    TextView questTitle;
    Button next;
    Button previous;
    TestResult testResult;

    List<Question> questions;
    List<FormAnswer> formAnswers;
    RadioGroup groupAnswers;
    int currQuest = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_form);

        if(!AccountManager.islogged()) finish();

        objTestTitle = findViewById(R.id.txtObjTestTitle);
        imgTitle = findViewById(R.id.imgTitle);
        questNum = findViewById(R.id.txtQuestNum);
        txtTime = findViewById(R.id.txtTime);
        questTitle = findViewById(R.id.txtTitle);
        groupAnswers = findViewById(R.id.groupAnswers);
        next = findViewById(R.id.btnNext);
        previous = findViewById(R.id.btnPrevious);

        View contentView = (View)findViewById(R.id.questionView);
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.d("An_Test","Touch flipper");
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d("An_Test","Touch down");
                        initialX = motionEvent.getX();
                        break;

                    case MotionEvent.ACTION_UP:
                        Log.d("An_Test","Touch up");
                        float finalX = motionEvent.getX();
                        if ((initialX-finalX) > 100) {
                            if(currQuest<questions.size()-1)
                                nextQuestion(view);
                        } else if((finalX-initialX) > 100){
                            if(currQuest>0)
                                previousQuestion(view);
                        }
                        break;
                }
                return true;
            }
        });

        isList=false;

        viewModel = new ViewModelProvider(this).get(AnswerListViewModel.class);
        type=getIntent().getIntExtra("type",0);
        subjId=getIntent().getStringExtra("subjId");
        chapId=getIntent().getStringExtra("chapId");
        if(type == 0) {
            ApiClient.getAPI().getchaptersByID(chapId).enqueue(new Callback<Chapter>() {
                @Override
                public void onResponse(Call<Chapter> call, Response<Chapter> response) {
                    Chapter chapter = response.body();
                    questions = chapter.getQuestions();

                    Log.d("An_test", chapter.getchapterId());
                    objTestTitle.setText(chapter.getTestName());

                    testResult = new TestResult();
                    testResult.setTestName(chapter.getTestName());
                    testResult.setTotalScore(chapter.getQuestions().size());
                    testResult.setUserId(AccountManager.getUid());
                    testResult.setType(type);
                    testResult.setCsId(chapId);
                    formAnswers = new ArrayList<>();
                    for (Question q : questions) {
                        formAnswers.add(new FormAnswer(chapter.getchapterId(), q.getQid()));
                    }
                    viewModel.setFormAnswers(formAnswers);

                    groupAnswers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            int qid = questions.get(currQuest).getQid();
                            String checkedAns = (String) ((RadioButton) findViewById(groupAnswers.getCheckedRadioButtonId())).getText();
                            String checkedHead = "" + checkedAns.trim().charAt(0);
                            Log.d("An_test", "" + checkedHead);
                            Log.d("An_test", "" + qid);
                            formAnswers.get(currQuest).setAnswerHead(checkedHead);
                            viewModel.setFormAnswers(formAnswers);
                        }
                    });
                    setQuestionView();
                }

                @Override
                public void onFailure(Call<Chapter> call, Throwable t) {
                    Log.d("An_test", "Error:" + t.getMessage());
                }
            });
        }
        else if(type==1 || type==2){
            Callback<ObjectiveTest> objectiveTestCall = new Callback<ObjectiveTest>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<ObjectiveTest> call, Response<ObjectiveTest> response) {
                    ObjectiveTest objectiveTest = response.body();
                    questions = new ArrayList<>();
                    for (TestQuest tq: objectiveTest.getTestQuestList()) {
                        questions.add(tq.getQuestion());
                    }

                    Log.d("An_test", objectiveTest.getTestName());
                    objTestTitle.setText(objectiveTest.getTestName());

                    testResult = new TestResult();
                    testResult.setTestName(objectiveTest.getTestName());
                    testResult.setTotalScore(objectiveTest.getTestQuestList().size());
                    testResult.setTime(objectiveTest.getTime());
                    testResult.setUserId(AccountManager.getUid());
                    testResult.setType(type);
                    if(type==1){
                        testResult.setCsId(chapId);
                    }
                    else if(type==2){
                        testResult.setCsId(subjId);
                    }

                    formAnswers = new ArrayList<>();
                    for (TestQuest tq : objectiveTest.getTestQuestList()) {
                        formAnswers.add(new FormAnswer(tq.getChapterId(), tq.getQuestion().getQid()));
                    }
                    viewModel.setFormAnswers(formAnswers);

                    groupAnswers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            int qid = questions.get(currQuest).getQid();
                            String checkedAns = (String) ((RadioButton) findViewById(groupAnswers.getCheckedRadioButtonId())).getText();
                            String checkedHead = "" + checkedAns.trim().charAt(0);
                            Log.d("An_test", "" + checkedHead);
                            Log.d("An_test", "" + qid);
                            formAnswers.get(currQuest).setAnswerHead(checkedHead);
                            viewModel.setFormAnswers(formAnswers);
                        }
                    });
                    setQuestionView();
                    startCountDown(objectiveTest.getTime());
                }

                @Override
                public void onFailure(Call<ObjectiveTest> call, Throwable t) {
                    Log.d("An_test", "Error:" + t.getMessage());
                }
            };
            if(type==1){
                ApiClient.getAPI().getChapterTest(chapId,15,15).enqueue(objectiveTestCall);
            }
            else if(type==2){
                ApiClient.getAPI().getSubjectTest(subjId,90,5).enqueue(objectiveTestCall);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startCountDown(int time) {
        mHandler = new Handler();
        txtTime.setVisibility(View.VISIBLE);
        System.out.println(time);
        thread = new Thread(()->{
            try {
                for (int i = time; i > 0; i--) {
                    int finalI = i;
                    dotime = time - finalI;

                    Duration duration = Duration.ofMinutes(finalI);
                    long hours = duration.toHours();
                    long mins = duration.minusHours(hours).toMinutes();

                    String formatted = String.format("%02d:%02d", hours, mins);
                    mHandler.post(() -> txtTime.setText(formatted));
                    Thread.sleep(1000);
                }
                dotime = time;
                txtTime.setVisibility(View.INVISIBLE);
                mHandler.post(() -> {
                    new AlertDialog.Builder(TestFormActivity.this)
                            .setTitle("Hết giờ")
                            .setMessage("Đã hết giờ, mời bạn nộp bài")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    send();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    send();
                                }
                            })
                            .show();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void setQuestionView(){
        imgTitle.setVisibility(View.GONE);
        currQuest = viewModel.getCurr();
        previous.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
        if(currQuest==0)
            previous.setVisibility(View.INVISIBLE);
        if(currQuest==questions.size()-1)
            next.setVisibility(View.INVISIBLE);

        questNum.setText("Câu "+(currQuest+1)+":");
        Question question = questions.get(currQuest);


        String questitle = question.getTitle();
        if(questitle.length()>5&&questitle.substring(0,4).indexOf(".")>0)
            questitle = questitle.substring(questitle.indexOf(".")+1);
        questTitle.setText(questitle);
        if(question.getImage()!=null){
            new DownloadImageTask(imgTitle)
                    .execute(question.getImage());
        }

        RadioButton button;
        groupAnswers.removeAllViews();
        for(int i = 0; i < question.getAnswers().size(); i++) {
            button = new RadioButton(getApplicationContext());
            button.setText(question.getAnswers().get(i).getAnswer());
            groupAnswers.addView(button);
            String chosenHead = formAnswers.get(currQuest).getAnswerHead();
            if((button.getText().toString().trim().charAt(0)+"").equals(chosenHead))
                button.setChecked(true);
        }
    }

    public void previousQuestion(View view) {
        currQuest--;
        viewModel.setCurr(currQuest);
        setQuestionView();
        changeAnswerList();
    }

    public void nextQuestion(View view) {
        currQuest++;
        viewModel.setCurr(currQuest);
        setQuestionView();
        changeAnswerList();
    }

    public void changeAnswerList(){
        if(isList) {
            viewModel.setCurr(currQuest);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.answerlistContainerView, AnswerListFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name") // name can be null
                    .commit();
        }
    }

    public void sendForm(View view) {
        new AlertDialog.Builder(TestFormActivity.this)
                .setTitle("Nộp bài")
                .setMessage("Bạn có chắc muốn nộp bài không?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        send();
                    }
                })
                .setNegativeButton(android.R.string.no,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void send(){
        testResult.setDotime(dotime);
        testResult.setFormAnswers(formAnswers);
        ApiClient.getAPI().sendTestForm(testResult).enqueue(new Callback<TestResult>() {
            @Override
            public void onResponse(Call<TestResult> call, Response<TestResult> response) {
                TestResult testResult = response.body();
                Log.d("An_test", "onResponse: "+testResult.getFormId());
                Intent intent = new Intent(getApplicationContext(), TestResultActivity.class);
                intent.putExtra("resultID",testResult.getFormId());
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<TestResult> call, Throwable t) {
                Log.d("An_test", "onFailure: ");
                startActivity(new Intent(getApplicationContext(),TestResultActivity.class));
                finish();
            }
        });
    }

    public void listAnswer(View view) {
        ImageButton imgbtn = (ImageButton) view;
        if(!isList) {
            imgbtn.setImageResource(R.drawable.ic_baseline_apps_24);
            viewModel.setCurr(currQuest);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.answerlistContainerView, AnswerListFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name") // name can be null
                    .commit();
            isList=true;
        }
        else {
            imgbtn.setImageResource(R.drawable.ic_baseline_apps_24_white);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .remove(fragmentManager.findFragmentById(R.id.answerlistContainerView))
                    .setReorderingAllowed(true)
                    .addToBackStack("name") // name can be null
                    .commit();
            isList=false;
            currQuest = viewModel.getCurr();
        }
    }

    public boolean regMatch(String patt, String str){
        Pattern pattern = Pattern.compile(patt, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        boolean matchFound = matcher.find();
        return matchFound;
    }

    @Override
    protected void onDestroy() {
        if(thread!=null)
            thread.interrupt();
        super.onDestroy();
    }

}