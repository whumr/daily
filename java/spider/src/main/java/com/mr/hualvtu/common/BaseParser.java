package com.mr.hualvtu.common;

import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;

import javax.annotation.PostConstruct;

/**
 * Created by Administrator on 2015/1/8.
 */
public abstract class BaseParser {

    protected Parser parser = new Parser();
    protected String encoding = "UTF-8";
    protected String content;

    @PostConstruct
    protected void init() {
    }

}