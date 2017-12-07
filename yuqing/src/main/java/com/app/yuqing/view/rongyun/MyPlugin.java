package com.app.yuqing.view.rongyun;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.app.yuqing.R;

import java.io.File;

import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;

/**
 * Created by Administrator on 2017/12/5.
 */

public class MyPlugin implements IPluginModule {

    private Conversation.ConversationType conversationType;
    private String targetId;
    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.icon_add);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getResources().getString(R.string.app_name);
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        conversationType = rongExtension.getConversationType();
        targetId = rongExtension.getTargetId();
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }

    /**
     * 发送图片消息上传到服务器
     * @param firstImgPath  视频第一帧画面的本地 path
     * @param videoPath     视频存储的本地路径
     * @param mConversationType  会话类型
     * @param targetId  目标id
     */
    public void sendVideoMessage(String firstImgPath,final String videoPath, Conversation.ConversationType mConversationType,String targetId) {
        if (TextUtils.isEmpty(firstImgPath)) {
            new RuntimeException("firstImgPath is null");
            return;
        }

        File f = new File(firstImgPath);
        if (!f.exists()) {
            new RuntimeException("image file is null");
            return;
        }
        if (TextUtils.isEmpty(videoPath)) {
            new RuntimeException("videoPath is null");
            return;
        }
        if (mConversationType == null) {
            new RuntimeException("ConversationType is null");
            return;
        }
        if (TextUtils.isEmpty(targetId)) {
            new RuntimeException("targetId is null");
            return;
        }

        ImageMessage imageMessage = ImageMessage.obtain(Uri.parse("file://" + firstImgPath), Uri.parse("file://" + firstImgPath));
        io.rong.imlib.model.Message message = io.rong.imlib.model.Message.obtain(targetId, mConversationType, imageMessage);
        RongIM.getInstance().getRongIMClient().sendImageMessage(message, null, null, new RongIMClient.SendImageMessageWithUploadListenerCallback() {

            @Override
            public void onAttached(final Message message, final RongIMClient.UploadImageStatusListener uploadImageStatusListener) {
                message.setSentStatus(Message.SentStatus.SENDING);
                RongIMClient.getInstance().setMessageSentStatus(message.getMessageId(), message.getSentStatus());

//                Runnable runnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        ImageMessage img = (ImageMessage) message.getContent();
//                        img.setLocalUri(Uri.parse((videoPath)));
//
//                        io.rong.imlib.model.Message msg = io.rong.imlib.model.Message.obtain(message.getTargetId(), message.getConversationType(), img);
//                        RongIMClient.getInstance().uploadMedia(msg, new RongIMClient.UploadMediaCallback() {
//                            @Override
//                            public void onProgress(io.rong.imlib.model.Message message, int progress) {
//                                uploadImageStatusListener.update(progress);
//                            }
//
//                            @Override
//                            public void onError(io.rong.imlib.model.Message message, RongIMClient.ErrorCode errorCode) {
//                                uploadImageStatusListener.error();
//                            }
//
//                            @Override
//                            public void onSuccess(io.rong.imlib.model.Message message) {
////                                uploadImageStatusListener.success();
//                            }
//                        });//
//                    }
//                };
//                new Handler().post(runnable);
            }

            @Override
            public void onError(io.rong.imlib.model.Message message, RongIMClient.ErrorCode code) {

            }

            @Override
            public void onSuccess(io.rong.imlib.model.Message message) {
                message.setSentStatus(Message.SentStatus.SENT);
                RongIMClient.getInstance().setMessageSentStatus(message.getMessageId(), message.getSentStatus());
            }

            @Override
            public void onProgress(io.rong.imlib.model.Message message, int progress) {

            }
        });
    }
}
