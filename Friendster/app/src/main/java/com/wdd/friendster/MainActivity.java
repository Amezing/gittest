package com.wdd.friendster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
/**
 * ��������Ȧ
 * @author wdd
 *
 */
public class MainActivity extends Activity implements OnClickListener{
	
	private Button qq_btn,wx_btn,sina_btn ;
	public static String SHARE_TYPE = "sharetype" ;
	public static int SHARE_TYPE_CLIENT = 0 ;
	public static int SHARE_TYPE_ALL = 1 ;
	
	//΢��
	 /** ΢��΢������ӿ�ʵ�� */
    private IWeiboShareAPI  mWeiboShareAPI = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		/*Weibo weibo = Weibo.getInstance();  
		Utility.setAuthorization(new AccessTokenHeader());  
		AccessToken accessToken = new AccessToken(accessToken, SINA_API_SECRET);  
		accessToken.setExpiresIn("99999");  
		weibo.setAccessToken(accessToken);  */
		
	}

	private void init() {
		// TODO Auto-generated method stub
		qq_btn = (Button) findViewById(R.id.qq_btn);
		qq_btn.setOnClickListener(this);
		wx_btn = (Button) findViewById(R.id.wx_btn);
		wx_btn.setOnClickListener(this);
		sina_btn = (Button) findViewById(R.id.sina_btn);
		sina_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.qq_btn:
			
			break;
		case R.id.wx_btn:
			if(!checkApkExist(this , "com.tencent.mm")) {
				Toast.makeText(this, "微信客户端不存在", Toast.LENGTH_LONG).show();
			}else{
				Intent wxintent = new Intent(MainActivity.this, WXShareActivity.class);
				startActivity(wxintent);
			}

			break;
		case R.id.sina_btn:
			Intent intent = new Intent(MainActivity.this, WBShareActivty.class);
			if(!checkApkExist(this , "com.sina.weibo")){
				Toast.makeText(this, "微博客户端不存在", Toast.LENGTH_LONG).show();
				intent.putExtra(SHARE_TYPE, SHARE_TYPE_ALL);
			}else{
				intent.putExtra(SHARE_TYPE, SHARE_TYPE_CLIENT);
			
			}startActivity(intent);
			break;
		}
	}


	public static boolean checkApkExist(Context context, String packageName) {
		if (packageName == null || "".equals(packageName))
		return false;
		try {
		ApplicationInfo info = context.getPackageManager()
		.getApplicationInfo(packageName,
		PackageManager.GET_UNINSTALLED_PACKAGES);
		return true;
		} catch (NameNotFoundException e) {
		return false;
		}
		}
}
