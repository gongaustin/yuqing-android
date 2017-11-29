package com.app.yuqing.fragment;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

import com.app.yuqing.R;
import com.app.yuqing.activity.CreateGroupActivity;
import com.app.yuqing.activity.GropListActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageFragment extends BaseFragment{

	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	@ViewInject(R.id.right_layout)
	private RelativeLayout rlRight;
	@ViewInject(R.id.right_image)
	private ImageView ivRight;
	@ViewInject(R.id.rl_group)
	private RelativeLayout rlGroup;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_message);
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initView();
		initListener();
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refrshConversation();
	}
	
	private void refrshConversation() {
        ConversationListFragment fragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                      .appendPath("conversationlist")
                      .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话，该会话聚合显示
                      .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话，该会话非聚合显示
                      .build();
        fragment.setUri(uri);  //设置 ConverssationListFragment 的显示属性

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();		
        
	} 
	
	private void initView() {
		tvTitle.setText("消息");
		rlRight.setVisibility(View.VISIBLE);
		ivRight.setImageResource(R.drawable.icon_newmessage);
	}
	
	private void initListener() {
		rlRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),CreateGroupActivity.class);
				getActivity().startActivity(intent);
			}
		});
		
		rlGroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),GropListActivity.class);
				getActivity().startActivity(intent);
			}
		});
	}
	
}
