package net.lzzy.practicesonline.activities.fragments;

import android.app.AlertDialog;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.models.Practice;
import net.lzzy.practicesonline.activities.models.PracticeFactory;
import net.lzzy.practicesonline.activities.models.UserCookies;
import net.lzzy.practicesonline.activities.network.PracticeService;
import net.lzzy.practicesonline.activities.utils.AbstractStaticHandler;
import net.lzzy.practicesonline.activities.utils.AppUtils;
import net.lzzy.practicesonline.activities.utils.DateTimeUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lzzy_gxy on 2019/4/16.
 * Description:
 */
public class PracticesFragment extends BaseFragment {

    private ListView lv;
    private SwipeRefreshLayout swipe;
    private TextView tvHint;
    private TextView tvTime;
    private List<Practice> practices;
    private GenericAdapter<Practice> adapter;
    private PracticeFactory factory = PracticeFactory.getInstance();
    private ThreadPoolExecutor executor = AppUtils.getExecutor();
    private DownloadHandler handler = new DownloadHandler(this);
    private static final int WHAT_PRACTICE_DONE = 0;
    private static final int WHAT_EXCEPTION = 1;
    private boolean isDeleting=false;
    private float touchX1;

    private static class DownloadHandler extends AbstractStaticHandler<PracticesFragment> {

        DownloadHandler(PracticesFragment context) {
            super(context);
        }

        @Override
        public void handleMessage(Message msg, PracticesFragment fragment) {
            switch (msg.what) {
                case WHAT_PRACTICE_DONE:
                    fragment.tvTime.setText(DateTimeUtils.DATE_TIME_FORMAT.format(new Date()));
                    UserCookies.getInstance().updateLastRefreshTime();
                    try {
                        List<Practice> practices = PracticeService.getPractices(msg.obj.toString());
                        for (Practice practice : practices) {
                            fragment.adapter.add(practice);
                        }
                        Toast.makeText(fragment.getContext(), "同步完成", Toast.LENGTH_SHORT).show();
                        fragment.finishRefresh();
                    } catch (Exception e) {
                        e.printStackTrace();
                        fragment.handlePracticeException(e.getMessage());
                    }
                    break;
                case WHAT_EXCEPTION:
                    fragment.handlePracticeException(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    }

    private void handlePracticeException(String message) {
        finishRefresh();
        Snackbar.make(lv,"同步失败\n"+message,Snackbar.LENGTH_LONG)
                .setAction("重试",v -> {
                    swipe.setRefreshing(true);
                    refreshListener.onRefresh();
                }).show();
    }


    private void finishRefresh(){
        swipe.setRefreshing(false);
        tvTime.setVisibility(View.GONE);
        tvHint.setVisibility(View.GONE);
    }


    @Override
    protected void populate() {
        initViews();
        loadPractices();
        initSwipe();
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = this::downloadPractices;

    private void downloadPractices() {
        tvTime.setVisibility(View.VISIBLE);
        tvHint.setVisibility(View.VISIBLE);
        executor.execute(() -> {
            try {
                String json = PracticeService.getPracticesFromServer();
                handler.sendMessage(handler.obtainMessage(WHAT_PRACTICE_DONE, json));
            } catch (IOException e) {
                e.printStackTrace();
                handler.sendMessage(handler.obtainMessage(WHAT_EXCEPTION, e.getMessage()));
            }
        });
    }

    private void initSwipe() {
        swipe.setOnRefreshListener(refreshListener);
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean isTop = view.getChildCount() == 0 || view.getChildAt(0).getTop() >= 0;
                swipe.setEnabled(isTop);
            }
        });
    }

    private void loadPractices() {
        practices = factory.get();
        Collections.sort(practices, (o1, o2) -> o2.getDownloadDate().compareTo(o1.getDownloadDate()));
        adapter = new GenericAdapter<Practice>(getContext(), R.layout.practice_item, practices) {
            @Override
            public void populate(ViewHolder holder, Practice practice) {
                holder.setTextView(R.id.practice_item_tv_name, practice.getName());
                Button btnOutlines = holder.getView(R.id.practice_item_btn_outlines);
                if (practice.isDownloaded()) {
                    btnOutlines.setVisibility(View.VISIBLE);
                    btnOutlines.setOnClickListener(v -> new AlertDialog.Builder(getContext())
                            .setMessage(practice.getOutlines())
                            .show());
                } else {
                    btnOutlines.setVisibility(View.GONE);
                }
                Button btnDel = holder.getView(R.id.practice_item_btn_del);
                btnDel.setVisibility(View.GONE);
                btnDel.setOnClickListener(v -> new AlertDialog.Builder(getContext())
                        .setTitle("删除确认")
                        .setMessage("确定要删除项目吗？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确认", (dialogInterface, i) ->
                        {   isDeleting=false;
                            adapter.remove(practice);
                            btnDel.setVisibility(View.GONE);
                        }).show());
                holder.getConvertView().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        slideToDelete(event, btnDel);
                        return true;
                    }
                });
            }

            @Override
            public boolean persistInsert(Practice practice) {
                return factory.add(practice);
            }

            @Override
            public boolean persistDelete(Practice practice) {
                return factory.deletePracticeAndRelated(practice);
            }
        };
        lv.setAdapter(adapter);
    }
    private void slideToDelete(MotionEvent event, Button btnDel) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchX1=event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if(touchX1-event.getX()>100){
                    if (!isDeleting){
                        btnDel.setVisibility(View.VISIBLE);
                        isDeleting=true;
                    }
                }else {
                        btnDel.setVisibility(View.GONE);
                        isDeleting=false;
                }
                break;
            default:
                break;
        }
    }

    private void initViews() {
        lv = find(R.id.fragment_practices_lv);
        TextView tvNone = find(R.id.fragment_practices_tv_none);
        lv.setEmptyView(tvNone);
        swipe = find(R.id.fragment_practices_swipe);
        tvHint = find(R.id.fragment_practices_tv_hint);
        tvTime = find(R.id.fragment_practices_tv_time);
        tvTime.setText(UserCookies.getInstance().getLastRefreshTime());
        tvHint.setVisibility(View.GONE);
        tvHint.setVisibility(View.GONE);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_practices;
    }

    @Override
    public void search(String kw) {

    }
}
