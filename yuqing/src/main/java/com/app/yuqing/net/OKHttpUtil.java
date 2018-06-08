package com.app.yuqing.net;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.text.TextUtils;

import com.app.yuqing.AppContext;
import com.app.yuqing.MyApplication;
import com.app.yuqing.net.bean.UserResponseBean;
import com.app.yuqing.utils.PersistentCookieStore;
import com.app.yuqing.utils.PreManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpUtil {

	private static OKHttpUtil instance;
	private OkHttpClient mOkHttpClient;
	
	private final PersistentCookieStore cookieStore = new PersistentCookieStore(MyApplication.instance.getApplicationContext());
	
	private boolean noCookie = false;
	private  OKHttpUtil() {
		initClient();
	}
	
	public static OKHttpUtil getInstance() {
		if (instance == null) {
			instance = new OKHttpUtil();
		}
		return instance;
	}
	
	public List<Cookie> getCookie(HttpUrl url) {
		List<Cookie> cookies = cookieStore.get(url);
		return cookies;
	}
	
    private void initClient(){
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
//                .cookieJar(new CookieJar()
//                {//这里可以做cookie传递，保存等操作
//                    @Override
//                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies)
//                    {//可以做保存cookies操作
//                    	StringBuffer sb = new StringBuffer();
//                    	for (int i = 0; i < cookies.size(); i++) {
//                    		Cookie cookie = cookies.get(i);
//                    		String cookieName = cookie.name();
//                    		String cookieValue = cookie.value();
//                    		if (!TextUtils.isEmpty(cookieName)
//                    				&& !TextUtils.isEmpty(cookieValue)) {
//                    			sb.append(cookieName + "=");
//                    			sb.append(cookieValue+";");
//                    		}
//                    	}
//                        PreManager.saveCookieValue(MyApplication.instance.getApplicationContext(), sb.toString());
//                        System.out.println("传递Cookie："+sb.toString());
//                        if (cookies != null && cookies.size() > 0) {
//                            for (Cookie item : cookies) {
//                                cookieStore.add(url, item);
//                            }
//                        }
//                    }
//                    @Override
//                    public List<Cookie> loadForRequest(HttpUrl url)
//                    {//加载新的cookies
////                    	if (noCookie) {
////                    		List<Cookie> cookies = new ArrayList<Cookie>();
////							return cookies;
////						} else {
////	                    	List<Cookie> cookies = cookieStore.get(url);
////	                        return cookies;
////						}
//                        return null;
//                    }
//                })
                .build();
    }
    
    public String request(String url,RequestBody requestBody,String type,boolean needClearCookie){
    	this.noCookie = needClearCookie;
    	System.out.println("URL:"+url);
        String responesStr;
        try{
            Request request = null;
            String authorization = PreManager.getString(MyApplication.instance.getApplicationContext(),AppContext.KEY_TOKEN);
            if(type.equalsIgnoreCase("post")){
                if (!needClearCookie && !TextUtils.isEmpty(authorization)) {
                    request=new Request.Builder().url(url).post(requestBody).addHeader("Authorization",authorization).build();
                } else {
                    request=new Request.Builder().url(url).post(requestBody).build();
                }
            }else{
                if (!needClearCookie && !TextUtils.isEmpty(authorization)) {
                    request=new Request.Builder().url(url).addHeader("Authorization",authorization).get().build();
                } else {
                    request=new Request.Builder().url(url).get().build();
                }
            }
            Response response1=mOkHttpClient.newCall(request).execute();
            responesStr = response1.body().string();
            System.out.println("result:"+url+"   "+responesStr);
        }catch (Exception err){
            err.printStackTrace();
            return null;
        }
        return responesStr;
    }    
}
