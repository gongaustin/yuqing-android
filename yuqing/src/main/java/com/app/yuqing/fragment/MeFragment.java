package com.app.yuqing.fragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.yuqing.AppContext;
import com.app.yuqing.R;
import com.app.yuqing.activity.AboutActivity;
import com.app.yuqing.activity.ChangePhoneNumberActivity;
import com.app.yuqing.activity.ChangePwdActivity;
import com.app.yuqing.activity.LoginActivity;
import com.app.yuqing.activity.MainActivity;
import com.app.yuqing.bean.UserBean;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.UserDetailResponseBean;
import com.app.yuqing.net.bean.UserResponseBean;
import com.app.yuqing.utils.CommonUtils;
import com.app.yuqing.utils.ImageLoaderUtil;
import com.app.yuqing.utils.ImageUtil;
import com.app.yuqing.utils.PictureUtils;
import com.app.yuqing.utils.PreManager;
import com.app.yuqing.view.BaseDialog.DialogListener;
import com.app.yuqing.view.ChangeHeadDialog;
import com.app.yuqing.view.ChangeHeadDialog.ChangeHeadDialogListener;
import com.app.yuqing.view.ExitDialog;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MeFragment extends BaseFragment {

	@ViewInject(R.id.iv_bg)
	private ImageView ivBG;
	@ViewInject(R.id.iv_head)
	private ImageView ivHead;
	@ViewInject(R.id.tv_username)
	private TextView tvUserName;
	@ViewInject(R.id.tv_role)
	private TextView tvRole;	
	@ViewInject(R.id.tv_number)
	private TextView tvNumber;	
	@ViewInject(R.id.rl_pwd)
	private RelativeLayout rlPwd;	
	@ViewInject(R.id.rl_phone)
	private RelativeLayout rlPhone;
	@ViewInject(R.id.rl_version)
	private RelativeLayout rlVersion;
	@ViewInject(R.id.rl_about)
	private RelativeLayout rlAbout;
	@ViewInject(R.id.tv_version)
	public TextView tvVersion;
	@ViewInject(R.id.rl_exit)
	private RelativeLayout rlExit;
	
	private ChangeHeadDialog dialog;
	private ExitDialog exitDialog;
	
	private static final int CODE_IMAGE_CAPTURE = 0x11;
	private static final int SELECT_PIC = 0x13;
	private static final int CROP_IMAGE = 0x14;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_me);
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initListener();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		getData();
	}

	private void getData() {
		UserResponseBean bean = PreManager.get(getActivity().getApplicationContext(), AppContext.KEY_LOGINUSER, UserResponseBean.class);
		if (bean != null && bean.getUser() != null && !TextUtils.isEmpty(bean.getUser().getId())) {
			pushEventNoProgress(EventCode.HTTP_QUERYUSERBYID, bean.getUser().getId());
		}

	}

	private void refreshView(UserBean bean) {
		if (bean != null) {
			if (!TextUtils.isEmpty(bean.getPhoto())) {
				ImageLoaderUtil.display(bean.getPhoto(),ivBG);
				ImageLoaderUtil.display(bean.getPhoto(),ivHead);
			} else {
				ivHead.setImageResource(R.drawable.rc_default_portrait);
			}
			if (!TextUtils.isEmpty(bean.getName())) {
				tvUserName.setText(bean.getName());
			}
			if (!TextUtils.isEmpty(bean.getMobile())) {
				tvNumber.setText(bean.getMobile());
			}
			if (!TextUtils.isEmpty(bean.getRoleNames())) {
				tvRole.setText(bean.getRoleNames());
			}
		}
	}
	
	private void initListener() {
		ivHead.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (dialog == null) {
					dialog = new ChangeHeadDialog(getActivity());
				}
				dialog.setListener(new ChangeHeadDialogListener() {
					
					@Override
					public void pic() {
						selectPicFromLocal();
					}
					
					@Override
					public void capture() {
						selectPicFromCamera();
					}
				});
				dialog.show();
			}
		});
		
		rlAbout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),AboutActivity.class);
				startActivity(intent);
			}
		});
		
		rlVersion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((MainActivity)getActivity()).getData();
				String clientID = PreManager.getClientId(getActivity().getApplicationContext());
				if (!TextUtils.isEmpty(clientID)) {
					pushEventBlock(EventCode.HTTP_GETUITEST,clientID);
				}

			}
		});
		
		rlPwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),ChangePwdActivity.class);
				startActivity(intent);
			}
		});
		
		rlPhone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),ChangePhoneNumberActivity.class);
				startActivity(intent);
			}
		});	
		
		rlExit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (exitDialog == null) {
					exitDialog = new ExitDialog(getActivity());
				}
				exitDialog.setListener(new DialogListener() {
					
					@Override
					public void update(Object object) {
						String result = (String) object;
						if ("true".equals(result)) {
							Intent intent = new Intent(getActivity(),LoginActivity.class);
							startActivity(intent);
							getActivity().finish();
						}
					}
				});
				exitDialog.show();
			}
		});
	}
	
	private File headFile;
    /**
     * 照相获取图片
     */
    protected void selectPicFromCamera() {
		Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri uri = CommonUtils.getTempUri(PictureUtils.instance().getUriPath(), (MainActivity)getActivity());
		if (uri != null) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(intent, CODE_IMAGE_CAPTURE);
		} else {
			showToast("调取相机失败");
		}
    }

    /**
     * 从图库获取图片
     */
    protected void selectPicFromLocal() {
		Intent intent2 =new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent2, SELECT_PIC); 
    }    
	
	/**
	 * 获取裁剪之后的图片数据
	 * 
	 * @param data
	 */
	private InputStream getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			try {
				Bitmap photo = extras.getParcelable("data");
				ivHead.setImageBitmap(photo);
				ivBG.setImageBitmap(photo);
				return ImageUtil.bitmap2InputStream(photo, 90);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}	
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (Activity.RESULT_OK != resultCode) {
			return;
		}
		switch(requestCode) {
		case SELECT_PIC:
			if(data == null) {
				showToast("获取图片失败");
				return;
			}
			Uri selectedImage = data.getData();
			String[] filePathColumns = { MediaStore.Images.Media.DATA };
	        Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
	        c.moveToFirst();
	        int columnIndex = c.getColumnIndex(filePathColumns[0]);
	        String picturePath = c.getString(columnIndex);
	        startPhotoZoom(Uri.parse(PictureUtils.instance().getUriPath(picturePath)));
			break;
		
		case CROP_IMAGE:
			if (data != null) {
				InputStream tmpPhoto = getImageToView(data);
				if (tmpPhoto != null) {
					File file = ImageUtil.inputStreamToFile(tmpPhoto);
					headFile = file;
					pushEvent(EventCode.HTTP_UPDATEHEAD, headFile);
				}
			}
			break;
			
		case CODE_IMAGE_CAPTURE:
			if(data == null) {
				try {
				     String caputrePicturePath = PictureUtils.instance().getUriPath();
				     System.out.println("path:"+caputrePicturePath);
				     String resultpath = PictureUtils.instance().compressFileToFile(caputrePicturePath);
				     startPhotoZoom(Uri.parse(PictureUtils.instance().getUriPath(resultpath)));
				} catch (Exception e) {
					// TODO: handle exception
					showToast("获取头像失败");
					e.printStackTrace();
				}
				 
			} else {
				Uri uri = data.getData();
				if(uri == null){  
			    	 System.out.println("拍照测试，返回isnull");
			    	 Bundle bundle = data.getExtras();    
			    	 if (bundle != null) {                 
			    		 Bitmap  photo = (Bitmap) bundle.get("data");  
			    		 String path = PictureUtils.instance().compressBitmapToFile(photo);
			    		 startPhotoZoom(Uri.parse(PictureUtils.instance().getUriPath(path)));
			    	 }   
			       }else{
			    	   System.out.println(uri.toString());
				       String[] cameraKeys = { MediaStore.Images.Media.DATA };
				       Cursor cur = getActivity().getContentResolver().query(uri, cameraKeys, null, null, null);
				       cur.moveToFirst();
				       int index = cur.getColumnIndex(cameraKeys[0]);
				       String filePath = cur.getString(index);
				       System.out.println("path:"+filePath);
				       String path = PictureUtils.instance().compressFileToFile(filePath);
				       System.out.println("最终路径："+path);
				       cur.close();
				       startPhotoZoom(Uri.parse(PictureUtils.instance().getUriPath(path)));
			       }   
			}
			break;
			
		default:
			super.onActivityResult(requestCode,resultCode,data);
		}    	
    }
    
	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		startActivityForResult(intent, CROP_IMAGE);
	}

	@Override
	public void onEventRunEnd(Event event) {
		super.onEventRunEnd(event);
		if (event.getEventCode() == EventCode.HTTP_QUERYUSERBYID) {
			if (event.isSuccess()) {
				UserDetailResponseBean bean = (UserDetailResponseBean) event.getReturnParamAtIndex(0);
				if (bean.getData() != null) {
					refreshView(bean.getData());
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
	}
}
