package com.mr.hualvtu.common;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by Administrator on 2015/1/7.
 */
@Service
public abstract class BaseService {

    @Autowired
    @Qualifier("baseSqlSession")
    protected SqlSession baseSqlSession;

    protected String namespace;

    @PostConstruct
    protected abstract void init();

    public void insert(BaseEntity entity) {
        baseSqlSession.insert(namespace + ".insert", entity);
    }

    public void batchInsert(List<? extends BaseEntity> entities) {
        baseSqlSession.insert(namespace + ".batchInsert", entities);
    }
}
