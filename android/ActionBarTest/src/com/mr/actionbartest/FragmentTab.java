package com.mr.actionbartest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentTab extends Fragment {

	String text;
	
	public FragmentTab() {
		super();
	}

	public FragmentTab(String text) {
		super();
		this.text = text;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment, container, false);
		((TextView)v.findViewById(R.id.textView)).setText(text);
		return v;
	}
}
