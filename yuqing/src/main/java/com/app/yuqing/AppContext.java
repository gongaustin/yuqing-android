package com.app.yuqing;

import java.io.File;

public class AppContext {
	public static final String TAG = "yuqing";	//文件名字
	
	public static final String PACKGE_NAME = "com.app.yuqing";

	//Log 管理信息
	public static final String LOG_NET = "net";
	public static final String LOG_MAP = "map";
	public static final String LOG_PUSH = "push";
	public static final String LOG_ERROR = "error";
	public static final String LOG_MAIN = "main";
	
	//缓存数据
	public static final String APP_NAME = "yuqing";	//文件名字
	public static final String FILE_SAVE_ROOT_DIRECTORY = File.separator + APP_NAME + File.separator;
	public static final int PICTURE_SIZE = 300;	//暂且没用到，控制压缩图片大小，单位kb	
	public static final String SAVE_PATH = "/"+APP_NAME;	//压缩文件保存路径
	public static final String TAKEPICTURE_PATH = "/"+APP_NAME+"/camera";	//通过相机拍照图片保存文件夹路径
	public static final String TAKEPICTURE_FILE = "temp.jgp";	//通过相机拍照图片保存文件名称	
	
	//数据KEY
	public static final String USER_KEY = "login_user_key";
	public static final String USER_ACCOUNT = "USER_ACCOUNT";
	public static final String USER_PWD = "USER_PWD";
	public static final String USER_LOGIN = "login_user_login";
	public static final String USER_ID = "login_user_id";
	public static final String URL_SUCCESSCODE = "1";

	public static final String KEY_LOGINUSER = "key_loginuser";
	
	public static final String KEY_NOLOGIN = "key_nologin";
	public static final String CODE_NOLOGIN = "401";

	public static final String KEY_TOKEN = "key_token";
	public static final String APPID = "1";
	public static final String APP_URL = "http://new.sevencai.com/app/#/";

	public static final String KEY_REFERNECE = "refernece";
}
