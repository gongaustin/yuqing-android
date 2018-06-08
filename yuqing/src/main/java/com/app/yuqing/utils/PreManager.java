package com.app.yuqing.utils;

import com.app.yuqing.bean.UserOldBean;
import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 数据存储封装保存数据到手机的各种方法，如文件存储，sharePreference,网络存储
 * 
 * @author
 * 
 */

public class PreManager {

	private static Gson gson = new Gson();
	/**
	 * 保存是否第一次使用app
	 * 
	 * @param context
	 * @param flag
	 */
	public static void saveIsFirstUse(Context context, boolean flag) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("app_first", flag).commit();
	}

	/**
	 * 获得是否是第一次使用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getIsFirstUse(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("app_first", true);
	}
	
	
	public static void saveLogin(Context context,boolean flag) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
			.putBoolean("key_login", flag).commit();
	}
	
	public static boolean getIsLogin(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
			.getBoolean("key_login", false);
	}
	
	/**
	 * 
	 * 放入一个对象
	 * @param context
	 * @param key
	 * @param t
	 */
	public static <T> void put(Context context, String key, T t) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		String value = gson.toJson(t);
		settings.edit().putString(key, value).commit();
	}
	
	/**
	 * 取出一个对象
	 * 
	 * @param context
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T> T get(Context context, String key, Class<T> clazz) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		T t = null;
		String value = settings.getString(key, null);
		if (value != null) {
			t = gson.fromJson(value, clazz);
		}
		return t;
	}

	
	public static void putString(Context context, String key, String s) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putString(key, s).commit();
	}
	
	public static String getString(Context context, String key) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		String value = settings.getString(key, null);
		return value;
	}

	public static void saveCookieName(Context context,String cookie) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("cookieName", cookie).commit();
	}
	
	public static String getCookieName(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("cookieName", "");
	}
	
	public static void saveCookieValue(Context context,String cookie) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("cookieValue", cookie).commit();
	}
	
	public static String getCookieValue(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("cookieValue", "");
	}	
	
	public static void saveCookieHost(Context context,String cookie) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("cookieHost", cookie).commit();
	}
	
	public static String getCookieHost(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("cookieHost", "");
	}

	public static void saveClientId(Context context,String clientId) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("clientId", clientId).commit();
	}

	public static String getClientId(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("clientId", "");
	}

	public static UserOldBean get(Context applicationContext, String keyLoginuser) {
		return null;
	}
}