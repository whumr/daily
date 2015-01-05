package test.tab;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class DymanicTableRowActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dymanictable);
		addRow();
	}
	
	private void addRow() {
//	<TableRow>
//		<TextView android:text="直播中"
//		android:textColor="#00FF00" android:gravity="center"/>
//		<TableLayout android:layout_span="2" 
//			android:stretchColumns="0,1" 
//			android:shrinkColumns="0,1">
//			<TableRow>
//				<TextView android:text="达拉斯小牛111111111111111111111111111111111" android:minWidth="200dip"/>
//				<TextView android:text="95 (22 36 24 26)"  android:gravity="center" android:minWidth="200dip"/>
//			</TableRow>
//			<TableRow>
//				<TextView android:layout_span="2" android:gravity="right" 
//				android:text="诺维斯基 (35分 10篮板 10助攻)2222222222222222222222222222" 
//				android:paddingRight="10dip"/>
//			</TableRow>
//				<View style="@style/seprateLine"/>
//			<TableRow>
//				<TextView android:text="俄克拉荷马雷霆"/>
//				<TextView android:text="95 (22 36 24 26)333333333333333333333333"/>
//			</TableRow>
//			<TableRow>
//				<TextView android:layout_span="2" android:gravity="right" 
//				android:text="杜兰特 (28分 14篮板 5助攻)" android:paddingRight="10dip"/>
//			</TableRow>
//		</TableLayout>
//	</TableRow>
		TableLayout tableLayout = (TableLayout)this.findViewById(R.id.dymanicTable);
		for(String[] ss : listData) {
			TableRow tableRow = new TableRow(this);
			
			TextView statusText = new TextView(this);
			statusText.setGravity(Gravity.CENTER);
			statusText.setText(ss[0]);
			
			TableLayout contentTable = new TableLayout(this);
			addMatch(contentTable, ss);
			
			tableRow.addView(statusText);
			tableRow.addView(contentTable);
			tableLayout.addView(tableRow);
		}
	}
	
	private void addMatch(TableLayout contentTable, String[] ss) {
		LayoutParams tablelayoutParams = new LayoutParams();
		tablelayoutParams.span = 2;
		contentTable.setLayoutParams(tablelayoutParams);
		contentTable.setStretchAllColumns(true);
		contentTable.setShrinkAllColumns(true);
		
		TableRow homeRow = new TableRow(this);
		TextView homeNameText = new TextView(this);
		homeNameText.setText(ss[1]);
		homeRow.addView(homeNameText);
		TextView homeScoreText = new TextView(this);
		homeScoreText.setText(ss[2]);
		homeRow.addView(homeScoreText);
		
		TableRow homePlayerRow = new TableRow(this);
		TextView homePlayerText = new TextView(this);
		LayoutParams homePlayerParams = new LayoutParams();
		homePlayerParams.span = 2;
		homePlayerText.setLayoutParams(homePlayerParams);
		homePlayerText.setText(ss[3]);
		homePlayerRow.addView(homePlayerText);
		
		
		TableRow awayRow = new TableRow(this);
		TextView awayNameText = new TextView(this);
		awayNameText.setText(ss[4]);
		awayRow.addView(awayNameText);
		TextView awayScoreText = new TextView(this);
		awayScoreText.setText(ss[5]);
		awayRow.addView(awayScoreText);
		
		TableRow awayPlayerRow = new TableRow(this);
		TextView awayPlayerText = new TextView(this);
		LayoutParams awayPlayerParams = new LayoutParams();
		awayPlayerParams.span = 2;
		awayPlayerText.setLayoutParams(awayPlayerParams);
		awayPlayerText.setText(ss[6]);
		awayPlayerRow.addView(awayPlayerText);
		
		contentTable.addView(homeRow);
		contentTable.addView(homePlayerRow);
		contentTable.addView(awayRow);
		contentTable.addView(awayPlayerRow);
	}
	
	static List<String[]> listData = new ArrayList<String[]>();
	static {
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
}