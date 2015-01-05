package com.ming.server.user.business.impl;

import java.util.List;

import com.ming.server.common.ServiceTemplate;
import com.ming.server.domain.exception.DaoException;
import com.ming.server.domain.ext.UserEx;
import com.ming.server.user.business.UserService;
import com.ming.server.user.dao.UserDao;

public class UserServiceImp extends ServiceTemplate implements UserService {
	
	private UserDao userDao;
	
	@Override
	public void createUser(UserEx user) throws DaoException {
		// TODO Auto-generated method stub
		userDao.createUser(user);
	}

	@Override
	public List<UserEx> getAllUsers() throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(UserEx user) throws DaoException {
		// TODO Auto-generated method stub

	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}