package com.test.demo.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.demo.R;
import com.test.demo.domain.App;

public class AppSearchListAdapter extends BaseAdapter {
	
	private static final int DEFAULT_APP_IMAGE_ID = R.drawable.product;
	private static final int DEFAULT_APP_SCORE_IMAGE_ID = R.drawable.score;

	private List<App> appList;
	private LayoutInflater inflater;
	
	public AppSearchListAdapter(Context context, List<App> appList) {
		super();
		this.appList = appList;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return appList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return appList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		// TODO Auto-generated method stub
		AppItemView  appItemView = null;   
        if (convertView == null) {   
        	appItemView = new AppItemView();    
            //获取list_item布局文件的视图   
            convertView = inflater.inflate(R.layout.app_item, null);
            appItemView.appImage = (ImageView)convertView.findViewById(R.id.appImage);
            appItemView.appScoreImage = (ImageView)convertView.findViewById(R.id.appScoreImage);
            appItemView.appNameText = (TextView)convertView.findViewById(R.id.appNameText);
            appItemView.appAuthorText = (TextView)convertView.findViewById(R.id.appAuthorText);
            appItemView.appDescriptionText = (TextView)convertView.findViewById(R.id.appDescriptionText);
            appItemView.appPriceText = (TextView)convertView.findViewById(R.id.appPriceText);
            appItemView.appTypeText = (TextView)convertView.findViewById(R.id.appTypeText);
            appItemView.appViewNumberText = (TextView)convertView.findViewById(R.id.appViewNumberText);
            appItemView.appOrderButton = (Button)convertView.findViewById(R.id.appOrderButton);
            //设置控件集到convertView   
            convertView.setTag(appItemView);
        } else {   
        	appItemView = (AppItemView)convertView.getTag();  
        }
        //设值
        final App app = appList.get(position);
        appItemView.appImage.setImageDrawable(convertView.getResources().getDrawable(DEFAULT_APP_IMAGE_ID));
        appItemView.appScoreImage.setImageDrawable(convertView.getResources().getDrawable(DEFAULT_APP_SCORE_IMAGE_ID));
        
        appItemView.appNameText.setText(app.getName());
        appItemView.appAuthorText.setText(app.getAuthor());
        appItemView.appDescriptionText.setText(app.getDescription());
        appItemView.appPriceText.setText(app.getPrice() + "");
        appItemView.appTypeText.setText(app.getType());
        appItemView.appViewNumberText.setText(app.getViewNum() + "");
        
        final Context context = convertView.getContext();
        appItemView.appOrderButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "order " + app.getName(), Toast.LENGTH_SHORT).show();
			}
        	
        });
		return convertView;
	}

	protected final class AppItemView {
		public ImageView appImage, appScoreImage;
		public TextView appNameText, appAuthorText, appDescriptionText, appPriceText, appTypeText, appViewNumberText;
		public Button appOrderButton;
	}
}
