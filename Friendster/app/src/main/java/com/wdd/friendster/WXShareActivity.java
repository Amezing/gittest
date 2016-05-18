package com.wdd.friendster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * ΢分享到微信
 * @author wdd
 *
 */
public class WXShareActivity extends Activity implements IWXAPIEventHandler{
	public static String WXAPP_KEY = "wxfe1b4da84798695b";
	public static String APPSECRET = "6cbe351298047f8e6e1b7c15fcf1a9b4";
	EditText et ;
	Button btn ;
	private Spinner spinner_ip;
	IWXAPI wxapi  ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wxshare);
		init();
		wxapi = WXAPIFactory.createWXAPI(this, WXAPP_KEY);
		wxapi.registerApp(WXAPP_KEY);
	}
	private void init() {
		// TODO Auto-generated method stub
		spinner_ip = (Spinner) findViewById(R.id.spinner_type);
		et = (EditText) findViewById(R.id.et);
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				shareText();
			}
		});
	}
	private void shareText() {
		String text = et.getText().toString();
		if (text == null || text.length() == 0) {
			return;
		}

		// ��ʼ��һ��WXTextObject����
		WXTextObject textObj = new WXTextObject();
		textObj.text = text;

		// ��WXTextObject�����ʼ��һ��WXMediaMessage����
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// �����ı����͵���Ϣʱ��title�ֶβ�������
		// msg.title = "Will be ignored";
		msg.description = text;

		// ����һ��Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text"); // transaction�ֶ�����Ψһ��ʶһ������
		req.message = msg;
		/**
		 * 分享到朋友圈WXSceneTimeline
		 * 	聊天界面WXSceneSession
		    收藏WXSceneFavorite
		 */
//		req.scene = SendMessageToWX.Req.WXSceneTimeline ; //

		if(spinner_ip.getSelectedItemPosition()==0){
			req.scene = SendMessageToWX.Req.WXSceneTimeline ;

		}else if(spinner_ip.getSelectedItemPosition()==1){
			req.scene = SendMessageToWX.Req.WXSceneSession; //
		}else{
			req.scene = SendMessageToWX.Req.WXSceneFavorite; //
		}
		// ����api�ӿڷ�����ݵ�΢��
		 wxapi.sendReq(req);

	}
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
		wxapi.handleIntent(intent, this);
	}
	// ����Ӧ�÷��͵�΢�ŵ�����������Ӧ����ص����÷���
		@Override
		public void onResp(BaseResp resp) {
			String result = "share";
			Log.e("onResp " ,"onResp="+resp.toString());
			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result = "share success";
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = "share cancel";
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = "share deny";
				break;
			default:
				result = "share unknown";
				//result = R.string.errcode_unknown;
				break;
			}
			
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		}
		@Override
		public void onReq(BaseReq onReq) {
			// TODO Auto-generated method stub
			Log.e("onReq " ,"onReq="+onReq.toString());
		}
}
