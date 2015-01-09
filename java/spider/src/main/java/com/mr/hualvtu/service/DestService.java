package com.mr.hualvtu.service;

import com.mr.hualvtu.common.BaseService;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2015/1/7.
 */
@Service("destService")
public class DestService extends BaseService {

    @Override
    protected void init() {
        namespace = "dest";
    }
}
