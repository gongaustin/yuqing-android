package com.app.yuqing.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.app.yuqing.AppContext;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class PictureUtils {
	private static PictureUtils instance;

	private PictureUtils() {

	}

	public static PictureUtils instance() {
		if (instance == null)
			instance = new PictureUtils();
		return instance;
	}
	
	/**
	 * 压缩像素，而不降低图片质量
	 * @param fromFilePath
	 * @return
	 */
	@SuppressLint("NewApi")
	public String compressFileToFile(String fromFilePath) {
		String toFilePath = preparePicturePathForJPG();
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;//只读边,不读内容 
		Bitmap bitmap = BitmapFactory.decodeFile(fromFilePath, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        float hh = 1080f;//  
        float ww = 720f;//  
        int be = 1;  
        if (w > h && w > ww) {  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {  
            be = (int) (newOpts.outHeight / hh);  
        }
        if (be <= 0)  
            be = 1;
        newOpts.inSampleSize = be;//设置采样率  
          
        newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设  
        newOpts.inPurgeable = true;// 同时设置才会有效  
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收
        
        bitmap = BitmapFactory.decodeFile(fromFilePath, newOpts); 
//        int compressOption = (int) (100f / be);
        int compressOption = 80;
//        System.out.println("压缩测试   文件图片压缩： 采样率 "+be+" 质量压缩比例 "+compressOption);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressOption, baos);
        System.out.println("压缩测试   文加压缩之质量压缩： 压缩之前大小 "+bitmap.getByteCount()+" 压缩之后大小 "+baos.toByteArray().length);
        FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(toFilePath));
			fos.write(baos.toByteArray(), 0, baos.toByteArray().length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "";
				}
			}
		}
		baos.reset();
		return toFilePath;
	}
	
	/**
	 * 压缩图片质量，而不压缩图片像素
	 * @param filePath
	 * @return
	 */
	public String compress(String filePath) {
		String filepath = preparePicturePathForJPG();
		Bitmap bp = BitmapFactory.decodeFile(filePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int count = (baos.toByteArray().length) / 1024;
		if(count > AppContext.PICTURE_SIZE) {
			int options = getOpitons(count);
			System.out.println("压缩之前大小:"+baos.toByteArray().length);
			baos.reset();
			bp.compress(Bitmap.CompressFormat.JPEG, options, baos);
			System.out.println("压缩之后大小:"+baos.toByteArray().length);
		}
		bp.recycle();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(filepath));
			fos.write(baos.toByteArray(), 0, baos.toByteArray().length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "";
				}
			}
		}
		baos.reset();
		System.gc();
		return filepath;
	}
	
	/**
	 * 将bitmap压缩百分之八十的质量，不压缩像素
	 * @param bp
	 * @return
	 */
	@SuppressLint("NewApi")
	public String compressBitmapToFile(Bitmap bp) {
		String toFilePath = preparePicturePathForJPG();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bp.compress(Bitmap.CompressFormat.PNG, 100, baos);
		System.out.println("压缩测试   画布压缩之质量压缩： 压缩之前大小 "+bp.getByteCount()+" 压缩之后大小 "+baos.toByteArray().length);
		bp.recycle();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(toFilePath));
			fos.write(baos.toByteArray(), 0, baos.toByteArray().length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "";
				}
			}
		}
		baos.reset();
		return toFilePath;
	}
	
	/**
	 * 压缩图片
	 * @param bp
	 * @return
	 */
	public String compress(Bitmap bp) {
		String filepath = preparePicturePathForPNG();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int count = (baos.toByteArray().length) / 1024;
		if(count > AppContext.PICTURE_SIZE) {
			int options = getOpitons(count);
			System.out.println("压缩之前大小:"+baos.toByteArray().length);
			baos.reset();
			bp.compress(Bitmap.CompressFormat.JPEG, options, baos);
			System.out.println("压缩之后大小:"+baos.toByteArray().length);
		}
		bp.recycle();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(filepath));
			fos.write(baos.toByteArray(), 0, baos.toByteArray().length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "";
				}
			}
		}
		baos.reset();
		System.gc();
		return filepath;
		
	}
	
	/**
	 * 压缩jpg图片的保存地址
	 * @return
	 */
	private String preparePicturePathForJPG() {
		String filename = CommonUtils.getFormedTime(System.currentTimeMillis()) + ".jpg";
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + 
				AppContext.SAVE_PATH;
		File pathFile = new File(path);
		if(!pathFile.exists()) {
			pathFile.mkdirs();
		}
		return path + "/" + filename;
	}
	
	/**
	 * 压缩png图片的保存地址
	 * @return
	 */
	private String preparePicturePathForPNG() {
		String filename = CommonUtils.getFormedTime(System.currentTimeMillis()) + ".png";
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + 
				AppContext.SAVE_PATH;
		File pathFile = new File(path);
		if(!pathFile.exists()) {
			pathFile.mkdirs();
		}
		return path + "/" + filename;
	}	
	
	/**
	 * 为每次拍照获取照片做准备
	 */
	public String prepareForTakePicture() {
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + 
				AppContext.TAKEPICTURE_PATH;
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
		}
		String filePathString = path + "/" +AppContext.TAKEPICTURE_FILE;
		File tempFile = new File(filePathString);
		if(tempFile.exists() && tempFile.isFile()) {
			tempFile.delete();
		}
		return filePathString;
	}
	
	/**
	 * 获取拍照图片缓存地址
	 * @return
	 */
	public String getUriPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + 
				AppContext.TAKEPICTURE_PATH + File.separator +AppContext.TAKEPICTURE_FILE;
	}
	
	public String getUriPath(String path) {
		if (!path.startsWith("file:"+File.separator+File.separator)) {
			path = "file:"+File.separator+File.separator+path;
		}
		return path;
	}
	
	private int getOpitons(int count) {
		int percent = (AppContext.PICTURE_SIZE * 100) / count;
		if(percent > 5) {
			percent = percent - 5;
		}
		return percent;
	}
}
