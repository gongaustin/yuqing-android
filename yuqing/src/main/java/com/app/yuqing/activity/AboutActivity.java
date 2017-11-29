package com.app.yuqing.activity;

import com.app.yuqing.R;
import com.app.yuqing.utils.CommonUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends BaseActivity {

	@ViewInject(R.id.tv_version)
	private TextView tvVersion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addBack(R.id.rl_back);
		setTitle("关于");
		String version = CommonUtils.getVersion();
		tvVersion.setText("当前版本："+version);
	}
	
	
}
