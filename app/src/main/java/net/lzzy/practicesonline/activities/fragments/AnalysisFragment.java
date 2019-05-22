package net.lzzy.practicesonline.activities.fragments;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.models.view.QuestionResult;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lzzy_gxy
 * @date 2019/5/16
 * Description:
 */
public class AnalysisFragment extends BaseFragment {
    public static final String ARGS_RESULT = "result";
    List<QuestionResult> results;

    /** 静态方法传参数 **/
    public static AnalysisFragment newInstance(List<QuestionResult> results){
        AnalysisFragment fragment=new AnalysisFragment();
        Bundle args=new Bundle();
        args.putParcelableArrayList(ARGS_RESULT, (ArrayList<? extends Parcelable>) results);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            results = getArguments().getParcelableArrayList(ARGS_RESULT);
        }
    }

    @Override
    protected void populate() {


        //region  柱状图
       /* BarChartView bar=find(R.id.bar);
        int right=0,miss=0,extra=0,wrong=0;
        String[] axis =new String[results.size()];
        for (int i = 0; i< WrongType.values().length; i++){
            axis [i]=WrongType.getInstance(i).toString();
        }
        for (QuestionResult questionResults:results){
            switch (questionResults.getType()){
                case RIGHT_OPTIONS:
                    right++;
                    break;
                case MISS_OPTIONS:
                    miss++;
                    break;
                case EXTRA_OPTIONS:
                    extra++;
                    break;
                case WRONG_OPTIONS:
                    wrong++;
                    break;
                default:
                    break;
            }
        }
        float[] data ={right,miss,extra,wrong};
        float max=right;
        for (float f:data){
            if (f>max){
                max=f;
            }
        }
        bar.setHorizontalAxis(axis);
        bar.setDataList(data, (int) max*2);*/
        //endregion

        //region  折线图


        //endregion

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_analysis;
    }

    @Override
    public void search(String kw) {

    }

    public interface OnChartSkipListener {
        /**
         * 跳转到GridFragment
         */
        void gotoAnalysis();
    }
}
