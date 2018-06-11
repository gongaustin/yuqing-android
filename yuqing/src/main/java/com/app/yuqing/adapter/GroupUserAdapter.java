package com.app.yuqing.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.yuqing.R;
import com.app.yuqing.bean.GroupMemberBean;
import com.app.yuqing.bean.GroupUserBean;
import com.app.yuqing.bean.MenuBean;
import com.app.yuqing.bean.ZhiHuiType;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.utils.ImageLoaderUtil;
import com.app.yuqing.view.RoundImageView;

public class GroupUserAdapter extends SetBaseAdapter<GroupMemberBean>{

	public GroupUserAdapter(Context context, List<GroupMemberBean> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_grid, null);
			holder = new ViewHolder();
			holder.ivHead = (RoundImageView) convertView.findViewById(R.id.iv_head);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		GroupMemberBean bean = mList.get(position);
		if (!TextUtils.isEmpty(bean.getRealname())) {
			holder.tvName.setText(bean.getRealname()+"("+bean.getDeptName()+")");
		}
		
		if (TextUtils.isEmpty(bean.getUserid())) {
			holder.ivHead.setImageResource(R.drawable.icon_add);
			holder.tvName.setText(bean.getRealname());
			holder.tvName.setTextColor(Color.BLUE);
		} else if (!TextUtils.isEmpty(bean.getAvatar())) {
			ImageLoaderUtil.display(bean.getAvatar(), holder.ivHead);
		} else {
			holder.ivHead.setImageResource(R.drawable.rc_default_group_portrait);
		}
		return convertView;
	}
	
	private class ViewHolder {
		private RoundImageView ivHead;
		private TextView tvName;
	}

}
