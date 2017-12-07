package com.app.yuqing.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.yuqing.AppContext;
import com.app.yuqing.R;
import com.app.yuqing.activity.UrlWebClientActivity;
import com.app.yuqing.adapter.ZhiHuiAdapter;
import com.app.yuqing.bean.BacklogBean;
import com.app.yuqing.bean.MenuBean;
import com.app.yuqing.bean.UserBean;
import com.app.yuqing.bean.YQType;
import com.app.yuqing.bean.ZhiHuiType;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.BacklogResponseBean;
import com.app.yuqing.net.bean.MenuResponseBean;
import com.app.yuqing.net.bean.UserResponseBean;
import com.app.yuqing.utils.CommonUtils;
import com.app.yuqing.utils.PreManager;
import com.app.yuqing.view.MyGridView;
import com.app.yuqing.view.NoRightDialog;
import com.lidroid.xutils.view.annotation.ViewInject;

public class WorkFragment extends BaseFragment {
	//快捷处理模块
	@ViewInject(R.id.ll_gallery)
	private LinearLayout mGallery;	
	//应急指挥模块
	@ViewInject(R.id.gv_yingjizhihui)
	private MyGridView mgZhiHui;
	private ZhiHuiAdapter zhAdapter;
	private List<MenuBean> zhList = new ArrayList<MenuBean>();
	//舆情模块
	@ViewInject(R.id.tv_middle1)
	private TextView tvMiddle1;
	@ViewInject(R.id.tv_middle2)
	private TextView tvMiddle2;
	@ViewInject(R.id.tv_middle3)
	private TextView tvMiddle3;
	@ViewInject(R.id.tv_middle4)
	private TextView tvMiddle4;
	@ViewInject(R.id.tv_middle5)
	private TextView tvMiddle5;
	@ViewInject(R.id.tv_middle6)
	private TextView tvMiddle6;
	@ViewInject(R.id.tv_middle7)
	private TextView tvMiddle7;
	@ViewInject(R.id.tv_middle8)
	private TextView tvMiddle8;
	
	@ViewInject(R.id.rl_middle1)
	private RelativeLayout rlYQZX;
	@ViewInject(R.id.rl_middle2)
	private RelativeLayout rlSJLB;
	@ViewInject(R.id.rl_middle3)
	private RelativeLayout rlSJFX;
	@ViewInject(R.id.rl_middle4)
	private RelativeLayout rlYQZZ;	
	@ViewInject(R.id.rl_middle5)
	private RelativeLayout rlYQYJ;	
	@ViewInject(R.id.rl_middle6)
	private RelativeLayout rlYQBG;		
	@ViewInject(R.id.rl_middle7)
	private RelativeLayout rlDXJZ;	
	@ViewInject(R.id.rl_middle8)
	private RelativeLayout rlALK;	
	
