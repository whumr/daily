package com.ming.server.user.business;

import java.util.List;

import com.ming.server.domain.exception.DaoException;
import com.ming.server.domain.ext.UserEx;

public interface UserService {

	public void createUser(UserEx user) throws DaoException;
	
	public List<UserEx> getAllUsers() throws DaoException;
	
	public void deleteUser(UserEx user) throws DaoException;
}
