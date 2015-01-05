package com.objectivasoftware.BCIA.library;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.objectivasoftware.BCIA.R;
import com.objectivasoftware.BCIA.model.FlightInfo;

public class FlightMonitorSearchAdapter extends BaseAdapter {
	private List<FlightInfo> mFlightInfoList;
	private LayoutInflater inflater;
	public Map<String, String> flyIds = new LinkedHashMap<String, String>();
	
	public final class ListItemView {
		public ImageView areaImage, stateImage1, stateImage2, stateImage3, stateImage4;
		public CheckBox check;
		public TextView seqNo, regCode, flgNo, airlineCode, actypeCode, schDttm, mntAgent;
		
	};
	
	 public FlightMonitorSearchAdapter(Context context, List<FlightInfo> mFlightInfoList) {   
	        inflater = LayoutInflater.from(context);   //创建视图容器并设置上下文   
	        this.mFlightInfoList = mFlightInfoList;   
	    } 
	
	public int getCount() {
		return mFlightInfoList.size();
	}

	public Object getItem(int position) {
		return mFlightInfoList.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}
	
    
	public View getView(int position, View convertView, ViewGroup parent) {  
		        //自定义视图   
		        ListItemView  listItemView = null;   
		        if (convertView == null) {   
		            listItemView = new ListItemView();    
		            //获取list_item布局文件的视图   
		            convertView = inflater.inflate(R.layout.search_flight_item, null);  
		            convertView.setMinimumHeight(79);
		            listItemView.areaImage = (ImageView)convertView.findViewById(R.id.search_flight_item_area);   
		            listItemView.seqNo = (TextView) convertView.findViewById(R.id.search_flight_item_seqno);
		            listItemView.check = (CheckBox) convertView.findViewById(R.id.search_flight_item_check);
		           
		            listItemView.regCode = (TextView) convertView.findViewById(R.id.search_flight_item_plane_num);
		            listItemView.flgNo = (TextView) convertView.findViewById(R.id.search_flight_item_schedule_num);
		            listItemView.airlineCode = (TextView) convertView.findViewById(R.id.search_flight_item_conpany);
		            listItemView.actypeCode = (TextView) convertView.findViewById(R.id.search_flight_item_model);
		            listItemView.schDttm = (TextView) convertView.findViewById(R.id.search_flight_item_plan_time);
		            listItemView.stateImage1 = (ImageView)convertView.findViewById(R.id.search_flight_item_state1);
		            listItemView.stateImage2 = (ImageView)convertView.findViewById(R.id.search_flight_item_state2);
		            listItemView.stateImage3 = (ImageView)convertView.findViewById(R.id.search_flight_item_state3);
		            listItemView.stateImage4 = (ImageView)convertView.findViewById(R.id.search_flight_item_state4);
		            //设置控件集到convertView   
		            convertView.setTag(listItemView);
		        } else {   
		            listItemView = (ListItemView)convertView.getTag();  
		        }   
		           
		        //设置文字和图片  
		        if ("E".equals(mFlightInfoList.get(position).getEwCode())) {
		        	listItemView.areaImage.setBackgroundResource(R.drawable.e_cofm_icon);
		        } else {
		        	listItemView.areaImage.setBackgroundResource(R.drawable.w_cofm_icon);
		        }
		        
		        listItemView.seqNo.setText(String.valueOf(position + 1));
		        listItemView.check.setId(position);
		        listItemView.check.setChecked(mFlightInfoList.get(position).isChecked());
	            listItemView.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						int index = buttonView.getId();
						mFlightInfoList.get(index).setChecked(isChecked);
						if(mFlightInfoList.get(index).isChecked()){
							flyIds.put(String.valueOf(index), mFlightInfoList.get(index).getFlgId());
						}else{
							flyIds.remove(String.valueOf(index));
						}
					}
				});
	            listItemView.regCode.setText(mFlightInfoList.get(position).getRegCode());
	            listItemView.flgNo.setText(mFlightInfoList.get(position).getFlgNo());
	            listItemView.airlineCode.setText(mFlightInfoList.get(position).getAirlineCode());
	            listItemView.actypeCode.setText(mFlightInfoList.get(position).getActypeCode());
	            listItemView.schDttm.setText(mFlightInfoList.get(position).getSchDttm());
		        
	            if ("1".equals(mFlightInfoList.get(position).getDeicingLabel())) {
	            	listItemView.stateImage1.setBackgroundResource(R.drawable.clean_state1);
	            } else {
	            	listItemView.stateImage1.setBackgroundResource(R.drawable.clean_state0);
	            } 
	            
	            if ("1".equals(mFlightInfoList.get(position).getVipRank())) {
	            	 listItemView.stateImage2.setBackgroundResource(R.drawable.star_icon1);
	            	 listItemView.stateImage2.setVisibility(View.VISIBLE);
	            } else if ("2".equals(mFlightInfoList.get(position).getVipRank())) {
	            	 listItemView.stateImage2.setBackgroundResource(R.drawable.star_icon2);
	            	 listItemView.stateImage2.setVisibility(View.VISIBLE);
	            } else {
	            	 listItemView.stateImage2.setBackgroundResource(R.drawable.default_icon);
	            }
	            
	            if ("BD".equals(mFlightInfoList.get(position).getFlightStatus())) {
	            	listItemView.stateImage3.setBackgroundResource(R.drawable.onboard_start);
	            	listItemView.stateImage3.setVisibility(View.VISIBLE);
	            } else if ("CC".equals(mFlightInfoList.get(position).getFlightStatus())) {
	            	listItemView.stateImage3.setBackgroundResource(R.drawable.onboard_over);
	            	listItemView.stateImage3.setVisibility(View.VISIBLE);
	            }  else if ("BF".equals(mFlightInfoList.get(position).getFlightStatus())) {
	            	listItemView.stateImage3.setBackgroundResource(R.drawable.onboard_brdgoff);
	            	listItemView.stateImage3.setVisibility(View.VISIBLE);
	            } else if ("RL".equals(mFlightInfoList.get(position).getFlightStatus())) {
	            	listItemView.stateImage3.setBackgroundResource(R.drawable.leave_icon);
	            	listItemView.stateImage3.setVisibility(View.VISIBLE);
	            } else if ("ES".equals(mFlightInfoList.get(position).getFlightStatus())) {
	            	listItemView.stateImage3.setBackgroundResource(R.drawable.clean_ready_icon);
	            	listItemView.stateImage3.setVisibility(View.VISIBLE);
	            } else if ("SD".equals(mFlightInfoList.get(position).getFlightStatus())) {
	            	listItemView.stateImage3.setBackgroundResource(R.drawable.clean_begin_icon);
	            	listItemView.stateImage3.setVisibility(View.VISIBLE);
	            } else if ("ED".equals(mFlightInfoList.get(position).getFlightStatus())) {
	            	listItemView.stateImage3.setBackgroundResource(R.drawable.clean_over_icon);
	            	listItemView.stateImage3.setVisibility(View.VISIBLE);
	            } else if ("AC".equals(mFlightInfoList.get(position).getFlightStatus())) {
	            	listItemView.stateImage3.setBackgroundResource(R.drawable.flight_icon);
	            	listItemView.stateImage3.setVisibility(View.VISIBLE);
	            } else {
	            	 listItemView.stateImage3.setVisibility(View.INVISIBLE);
	            }
	            
	            
	            if (mFlightInfoList.get(position).getControlStart() != null) {
	            	listItemView.stateImage4.setBackgroundResource(R.drawable.ordered_icon);
	            } else {
	            	listItemView.stateImage4.setBackgroundResource(R.drawable.ordered_negative_icon);
	            } 
	            
		        return convertView;  
	}
}
