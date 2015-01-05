package com.objectivasoftware.BCIA.library;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.objectivasoftware.BCIA.R;
import com.objectivasoftware.BCIA.model.FlightDeicingInfo;
import com.objectivasoftware.BCIA.view.InfoRecordInputActivity;

public class InfoRecordAdapter extends BaseAdapter {

	private Context context;
	private List<FlightDeicingInfo> mFlightDeicingInfos;
	private LayoutInflater inflater;
	public Map<String, String> flyIds = new LinkedHashMap<String, String>();
	
	public final class ListItemView {
		public ImageView areaImage, state1, state2, state3, state4;
		public CheckBox check;
		public TextView seq_no, regCode, flgNo, airlineCode, completePct, time;
		public Button editButton;
	};

	public InfoRecordAdapter(Context context, List<FlightDeicingInfo> mFlightDeicingInfo) {   
		this.context = context;            
		inflater = LayoutInflater.from(context);
		this.mFlightDeicingInfos = mFlightDeicingInfo;   
	}
	
	public int getCount() {
		return mFlightDeicingInfos.size();
	}

	public Object getItem(int position) {
		return mFlightDeicingInfos.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}
	
    
	public View getView(final int position, View convertView, ViewGroup parent) {
		Log.e("Method", "getView");   
        //自定义视图   
        ListItemView  listItemView = null; 
        if (convertView == null) {   
        	listItemView = new ListItemView();
            //获取list_item布局文件的视图   
            convertView = inflater.inflate(R.layout.info_record_item, null);   
            convertView.setMinimumHeight(79);
            if ((position + 1)%2  == 0) {
            	convertView.setBackgroundResource(R.drawable.deicing_item_green);
            } else {
            	convertView.setBackgroundResource(R.drawable.deicing_item_blue);
            }
            
            
            listItemView.areaImage = (ImageView)convertView.findViewById(R.id.info_record_zone);   
            listItemView.seq_no = (TextView) convertView.findViewById(R.id.info_record_num);
            listItemView.check = (CheckBox) convertView.findViewById(R.id.info_record_confirm);
            listItemView.regCode = (TextView) convertView.findViewById(R.id.info_record_planeNo);
            listItemView.flgNo = (TextView) convertView.findViewById(R.id.info_record_flightNo);
            listItemView.airlineCode = (TextView) convertView.findViewById(R.id.info_record_companyName);
            listItemView.time = (TextView) convertView.findViewById(R.id.info_record_time);
            
            listItemView.state1 = (ImageView) convertView.findViewById(R.id.info_record_state1);
            listItemView.state2 = (ImageView) convertView.findViewById(R.id.info_record_state2);
            listItemView.state3 = (ImageView) convertView.findViewById(R.id.info_record_state3);
            listItemView.state4 = (ImageView) convertView.findViewById(R.id.info_record_state4);
            listItemView.completePct = (TextView) convertView.findViewById(R.id.info_record_complete_pct);
            
            listItemView.editButton = (Button) convertView.findViewById(R.id.info_record_messageIn);
            
            //设置控件集到convertView   
            convertView.setTag(listItemView);   
        }else {   
            listItemView = (ListItemView)convertView.getTag();   
        }   
           
        //设置文字和图片   
        if ("E".equals(mFlightDeicingInfos.get(position).getEwCode())) {
        	 listItemView.areaImage.setBackgroundResource(R.drawable.e_cofm_icon);
        } else {
        	listItemView.areaImage.setBackgroundResource(R.drawable.w_cofm_icon);
        }
        
        listItemView.seq_no.setText(String.valueOf(position + 1));
        listItemView.check.setId(position);
        listItemView.check.setChecked(mFlightDeicingInfos.get(position).isChecked());
        listItemView.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				int index = buttonView.getId();
				mFlightDeicingInfos.get(index).setChecked(isChecked);
				if(mFlightDeicingInfos.get(index).isChecked()){
					flyIds.put(String.valueOf(index), mFlightDeicingInfos.get(index).getFlgtId());
				}else{
					flyIds.remove(String.valueOf(index));
				}
			}
		});
        listItemView.regCode.setText(mFlightDeicingInfos.get(position).getRegCode());
        listItemView.flgNo.setText(mFlightDeicingInfos.get(position).getFlgtNo());
        listItemView.airlineCode.setText(mFlightDeicingInfos.get(position).getAirlineCode());
        
        listItemView.completePct.setText(mFlightDeicingInfos.get(position).getCompletePct());
        listItemView.time.setText(mFlightDeicingInfos.get(position).getSchDttm());
        
        listItemView.editButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("CurrentFlightId", mFlightDeicingInfos.get(position).getFlgtId());
				intent.putExtra("CurrentFlightName", mFlightDeicingInfos.get(position).getRegCode());
				intent.setClass(context, InfoRecordInputActivity.class);
				context.startActivity(intent);
			}
		});

        if ("1".equals(mFlightDeicingInfos.get(position).getDeicingLabel())) {
        	listItemView.state1.setBackgroundResource(R.drawable.clean_state1);
        } else {
        	listItemView.state1.setBackgroundResource(R.drawable.clean_state0);
        } 
        
        if ("1".equals(mFlightDeicingInfos.get(position).getVipRank())) {
        	 listItemView.state2.setBackgroundResource(R.drawable.star_icon1);
        	 listItemView.state2.setVisibility(View.VISIBLE);
        } else if ("2".equals(mFlightDeicingInfos.get(position).getVipRank())) {
        	 listItemView.state2.setBackgroundResource(R.drawable.star_icon2);
        	 listItemView.state2.setVisibility(View.VISIBLE);
        } else {
        	 listItemView.state2.setBackgroundResource(R.drawable.default_icon);
        }
        
        if ("BD".equals(mFlightDeicingInfos.get(position).getFlightStatus())) {
        	listItemView.state3.setBackgroundResource(R.drawable.onboard_start);
        	listItemView.state3.setVisibility(View.VISIBLE);
        } else if ("CC".equals(mFlightDeicingInfos.get(position).getFlightStatus())) {
        	listItemView.state3.setBackgroundResource(R.drawable.onboard_over);
        	listItemView.state3.setVisibility(View.VISIBLE);
        }  else if ("BF".equals(mFlightDeicingInfos.get(position).getFlightStatus())) {
        	listItemView.state3.setBackgroundResource(R.drawable.onboard_brdgoff);
        	listItemView.state3.setVisibility(View.VISIBLE);
        } else if ("RL".equals(mFlightDeicingInfos.get(position).getFlightStatus())) {
        	listItemView.state3.setBackgroundResource(R.drawable.leave_icon);
        	listItemView.state3.setVisibility(View.VISIBLE);
        } else if ("ES".equals(mFlightDeicingInfos.get(position).getFlightStatus())) {
        	listItemView.state3.setBackgroundResource(R.drawable.clean_ready_icon);
        	listItemView.state3.setVisibility(View.VISIBLE);
        } else if ("SD".equals(mFlightDeicingInfos.get(position).getFlightStatus())) {
        	listItemView.state3.setBackgroundResource(R.drawable.clean_begin_icon);
        	listItemView.state3.setVisibility(View.VISIBLE);
        } else if ("ED".equals(mFlightDeicingInfos.get(position).getFlightStatus())) {
        	listItemView.state3.setBackgroundResource(R.drawable.clean_over_icon);
        	listItemView.state3.setVisibility(View.VISIBLE);
        } else if ("AC".equals(mFlightDeicingInfos.get(position).getFlightStatus())) {
        	listItemView.state3.setBackgroundResource(R.drawable.flight_icon);
        	listItemView.state3.setVisibility(View.VISIBLE);
        } else {
        	 listItemView.state3.setVisibility(View.INVISIBLE);
        }
        
        if (mFlightDeicingInfos.get(position).getControlStart() != null ) {
        	listItemView.state4.setBackgroundResource(R.drawable.ordered_icon);
        } else {
        	listItemView.state4.setBackgroundResource(R.drawable.ordered_negative_icon);
        } 
        
        return convertView;  
	}

}
