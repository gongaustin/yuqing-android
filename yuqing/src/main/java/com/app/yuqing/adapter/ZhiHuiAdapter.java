package com.app.yuqing.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.yuqing.R;
import com.app.yuqing.bean.MenuBean;
import com.app.yuqing.bean.ZhiHuiType;

public class ZhiHuiAdapter extends SetBaseAdapter<MenuBean>{

	public ZhiHuiAdapter(Context context, List<MenuBean> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_zhihui, null);
			holder = new ViewHolder();
			holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MenuBean bean = mList.get(position);
		if (!TextUtils.isEmpty(bean.getName())) {
			holder.tvName.setText(bean.getName());
		}
		if (!TextUtils.isEmpty(bean.getName())) {
			if (bean.getName().equals(ZhiHuiType.BL.getType())) {
				holder.ivIcon.setImageResource(R.drawable.icon_jx);
			}
			if (bean.getName().equals(ZhiHuiType.JX.getType())) {
				holder.ivIcon.setImageResource(R.drawable.icon_jx);
			}
			if (bean.getName().equals(ZhiHuiType.SB.getType())) {
				holder.ivIcon.setImageResource(R.drawable.icon_sb);
			}
			if (bean.getName().equals(ZhiHuiType.XP.getType())) {
				holder.ivIcon.setImageResource(R.drawable.icon_xp);
			}
			if (bean.getName().equals(ZhiHuiType.YJZH.getType())) {
				holder.ivIcon.setImageResource(R.drawable.icon_yjzh);
			}
		}
		return convertView;
	}
	
	private class ViewHolder {
		private ImageView ivIcon;
		private TextView tvName;
	}

}
