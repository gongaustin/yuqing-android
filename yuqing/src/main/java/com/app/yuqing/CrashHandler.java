package com.app.yuqing;

import java.lang.Thread.UncaughtExceptionHandler;

import android.util.Log;

public class CrashHandler implements UncaughtExceptionHandler{

	private static CrashHandler INSTANCE;
	
	private CrashHandler() {
		
	}
	
	public static CrashHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CrashHandler();
		}
		return INSTANCE;
	}
	
	private MyApplication app;
	public void init(MyApplication app) {
		this.app = app;
        // 设置该类为线程默认UncatchException的处理器�?
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e(AppContext.LOG_ERROR, ex.getMessage());
		ex.printStackTrace();
        //应用程序信息收集
		
        //保存错误报告文件到文件�??
        //判断是否为UI线程异常，thread.getId()==1 为UI线程
        if (thread.getId() != 1) {
            thread.interrupt();
            //弹出对话框提示用户是否上传异常日志至服务�?

        } else {
            // 方案�?:将所有Activity放入Activity列表中，然后循环从列表中删除，即可�??出程�?
           app.exit();
           android.os.Process.killProcess(android.os.Process.myPid());  
        }		
	}

}
