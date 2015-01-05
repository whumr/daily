package com.ming.server.user.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.ming.server.common.DaoSupport;
import com.ming.server.domain.exception.DaoException;
import com.ming.server.domain.ext.UserEx;
import com.ming.server.user.dao.UserDao;

public class UserDaoImp extends DaoSupport implements UserDao {
	
	private static Logger logger = Logger.getLogger(UserDaoImp.class);

	@Override
	public void createUser(UserEx user) throws DaoException {
		// TODO Auto-generated method stub
		this.insert(user);
		logger.info("create user:" + user.toString());
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
	
}
