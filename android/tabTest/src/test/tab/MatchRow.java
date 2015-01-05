package test.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TableRow;

public class MatchRow extends TableRow {
	
	String mid;

	public MatchRow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public MatchRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MatchRow);
		mid = a.getString(R.styleable.MatchRow_mid);  
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}
}
