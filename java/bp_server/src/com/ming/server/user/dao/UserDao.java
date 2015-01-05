package com.ming.server.user.dao;

import java.util.List;

import com.ming.server.domain.exception.DaoException;
import com.ming.server.domain.ext.UserEx;

public interface UserDao {

	public void createUser(UserEx user) throws DaoException;
	
	public List<UserEx> getAllUsers() throws DaoException;
	
	public void deleteUser(UserEx user) throws DaoException;
}
