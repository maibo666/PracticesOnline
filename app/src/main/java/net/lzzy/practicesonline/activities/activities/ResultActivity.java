package net.lzzy.practicesonline.activities.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.fragments.AnalysisFragment;
import net.lzzy.practicesonline.activities.fragments.ChartFragment;
import net.lzzy.practicesonline.activities.fragments.GridFragment;
import net.lzzy.practicesonline.activities.models.view.QuestionResult;

import java.util.List;

/**
 * @author lzzy_gxy on 2019/5/13.
 * Description:
 */
public class ResultActivity extends BaseActivity implements GridFragment.OnGridSkipListener
        ,ChartFragment.OnChartSkipListener, AnalysisFragment.OnChartSkipListener{
    public static final String POSITION = "position";
    public static final int RESULT_CODE = 1;
    public static final String PRACTICE_ID = "practiceId";
    private List<QuestionResult> results;
    public static final String EXTRA_QUESTION_POS = "extra_question_pos";
    public static final String EXTRA_FAVORITE_FLAG = "favorite_flag";
    private int questionPos;
    private String practiceId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        practiceId = getIntent().getStringExtra(QuestionActivity.EXTRA_PRACTICE_ID);
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_result;
    }

    @Override
    protected int getContainerId() {
        return R.id.activity_result_container;
    }

    @Override
    protected Fragment createFragment() {
        results = getIntent().getParcelableArrayListExtra(QuestionActivity.EXTRA_RESULT);
        return GridFragment.newInstance(results);
    }

    @Override
    public void onGridSkip(int position) {
        Intent intent=new Intent(this,QuestionActivity.class);
        intent.putExtra(POSITION,position);
        setResult(RESULT_CODE,intent);
        finish();
    }

    @Override
    public void gotoChart() {
        getManager().beginTransaction()
                .replace(R.id.activity_result_container, ChartFragment
                        .newInstance(results)).commit();
    }


    @Override
    public void gotoGrid() {
        getManager().beginTransaction()
                .replace(R.id.activity_result_container,GridFragment
                        .newInstance(results)).commit();
    }


    @Override
    public void gotoAnalysis() {
        getManager().beginTransaction()
                .replace(R.id.activity_result_container, AnalysisFragment
                        .newInstance(results)).commit();
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("返回到哪里")
                .setNeutralButton("返回题目",(dialog, which) -> {
                    Intent intent=new Intent();
                    intent.putExtra(EXTRA_QUESTION_POS,questionPos);
                    setResult(RESULT_OK,intent);
                    finish();
                    super.onBackPressed();
                })
                .setPositiveButton("查看收藏",(dialog, which) -> {
                    Intent intent=new Intent();
                    intent.putExtra(PRACTICE_ID,practiceId);
                    setResult(RESULT_OK,intent);
                    finish();
                    super.onBackPressed();
                })
                .setNegativeButton("章节列表",(dialog, which) -> {
                    startActivity(new Intent(this,PracticesActivity.class));
                    finish();
                    super.onBackPressed();
                })
                .show();


    }
}
