package com.app.yuqing.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ImageLoaderUtil {
	private static DisplayImageOptions options;

	private static void setOptions(int defaultPic, BitmapDisplayer displayer) {
		options = new DisplayImageOptions.Builder().showImageOnLoading(defaultPic).showImageForEmptyUri(defaultPic).showImageOnFail(defaultPic)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY)
				.displayer(displayer).build();
	}

	private static void setOptions(int defaultPic) {
		options = new DisplayImageOptions.Builder().showImageOnLoading(defaultPic).showImageForEmptyUri(defaultPic).showImageOnFail(defaultPic)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY)
				.build();
	}

	private static DisplayImageOptions noCacheFadeInResetOptions = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisk(false)
			.considerExifParams(true).resetViewBeforeLoading(true).displayer(new FadeInBitmapDisplayer(300)).bitmapConfig(Bitmap.Config.RGB_565)
			.imageScaleType(ImageScaleType.EXACTLY).build();

	private static DisplayImageOptions noCacheResetOptions = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisk(false).considerExifParams(true)
			.resetViewBeforeLoading(true)// 加载图片之前清除以前设置的图片
			.bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY).build();

	public static DisplayImageOptions resetFadeInOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(300)).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY)
			.build();
	
	public static void disPlayRounded(String uri, ImageView imageView, int defaultPic, int cornerRadiusPixels, ImageLoadingListener listener) {
		setOptions(defaultPic, new RoundedBitmapDisplayer(cornerRadiusPixels));
		ImageLoader.getInstance().displayImage(uri, imageView, options, listener);
	}

	public static void diaPlayNormal(String uri, ImageView imageView, int defaultPic, ImageLoadingListener listener) {
		setOptions(defaultPic);
		ImageLoader.getInstance().displayImage(uri, imageView, options, listener);
	}

	public static void disPlayCustomRounded(String uri, ImageView imageView, int defaultPic, int cornerRadiusPixels, boolean cache,
			ImageLoadingListener listener) {
		RoundedBitmapDisplayer roundedBitmapDisplayer = new RoundedBitmapDisplayer(cornerRadiusPixels);
		DisplayImageOptions optionsnochshwithround = new DisplayImageOptions.Builder().showImageOnLoading(defaultPic).showImageForEmptyUri(defaultPic)
				.showImageOnFail(defaultPic).cacheInMemory(cache).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).resetViewBeforeLoading(true).displayer(roundedBitmapDisplayer).build();
		ImageLoader.getInstance().displayImage(uri, imageView, optionsnochshwithround, listener);
	}

	public static ImageLoader getImageLoader() {
		return ImageLoader.getInstance();
	}

	public static boolean checkImageLoader() {
		return ImageLoader.getInstance().isInited();
	}

	/**
	 * 获取默认的ImageLoader的options
	 * 
	 * @return 返回结果调用build()方法获得options
	 */
	public static DisplayImageOptions.Builder getDefaultOptionsBuilder() {
		return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY);
	}

	/**
	 * 获取没有缓存的ImageLoader的options
	 * 
	 * @return 返回结果调用build()方法获得options
	 */
	public static DisplayImageOptions.Builder getNoCacheOptionsBuilder() {
		return new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisk(false).considerExifParams(true).displayer(new FadeInBitmapDisplayer(300))
				.bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY);
	}

	/**
	 * 获取圆角ImageLoader的options
	 * 
	 * @param cornerRadiusPixels
	 *            圆角半径 单位px
	 * @return 返回结果调用build()方法获得options
	 */
	public static DisplayImageOptions.Builder getRoundedOptionsBuilder(int cornerRadiusPixels) {
		return new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisk(false).considerExifParams(true).displayer(new FadeInBitmapDisplayer(300))
				.bitmapConfig(Bitmap.Config.RGB_565).displayer(new RoundedBitmapDisplayer(cornerRadiusPixels)).imageScaleType(ImageScaleType.EXACTLY);
	}

	public static void display(String uri, ImageView imageView) {
		ImageLoader.getInstance().displayImage(uri, imageView);
	}
	
	public static void display(String uri, ImageView imageView, ImageLoadingListener listener) {
		ImageLoader.getInstance().displayImage(uri, imageView, listener);
	}

	public static void disPlayWithNoCacheReset(String uri, ImageView imageView) {
		ImageLoader.getInstance().displayImage(uri, imageView, noCacheResetOptions);
	}

	public static void disPlayWithNoCacheFadeInReset(String uri, ImageAware imageAware) {
		ImageLoader.getInstance().displayImage(uri, imageAware, noCacheFadeInResetOptions);
	}

	public static void disPlayWithNoCacheFadeInReset(String uri, ImageAware imageAware, ImageLoadingListener listener) {
		ImageLoader.getInstance().displayImage(uri, imageAware, noCacheFadeInResetOptions, listener);
	}
	
	public static void displayWithCache(String uri, ImageView imageView)
	{
		ImageLoader.getInstance().displayImage(uri, imageView, resetFadeInOptions);
	}

	public static void displayWithCache(String uri, ImageView imageView, ImageLoadingListener listener)
	{
		ImageLoader.getInstance().displayImage(uri, imageView, resetFadeInOptions, listener);
	}

	/**
	 * 根据uri获取图片的bitmap
	 * 
	 * @param uri
	 *            图片的uri地址
	 * @param width
	 *            指定图片的最大宽度
	 * @param height
	 *            指定图片的最大高度
	 * @return
	 */
	public static Bitmap getImageBitmap(String uri, int width, int height) {
		// result Bitmap will be fit to this size
		ImageSize targetSize = new ImageSize(width, height);
		return ImageLoader.getInstance().loadImageSync(uri, targetSize);
	}

	/**
	 * 根据图片的高宽来设置imageView的高宽
	 * 
	 * @param uri
	 * @param imageView
	 */
	public static void disPlayByImage(final String uri, final ImageView imageView) {
		// 方法一:
		// ImageLoader.getInstance().loadImage(uri, new ImageSize(400, 400),new
		// SimpleImageLoadingListener(){
		// @Override
		// public void onLoadingComplete(String imageUri, View view, Bitmap
		// loadedImage) {
		// imageView.getLayoutParams().width =
		// CommonUtil.dip2px(imageView.getContext(), loadedImage.getWidth() /
		// 2);
		// imageView.getLayoutParams().height =
		// CommonUtil.dip2px(imageView.getContext(), loadedImage.getHeight() /
		// 2);
		// imageView.setImageBitmap(loadedImage);
		// }
		// });

		// 方法二:
		new AsyncTask<Void, Void, Bitmap>() {
			@Override
			protected void onPreExecute() {
				if (!uri.equals(imageView.getTag())) {
					imageView.setImageBitmap(null);
					imageView.setTag(uri);
				}
			}

			@Override
			protected Bitmap doInBackground(Void... params) {
				Bitmap bt = getImageBitmap(uri, 400, 400);
				return bt;
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				if (result == null)
					return;
				imageView.getLayoutParams().width = CommonUtils.dip2px(imageView.getContext(), result.getWidth() / 2);
				imageView.getLayoutParams().height = CommonUtils.dip2px(imageView.getContext(), result.getHeight() / 2);
				imageView.setImageBitmap(result);
			}
		}.execute();
	}

	public static void clear() {
		ImageLoader.getInstance().clearMemoryCache();
		ImageLoader.getInstance().clearDiskCache();
	}

	public static void clearAll() {
		clear();
		// AnimateFirstDisplayListener.displayedImages.clear();
		// AlbumUtil.clear();
		// PhotoSelectUtil.clear();
	}

	public static void resume() {
		ImageLoader.getInstance().resume();
	}

	/**
	 * 暂停加载
	 */
	public static void pause() {
		ImageLoader.getInstance().pause();
	}

	/**
	 * 停止加载
	 */
	public static void stop() {
		ImageLoader.getInstance().stop();
	}

	/**
	 * 销毁加载
	 */
	public static void destroy() {
		ImageLoader.getInstance().destroy();
	}

	public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
		public List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(view, 300);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
