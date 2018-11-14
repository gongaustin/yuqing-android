package com.app.yuqing.fragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.yuqing.AppContext;
import com.app.yuqing.MyApplication;
import com.app.yuqing.R;
import com.app.yuqing.activity.AboutActivity;
import com.app.yuqing.activity.ChangePhoneNumberActivity;
import com.app.yuqing.activity.ChangePwdActivity;
import com.app.yuqing.activity.LoginActivity;
import com.app.yuqing.activity.MainActivity;
import com.app.yuqing.bean.PersonalBean;
import com.app.yuqing.bean.UserOldBean;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.BaseResponseBean;
import com.app.yuqing.net.bean.PersonalInfoResponseBean;
import com.app.yuqing.net.bean.UploadFileResponseBean;
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

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;
import pub.devrel.easypermissions.EasyPermissions;

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

	private ExitDialog exitDialog;

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
		pushEventNoProgress(EventCode.HTTP_PERSONALINFO);

	}

	private void refreshView(PersonalBean bean) {
		if (bean != null) {
			if (!TextUtils.isEmpty(bean.getAvatar())) {
				ImageLoaderUtil.display(bean.getAvatar(),ivBG);
				ImageLoaderUtil.display(bean.getAvatar(),ivHead);
			} else {
				ivHead.setImageResource(R.drawable.rc_default_portrait);
			}
			if (!TextUtils.isEmpty(bean.getRealname())) {
				tvUserName.setText(bean.getRealname());
			}
			if (!TextUtils.isEmpty(bean.getPhone())) {
				tvNumber.setText(bean.getPhone());
			}
			if (!TextUtils.isEmpty(bean.getDeptName())) {
				tvRole.setText(bean.getDeptName());
			}
		}
	}
	
	private void initListener() {
		ivHead.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				choicePhotoWrapper("test");
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
							PreManager.putString(getActivity().getApplicationContext(),AppContext.USER_KEY,"");
							MyApplication.instance.exit();
						}
					}
				});
				exitDialog.show();
			}
		});
	}
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (Activity.RESULT_OK != resultCode) {
			return;
		}
		switch(requestCode) {
			case REQUEST_CODE_CHOOSE_PHOTO :
				if (BGAPhotoPickerActivity.getSelectedImages(data) != null
						&& BGAPhotoPickerActivity.getSelectedImages(data).size() > 0) {
					if (!TextUtils.isEmpty(BGAPhotoPickerActivity.getSelectedImages(data).get(0))) {
						File file = new File(BGAPhotoPickerActivity.getSelectedImages(data).get(0));
						if (!file.exists()) {
							file.getParentFile().mkdirs();
							try {
								file.createNewFile();
								pushEventBlock(EventCode.HTTP_UPLOADFILE, file);
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							pushEventBlock(EventCode.HTTP_UPLOADFILE, file);
						}
					}
				}
				break;
			
		default:
			super.onActivityResult(requestCode,resultCode,data);
		}    	
    }

	@Override
	public void onEventRunEnd(Event event) {
		super.onEventRunEnd(event);
		if (event.getEventCode() == EventCode.HTTP_PERSONALINFO) {
			if (event.isSuccess()) {
				PersonalInfoResponseBean bean = (PersonalInfoResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null && bean.getData() != null) {
					PreManager.put(getActivity().getApplicationContext(),AppContext.KEY_LOGINUSER,bean.getData());
					refreshView(bean.getData());
				}

			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}

		if (event.getEventCode() == EventCode.HTTP_UPLOADFILE) {
			if (event.isSuccess()) {
				UploadFileResponseBean bean = (UploadFileResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null
						&& bean.getData() != null
						&& !TextUtils.isEmpty(bean.getData().getUrl())) {
					pushEventBlock(EventCode.HTTP_MODIFYAVATAR,bean.getData().getUrl());
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}

		if (event.getEventCode() == EventCode.HTTP_MODIFYAVATAR) {
			if (event.isSuccess()) {
				String url = (String) event.getReturnParamAtIndex(1);
				if (!TextUtils.isEmpty(url)) {
					ImageLoaderUtil.display(url,ivHead);
					ImageLoaderUtil.display(url,ivBG);
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
	}

	//处理拍照问题

	private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;

	private static final int REQUEST_CODE_CHOOSE_PHOTO = 2;

	private void choicePhotoWrapper(String tag){
		String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
		if (EasyPermissions.hasPermissions(getActivity(), perms)) {
			// 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
			File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");
			startActivityForResult(BGAPhotoPickerActivity.newIntent(getActivity(), takePhotoDir, 1, null, false), REQUEST_CODE_CHOOSE_PHOTO);

		} else {
			EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
		}
	}
}
