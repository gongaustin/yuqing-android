package com.app.yuqing.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import com.app.yuqing.AppContext;
import com.app.yuqing.MyApplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

public class CommonUtils {
	public static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
	public static void showToast(CharSequence cs) {
		Toast.makeText(MyApplication.instance, cs, Toast.LENGTH_LONG).show();
	}
	
	public static String getTodayDate() {
		return sdfDate.format(new Date(System.currentTimeMillis()));
	}
	
	public static float dpToPx(Resources resources, float dp) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
	}
	
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}
	
	public static int getDpi(Context context) {
		final float dpi = context.getResources().getDisplayMetrics().densityDpi;
		return (int) (dpi);
	}	
	
	public static String EncoderByMd5(String str) throws Exception{
	  try {
	        // 生成一个MD5加密计算摘要
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        // 计算md5函数
	        md.update(str.getBytes());
	        // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
	        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
//	        return new BigInteger(1, md.digest()).toString(16);
	        
	        StringBuilder sb = new StringBuilder();  
	        String tmp = null;  
	        for (byte b : md.digest())  
	        {  
	            // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制  
	            tmp = Integer.toHexString(0xFF & b);  
	            if (tmp.length() == 1)// 每个字节8为，转为16进制标志，2个16进制位  
	            {  
	                tmp = "0" + tmp;  
	            }  
	            sb.append(tmp);  
	        }  
	  
	        return sb.toString();
	    } catch (Exception e) {
	        throw new Exception("MD5加密出现错误");
	    }
	}	
	
	public static String getVersion() {
		try {
			PackageManager pm = MyApplication.instance.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(MyApplication.instance.getPackageName(),PackageManager.GET_CONFIGURATIONS);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static boolean checkVersionIsSame(String version) {
		String versionName = getVersion();
		if (TextUtils.isEmpty(version) || TextUtils.isEmpty(versionName)) {
			return true;
		} else {
			if (!version.equals(versionName)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 获取手机电话号码
	 */
	public static String getPhoneNumber() {
		TelephonyManager tm = (TelephonyManager)MyApplication.instance.getSystemService(Context.TELEPHONY_SERVICE);  
		String deviceid = tm.getDeviceId();//获取智能设备唯一编号  
		String te1  = tm.getLine1Number();//获取本机号码  
		String imei = tm.getSimSerialNumber();//获得SIM卡的序号  
		String imsi = tm.getSubscriberId();//得到用户Id
		
		return te1;
	}
	
	/**
	 * 获取手机安卓id
	 * @param context
	 * @return
	 */
	public static String getAndroidId(Context context) {
		String m_szAndroidID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		return m_szAndroidID; 
	}
	
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * 获取mac地址
	 * 
	 * @return
	 */
	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}
	
	/**
	 * 获得手机总内存
	 * 
	 * @return
	 */
	public static String getmemTotal() {
		long mTotal;
		// /proc/meminfo读出的内核信息进行解释
		String path = "/proc/meminfo";
		String content = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path), 8);
			String line;
			if ((line = br.readLine()) != null) {
				content = line;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// beginIndex
		int begin = content.indexOf(':');
		// endIndex
		int end = content.indexOf('k');
		// 截取字符串信息

		content = content.substring(begin + 1).trim();
		return content;
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @param
	 * @return
	 */
	public static int getScreenWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @param
	 * @return
	 */
	public static int getScreenHeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * 计算控件宽度高度
	 * 注：RelativeLayout不适用（空指针）
	 * @param view
	 */
	public static void measureView(View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
	}
	
	/**
	 * 得到系统状态栏高度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getSystemStatusHeight(Activity activity) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = activity.getResources().getDimensionPixelSize(x);
			return sbar;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return 0;
	}

	/**
	 * 格式化电话号码：133***09090
	 * 
	 * @param phone
	 */
	public static String getPhoneFormat(String phone) {
		int length = phone.length();
		String newPhone = phone.substring(0, 3) + "****" + phone.substring(length - 4, length);
		return newPhone;
	}
	
	/**
	 * 获取字符字节长度
	 * @param s
	 * @return
	 */
	public static int getWordCount(String s) {  
        int length = 0;  
        for(int i = 0; i < s.length(); i++)  
        {  
            int ascii = Character.codePointAt(s, i);  
            if(ascii >= 0 && ascii <=255)  
                length++;  
            else  
                length += 2;  
                  
        }  
        return length;   
    }
	
	public static boolean isEmpty(String str) {
		if ("null".equals(str)) {
			return true;
		}
		return TextUtils.isEmpty(str);
	}
	
	/**
	 * 判断坐标是否合法
	 * @param str
	 * @return
	 */
	public static boolean isLocationEmpty(String str) {
		if ("null".equals(str)) {
			return true;
		}
		if ("0".equals(str)) {
			return true;
		}
		if ("0.0".equals(str)) {
			return true;
		}		
		return TextUtils.isEmpty(str);
	}	
	
	/**
	 * 获取首字母
	 * 
	 * @param str
	 * @return
	 */
	public static String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式匹配
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(); // 将小写字母转换为大写
		} else {
			return "#";
		}
	}
	 
	public static String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) MyApplication.instance.getSystemService(Context.ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = MyApplication.instance.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}   
	
	public static String getMsgStr(int msgCount) {
		if (msgCount >= 100 ) {
			return "99+";
		}
		return msgCount + "";
	}
	
	public static String getDistance(int distance) {
		float formDistance = distance / 1000.0f;
		if(formDistance < 1) {
			return getFromXiaoShu((formDistance * 1000)+"",2) + "米";
		} else {
			return getFromXiaoShu(formDistance+"",2) + "千米";
		}
	}
	
	public static String getDuration(int duration) {
		float formDuration = duration / 3600.0f;
		if(formDuration < 1) {
			return getFromXiaoShu((formDuration * 60)+"",2) + "分钟";
		} else {
			return getFromXiaoShu(formDuration+"",2) + "小时";
		}
	}
	
	public static String getFromXiaoShu(String str,int count) {
		if (isEmpty(str)) {
			return "";
		}
		if (str.contains(".")) {
			if (str.length() < (str.indexOf(".") + count)) {
				return str;
			}
			return str.substring(0,str.indexOf(".") + count);
		}
		return str;
	}
	
	public static String getFormedTime(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date(time);
		return sdf.format(date);
	}
	
    public static String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }	
    
    public static String getSaveDir() {
    	return getSdcardDir()+File.separator+AppContext.APP_NAME;
    }
    
    private static final String DATE_FORMAT = "ddMM月";
    /** 
     * 日期转换为字符串 
     * @param timeStr 
     * @param format 
     * @return 
     */  
    public static SpannableStringBuilder dateToString(String timeStr, String format) {  
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String str = sdf.format(new Date(Long.valueOf(timeStr)));
        SpannableStringBuilder spannable=new SpannableStringBuilder(str);
        AbsoluteSizeSpan span=new AbsoluteSizeSpan(dip2px(MyApplication.instance.getApplicationContext(), 16)); 
		spannable.setSpan(span, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		AbsoluteSizeSpan span1=new AbsoluteSizeSpan(dip2px(MyApplication.instance.getApplicationContext(), 12)); 
		spannable.setSpan(span1, 2, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }
    
    public static SpannableStringBuilder getMyZongTime(String dateStr) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.get(Calendar.DAY_OF_MONTH); 
    	long now = calendar.getTimeInMillis(); 
    	long past = Long.valueOf(dateStr);
    	
    	SpannableStringBuilder spannable = null;
    	// 相差的秒数  
        long time = (now - past) / 1000;
        StringBuffer sb = new StringBuffer();  
        if (time >= 0 && time < 3600 * 24) {
        	spannable = new SpannableStringBuilder(sb.append("今天").toString());
            return spannable;  
        }else if (time >= 3600 * 24 && time < 3600 * 48) { 
        	spannable = new SpannableStringBuilder(sb.append("昨天").toString());
            return spannable;  
        }else if (time >= 3600 * 48 && time < 3600 * 72) { 
        	spannable = new SpannableStringBuilder(sb.append("前天").toString());
        	return spannable;  
        }else if (time >= 3600 * 72) {  
            return dateToString(dateStr, DATE_FORMAT);  
        }  
    	return dateToString(dateStr, DATE_FORMAT);
    }
    
    public static String getFormTime(String dateStr) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.get(Calendar.DAY_OF_MONTH); 
    	long now = calendar.getTimeInMillis(); 
    	long past = Long.valueOf(dateStr);
    	
    	// 相差的秒数  
        long time = (now - past) / 1000;
        StringBuffer sb = new StringBuffer();  
        if (time > 0 && time < 60) { // 1小时内  
            return sb.append(time + "秒前").toString();  
        } else if (time > 60 && time < 3600) {  
            return sb.append(time / 60+"分钟前").toString();  
        } else if (time >= 3600 && time < 3600 * 24) {  
            return sb.append(time / 3600 +"小时前").toString();  
        }else if (time >= 3600 * 24 && time < 3600 * 48) {  
            return sb.append("昨天").toString();  
        }else if (time >= 3600 * 48 && time < 3600 * 72) {  
            return sb.append("前天").toString();  
        }else if (time >= 3600 * 72) {  
            return "";  
        }  
    	return "";
    }
    
    /**
     * 获得平均数，并且保留小数后两位数字
     * @param countData
     * @param count
     * @return
     */
    public static double getAreaData(double sumData,int count) {
    	if (count == 0) {
			return 0;
		}
    	double areaData = (sumData / count) + 0.00;
    	String data = areaData + "";
    	return Double.valueOf(getFromXiaoShu(data, 3));
    }
    
    /**
     * 保存图片到本地
     * @param mContext
     * @param bm
     * @param fileName
     * @param subForder
     * @throws IOException
     */
    public static void saveFile(Context mContext,Bitmap bm, String fileName, String subForder) throws IOException {
    	File foder = new File(subForder);
    	if (!foder.exists()) {
    		foder.mkdirs();
    	}
    	File myCaptureFile = new File(subForder, fileName);
    	if (!myCaptureFile.exists()) {
    		myCaptureFile.createNewFile();
    	}
    	BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
    	bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
    	bos.flush();
    	bos.close();
    	
    	Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    	Uri uri = Uri.fromFile(myCaptureFile);
    	intent.setData(uri);
    	mContext.sendBroadcast(intent);
    }
    
	public static int getBuma(int data) throws Exception {
		if (data > 255 || data < 0) {
			throw new Exception("数据格式有误:"+data);
		}
		if (data > 127) {
			return  (data - 256);			
		} 
		return data;
	}
	
	public static int getLastData(short[] data) throws Exception {
    	int sum = 0;
    	int length = data.length;
    	if (length < 3) {
    		throw new Exception("数据格式有误:"+length);
		}
    	for(int i = 3 ; i < data[2] + 3 ; i ++) {
    		sum = sum + data[i];
    	}
    	sum = (255 - (sum%256));
    	return getBuma(sum);
	}
	
	public static String buildTransaction() {
		return System.currentTimeMillis() + "jihua";
	}
	
	 public static String decodeUnicode(String theString) {    
	     char aChar;    
	      int len = theString.length();    
	     StringBuffer outBuffer = new StringBuffer(len);    
	     for (int x = 0; x < len;) {    
	      aChar = theString.charAt(x++);    
	      if (aChar == '\\') {    
	       aChar = theString.charAt(x++);    
	       if (aChar == 'u') {    
	        // Read the xxxx    
	        int value = 0;    
	        for (int i = 0; i < 4; i++) {    
	         aChar = theString.charAt(x++);    
	         switch (aChar) {    
	         case '0':    
	         case '1':    
	         case '2':    
	         case '3':    
	        case '4':    
	         case '5':    
	          case '6':    
	           case '7':    
	           case '8':    
	           case '9':    
	            value = (value << 4) + aChar - '0';    
	            break;    
	           case 'a':    
	           case 'b':    
	           case 'c':    
	           case 'd':    
	           case 'e':    
	           case 'f':    
	            value = (value << 4) + 10 + aChar - 'a';    
	           break;    
	           case 'A':    
	           case 'B':    
	           case 'C':    
	           case 'D':    
	           case 'E':    
	           case 'F':    
	            value = (value << 4) + 10 + aChar - 'A';    
	            break;    
	           default:    
	            throw new IllegalArgumentException(    
	              "Malformed   \\uxxxx   encoding.");    
	           }    
	  
	         }    
	          outBuffer.append((char) value);    
	         } else {    
	          if (aChar == 't')    
	           aChar = '\t';    
	          else if (aChar == 'r')    
	           aChar = '\r';    
	          else if (aChar == 'n')    
	           aChar = '\n';    
	          else if (aChar == 'f')    
	           aChar = '\f';    
	          outBuffer.append(aChar);    
	         }    
	        } else   
	        outBuffer.append(aChar);    
	       }    
	       return outBuffer.toString();    
	      } 
	 
	 public static String getCurProcessName(Context context) {
		 int pid = android.os.Process.myPid();
		 ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		 for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
			 if (appProcess.pid == pid) {
			   return appProcess.processName;
			 }
		 }
		 return null;
	}
	 
     //从InputStream中读取数据，转换成byte数组，最后关闭InputStream
     public static byte[] getBytesByInputStream(InputStream is) {
         byte[] bytes = null;
         BufferedInputStream bis = new BufferedInputStream(is);
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         BufferedOutputStream bos = new BufferedOutputStream(baos);
         byte[] buffer = new byte[1024 * 8];
         int length = 0;
         try {
             while ((length = bis.read(buffer)) > 0) {
                 bos.write(buffer, 0, length);
             }
             bos.flush();
             bytes = baos.toByteArray();
         } catch (IOException e) {
             e.printStackTrace();
         } finally {
             try {
                 bos.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
             try {
                 bis.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }

         return bytes;
     }	 
     
     //读取响应头
     public static String getResponseHeader(HttpURLConnection conn) {
         Map<String, List<String>> responseHeaderMap = conn.getHeaderFields();
         int size = responseHeaderMap.size();
         StringBuilder sbResponseHeader = new StringBuilder();
         for(int i = 0; i < size; i++){
             String responseHeaderKey = conn.getHeaderFieldKey(i);
             String responseHeaderValue = conn.getHeaderField(i);
             sbResponseHeader.append(responseHeaderKey);
             sbResponseHeader.append(":");
             sbResponseHeader.append(responseHeaderValue);
             sbResponseHeader.append("\n");
         }
         return sbResponseHeader.toString();
     }
     
 	/**
 	 * 获取图片Uri
 	 * @param filePath
 	 * @param context
 	 * @return
 	 */
 	public static Uri getTempUri(String filePath,Context context) {
 		if (checkSdcard(context)) {
 			File file = new File(filePath);
 			if (!file.getParentFile().exists()) {
 				file.getParentFile().mkdirs();
 			}
 			if (!file.exists()) {
 				try {
 					file.createNewFile();
 				} catch (IOException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			}
 			return Uri.fromFile(file);
 		} 
 		return null;
 	}
 	
	/**
	 * 检查sd卡
	 * @param context
	 * @return
	 */
	public static boolean checkSdcard(Context context) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			Toast.makeText(context, "请插入sd卡", Toast.LENGTH_LONG).show();
			return false;
		}
	} 	
}
