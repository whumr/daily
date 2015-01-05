package com.test.dubbo.impl;

import com.test.dubbo.TestService;

public class TestServiceImpl implements TestService {

	@Override
	public String sayHello(String name) {
		return "hello, " + name;
	}

}
