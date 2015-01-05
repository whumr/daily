package com.test.demo.test;

import java.util.ArrayList;
import java.util.List;

public class apps {

	private List<app> mApp = new ArrayList<app>();

	/**
	 * @return the mApp
	 */
	public List<app> getmApp() {
		return mApp;
	}

	/**
	 * @param mApp the mApp to set
	 */
	public void addmApp(app app) {
		this.mApp.add(app);
	}
}
