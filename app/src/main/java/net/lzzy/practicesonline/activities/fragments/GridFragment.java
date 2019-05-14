package net.lzzy.practicesonline.activities.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.GridView;
import android.widget.ListAdapter;
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

    public static final String RESULTS = "results";
    private List<QuestionResult> results;

    public static GridFragment newInstance(List<QuestionResult> results){
        GridFragment fragment=new GridFragment();
        Bundle args=new Bundle();
        args.putParcelableArrayList(RESULTS, (ArrayList<? extends Parcelable>) results);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            results= getArguments().getParcelableArrayList(RESULTS);
        }
    }
    @Override
    protected void populate() {
        GridView gridView = find(R.id.fragment_gird_g1);
        TextView textView = find(R.id.fragment_gird_g2);
        GenericAdapter<QuestionResult> adapter = new GenericAdapter<QuestionResult>(getContext(),R.layout.grid_item,results) {
            @Override
            public void populate(ViewHolder viewHolder, QuestionResult questionResult) {
                TextView textView1 = viewHolder.getView(R.id.grid_item_g1);
                viewHolder.setTextView(R.id.grid_item_g1,getPosition(questionResult)+1+"");
                if (questionResult.isRight()){
                    textView1.setBackgroundResource(R.drawable.btn_sector_grid);
                }else {
                    textView1.setBackgroundResource(R.drawable.btn_sector_gridr);
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
        gridView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_grid;
    }

    @Override
    public void search(String kw) {

    }
}
