package test.tab;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListTableActivity extends Activity {

	static List<String[]> listData = new ArrayList<String[]>();
	static {
//		TextView statusText;
//		TextView homeTeamText;
//		TextView homeScoreText;
//		TextView homePlayerText;
//		TextView awayTeamText;
//		TextView awayScoreText;
//		TextView awayPlayerText;
		listData.add(new String[]{
			"直播中",
			"达拉斯小牛",
			"95 (22 36 24 26)",
			"诺维斯基 (35分 10篮板 10助攻)",
			"俄克拉荷马雷霆",
			"95 (22 36 24 26)",
			"杜兰特 (28分 14篮板 5助攻)"
		});
		listData.add(new String[]{
				"已结束",
				"芝加哥公牛",
				"120 (23 31 34 25)",
				"罗斯 (27分 8篮板 12助攻)",
				"迈阿密热火",
				"105 (24 15 37 25)",
				"韦德 (19分 12篮板 9助攻)"
		});
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table1);
		ListView list = (ListView)findViewById(R.id.listView1);
		list.setAdapter(new TableListAdaper(listData, this));  
	}
}

class TableListViewHolder {
	TextView statusText;
	TextView homeTeamText;
	TextView homeScoreText;
	TextView homePlayerText;
	TextView awayTeamText;
	TextView awayScoreText;
	TextView awayPlayerText;
}

class TableListAdaper extends BaseAdapter {
	
	private List<String[]> list;
	private TableListViewHolder viewHolder = new TableListViewHolder();
	private LayoutInflater mInflater;
	private Context context;
//	private int windowWidth;
	
	public TableListAdaper() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TableListAdaper(List<String[]> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
//		Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
//		this.windowWidth = display.getWidth();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TableListViewHolder tableListViewHolder;
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.table1list2, null);
			
			tableListViewHolder = new TableListViewHolder();
		
			tableListViewHolder.statusText = (TextView)convertView.findViewById(R.id.statusText);
			tableListViewHolder.homeTeamText = (TextView)convertView.findViewById(R.id.homeTeamText);
			tableListViewHolder.homeScoreText = (TextView)convertView.findViewById(R.id.homeScoreText);
			tableListViewHolder.homePlayerText = (TextView)convertView.findViewById(R.id.homePlayerText);
			tableListViewHolder.awayTeamText = (TextView)convertView.findViewById(R.id.awayTeamText);
			tableListViewHolder.awayScoreText = (TextView)convertView.findViewById(R.id.awayScoreText);
			tableListViewHolder.awayPlayerText = (TextView)convertView.findViewById(R.id.awayPlayerText);
			
			convertView.setTag(tableListViewHolder);
		} else {
			tableListViewHolder = (TableListViewHolder)convertView.getTag();
		}
		tableListViewHolder.statusText.setText(list.get(position)[0]);
		tableListViewHolder.homeTeamText.setText(list.get(position)[1]);
		tableListViewHolder.homeScoreText.setText(list.get(position)[2]);
		tableListViewHolder.homePlayerText.setText(list.get(position)[3]);
		tableListViewHolder.awayTeamText.setText(list.get(position)[4]);
		tableListViewHolder.awayScoreText.setText(list.get(position)[5]);
		tableListViewHolder.awayPlayerText.setText(list.get(position)[6]);
//		viewHolder.statusText = (TextView)convertView.findViewById(R.id.statusText);
//		viewHolder.homeTeamText = (TextView)convertView.findViewById(R.id.homeTeamText);
//		viewHolder.homeScoreText = (TextView)convertView.findViewById(R.id.homeScoreText);
//		viewHolder.homePlayerText = (TextView)convertView.findViewById(R.id.homePlayerText);
//		viewHolder.awayTeamText = (TextView)convertView.findViewById(R.id.awayTeamText);
//		viewHolder.awayScoreText = (TextView)convertView.findViewById(R.id.awayScoreText);
//		viewHolder.awayPlayerText = (TextView)convertView.findViewById(R.id.awayPlayerText);
//		
//		
//		viewHolder.statusText.setText(list.get(position)[0]);
//		viewHolder.homeTeamText.setText(list.get(position)[1]);
//		viewHolder.homeScoreText.setText(list.get(position)[2]);
//		viewHolder.homePlayerText.setText(list.get(position)[3]);
//		viewHolder.awayTeamText.setText(list.get(position)[4]);
//		viewHolder.awayScoreText.setText(list.get(position)[5]);
//		viewHolder.awayPlayerText.setText(list.get(position)[6]);

//		int c1 = (int)(windowWidth * 0.2);
//		int c2 = (int)(windowWidth * 0.4);
//		
//		viewHolder.statusText.setLayoutParams(new TableRow.LayoutParams(c1, TableRow.LayoutParams.WRAP_CONTENT));
//		viewHolder.homeTeamText.setLayoutParams(new TableRow.LayoutParams(c2, TableRow.LayoutParams.WRAP_CONTENT));
//		viewHolder.homeScoreText.setLayoutParams(new TableRow.LayoutParams(c2, TableRow.LayoutParams.WRAP_CONTENT));
		
		return convertView;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public List<String[]> getList() {
		return list;
	}
	
	public void setList(List<String[]> list) {
		this.list = list;
	}
	
	public TableListViewHolder getViewHolder() {
		return viewHolder;
	}
	
	public void setViewHolder(TableListViewHolder viewHolder) {
		this.viewHolder = viewHolder;
	}

	public LayoutInflater getmInflater() {
		return mInflater;
	}

	public void setmInflater(LayoutInflater mInflater) {
		this.mInflater = mInflater;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
	}

//	public int getWindowWidth() {
//		return windowWidth;
//	}
//
//	public void setWindowWidth(int windowWidth) {
//		this.windowWidth = windowWidth;
//	}
}