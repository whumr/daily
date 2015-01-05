package com.blossom.test.dao;

import java.util.ArrayList;

public class PageList<E> extends ArrayList<E> {

	private static final long serialVersionUID = -6969564411562215735L;

	public static final int PAGE = 1, PAGE_SIZE = 10, COUNT = 0;
	
	private int page = PAGE, page_size = PAGE_SIZE, count = COUNT;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPage_size() {
		return page_size;
	}

	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
