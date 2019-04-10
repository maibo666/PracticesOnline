package net.lzzy.practicesonline.activities.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lzzy_gxy on 2019/3/11.
 * Description:
 */
public class AppUtils extends Application {
    private static List<Activity> activities = new LinkedList<>();
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void exit(){
        for (Activity activity:activities){
            if(activity != null){
                activity.finish();
            }
        }
        System.exit(0);
    }

    public static Activity getRunningActivity(){
        for (Activity activity:activities){
            if (!activity.isDestroyed()){
                return activity;
            }
        }
        return null;
    }

    public static Context getContext(){
        return context;
    }


}
