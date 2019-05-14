package net.lzzy.practicesonline.activities.activities;

import androidx.fragment.app.Fragment;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.fragments.GridFragment;
import net.lzzy.practicesonline.activities.models.view.QuestionResult;

import java.util.List;

/**
 * @author lzzy_gxy on 2019/5/13.
 * Description:
 */
public class ResultActivity extends BaseActivity{

    private List<QuestionResult> results;

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
}
