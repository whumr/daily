package com.mr.youmitest;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		AdManager.getInstance(this).init("bfe235ac86875adc", "32e80d2579252174", true);
		
		SpotManager.getInstance(this).setSpotOrientation(SpotManager.ORIENTATION_PORTRAIT);
		SpotManager.getInstance(this).loadSpotAds();
		
		Button show_button = (Button)findViewById(R.id.show_button);
		show_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				if(SpotManager.getInstance(MainActivity.this).checkLoadComplete()){
					SpotManager.getInstance(MainActivity.this).showSpotAds(MainActivity.this);
//				}
			}
		});
		
		SpotManager.getInstance(this).showSpotAds(this, new SpotDialogListener() {
		    @Override
		    public void onShowSuccess() {
		        Log.i("Youmi", "onShowSuccess");
		    }

		    @Override
		    public void onShowFailed() {
		        Log.i("Youmi", "onShowFailed");
		    }

		    @Override
		    public void onSpotClosed() {
		        Log.e("sdkDemo", "closed");
		    }
		});
		
//		------------广告条----------
		
//		// 实例化广告条
//		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
//
//		// 获取要嵌入广告条的布局
//		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
//
//		// 将广告条加入到布局中
//		adLayout.addView(adView);
			
//		--------------游戏广告条-----------
		
//		// 实例化 LayoutParams（重要）
//		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//				FrameLayout.LayoutParams.FILL_PARENT,
//				FrameLayout.LayoutParams.WRAP_CONTENT);
//
//		// 设置广告条的悬浮位置
//		layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角
//
//		// 实例化广告条
//		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
//
//		// 调用 Activity 的 addContentView 函数
//		this.addContentView(adView, layoutParams);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		SpotManager.getInstance(this).unregisterSceenReceiver();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
	    // 如果有需要，可以点击后退关闭插屏广告（可选）。
	    if (!SpotManager.getInstance(this).disMiss(true)) {
	        super.onBackPressed();
	    }
	}

	@Override
	protected void onStop() {
	    //如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
	    SpotManager.getInstance(this).disMiss(false);
	    super.onStop();
	}
}
