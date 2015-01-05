package com.ming.server.common;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ming.server.common.domain.BaseEntity;
import com.ming.server.common.domain.Page;
import com.ming.server.common.domain.TableColumn;
import com.ming.server.domain.exception.DaoException;
import com.ming.server.util.SQLUtil;

public class DaoSupport implements BaseDao {
	
	protected JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int deleteByPk(BaseEntity entity) throws DaoException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteByPks(List<BaseEntity> entities) throws DaoException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(List<BaseEntity> entities) throws DaoException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(BaseEntity entity) throws DaoException {
		// TODO Auto-generated method stub
		Object[] values = new Object[entity.getFiledCount()];
		int[] types = new int[entity.getFiledCount()];
		int index = 0;
		for(TableColumn column : entity.getColumns()) {
			if(!column.isPrimaryKey()) {
				values[index] = column.getValue();
				types[index++] = column.getType();
			}
		}
		return jdbcTemplate.update(SQLUtil.getInsertSql(entity), values, types);
	}

	@Override
	public int updateByPk(BaseEntity entity) throws DaoException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BaseEntity getByPk(BaseEntity entity) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BaseEntity> getAll(BaseEntity entity) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BaseEntity> getAll(BaseEntity entity, Page page)
			throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

}
