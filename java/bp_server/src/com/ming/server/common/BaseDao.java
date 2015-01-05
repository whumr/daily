package com.ming.server.common;

import java.util.List;

import com.ming.server.common.domain.BaseEntity;
import com.ming.server.common.domain.Page;
import com.ming.server.domain.exception.DaoException;

public interface BaseDao {

	public int deleteByPk(BaseEntity entity) throws DaoException;
	
    public int deleteByPks(List<BaseEntity> entities) throws DaoException;
    
    public int insert(List<BaseEntity> entities) throws DaoException;
    
    public int insert(BaseEntity entity) throws DaoException;
    
    public int updateByPk(BaseEntity entity) throws DaoException;
    
    public BaseEntity getByPk(BaseEntity entity) throws DaoException;

    public List<BaseEntity> getAll(BaseEntity entity) throws DaoException;

    public List<BaseEntity> getAll(BaseEntity entity, Page page) throws DaoException;
}