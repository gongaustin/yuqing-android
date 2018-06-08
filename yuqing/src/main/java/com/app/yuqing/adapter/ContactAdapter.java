package com.app.yuqing.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.yuqing.R;
import com.app.yuqing.bean.UserBean;
import com.app.yuqing.utils.ImageLoaderUtil;
import com.app.yuqing.view.RoundImageView;

public class ContactAdapter extends SetBaseAdapter<UserBean>{

	public ContactAdapter(Context context, List<UserBean> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contact, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.ivHead = (RoundImageView) convertView.findViewById(R.id.iv_head);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		UserBean bean = mList.get(position);
		holder.tvName.setText(bean.getRealname()+"("+bean.getDeptName()+")");
		if (!TextUtils.isEmpty(bean.getAvatar())) {
			ImageLoaderUtil.display(bean.getAvatar(), holder.ivHead);
		} else {
			holder.ivHead.setImageResource(R.drawable.rc_default_portrait);
		}
		return convertView;
	}
	
	private class ViewHolder {
		private TextView tvName;
		private RoundImageView ivHead;
	}

}
