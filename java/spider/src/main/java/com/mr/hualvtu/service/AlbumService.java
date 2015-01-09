package com.mr.hualvtu.service;

import org.springframework.stereotype.Service;

import com.mr.hualvtu.common.BaseService;

/**
 * Created by Administrator on 2015/1/7.
 */
@Service("albumService")
public class AlbumService extends BaseService {

    @Override
    protected void init() {
        namespace = "album";
    }
}
