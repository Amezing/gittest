package com.wdd.friendster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * ΢������
 * @author tj
 *
 */
public class WBShareActivty extends Activity implements IWeiboHandler.Response {
	private static final String WEIBO_APPKEY = "118244789";
	public static final String REDIRECT_URL = "http://www.sina.com";
	public static final String SCOPE = 
            "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
	EditText et ;
	Button btn ; 
	
	 /** ΢��΢������ӿ�ʵ�� */
    private IWeiboShareAPI  mWeiboShareAPI = null;
    private int SHARE_TYPE = 0 ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_layout);
		SHARE_TYPE = getIntent().getIntExtra(MainActivity.SHARE_TYPE, 0);
		init();
		Log.e("WBShareActivty","onCreate");
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, WEIBO_APPKEY);
		mWeiboShareAPI.registerApp() ;
		  if (savedInstanceState != null) {
	            mWeiboShareAPI.handleWeiboResponse(getIntent(),  this);
	        }
	}

	private void init() {
		// TODO Auto-generated method stub
		et = (EditText) findViewById(R.id.et);
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//sendMultiMessage(true ,false ,false ,false ,false ,false);
				/* SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
			        // ��transactionΨһ��ʶһ������
//				 WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//��ʼ��΢���ķ�����Ϣ
				  WeiboMessage weiboMessage = new WeiboMessage();
//				    if (hasText) {
				        weiboMessage.mediaObject = getTextObj();
//				    }
//				 weiboMessage = getTextObj();
			        request.transaction = String.valueOf(System.currentTimeMillis());
			        request.message = weiboMessage;
			        
			        // 3. ����������Ϣ��΢��������΢���������
			        mWeiboShareAPI.sendRequest(WBShareActivty.this, request);*/
				
//				sendMultiMessage(true ,false ,false ,false ,false ,false);
				
				if (SHARE_TYPE == MainActivity.SHARE_TYPE_CLIENT) {
		            if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
		                int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
		                if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
		                	sendMultiMessage(true ,false ,false ,false ,false ,false);
		                } else {
		                    sendSingleMessage(true ,false ,false ,false ,false /*, hasVoice*/);
		                }
		            } else {
		                Toast.makeText(WBShareActivty.this, "api��֧��", Toast.LENGTH_SHORT).show();
		            }
		        }
		        else if (SHARE_TYPE == MainActivity.SHARE_TYPE_ALL) {
		            sendMultiMessage(true ,false ,false ,false ,false ,false);
		        }
			}
		});
	}
	
	
	
	 /**
     * ����Ӧ�÷���������Ϣ��΢��������΢��������档
     * ע�⣺�� {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 ʱ��֧��ͬʱ���������Ϣ��
     * ͬʱ���Է����ı���ͼƬ�Լ�����ý����Դ����ҳ�����֡���Ƶ�������е�һ�֣���
     * 
     * @param hasText    ����������Ƿ����ı�
     * @param hasImage   ����������Ƿ���ͼƬ
     * @param hasWebpage ����������Ƿ�����ҳ
     * @param hasMusic   ����������Ƿ�������
     * @param hasVideo   ����������Ƿ�����Ƶ
     * @param hasVoice   ����������Ƿ�������
     */
    private void sendMultiMessage(boolean hasText, boolean hasImage, boolean hasWebpage,
            boolean hasMusic, boolean hasVideo, boolean hasVoice) {
        
        // 1. ��ʼ��΢���ķ�����Ϣ
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }
        
        /*if (hasImage) {
            weiboMessage.imageObject = getImageObj();
        }
        
        // �û����Է�������ý����Դ����ҳ�����֡���Ƶ�������е�һ�֣�
        if (hasWebpage) {
            weiboMessage.mediaObject = getWebpageObj();
        }
        if (hasMusic) {
            weiboMessage.mediaObject = getMusicObj();
        }
        if (hasVideo) {
            weiboMessage.mediaObject = getVideoObj();
        }
        if (hasVoice) {
            weiboMessage.mediaObject = getVoiceObj();
        }*/
        
        // 2. ��ʼ���ӵ���΢������Ϣ����
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // ��transactionΨһ��ʶһ������
        request.transaction =buildTransaction("");
        request.multiMessage = weiboMessage;
        /* if (mWeiboShareAPI.isWeiboAppSupportAPI()) { 
//        	 int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
        	 SHARE_TYPE = MainActivity.SHARE_TYPE_CLIENT ;
         }else{
        	 SHARE_TYPE = MainActivity.SHARE_TYPE_ALL ;
         }*/
        // 3. ����������Ϣ��΢��������΢���������
        if (SHARE_TYPE == MainActivity.SHARE_TYPE_CLIENT) {
        	 mWeiboShareAPI.sendRequest(WBShareActivty.this, request);
        }
        else if (SHARE_TYPE == MainActivity.SHARE_TYPE_ALL) {
        	Log.e("wbshare", "=="+SHARE_TYPE);
            AuthInfo authInfo = new AuthInfo(this, WEIBO_APPKEY, REDIRECT_URL, SCOPE);
            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
            String token = "";
            if (accessToken != null) {
                token = accessToken.getToken();
            }
            mWeiboShareAPI.sendRequest(this, request, authInfo, token, new WeiboAuthListener() {
                
                @Override
                public void onWeiboException( WeiboException arg0 ) {
                	Log.e("WeiboException" , "e="+arg0.toString());
                }
                
                @Override
                public void onComplete( Bundle bundle ) {
                    // TODO Auto-generated method stub
                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                    AccessTokenKeeper.writeAccessToken(getApplicationContext(), newToken);
                    Toast.makeText(getApplicationContext(), "onAuthorizeComplete token = " + newToken.getToken(), 0).show();
                }
                
                @Override
                public void onCancel() {
                }
            });
        }
    }

    /**
     * ����Ӧ�÷���������Ϣ��΢��������΢��������档
     * ��{@link IWeiboShareAPI#getWeiboAppSupportAPI()} < 10351 ʱ��ֻ֧�ַ��?����Ϣ����
     * �ı���ͼƬ����ҳ�����֡���Ƶ�е�һ�֣���֧��Voice��Ϣ��
     * 
     * @param hasText    ����������Ƿ����ı�
     * @param hasImage   ����������Ƿ���ͼƬ
     * @param hasWebpage ����������Ƿ�����ҳ
     * @param hasMusic   ����������Ƿ�������
     * @param hasVideo   ����������Ƿ�����Ƶ
     */
    private void sendSingleMessage(boolean hasText, boolean hasImage, boolean hasWebpage,
            boolean hasMusic, boolean hasVideo/*, boolean hasVoice*/) {
        
        // 1. ��ʼ��΢���ķ�����Ϣ
        // �û����Է����ı���ͼƬ����ҳ�����֡���Ƶ�е�һ��
        WeiboMessage weiboMessage = new WeiboMessage();
        if (hasText) {
            weiboMessage.mediaObject = getTextObj();
        }
       /* if (hasImage) {
            weiboMessage.mediaObject = getImageObj();
        }
        if (hasWebpage) {
            weiboMessage.mediaObject = getWebpageObj();
        }
        if (hasMusic) {
            weiboMessage.mediaObject = getMusicObj();
        }
        if (hasVideo) {
            weiboMessage.mediaObject = getVideoObj();
        }*/
        /*if (hasVoice) {
            weiboMessage.mediaObject = getVoiceObj();
        }*/
        
        // 2. ��ʼ���ӵ���΢������Ϣ����
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // ��transactionΨһ��ʶһ������
        request.transaction = buildTransaction("");
        request.message = weiboMessage;
        
        // 3. ����������Ϣ��΢��������΢���������
        mWeiboShareAPI.sendRequest(WBShareActivty.this, request);
    }

    /**
     * ��ȡ������ı�ģ�塣
     * 
     * @return ������ı�ģ��
     */
    /*private String getSharedText() {
        int formatId = R.string.weibosdk_demo_share_text_template;
        String format = getString(formatId);
        String text = format;
        String demoUrl = getString(R.string.weibosdk_demo_app_url);
        if (mTextCheckbox.isChecked() || mImageCheckbox.isChecked()) {
            format = getString(R.string.weibosdk_demo_share_text_template);
        }
        if (mShareWebPageView.isChecked()) {
            format = getString(R.string.weibosdk_demo_share_webpage_template);
            text = String.format(format, getString(R.string.weibosdk_demo_share_webpage_demo), demoUrl);
        }
        if (mShareMusicView.isChecked()) {
            format = getString(R.string.weibosdk_demo_share_music_template);
            text = String.format(format, getString(R.string.weibosdk_demo_share_music_demo), demoUrl);
        }
        if (mShareVideoView.isChecked()) {
            format = getString(R.string.weibosdk_demo_share_video_template);
            text = String.format(format, getString(R.string.weibosdk_demo_share_video_demo), demoUrl);
        }
        if (mShareVoiceView.isChecked()) {
            format = getString(R.string.weibosdk_demo_share_voice_template);
            text = String.format(format, getString(R.string.weibosdk_demo_share_voice_demo), demoUrl);
        }
        
        return text;
    }*/

    /**
     * �����ı���Ϣ����
     * 
     * @return �ı���Ϣ����
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        Editable info = et.getText() ;
        if(info!=null&&info.length()>0){
        	 textObject.text =info.toString();
        }else{
        	textObject.text ="text null";
        }
        
        return textObject;
    }

    /**
     * ����ͼƬ��Ϣ����
     * 
     * @return ͼƬ��Ϣ����
     */
   /* private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) mImageView.getDrawable();
        imageObject.setImageObject(bitmapDrawable.getBitmap());
        return imageObject;
    }*/

    /**
     * ������ý�壨��ҳ����Ϣ����
     * 
     * @return ��ý�壨��ҳ����Ϣ����
     */
    /*private WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = mShareWebPageView.getTitle();
        mediaObject.description = mShareWebPageView.getShareDesc();
        
        // ���� Bitmap ���͵�ͼƬ����Ƶ������
        mediaObject.setThumbImage(mShareWebPageView.getThumbBitmap());
        mediaObject.actionUrl = mShareWebPageView.getShareUrl();
        mediaObject.defaultText = "Webpage Ĭ���İ�";
        return mediaObject;
    }
*/
    /**
     * ������ý�壨���֣���Ϣ����
     * 
     * @return ��ý�壨���֣���Ϣ����
     */
    /*private MusicObject getMusicObj() {
        // ����ý����Ϣ
        MusicObject musicObject = new MusicObject();
        musicObject.identify = Utility.generateGUID();
        musicObject.title = mShareMusicView.getTitle();
        musicObject.description = mShareMusicView.getShareDesc();
        
        // ���� Bitmap ���͵�ͼƬ����Ƶ������
        musicObject.setThumbImage(mShareMusicView.getThumbBitmap());
        musicObject.actionUrl = mShareMusicView.getShareUrl();
        musicObject.dataUrl = "www.weibo.com";
        musicObject.dataHdUrl = "www.weibo.com";
        musicObject.duration = 10;
        musicObject.defaultText = "Music Ĭ���İ�";
        return musicObject;
    }*/

    /**
     * ������ý�壨��Ƶ����Ϣ����
     * 
     * @return ��ý�壨��Ƶ����Ϣ����
     */
   /* private VideoObject getVideoObj() {
        // ����ý����Ϣ
        VideoObject videoObject = new VideoObject();
        videoObject.identify = Utility.generateGUID();
        videoObject.title = mShareVideoView.getTitle();
        videoObject.description = mShareVideoView.getShareDesc();
        
        // ���� Bitmap ���͵�ͼƬ����Ƶ������
        videoObject.setThumbImage(mShareVideoView.getThumbBitmap());
        videoObject.actionUrl = mShareVideoView.getShareUrl();
        videoObject.dataUrl = "www.weibo.com";
        videoObject.dataHdUrl = "www.weibo.com";
        videoObject.duration = 10;
        videoObject.defaultText = "Vedio Ĭ���İ�";
        return videoObject;
    }*/

    /**
     * ������ý�壨��Ƶ����Ϣ����
     * 
     * @return ��ý�壨���֣���Ϣ����
     */
   /* private VoiceObject getVoiceObj() {
        // ����ý����Ϣ
        VoiceObject voiceObject = new VoiceObject();
        voiceObject.identify = Utility.generateGUID();
        voiceObject.title = mShareVoiceView.getTitle();
        voiceObject.description = mShareVoiceView.getShareDesc();
        
        // ���� Bitmap ���͵�ͼƬ����Ƶ������
        voiceObject.setThumbImage(mShareVoiceView.getThumbBitmap());
        voiceObject.actionUrl = mShareVoiceView.getShareUrl();
        voiceObject.dataUrl = "www.weibo.com";
        voiceObject.dataHdUrl = "www.weibo.com";
        voiceObject.duration = 10;
        voiceObject.defaultText = "Voice Ĭ���İ�";
        return voiceObject;
    }*/
	
	
	
	
	
	
	
	
	/*private TextObject getTextObj() {
	    TextObject textObject = new TextObject();
	    textObject.text = et.getText().toString();
	    return textObject;
	}*/

	/*private void sendMultiMessage(boolean hasText, boolean hasImage, boolean hasWebpage,
	        boolean hasMusic, boolean hasVideo, boolean hasVoice) {
	    WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//��ʼ��΢���ķ�����Ϣ
	    if (hasText) {
	        weiboMessage. textObject = getTextObj();
	    }
	    SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
	    request.transaction = String.valueOf(System.currentTimeMillis());
	    request.multiMessage = weiboMessage;
	    
	    AuthInfo authInfo = new AuthInfo(this, WEIBO_APPKEY, REDIRECT_URL, SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        mWeiboShareAPI.sendRequest(this, request, authInfo, token, new WeiboAuthListener() {
            
            @Override
            public void onWeiboException( WeiboException arg0 ) {
            }
            
            @Override
            public void onComplete( Bundle bundle ) {
                // TODO Auto-generated method stub
            	AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
                if (mAccessToken.isSessionValid()) {
                    // ���� Token �� SharedPreferences
                    AccessTokenKeeper.writeAccessToken(WBAuthActivity.this, mAccessToken);
                    .........
                } else {
                // ����ע���Ӧ�ó���ǩ����ȷʱ���ͻ��յ� Code����ȷ��ǩ����ȷ
                    String code = values.getString("code", "");
                    .........
                }
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(getApplicationContext(), newToken);
                Toast.makeText(getApplicationContext(), "onAuthorizeComplete token = " + newToken.getToken(), 0).show();
            }
            
            @Override
            public void onCancel() {
            }
        });
	    mWeiboShareAPI.sendRequest(this , request); //����������Ϣ��΢��������΢���������
	}*/

	@Override
	public void onResponse(BaseResponse arg0) {
		// TODO Auto-generated method stub
		switch (arg0.errCode) {
		
	 case WBConstants.ErrorCode.ERR_OK:
         Toast.makeText(this, "share success", Toast.LENGTH_LONG).show();
         break;
     case WBConstants.ErrorCode.ERR_CANCEL:
         Toast.makeText(this," share cancel" , Toast.LENGTH_LONG).show();
         break;
     case WBConstants.ErrorCode.ERR_FAIL:
         Toast.makeText(this, 
                 "share fail "+ arg0.errMsg, 
                 Toast.LENGTH_LONG).show();
         break;
	}
	}
	
	@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        
        // �ӵ�ǰӦ�û���΢�������з���󣬷��ص���ǰӦ��ʱ����Ҫ�ڴ˴����øú���
        // ������΢���ͻ��˷��ص���ݣ�ִ�гɹ������� true��������
        // {@link IWeiboHandler.Response#onResponse}��ʧ�ܷ��� false�������������ص�
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }
	private String buildTransaction(final String type) {
		Log.e("millis" , System.currentTimeMillis()+"==");
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
}
