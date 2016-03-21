package com.wdd.robmoney;

import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class RobMoney extends AccessibilityService {
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.e("RobMoney", "onStart");
	}
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		// TODO Auto-generated method stub
		int eventType = event.getEventType();
		switch (eventType) {
		//��һ��������֪ͨ����Ϣ
		case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
			List<CharSequence> texts = event.getText();
			if (!texts.isEmpty()) {
				for (CharSequence text : texts) {
					String content = text.toString();
					Log.e("onAccessibilityEvent", "text:"+content);
					if (content.contains("[΢�ź��]")) {
						//ģ���֪ͨ����Ϣ
						if (event.getParcelableData() != null
								&& 
							event.getParcelableData() instanceof Notification) {
							Notification notification = (Notification) event.getParcelableData();
							PendingIntent pendingIntent = notification.contentIntent;
							try {
								pendingIntent.send();
							} catch (CanceledException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			break;
		//�ڶ����������Ƿ����΢�ź����Ϣ����
		case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
			Log.e("onAccessibilityEvent", "TYPE_WINDOW_STATE_CHANGED");
			String className = event.getClassName().toString();
			if (className.equals("com.tencent.mm.ui.LauncherUI")) {
				//��ʼ�����
				getPacket();
			} else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")) {
				//��ʼ�򿪺��
				openPacket();
			}
			break;
		}
	}

	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * ���ҵ�
	 */
	@SuppressLint("NewApi")
	private void openPacket() {
		AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
		if (nodeInfo != null) {
			List<AccessibilityNodeInfo> list = nodeInfo
					.findAccessibilityNodeInfosByText("�����");
			for (AccessibilityNodeInfo n : list) {
				n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			}
		}

	}

	@SuppressLint("NewApi")
	private void getPacket() {
		AccessibilityNodeInfo rootNode = getRootInActiveWindow();
		recycle(rootNode);
	}
	
	/**
	 * ��ӡһ���ڵ�Ľṹ
	 * @param info
	 */
	@SuppressLint("NewApi")
	public void recycle(AccessibilityNodeInfo info) {  
        if (info.getChildCount() == 0) { 
        	if(info.getText() != null){
        		if("��ȡ���".equals(info.getText().toString())){
        			//������һ��������Ҫע�⣬������Ҫ�ҵ�һ�����Ե����View
                	Log.i("demo", "Click"+",isClick:"+info.isClickable());
                	info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                	AccessibilityNodeInfo parent = info.getParent();
                	while(parent != null){
                		Log.i("demo", "parent isClick:"+parent.isClickable());
                		if(parent.isClickable()){
                			parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                			break;
                		}
                		parent = parent.getParent();
                	}
                	
            	}
        	}
        	
        } else {  
            for (int i = 0; i < info.getChildCount(); i++) {  
                if(info.getChild(i)!=null){  
                    recycle(info.getChild(i));  
                }  
            }  
        }  
    }  

}