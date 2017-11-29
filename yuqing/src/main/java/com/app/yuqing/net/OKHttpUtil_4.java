package com.app.yuqing.net;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Hello world!
 *
 */
public class OKHttpUtil_4
{
    private OkHttpClient mOkHttpClient;
    private List<Cookie> cookiesA = new ArrayList<Cookie>();
    public void test(){
        try {
            initClient();
            String userInfo = login();
            System.out.println(userInfo);
            //调用token
            RequestBody body=new FormBody.Builder()
                    .add("userId","1db209893129418ba05edad6a552f942")
                    .add("name","csyh")
                    .add("portraitUri","").build();
           String token = request("http://sevencai.cn/rong/user/getToken",body,"post");
           System.out.println(token);

           //调用机构
            String treeData=request("http://sevencai.cn/a/sys/office/treeData",null,"get");
            System.out.println(treeData);

            //文件上传
            File file = new File("d:/a.jpg");
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "file.jpg", fileBody)
                    .build();
            String updateResult=request("http://sevencai.cn/a/sys/user/updateHead",requestBody,"post");
            System.out.println(updateResult);

        }catch (Exception err){
            err.printStackTrace();
        }
    }
    private String login(){
        String responseStr="";
       try{
           RequestBody formBody = new FormBody.Builder()
                   .add("username", "admin")
                   .add("password", "blt606")
                   .add("isAjax","true")
                   .build();
           Request request = new Request.Builder()
                   .url("http://sevencai.cn/a/login")
                   .post(formBody)
                   .build();
           Response response = mOkHttpClient.newCall(request).execute();
           responseStr = response.body().string();

       }catch (Exception err){
           err.printStackTrace();
           return null;
       }
       return responseStr;
    }

    private String request(String url,RequestBody requestBody,String type){
        String responesStr;
        try{
            Request request;
            if(type.equalsIgnoreCase("post")){
                request=new Request.Builder().url(url).post(requestBody).build();
            }else{
                request=new Request.Builder().url(url).get().build();
            }
            Response response1=mOkHttpClient.newCall(request).execute();
            responesStr = response1.body().string();
        }catch (Exception err){
            err.printStackTrace();
            return null;
        }
        return responesStr;
    }

    private void initClient(){
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .cookieJar(new CookieJar()
                {//这里可以做cookie传递，保存等操作
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies)
                    {//可以做保存cookies操作
                        cookiesA = cookies;
                    }
                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url)
                    {//加载新的cookies
                        for(Cookie cookie : cookiesA) {
                        	System.out.println(cookie.name()+"|"+cookie.value());
                        }
                        return cookiesA;
                    }
                })
                .build();
    }
}