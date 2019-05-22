package net.lzzy.practicesonline.activities.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import net.lzzy.practicesonline.activities.utils.AppUtils;

/**
 * @author lzzy_gxy on 2019/4/11.
 * Description:
 */
@SuppressLint("Registered")
public  abstract class BaseActivity extends AppCompatActivity {
    private Fragment fragment;
    private FragmentManager manager;

    public BaseActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutRes());
        /** 调用添加方法**/
        AppUtils.addActivity(this);
        //region  托管Fragment
        manager = getSupportFragmentManager();
        fragment = manager.findFragmentById(getContainerId());
        if(fragment == null){
            fragment = createFragment();
            manager.beginTransaction().add(getContainerId(),fragment).commit();
        }
        //endregion
    }

    protected Fragment getFragment(){
        return fragment;
    }

    protected FragmentManager getManager(){
        return manager;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /** 调用移除方法**/
        AppUtils.removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.setRunning(getLocalClassName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppUtils.setStopped(getLocalClassName());
    }

    /**
     *Activity的布局文件id
     * @return 布局资源猫id
     */
    protected abstract int getLayoutRes();
    /** Fragment容器id
     *
     * @return 容器id
     */
    protected abstract int getContainerId();
    /**
     * 生成托管的Fragment对象
     * @return fragment
     */
    protected abstract Fragment createFragment();
}
