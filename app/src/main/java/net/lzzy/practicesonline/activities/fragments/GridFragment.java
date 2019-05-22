package net.lzzy.practicesonline.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.models.view.QuestionResult;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lzzy_gxy on 2019/5/13.
 * Description:
 */
public class GridFragment extends BaseFragment{
    private GridView gv;
    private TextView tvView;
    public static final String ARGS_RESULT = "results";
    List<QuestionResult> results;
    private GenericAdapter<QuestionResult> adapter;
    private OnGridSkipListener listener;
    private AnalysisFragment.OnChartSkipListener listener1;

    public static GridFragment newInstance(List<QuestionResult> results){
        GridFragment fragment=new GridFragment();
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
        gv = find(R.id.fragment_gird_g1);
        tvView = find(R.id.fragment_gird_g2);
        //region  显示
        adapter = new GenericAdapter<QuestionResult>(getContext(),R.layout.fragment_grid_item,results) {
            @Override
            public void populate(ViewHolder viewHolder, QuestionResult questionResult) {
                TextView tvLabel=viewHolder.getView(R.id.fragment_grid_item_label);

                viewHolder.setTextView(R.id.fragment_grid_item_label,getPosition(questionResult)+1+"");
                if (questionResult.isRight()){
                    tvLabel.setBackgroundResource(R.drawable.circle1);
                }else {
                    tvLabel.setBackgroundResource(R.drawable.circle2);
                }


            }

            @Override
            public boolean persistInsert(QuestionResult questionResult) {
                return false;
            }

            @Override
            public boolean persistDelete(QuestionResult questionResult) {
                return false;
            }

        };
        gv.setAdapter(adapter);
        //endregion

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               listener.onGridSkip(adapter.getPosition(results.get(position)));
            }
        });

        tvView.setOnClickListener(v -> listener.gotoChart());
        TextView defined=find(R.id.defined);
        defined.setOnClickListener(v -> listener1.gotoAnalysis());
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_grid;
    }

    @Override
    public void search(String kw) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener= (OnGridSkipListener) context;
            listener1= (AnalysisFragment.OnChartSkipListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"必须实现OnGridSkipListener接口");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
        listener1=null;
    }

    public interface OnGridSkipListener {
        /**
         * 跳转返回Question视图查看题目
         * @param position
         */
        void onGridSkip(int position);

        /**
         * 跳转到ChartFragment
         */
        void gotoChart();
    }
}