	private NoRightDialog noRightDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_work);
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initView();
		initListener();
		initData();
		super.onActivityCreated(savedInstanceState);
	}
	
	private void initView() {
		zhAdapter = new ZhiHuiAdapter(getActivity(), zhList);
		mgZhiHui.setAdapter(zhAdapter);
	}
	
	private void initListener() {
		mgZhiHui.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				MenuBean bean = zhList.get(position);
				if (bean != null && !TextUtils.isEmpty(bean.getHref())) {
					Intent intent = new Intent(getActivity(),UrlWebClientActivity.class);
					intent.putExtra(UrlWebClientActivity.KEY_URL, bean.getHref());
					startActivity(intent);
				}
			}
		});
		
		rlYQZX.setOnClickListener(rightOnclickListener);
		rlSJLB.setOnClickListener(rightOnclickListener);
		rlSJFX.setOnClickListener(rightOnclickListener);
		rlYQZZ.setOnClickListener(rightOnclickListener);
		rlYQYJ.setOnClickListener(rightOnclickListener);
		rlYQBG.setOnClickListener(rightOnclickListener);
		rlDXJZ.setOnClickListener(rightOnclickListener);
		rlALK.setOnClickListener(rightOnclickListener);

		//测试
		rlYQZX.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserResponseBean bean = PreManager.get(getActivity().getApplicationContext(), AppContext.KEY_LOGINUSER,UserResponseBean.class);
				if (bean != null && !TextUtils.isEmpty(bean.getUser().getToken())) {
					String url = "http://121.41.77.80:9090/qch5/sb.html?token="+bean.getUser().getToken();
					Intent intent = new Intent(getActivity(),UrlWebClientActivity.class);
					intent.putExtra(UrlWebClientActivity.KEY_URL,url);
					startActivity(intent);
				}
			}
		});

		rlSJLB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserResponseBean bean = PreManager.get(getActivity().getApplicationContext(), AppContext.KEY_LOGINUSER,UserResponseBean.class);
				if (bean != null && !TextUtils.isEmpty(bean.getUser().getToken())) {
					String url = "http://121.41.77.80:9090/qch5/xp.html?token="+bean.getUser().getToken();
					Intent intent = new Intent(getActivity(),UrlWebClientActivity.class);
					intent.putExtra(UrlWebClientActivity.KEY_URL,url);
					startActivity(intent);
				}
			}
		});
	}
	
	private void initData() {
		pushEventNoProgress(EventCode.HTTP_MENULIST);
		pushEventNoProgress(EventCode.HTTP_QUERYBACKLOG);
	}
	
	private void refreshView(final MenuResponseBean bean) {
		//应急指挥模块
		if (bean.getData() != null) {
			List<MenuBean> tmpList = new ArrayList<MenuBean>();
			for(ZhiHuiType zhtye : ZhiHuiType.values()) {
				for(MenuBean mb : bean.getData()) {
					if (zhtye.getType().equals(mb.getName())) {
						tmpList.add(mb);
					}
				}
			}
			zhAdapter.updateData(tmpList);
		}
		
		//舆情模块
		if (bean.getData() != null) {
			for(final MenuBean mb : bean.getData()) {
				if (YQType.YQZX.getType().equals(mb.getName())) {
					rlYQZX.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(),UrlWebClientActivity.class);
							intent.putExtra(UrlWebClientActivity.KEY_URL, mb.getHref());
							startActivity(intent);
						}
					});
					if (TextUtils.isEmpty(mb.getHref())) {
						tvMiddle1.setTextColor(getResources().getColor(R.color.color_worktext_gray));
					} else {
						tvMiddle1.setTextColor(getResources().getColor(R.color.color_banner));
					}
				}
				if (YQType.SJLB.getType().equals(mb.getName())) {
					rlSJLB.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(),UrlWebClientActivity.class);
							intent.putExtra(UrlWebClientActivity.KEY_URL, mb.getHref());
							startActivity(intent);
						}
					});	
					if (TextUtils.isEmpty(mb.getHref())) {
						tvMiddle2.setTextColor(getResources().getColor(R.color.color_worktext_gray));
					} else {
						tvMiddle2.setTextColor(getResources().getColor(R.color.color_banner));
					}
				}
				if (YQType.SJFX.getType().equals(mb.getName())) {
					rlSJFX.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(),UrlWebClientActivity.class);
							intent.putExtra(UrlWebClientActivity.KEY_URL, mb.getHref());
							startActivity(intent);
						}
					});	
					if (TextUtils.isEmpty(mb.getHref())) {
						tvMiddle3.setTextColor(getResources().getColor(R.color.color_worktext_gray));
					} else {
						tvMiddle3.setTextColor(getResources().getColor(R.color.color_banner));
					}
				}	
				if (YQType.YQZZ.getType().equals(mb.getName())) {
					rlYQZZ.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(),UrlWebClientActivity.class);
							intent.putExtra(UrlWebClientActivity.KEY_URL, mb.getHref());
							startActivity(intent);
						}
					});	
					if (TextUtils.isEmpty(mb.getHref())) {
						tvMiddle4.setTextColor(getResources().getColor(R.color.color_worktext_gray));
					} else {
						tvMiddle4.setTextColor(getResources().getColor(R.color.color_banner));
					}
				}
				if (YQType.YQYJ.getType().equals(mb.getName())) {
					rlYQYJ.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(),UrlWebClientActivity.class);
							intent.putExtra(UrlWebClientActivity.KEY_URL, mb.getHref());
							startActivity(intent);
						}
					});		
					if (TextUtils.isEmpty(mb.getHref())) {
						tvMiddle5.setTextColor(getResources().getColor(R.color.color_worktext_gray));
					} else {
						tvMiddle5.setTextColor(getResources().getColor(R.color.color_banner));
					}
				}	
				if (YQType.YQBG.getType().equals(mb.getName())) {
					rlYQBG.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(),UrlWebClientActivity.class);
							intent.putExtra(UrlWebClientActivity.KEY_URL, mb.getHref());
							startActivity(intent);
						}
					});	
					if (TextUtils.isEmpty(mb.getHref())) {
						tvMiddle6.setTextColor(getResources().getColor(R.color.color_worktext_gray));
					} else {
						tvMiddle6.setTextColor(getResources().getColor(R.color.color_banner));
					}
				}
				if (YQType.DXJZ.getType().equals(mb.getName())) {
					rlDXJZ.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(),UrlWebClientActivity.class);
							intent.putExtra(UrlWebClientActivity.KEY_URL, mb.getHref());
							startActivity(intent);
						}
					});	
					if (TextUtils.isEmpty(mb.getHref())) {
						tvMiddle7.setTextColor(getResources().getColor(R.color.color_worktext_gray));
					} else {
						tvMiddle7.setTextColor(getResources().getColor(R.color.color_banner));
					}
				}
				if (YQType.ALK.getType().equals(mb.getName())) {
					rlALK.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(),UrlWebClientActivity.class);
							intent.putExtra(UrlWebClientActivity.KEY_URL, mb.getHref());
							startActivity(intent);
						}
					});	
					if (TextUtils.isEmpty(mb.getHref())) {
						tvMiddle8.setTextColor(getResources().getColor(R.color.color_worktext_gray));
					} else {
						tvMiddle8.setTextColor(getResources().getColor(R.color.color_banner));
					}
				}				
			}
		}
	}
	
	private RightOnclickListener rightOnclickListener = new RightOnclickListener();
	private class RightOnclickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (noRightDialog == null) {
				noRightDialog = new NoRightDialog(getActivity());
			}
			noRightDialog.show();
		}
		
	}
	
	//快捷模块 
	private void refreshGallery(BacklogResponseBean bean) {
		mGallery.removeAllViews();
		for(int i = 0 ; i < bean.getData().size() ; i++) {
			final BacklogBean bl = bean.getData().get(i);
			View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_kuaijie,  
                    mGallery, false);  
			TextView tvCount = (TextView) view.findViewById(R.id.tv_top_count);
            TextView tvName = (TextView) view  
                    .findViewById(R.id.tv_top);  
            View divider = view.findViewById(R.id.view_divider);
            if (i == (bean.getData().size() - 1)) {
				divider.setVisibility(View.GONE);
			} else {
				divider.setVisibility(View.VISIBLE);
			}
            
            tvCount.setText(bl.getCount());  
            tvName.setText(bl.getName());
            LinearLayout llTop = (LinearLayout) view.findViewById(R.id.ll_top);
            llTop.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (!TextUtils.isEmpty(bl.getLink())) {
						Intent intent = new Intent(getActivity(),UrlWebClientActivity.class);
						intent.putExtra(UrlWebClientActivity.KEY_URL, bl.getLink());
						startActivity(intent);
					}
				}
			});
            mGallery.addView(view);  
		}
	}	
	
	@Override
	public void onEventRunEnd(Event event) {
		// TODO Auto-generated method stub
		super.onEventRunEnd(event);
		if (event.getEventCode() == EventCode.HTTP_MENULIST) {
			if (event.isSuccess()) {
				MenuResponseBean bean = (MenuResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null) {
					refreshView(bean);
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
		if (event.getEventCode() == EventCode.HTTP_QUERYBACKLOG) {
			if (event.isSuccess()) {
				BacklogResponseBean bean = (BacklogResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null) {
					refreshGallery(bean);
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}		
	}
}
