package net.lzzy.practicesonline.activities.activities;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.fragments.SplashFragment;
import net.lzzy.practicesonline.activities.utils.AppUtils;

public class SplashActivity extends AppCompatActivity implements SplashFragment.OnSplashFinishedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        AppUtils.addActivity(this);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_splash_container);
        if(fragment == null){
            fragment = new SplashFragment();
            manager.beginTransaction().add(R.id.fragment_splash_container,fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new AlertDialog.Builder(this)
                .setMessage("要退出吗？")
                .setPositiveButton("确定",((dialog, which) -> AppUtils.exit()))
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void cancelCount() {

    }
}
