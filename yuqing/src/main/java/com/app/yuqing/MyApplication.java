package com.app.yuqing;

import io.rong.imkit.RongIM;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.app.yuqing.net.NetUtils;
import com.app.yuqing.utils.CommonUtils;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

public class MyApplication extends Application{
	public static MyApplication instance;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
        CrashHandler ch = CrashHandler.getInstance();
        ch.init(this);
		NetUtils.init();
		if (initDirs()) {
			initImageLoader();
		}
		RongIM.init(this);
		RongIM.getInstance().setMessageAttachedUserInfo(true);
	}
	
	/**
	 * 初始化sd卡环境
	 * @return
	 */
    private boolean initDirs() {
       String mSDCardPath = CommonUtils.getSdcardDir();
       if (mSDCardPath == null) {
           return false;
       }
       File f = new File(mSDCardPath, AppContext.APP_NAME);
       if (!f.exists()) {
           try {
               f.mkdir();
               f.createNewFile();
           } catch (Exception e) {
               e.printStackTrace();
               return false;
           }
       }
       return true;
   }
    
	/**
	 * 初始化图片管理
	 */
	private void initImageLoader() {
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY).build();

		ImageLoaderConfiguration imageconfig = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.threadPoolSize(1).memoryCacheExtraOptions(480, 800)
				.memoryCache(new UsingFreqLimitedMemoryCache(5* 1024 * 1024))   
				.memoryCacheSize(5 * 1024 * 1024)  
				// 100Mb
				.diskCacheSize(100 * 1024 * 1024).threadPriority(Thread.NORM_PRIORITY - 2)
				.defaultDisplayImageOptions(options)
				.denyCacheImageMultipleSizesInMemory().build();
		ImageLoader.getInstance().init(imageconfig);
	}    
	
	private List<Activity> activityList = new LinkedList<Activity>();

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	
	public Activity getActivity() {
		return activityList.get(activityList.size() - 1);
	}	

	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
	}
}
