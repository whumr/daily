package com.mr.hualvtu.service;

import com.mr.hualvtu.common.BaseService;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2015/1/7.
 */
@Service("landService")
public class LandService extends BaseService {

    @Override
    protected void init() {
        namespace = "land";
    }
}
