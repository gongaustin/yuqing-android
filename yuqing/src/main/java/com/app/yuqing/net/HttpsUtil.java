package com.app.yuqing.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.app.yuqing.utils.CommonUtils;
import com.google.gson.Gson;

import android.text.TextUtils;

public class HttpsUtil {
	
	private static final int TIMEOUT_CONNECTION = 10000;
	
	private static Gson gson = new Gson();
	public static enum HttpState {
		Created("Created"),BadRequest("Bad Request");
		
		private String state;
		HttpState(String state) {
			this.state = state;
		}
		
		public String getState() {
			return this.state;
		}
	}
	
	public static String doGetString(String url) {
		String result = "";
		if (TextUtils.isEmpty(url)) {
			return result;
		}
		try {
			URL getUrl = new URL(url);
			HttpURLConnection httpsURLConnection = (HttpURLConnection) getUrl.openConnection();
			httpsURLConnection.setConnectTimeout(TIMEOUT_CONNECTION);
			httpsURLConnection.setRequestMethod("GET");
			httpsURLConnection.setRequestProperty("Content-Type", "application/json");
			
			InputStream in = httpsURLConnection.getInputStream();
			result = convertStreamToString(in);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static String doPost(String url,HashMap<String,String> map) {
		String result = "";
		if (TextUtils.isEmpty(url)) {
			return result;
		}
		System.out.println("请求地址："+url);
		
		try {
			URL getUrl = new URL(url);
			HttpURLConnection httpsURLConnection =  (HttpURLConnection) getUrl.openConnection();
			httpsURLConnection.setConnectTimeout(TIMEOUT_CONNECTION);
			httpsURLConnection.setRequestMethod("POST");
			httpsURLConnection.setDoOutput(true);
	        
			OutputStream os = httpsURLConnection.getOutputStream();
			StringBuffer sbf = new StringBuffer();
			String param = "";
            String responseHeader = null;//响应头
            byte[] responseBody = null;//响应体
            
			for(String key : map.keySet()) {
				sbf.append(key).append("=").append(map.get(key)).append("&");
			}
			if (sbf.toString().endsWith("&")) {
				param = sbf.toString().substring(0, sbf.toString().length() - 1);
			} else {
				param = sbf.toString();
			}
            
            param = gson.toJson(map);
            
			System.out.println("请求参数："+param);
			os.write(param.getBytes("UTF-8"));
			os.flush();
			os.close();
			InputStream is = httpsURLConnection.getInputStream();
			responseBody = CommonUtils.getBytesByInputStream(is);
			responseHeader = CommonUtils.getResponseHeader(httpsURLConnection);
	        Map<String, List<String>> cookie_map = httpsURLConnection.getHeaderFields();
            List<String> cookies = cookie_map.get("Set-Cookie");
            if (null != cookies && 0 < cookies.size()) {
                String s = "";
                for (String cookie : cookies) {
                    if (s.isEmpty()) {
                        s = cookie;
                    } else {
                        s += ";" + cookie;
                    }
                }
                System.out.println("Cookie:"+s);
            }
			result = new String(responseBody);
			System.out.println("返回参数："+result);
			System.out.println("返回头部信息："+responseHeader);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}	
	
	/**
     * map转url参数
     */
    public static String map2Url(HashMap<String, String> paramToMap) {
        if (null == paramToMap || paramToMap.isEmpty()) {
            return null;
        }
        StringBuffer url    = new StringBuffer();
        boolean      isfist = true;
        for (HashMap.Entry<String, String> entry : paramToMap.entrySet()) {
            if (isfist) {
                isfist = false;
            } else {
                url.append("&");
            }
            url.append(entry.getKey()).append("=");
            String value = entry.getValue();
            if (!TextUtils.isEmpty(value)) {
                try {
                    url.append(URLEncoder.encode(value, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("request data : "+url.toString());
        return url.toString();
    }	
    
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer sbf = new StringBuffer();
		String line = null;
		try {
			while((line = reader.readLine()) != null) {
				sbf.append(line);
			}
		} catch (IOException  e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sbf.toString();
	}

	private static HostnameVerifier getHostnameVerifier() {
		return new HostnameVerifier() {

			@Override
			public boolean verify(String hostname, SSLSession session) {
				// TODO Auto-generated method stub
				return true;
			}
			
		};
	}
	
	private static SSLContext getSSLContext() {
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, getTrustManager(), null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sslContext;
	}
	
	private static TrustManager[] getTrustManager() {
		TrustManager[] trustAllCerts = new TrustManager[]{
				new X509TrustManager(){

					@Override
					public void checkClientTrusted(X509Certificate[] chain,
							String authType) throws CertificateException {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void checkServerTrusted(X509Certificate[] chain,
							String authType) throws CertificateException {
						// TODO Auto-generated method stub
						
					}

					@Override
					public X509Certificate[] getAcceptedIssuers() {
						// TODO Auto-generated method stub
						return new X509Certificate[0];
					}
				}
		};
		return trustAllCerts;
	}
	
}
