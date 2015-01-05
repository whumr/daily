package com.objectivasoftware.BCIA.view;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;

import com.objectivasoftware.BCIA.R;

public class HelpActivity extends BaseActivity{


	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		WebView imageView=(WebView)findViewById(R.id.help_img);
		imageView.loadUrl("file:///android_asset/help_image.html");
		
		ImageView iView = (ImageView) findViewById(R.id.back_button);
		iView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
	}
	
}
