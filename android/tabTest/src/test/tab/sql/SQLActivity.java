package test.tab.sql;

import test.tab.R;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SQLActivity extends Activity {
	
	private static final String dbName = "test.db";
	
	private static final String insert = "insert into test(id, name, age) values (?, ?, ?)";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sql1);
		init();
	}
	
	private void init() {
		SQLHelper1 sqlHelper = new SQLHelper1(this, dbName, null, 1);
		final SQLiteDatabase db = sqlHelper.getWritableDatabase();
		updateList(db);
		
		
		final TextView name = (TextView)findViewById(R.id.name);
		final TextView age = (TextView)findViewById(R.id.age);
		Button regButton =(Button)findViewById(R.id.regButton);
		regButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!db.isOpen())
					SQLiteDatabase.openDatabase(dbName, null, SQLiteDatabase.OPEN_READWRITE);
				db.execSQL(insert, new Object[]{null, name.getText(), age.getText()});
				updateList(db);
			}
			
		});
	}
	
	private void updateList(SQLiteDatabase db) {
		if(!db.isOpen())
			SQLiteDatabase.openDatabase(dbName, null, SQLiteDatabase.OPEN_READWRITE);
		Cursor c = db.rawQuery("select * from test", null);
		int idIndex = c.getColumnIndexOrThrow("id");  
		int nameIndex = c.getColumnIndexOrThrow("name");  
		int ageIndex = c.getColumnIndexOrThrow("age");
		String[] ss = new String[c.getCount()];
		int index = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext(), index++) {
			int id = c.getInt(idIndex);
			String name = c.getString(nameIndex);  
			int age = c.getInt(ageIndex);
			ss[index] = id + "\t" + name + "\t" + age;
		}
		c.close();
		db.close();
		ListView lv = (ListView)findViewById(R.id.listView3);
		lv.setAdapter(new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, ss));
	}
}
